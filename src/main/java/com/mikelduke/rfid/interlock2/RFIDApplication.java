package com.mikelduke.rfid.interlock2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jackson.map.ObjectMapper;

import com.mikelduke.rfid.interlock2.makertracker.MakerTrackerClient;

import spark.Spark;

public class RFIDApplication {
	private static final String CLAZZ = RFIDApplication.class.getName();
	private static final Logger LOGGER = Logger.getLogger(CLAZZ);
	
	private volatile AccessInfo accessInfo = null;
	
	private final AccessControlClient client;
	
	private ConsoleReader consoleReader = null;
	private Thread consoleReaderThread = null;
	
	private final ScheduledExecutorService backupExecutorService = Executors.newSingleThreadScheduledExecutor();
	
	public static void main(String[] args) {
		LOGGER.info("RFID Interlock 2");
		new RFIDApplication(args);
	}
	
	public RFIDApplication(String[] args) {
		loadConfig(args);
		
		Spark.port(Integer.parseInt(Configuration.getProperties().getProperty(Configuration.PORT, "8080")));
		Spark.get("/status", InfoServer::status);
		Spark.awaitInitialization();
		LOGGER.logp(Level.INFO, CLAZZ, "main", "Server started on port " + Spark.port());
		
		client = new MakerTrackerClient();
		client.configure(Configuration.getProperties());
		
		if (Boolean.parseBoolean(Configuration.getProperty(Configuration.ENABLE_BACKUP, "true"))) {
			loadLastBackup();
			setupBackupTask();
		}
		
		if (Boolean.parseBoolean(Configuration.getProperty(Configuration.ENABLE_CONSOLE, "true"))) {
			consoleReader = new ConsoleReader(client);
			consoleReaderThread = new Thread(consoleReader);
			consoleReaderThread.start();
		}
	}

	private void loadLastBackup() {
		ObjectMapper mapper = new ObjectMapper();
		File f = new File("accessInfo.json");
		if (f.exists() && f.canRead()) {
			try {
				AccessInfo ai = mapper.readValue(new FileInputStream(f), AccessInfo.class);
				this.accessInfo = ai;
				LOGGER.logp(Level.INFO, CLAZZ, "loadLastBackup", "Loaded last backup from file");
			} catch (IOException e) {
				LOGGER.logp(Level.WARNING, CLAZZ, "loadLastBackup", "Error loading last AccessInfo Json File", e);
			}
		}
	}

	private void setupBackupTask() {
		backupExecutorService.scheduleAtFixedRate(()-> {
			try {
				LOGGER.logp(Level.INFO, CLAZZ, "backupService", "Retrieving backup Access Info");
				
				accessInfo = client.getAccessInfo();

				File f = new File("accessInfo.json");
				f.delete();
				
				new ObjectMapper()
						.writerWithDefaultPrettyPrinter()
						.writeValue(new FileOutputStream(f), accessInfo);
				LOGGER.logp(Level.INFO, CLAZZ, "backupService", "Backup saved to " + f.getAbsolutePath());
			} catch (Throwable e) {
				LOGGER.logp(Level.SEVERE, CLAZZ, "backupService", "Error saving accessInfo backup", e);
			}
		}, 0, Long.parseLong(
				Configuration.getProperties().getProperty(Configuration.BACKUP_REFRESH_MINS, "60")), 
				TimeUnit.MINUTES);
	}

	private void loadConfig(String[] args) {
		try {
			Configuration.load(args);
		} catch (IOException e) {
			LOGGER.logp(Level.SEVERE, CLAZZ, "main", "Error loading application properties", e);
			System.exit(1);
		}
	}
	
	public AccessControlClient getClient() {
		return this.client;
	}
	
	public AccessInfo getAccessInfo() {
		return this.accessInfo;
	}
}

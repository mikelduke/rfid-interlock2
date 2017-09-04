package com.mikelduke.rfid.interlock2;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mikelduke.rfid.interlock2.makertracker.MakerTrackerClient;

import spark.Spark;

public class RFIDApplication {
	private static final String CLAZZ = RFIDApplication.class.getName();
	private static final Logger LOGGER = Logger.getLogger(CLAZZ);
	
	private volatile AccessInfo accessInfo = null;
	
	private final AccessControlClient client;
	private final ConsoleReader consoleReader;
	private final Thread consoleReaderThread;
	
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
		
		consoleReader = new ConsoleReader(client);
		consoleReaderThread = new Thread(consoleReader);
		consoleReaderThread.start();
		
		//TODO Need a timer to update and save the accessInfo object for use as a backup when service is down
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

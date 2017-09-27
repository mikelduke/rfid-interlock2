package com.mikelduke.rfid.interlock2.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mikelduke.rfid.interlock2.AccessControlClient;
import com.mikelduke.rfid.interlock2.InterlockController;

public class ConsoleReader implements RFIDReader {
	private static final String CLAZZ = ConsoleReader.class.getName();
	private static final Logger LOGGER = Logger.getLogger(CLAZZ);
	
	private boolean run = true;
	private AccessControlClient client;
	private InputStream is;
	
	@Override
	public void configure(AccessControlClient client, Properties p) {
		this.client = client;
		this.is = System.in;
	}
	
	@Override
	public void start() {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		while (run) {
			System.out.print("Enter ID: ");
			String input;
			try {
				input = br.readLine();
				handleInput(input);
			} catch (IOException e) {
				LOGGER.logp(Level.SEVERE, CLAZZ, "run", "Error Reading Console", e);
				run = false;
			}
		}
	}
	
	@Override
	public void stop() {
		run = false;
	}

	private void handleInput(String input) {
		if (input.isEmpty()) {
			//do nothing
		} else if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit")) {
			LOGGER.info("Exiting Application");
			System.exit(0);
			return;
		} else if (input.equalsIgnoreCase("stop")) {
			InterlockController.getInstance("1").cancel();
		} else if (input.equalsIgnoreCase("getAccessInfo")) {
			System.out.println(client.getAccessInfo());
		} else if (input.equalsIgnoreCase("timeleft")) {
			System.out.println("Time Left: " + InterlockController.getInstance("1").getTimeLeft());
		} else {
			try {
				long accessTimeMS = client.getAccessTime(input);
			
				System.out.println("Access Time: " + accessTimeMS); //TODO Enable power for some time
				
				InterlockController.getInstance("1").enable(accessTimeMS); //TODO Just one instance for now
			} catch (Exception e) {
				LOGGER.logp(Level.SEVERE, CLAZZ, "handleInput", "Error thrown when retrieving access time", e);
			}
		}
	}
}

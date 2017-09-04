package com.mikelduke.rfid.interlock2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsoleReader implements Runnable {
	private static final String CLAZZ = ConsoleReader.class.getName();
	private static final Logger LOGGER = Logger.getLogger(CLAZZ);
	
	private boolean run = true;
	private final AccessControlClient client;
	
	public ConsoleReader(AccessControlClient client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
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

	private void handleInput(String input) {
		if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit")) {
			LOGGER.info("Exiting Application");
			System.exit(0);
			return;
		} else if (input.equalsIgnoreCase("stop")) {
			//TODO Turn off power, also need to listen for a hardware button too
		} else if (input.equalsIgnoreCase("getAccessInfo")) {
			System.out.println(client.getAccessInfo());
		} else {
			try {
				long accessTimeMS = client.getAccessTime(input);
			
				System.out.println("Access Time: " + accessTimeMS); //TODO Enable power for some time
			} catch (Exception e) {
				LOGGER.logp(Level.SEVERE, CLAZZ, "handleInput", "Error thrown when retrieving access time", e);
			}
		}
	}
}

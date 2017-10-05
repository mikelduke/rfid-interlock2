package com.mikelduke.rfid.interlock2.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mikelduke.rfid.interlock2.event.Event;
import com.mikelduke.rfid.interlock2.event.EventType;

public class ConsoleReader extends AbstractRFIDReader {
	private static final String CLAZZ = ConsoleReader.class.getName();
	private static final Logger LOGGER = Logger.getLogger(CLAZZ);
	
	private boolean run = true;
	private InputStream is;
	
	@Override
	public void configure(Properties p) {
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
			notifyListeners(new Event(EventType.EXIT, "Exit requested"));
		} else if (input.equalsIgnoreCase("stop")) {
			notifyListeners(new Event(EventType.STOP, "Stop requested"));
		} else if (input.equalsIgnoreCase("getAccessInfo")) {
			notifyListeners(new Event(EventType.INFO, "InfoRequested"));
		} else if (input.equalsIgnoreCase("timeleft")) {
			notifyListeners(new Event(EventType.TIME_LEFT, "Get Time Left"));
		} else {
			notifyListeners(new Event(EventType.RFID_READ, input));
		}
	}
}

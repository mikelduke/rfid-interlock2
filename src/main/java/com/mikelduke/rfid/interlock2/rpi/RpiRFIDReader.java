package com.mikelduke.rfid.interlock2.rpi;

import java.util.Properties;

import com.mikelduke.rfid.interlock2.AccessControlClient;
import com.mikelduke.rfid.interlock2.io.RFIDReader;

public class RpiRFIDReader implements RFIDReader {

	private AccessControlClient client;
	private boolean run = true;
	
	@Override
	public void configure(AccessControlClient client, Properties p) {
		this.client = client;
	}

	@Override
	public void start() {
		while (run) {
			String rfid = readRFID();
			handle(rfid);
		}
	}

	@Override
	public void stop() {
		run = false;
	}
	
	private String readRFID() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void handle(String rfid) {
		
	}
}

package com.mikelduke.rfid.interlock2.io;

import java.util.Properties;

import com.mikelduke.rfid.interlock2.AccessControlClient;

public interface RFIDReader {
	public void configure(AccessControlClient client, Properties p);
	public void start();
	public void stop();
}

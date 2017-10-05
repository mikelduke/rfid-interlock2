package com.mikelduke.rfid.interlock2.io;

import java.util.Properties;

import com.mikelduke.rfid.interlock2.event.EventSource;

public interface RFIDReader extends EventSource {
	public void configure(Properties p);
	public void start();
	public void stop();
}

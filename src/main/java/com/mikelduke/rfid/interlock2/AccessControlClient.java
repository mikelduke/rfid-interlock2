package com.mikelduke.rfid.interlock2;

import java.util.Properties;

public interface AccessControlClient {
	public void configure(Properties properties);
	public long getAccessTime(String rfid);
	public AccessInfo getAccessInfo();
}

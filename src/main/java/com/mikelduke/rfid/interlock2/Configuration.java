package com.mikelduke.rfid.interlock2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Configuration {
	private static final Properties PROPERTIES = new Properties();
	
	public static final String DEFAULT_NAME = "rfid.properties";
	public static final String TOOL_ID = "tool-id";
	
	public static final String PORT = "port";
	public static final String BACKUP_REFRESH_MINS = "backup-refresh-mins";
	public static final String ENABLE_BACKUP = "backup-refresh-enabled";
	public static final String ENABLE_CONSOLE = "enable-console";
	public static final String INTERLOCK_IMPL = "interlock";
	public static final String READER_IMPL = "rfid-reader";
	
	public static void load() throws FileNotFoundException, IOException {
		load(null);
	}
	
	public static void load(String[] args) throws FileNotFoundException, IOException {
		if (args != null && args.length > 0) {
			PROPERTIES.load(new FileInputStream(args[0]));
		} else {
			PROPERTIES.load(new FileInputStream(DEFAULT_NAME));
		}
	}
	
	public static Properties getProperties() {
		return PROPERTIES;
	}
	
	public static String getProperty(String key) {
		return PROPERTIES.getProperty(key);
	}
	
	public static String getProperty(String key, String defaultValue) {
		return PROPERTIES.getProperty(key, defaultValue);
	}
}

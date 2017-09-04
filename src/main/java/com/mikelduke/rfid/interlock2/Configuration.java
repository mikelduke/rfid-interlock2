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
}

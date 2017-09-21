package com.mikelduke.rfid.interlock2.io;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public interface Interlock {
	public String getName();
	public void configure(Properties config);
	
	public void enable();
	public void disable();
	
	static class Factory {
		private static Map<String, Interlock> interlocks = new HashMap<>();
		
		private Factory() { }
		
		public static Interlock getInterlock(String name) {
			return interlocks.get(name);
		}
		
		public static Interlock getNewInterlock(String name, String className) 
				throws ClassNotFoundException, InstantiationException, IllegalAccessException {

			if (interlocks.get(name) != null) {
				throw new IllegalStateException("Interlock named " + name + " already created");
			}
			
			Class<?> c = Class.forName(className);
			
			Interlock i = (Interlock) c.newInstance();
			interlocks.put(name, i);
			
			return i;
		}
	}
}

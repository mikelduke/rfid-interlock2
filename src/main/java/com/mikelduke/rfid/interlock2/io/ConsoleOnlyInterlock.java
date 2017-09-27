package com.mikelduke.rfid.interlock2.io;

import java.util.Properties;

public class ConsoleOnlyInterlock implements Interlock {

	@Override
	public String getName() {
		return "Console Testing Interlock";
	}

	@Override
	public void configure(Properties config) {
		//Nothing to configure
	}

	@Override
	public void enable() {
		System.out.println("ConsoleInterlock: Enabled");
	}

	@Override
	public void disable() {
		System.out.println("ConsoleInterlock: Disabled");
	}
}

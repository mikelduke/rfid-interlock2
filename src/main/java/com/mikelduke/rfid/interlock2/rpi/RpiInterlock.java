package com.mikelduke.rfid.interlock2.rpi;

import java.util.Properties;

import com.mikelduke.rfid.interlock2.io.Interlock;

public class RpiInterlock implements Interlock {

	@Override
	public String getName() {
		return "RaspberryPi Interlock";
	}

	@Override
	public void configure(Properties config) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enable() {
		// TODO Auto-generated method stub
		System.out.println("RPI Enable");
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		System.out.println("RPI Disable");
	}

}

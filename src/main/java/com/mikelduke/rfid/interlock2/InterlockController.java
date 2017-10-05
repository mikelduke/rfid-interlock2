package com.mikelduke.rfid.interlock2;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.mikelduke.rfid.interlock2.io.ConsoleOnlyInterlock;
import com.mikelduke.rfid.interlock2.io.Interlock;

public class InterlockController {
	public static final String DEFAULT = "default";
	
	private static Map<String, InterlockController> instances = new HashMap<>();
	
	private final ScheduledExecutorService interlockContollerExecutorService = Executors.newSingleThreadScheduledExecutor();
	private final String name;
	
	private volatile ScheduledFuture<?> currentUnlockTask;
	private Interlock interlock;
	
	public static InterlockController getInstance(String name) {
		if (instances.get(name) == null) {
			synchronized (instances) {
				if (instances.get(name) == null) {
					InterlockController instance = new InterlockController(name);
					instances.put(name, instance);
				}
			}
		}

		return instances.get(name);
	}

	private InterlockController(String name) {
		this.name = name;
	}

	public void configure(Properties config) {
		try {
			interlock = Interlock.Factory.
					getNewInterlock(this.name, 
							Configuration.getProperty(Configuration.INTERLOCK_IMPL,
									ConsoleOnlyInterlock.class.getName()));
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			System.err.println("Error loading interlock implementation");
			e.printStackTrace();
		}
	}

	public void enable(long ms) {
		System.out.println("Enabling Interlock access for " + ms + " ms");
		
		if (this.currentUnlockTask != null && !this.currentUnlockTask.isDone()) {
			currentUnlockTask.cancel(true);
		}
		
		interlock.enable();
		
		this.currentUnlockTask = interlockContollerExecutorService.schedule(()-> {
			System.out.println("Interlock disabled, time is up");
			interlock.disable();
		}, ms, TimeUnit.MILLISECONDS);
	}
	
	public boolean isEnabled() {
		if (currentUnlockTask == null) {
			return false;
		}
		
		return this.currentUnlockTask.isDone();
	}
	
	public void cancel() {
		if (currentUnlockTask == null) {
			return;
		}
		
		if (!this.currentUnlockTask.isDone()) {
			this.currentUnlockTask.cancel(true);
		}
		
		interlock.disable();
		System.out.println("Cancelled");
	}
	
	public long getTimeLeft() {
		return currentUnlockTask.getDelay(TimeUnit.MILLISECONDS);
	}
}

package com.mikelduke.rfid.interlock2;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class InterlockController {
	//TODO This should be the interface to control the interlock hardware

	private static Map<String, InterlockController> instances = new HashMap<>();
	
	private final ScheduledExecutorService interlockContollerExecutorService = Executors.newSingleThreadScheduledExecutor();
	
	private volatile ScheduledFuture<?> currentUnlockTask;
	
	public static InterlockController getInstance(String name) {
		if (instances.get(name) == null) {
			synchronized (instances) {
				if (instances.get(name) == null) {
					InterlockController instance = new InterlockController();
					instances.put(name, instance);
				}
			}
		}

		return instances.get(name);
	}

	private InterlockController() { }

	public void configure(Configuration config) {
		//TODO load ports etc
	}

	public void enable(long ms) {
		System.out.println("Enabling Interlock access for " + ms + " ms");
		
		if (this.currentUnlockTask != null && !this.currentUnlockTask.isDone()) {
			currentUnlockTask.cancel(true);
		}
		
		//TODO Turn on
		
		this.currentUnlockTask = interlockContollerExecutorService.schedule(()-> {
			System.out.println("Interlock disabled, time is up");
			//TODO Turn off
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
		
		//TODO Turn off
		
		System.out.println("Cancelled");
	}
	
	public long getTimeLeft() {
		return currentUnlockTask.getDelay(TimeUnit.MILLISECONDS);
	}
}

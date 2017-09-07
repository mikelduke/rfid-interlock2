package com.mikelduke.rfid.interlock2;

public class BackupListTask implements Runnable {

	private final AccessControlClient client;
	
	public BackupListTask(AccessControlClient client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}

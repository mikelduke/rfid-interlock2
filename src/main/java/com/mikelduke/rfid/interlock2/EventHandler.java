package com.mikelduke.rfid.interlock2;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mikelduke.rfid.interlock2.event.Event;
import com.mikelduke.rfid.interlock2.event.EventListener;
import com.mikelduke.rfid.interlock2.event.EventType;

public class EventHandler implements EventListener {
	private static final String CLAZZ = EventHandler.class.getName();
	private static final Logger LOGGER = Logger.getLogger(CLAZZ);
	
	private AccessControlClient client;
	private RFIDApplication app;
	
	public void configure(RFIDApplication app, AccessControlClient client, Properties p) {
		this.client = client;
		this.app = app;
	}

	@Override
	public void update(Event e) {
		if (e.getType().equals(EventType.EXIT)) {
			LOGGER.info("Exiting Application");
			System.exit(0);
		} 
		else if (e.getType().equals(EventType.RFID_READ)) {
			try {
				long accessTimeMS = client.getAccessTime(e.getMessage());
				LOGGER.logp(Level.INFO, CLAZZ, "update", "Access Time: " + accessTimeMS);
				
				InterlockController.getInstance(InterlockController.DEFAULT).enable(accessTimeMS);
			} catch (Exception ex) {
				LOGGER.logp(Level.SEVERE, CLAZZ, "update", "Error thrown when retrieving access time", ex);
				
				if (app.getAccessInfo().getRfidList().contains(e.getMessage())) {
					LOGGER.logp(Level.INFO, CLAZZ, "update", 
							"Enabling Interlock using backup info for user " + e.getMessage() + 
							" and time " + app.getAccessInfo().getAccessTimeMS());
					InterlockController.getInstance(InterlockController.DEFAULT).enable(app.getAccessInfo().getAccessTimeMS());
				}
			}
		} else if (e.getType().equals(EventType.STOP)) {
			InterlockController.getInstance(InterlockController.DEFAULT).cancel();
		} else if (e.getType().equals(EventType.INFO)) {
			LOGGER.logp(Level.INFO, CLAZZ, "update", app.getAccessInfo().toString());
		} else if (e.getType().equals(EventType.TIME_LEFT)) {
			long time = InterlockController.getInstance(InterlockController.DEFAULT).getTimeLeft();
			time = Math.max(time,  0);
			LOGGER.logp(Level.INFO, CLAZZ, "update", "Time Left: " + time);
		} else {
			LOGGER.logp(Level.WARNING, CLAZZ, "update", "Unhandled Event Type: " + e.getType() + " for event " + e);
		}
	}
}

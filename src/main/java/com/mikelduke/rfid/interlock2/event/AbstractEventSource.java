package com.mikelduke.rfid.interlock2.event;

import java.util.ArrayList;
import java.util.List;

public class AbstractEventSource implements EventSource {

	protected List<EventListener> listeners = new ArrayList<>();
	
	@Override
	public void addListener(EventListener e) {
		listeners.add(e);
	}

	@Override
	public void removeListener(EventListener e) {
		listeners.remove(e);
	}

	@Override
	public void notifyListeners(Event event) {
		for (EventListener el : this.listeners) {
			el.update(event);
		}
	}

}

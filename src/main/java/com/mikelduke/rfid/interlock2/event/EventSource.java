package com.mikelduke.rfid.interlock2.event;

public interface EventSource {
	public void addListener(EventListener e);
	public void removeListener(EventListener e);
	public void notifyListeners(Event event);
}

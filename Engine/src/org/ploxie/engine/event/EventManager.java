package org.ploxie.engine.event;

import java.util.ArrayList;
import java.util.List;

public class EventManager {

	private List<EventListener> listeners = new ArrayList<EventListener>();
	
	public void register(final EventListener listener) {
		listeners.add(listener);
	}
	
	public void remove(final EventListener listener) {
		listeners.remove(listener);
	}
	
	public void dispatch(final Event event) {
		for(EventListener l : listeners) {
			event.dispatch(l);
		}
	}
	
	
}

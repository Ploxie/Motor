package org.ploxie.engine.event;

public abstract class Event {

	protected abstract void dispatch(EventListener listener);
	
}

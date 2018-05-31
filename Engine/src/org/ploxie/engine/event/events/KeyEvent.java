package org.ploxie.engine.event.events;

import org.ploxie.engine.event.Event;
import org.ploxie.engine.event.EventListener;

public class KeyEvent extends Event{

	private final int key;
	private final boolean released;
	private final boolean held;
	
	public KeyEvent(int key) {
		this.key = key;
		this.held = false;
		this.released = false;
	}
	
	public KeyEvent(int key, boolean down, boolean released) {
		this.key = key;
		this.held = down;
		this.released = released;
	}
	
	public int getKey() {
		return key;
	}	
		
	@Override
	protected void dispatch(EventListener listener) {
		if(!(listener instanceof KeyboardListener)) {
			return;
		}
		KeyboardListener keyListener = (KeyboardListener) listener;
		
		if(released) {
			
			keyListener.keyReleased(this);
			return;
		}
		
		if(held) {
			keyListener.keyHeld(this);
			
			return;
		}
		
		keyListener.keyPressed(this);
	}

}

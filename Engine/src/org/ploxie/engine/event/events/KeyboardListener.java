package org.ploxie.engine.event.events;

import org.ploxie.engine.event.EventListener;

public interface KeyboardListener extends EventListener{

	public void keyPressed(KeyEvent event);
	public void keyReleased(KeyEvent event);
	public void keyHeld(KeyEvent event);
	
}

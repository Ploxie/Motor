package org.ploxie.engine.event.events;

import org.ploxie.engine.event.EventListener;

public interface MouseListener extends EventListener{

	public void onMouseDown(MouseKeyEvent event);
	public void onMouseRelease(MouseKeyEvent event);
	
	public void onMouseHeld(MouseKeyEvent event);
	
	public void onMouseMove(MouseMoveEvent event);

	
}

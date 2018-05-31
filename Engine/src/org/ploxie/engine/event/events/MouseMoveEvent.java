package org.ploxie.engine.event.events;

import org.ploxie.engine.event.Event;
import org.ploxie.engine.event.EventListener;
import org.ploxie.utils.math.vector.Vector2i;

public class MouseMoveEvent extends Event{

	private final Vector2i position;
	private final Vector2i delta;
	
	public MouseMoveEvent(Vector2i position, Vector2i delta) {
		this.position = position;
		this.delta = delta;
	}
	
	public Vector2i getPosition() {
		return position;
	}
	
	public Vector2i getDelta() {
		return delta;
	}
	
	@Override
	protected void dispatch(EventListener listener) {
		if((listener instanceof MouseListener)) {
			MouseListener keyListener = (MouseListener) listener;
			keyListener.onMouseMove(this);
		}
	}

}

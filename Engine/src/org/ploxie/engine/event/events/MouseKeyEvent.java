package org.ploxie.engine.event.events;

import org.ploxie.engine.event.Event;
import org.ploxie.engine.event.EventListener;
import org.ploxie.utils.math.vector.Vector2i;

public class MouseKeyEvent extends Event {

	private final Vector2i position;
	private final int button;
	private final boolean isHeld;
	private final boolean isReleased;
	

	public MouseKeyEvent(Vector2i position) {
		this.position = position;
		this.isHeld = false;
		this.button = -1;
		this.isReleased = false;
	}

	public MouseKeyEvent(Vector2i position, int button) {
		this.position = position;
		this.button = button;
		this.isHeld = false;
		this.isReleased = false;
	}

	public MouseKeyEvent(Vector2i position, int button, boolean isDown, boolean isReleased) {
		this.position = position;
		this.button = button;
		this.isHeld = isDown;
		this.isReleased = isReleased;
	}

	public Vector2i getPosition() {
		return position;
	}
	
	public int getButton() {
		return button;
	}
	
	public boolean isDown() {
		return isHeld;
	}

	@Override
	protected void dispatch(EventListener listener) {
		if (!(listener instanceof MouseListener)) {
			return;
		}
		MouseListener keyListener = (MouseListener) listener;

		if(isReleased) {
			keyListener.onMouseRelease(this);
			return;
		}
		
		if (isHeld) {
			keyListener.onMouseHeld(this);
			return;
		}

		keyListener.onMouseDown(this);

	}

}

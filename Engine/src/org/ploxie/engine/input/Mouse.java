package org.ploxie.engine.input;

import org.ploxie.engine.display.listeners.MouseButtonListener;
import org.ploxie.engine.display.listeners.MouseMovementListener;
import org.ploxie.utils.math.vector.Vector2i;

public class Mouse implements MouseMovementListener, MouseButtonListener {
	
	private boolean[] isPressed = new boolean[256];
	private Vector2i location = new Vector2i();
	private Vector2i delta = new Vector2i();

	public Vector2i getLocation() {
		return location;
	}
	
	public Vector2i getDelta(){
		return delta;
	}

	public boolean isPressed(final int buttonID) {
		if (buttonID >= 0 && buttonID < isPressed.length) {
			return isPressed[buttonID];
		}
		return false;
	}

	@Override
	public void onMove(Vector2i location, Vector2i delta) {
		this.location = location;
		this.delta = delta;
	}

	@Override
	public void onPress(int buttonID, Vector2i location) {
		if (buttonID >= 0 && buttonID < isPressed.length) {
			isPressed[buttonID] = true;
		}
	}

	@Override
	public void onRelease(int buttonID, Vector2i location) {
		if (buttonID >= 0 && buttonID < isPressed.length) {
			isPressed[buttonID] = false;
		}
	}

	public static final int BUTTON_1 = 0;
	public static final int BUTTON_2 = 1;
	public static final int BUTTON_3 = 2;
	public static final int BUTTON_4 = 3;
	public static final int BUTTON_5 = 4;
	public static final int BUTTON_6 = 5;
	public static final int BUTTON_7 = 6;
	public static final int BUTTON_8 = 7;
	public static final int BUTTON_LAST = 7;
	public static final int BUTTON_LEFT = 0;
	public static final int BUTTON_RIGHT = 1;
	public static final int BUTTON_MIDDLE = 2;

}
package org.ploxie.engine2.input;

import org.ploxie.utils.math.vector.Vector2f;

public interface Input {

	public void create(long windowId);
	public void update();
	public void shutdown();
	
	public boolean isKeyPushed(int key);
	public boolean isKeyHolding(int key);
	public boolean isKeyReleased(int key);
	public boolean isButtonPushed(int key);
	public boolean isButtonHolding(int key);
	public boolean isButtonReleased(int key);
	
	public float getScrollOffset();
	public Vector2f getCursorPosition();
	public Vector2f getLockedCursorPosition();
	
}

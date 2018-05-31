package org.ploxie.engine.display.listeners;

import org.ploxie.utils.math.vector.Vector2f;

public interface MouseScrollListener {

	/**
	 * Will be called when a scrolling device is used, such as a mouse wheel or
	 * scrolling area of a touchpad.
	 * 
	 * @param offset
	 */
	public default void onScroll(final Vector2f offset) {
	}

}
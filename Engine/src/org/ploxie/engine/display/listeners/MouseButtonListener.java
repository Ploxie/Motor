package org.ploxie.engine.display.listeners;

import org.ploxie.utils.math.vector.Vector2i;

public interface MouseButtonListener {

	public default void onPress(final int buttonID, final Vector2i location) {
	}

	public default void onRelease(final int buttonID, final Vector2i location) {
	}

}
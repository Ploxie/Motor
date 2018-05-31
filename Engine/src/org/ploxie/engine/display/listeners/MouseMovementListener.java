package org.ploxie.engine.display.listeners;

import org.ploxie.utils.math.vector.Vector2i;

public interface MouseMovementListener {

	public default void onMove(final Vector2i location, final Vector2i delta) {
	}

}
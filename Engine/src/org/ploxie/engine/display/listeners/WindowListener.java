package org.ploxie.engine.display.listeners;

import org.ploxie.utils.math.vector.Vector2i;

public interface WindowListener {

	public default void onResize(final Vector2i newDimensions) {

	}

	public default void onMove(final Vector2i newLocation) {

	}

	public default void onClose() {

	}

	public default void onMinimized(final boolean minimized) {

	}

	public default void onRefresh() {

	}

}
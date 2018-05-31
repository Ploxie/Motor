package org.ploxie.engine.display.listeners;

public interface KeyListener {

	public default void onPress(final int key, final int mods, final boolean repeat) {
	}

	public default void onRelease(final int key, final int mods) {
	}

	public default void onType(final int character) {
	}

}
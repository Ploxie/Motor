package org.ploxie.engine.display;

import java.util.ArrayList;
import java.util.List;

import org.ploxie.engine.display.listeners.FocusListener;
import org.ploxie.engine.display.listeners.KeyListener;
import org.ploxie.engine.display.listeners.MouseButtonListener;
import org.ploxie.engine.display.listeners.MouseMovementListener;
import org.ploxie.engine.display.listeners.MouseScrollListener;
import org.ploxie.engine.input.Keyboard;
import org.ploxie.engine.input.Mouse;
import org.ploxie.utils.math.vector.Vector2i;

import lombok.Data;

public interface IDrawSurface {

	@Data
	public static class DrawingSurfaceEventHandler {

		private Keyboard keyboard = new Keyboard();
		private Mouse mouse = new Mouse();

		private List<MouseMovementListener> mouseMovementListeners = new ArrayList<MouseMovementListener>();
		private List<MouseButtonListener> mouseButtonListeners = new ArrayList<MouseButtonListener>();
		private List<MouseScrollListener> mouseScrollListeners = new ArrayList<MouseScrollListener>();
		private List<KeyListener> keyListeners = new ArrayList<KeyListener>();
		private List<FocusListener> focusListeners = new ArrayList<FocusListener>();

	}

	public abstract long getHandle();
	
	public abstract DrawingSurfaceEventHandler getEventHandler();

	public abstract Vector2i getDimensions();

	public default void preRender() {

	}

	public default void postRender() {

	}

	public abstract void pollEvents();

	public abstract void setVsync(boolean vsync);

	public abstract boolean isVsync();

	public abstract boolean isFocused();

	public abstract boolean isDestroyed();
	
	public abstract void show();

	public default Mouse getMouse() {
		return getEventHandler().getMouse();
	}

	public default Keyboard getKeyboard() {
		return getEventHandler().getKeyboard();
	}

	public default List<MouseMovementListener> getMouseMovementListeners() {
		return getEventHandler().getMouseMovementListeners();
	}

	public default void addMouseMovementListener(final MouseMovementListener listener) {
		DrawingSurfaceEventHandler eventHandler = getEventHandler();
		List<MouseMovementListener> mouseMovementListeners = eventHandler.getMouseMovementListeners();
		synchronized (mouseMovementListeners) {
			mouseMovementListeners.add(listener);
		}
	}

	public default void removeMouseMovementListener(final MouseMovementListener listener) {
		DrawingSurfaceEventHandler eventHandler = getEventHandler();
		List<MouseMovementListener> mouseMovementListeners = eventHandler.getMouseMovementListeners();
		synchronized (mouseMovementListeners) {
			mouseMovementListeners.remove(listener);
		}
	}

	public default void clearMouseMovementListeners() {
		DrawingSurfaceEventHandler eventHandler = getEventHandler();
		List<MouseMovementListener> mouseMovementListeners = eventHandler.getMouseMovementListeners();
		synchronized (mouseMovementListeners) {
			mouseMovementListeners.clear();
		}
		addMouseMovementListener(eventHandler.getMouse());
	}

	public default List<MouseButtonListener> getMouseButtonListeners() {
		return getEventHandler().getMouseButtonListeners();
	}

	public default void addMouseButtonListener(final MouseButtonListener listener) {
		DrawingSurfaceEventHandler eventHandler = getEventHandler();
		List<MouseButtonListener> mouseButtonListeners = eventHandler.getMouseButtonListeners();
		synchronized (mouseButtonListeners) {
			mouseButtonListeners.add(listener);
		}
	}

	public default void removeMouseButtonListener(final MouseButtonListener listener) {
		DrawingSurfaceEventHandler eventHandler = getEventHandler();
		List<MouseButtonListener> mouseButtonListeners = eventHandler.getMouseButtonListeners();
		synchronized (mouseButtonListeners) {
			mouseButtonListeners.remove(listener);
		}
	}

	public default void clearMouseButtonListeners() {
		DrawingSurfaceEventHandler eventHandler = getEventHandler();
		List<MouseButtonListener> mouseButtonListeners = eventHandler.getMouseButtonListeners();
		synchronized (mouseButtonListeners) {
			mouseButtonListeners.clear();
		}
		addMouseButtonListener(eventHandler.getMouse());
	}

	public default List<MouseScrollListener> getMouseScrollListeners() {
		return getEventHandler().getMouseScrollListeners();
	}

	public default void addMouseScrollListener(final MouseScrollListener listener) {
		DrawingSurfaceEventHandler eventHandler = getEventHandler();
		List<MouseScrollListener> mouseScrollListeners = eventHandler.getMouseScrollListeners();
		synchronized (mouseScrollListeners) {
			mouseScrollListeners.add(listener);
		}
	}

	public default void removeMouseScrollListener(final MouseScrollListener listener) {
		DrawingSurfaceEventHandler eventHandler = getEventHandler();
		List<MouseScrollListener> mouseScrollListeners = eventHandler.getMouseScrollListeners();
		synchronized (mouseScrollListeners) {
			mouseScrollListeners.remove(listener);
		}
	}

	public default void clearMouseScrollListeners() {
		DrawingSurfaceEventHandler eventHandler = getEventHandler();
		List<MouseScrollListener> mouseScrollListeners = eventHandler.getMouseScrollListeners();
		synchronized (mouseScrollListeners) {
			mouseScrollListeners.clear();
		}
	}

	// else incompatible with Canvas class
	public default List<KeyListener> getKeyInputListeners() {
		return getEventHandler().getKeyListeners();
	}

	public default void addKeyListener(final KeyListener listener) {
		DrawingSurfaceEventHandler eventHandler = getEventHandler();
		List<KeyListener> keyListeners = eventHandler.getKeyListeners();
		synchronized (keyListeners) {
			keyListeners.add(listener);
		}
	}

	public default void removeKeyListener(final KeyListener listener) {
		DrawingSurfaceEventHandler eventHandler = getEventHandler();
		List<KeyListener> keyListeners = eventHandler.getKeyListeners();
		synchronized (keyListeners) {
			keyListeners.remove(listener);
		}
	}

	public default void clearKeyListeners() {
		DrawingSurfaceEventHandler eventHandler = getEventHandler();
		List<KeyListener> keyListeners = eventHandler.getKeyListeners();
		synchronized (keyListeners) {
			keyListeners.clear();
			keyListeners.add(eventHandler.getKeyboard());
		}
	}

	// else incompatible with Canvas class
	public default List<FocusListener> getFocusChangeListeners() {
		return getEventHandler().getFocusListeners();
	}

	public default void addFocusListener(final FocusListener listener) {
		DrawingSurfaceEventHandler eventHandler = getEventHandler();
		List<FocusListener> focusListeners = eventHandler.getFocusListeners();
		synchronized (focusListeners) {
			focusListeners.add(listener);
		}
	}

	public default void removeFocusListener(final FocusListener listener) {
		DrawingSurfaceEventHandler eventHandler = getEventHandler();
		List<FocusListener> focusListeners = eventHandler.getFocusListeners();
		synchronized (focusListeners) {
			focusListeners.remove(listener);
		}
	}

	public default void clearFocusListeners() {
		DrawingSurfaceEventHandler eventHandler = getEventHandler();
		List<FocusListener> focusListeners = eventHandler.getFocusListeners();
		synchronized (focusListeners) {
			focusListeners.clear();
		}
	}

}
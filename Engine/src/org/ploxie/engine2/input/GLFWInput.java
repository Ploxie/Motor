package org.ploxie.engine2.input;

import java.util.HashSet;
import java.util.Set;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.ploxie.utils.math.vector.Vector2f;

public class GLFWInput implements Input {

	private Set<Integer> pushedKeys = new HashSet<>();
	private Set<Integer> keysHolding = new HashSet<>();
	private Set<Integer> releasedKeys = new HashSet<>();

	private Set<Integer> pushedButtons = new HashSet<>();
	private Set<Integer> buttonsHolding = new HashSet<>();
	private Set<Integer> releasedButtons = new HashSet<>();

	private Vector2f cursorPosition;
	private Vector2f lockedCursorPosition;
	private float scrollOffset;

	private boolean pause = false;

	private GLFWKeyCallback keyCallback;

	private GLFWCursorPosCallback cursorPosCallback;

	private GLFWMouseButtonCallback mouseButtonCallback;

	private GLFWScrollCallback scrollCallback;

	private GLFWFramebufferSizeCallback framebufferSizeCallback;

	public GLFWInput() {
		cursorPosition = new Vector2f();
	}

	public void create(long window) {

		GLFW.glfwSetFramebufferSizeCallback(window, (framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
			@Override
			public void invoke(long window, int width, int height) {
				// Todo
			}
		}));

		GLFW.glfwSetKeyCallback(window, (keyCallback = new GLFWKeyCallback() {

			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (action == GLFW.GLFW_PRESS) {
					if (!pushedKeys.contains(key)) {
						pushedKeys.add(key);
						keysHolding.add(key);
					}
				}

				if (action == GLFW.GLFW_RELEASE) {
					keysHolding.remove(new Integer(key));
					releasedKeys.add(key);
				}
			}
		}));

		GLFW.glfwSetMouseButtonCallback(window, (mouseButtonCallback = new GLFWMouseButtonCallback() {

			@Override
			public void invoke(long window, int button, int action, int mods) {
				if (button == 2 && action == GLFW.GLFW_PRESS) {
					lockedCursorPosition = new Vector2f(cursorPosition);
					GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
				}

				if (button == 2 && action == GLFW.GLFW_RELEASE) {
					GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
				}

				if (action == GLFW.GLFW_PRESS) {
					if (!pushedButtons.contains(button)) {
						pushedButtons.add(button);
						buttonsHolding.add(button);
					}
				}

				if (action == GLFW.GLFW_RELEASE) {
					releasedButtons.add(button);
					buttonsHolding.remove(new Integer(button));
				}
			}
		}));

		GLFW.glfwSetCursorPosCallback(window, (cursorPosCallback = new GLFWCursorPosCallback() {

			@Override
			public void invoke(long window, double xpos, double ypos) {
				cursorPosition.x((float) xpos);
				cursorPosition.y((float) ypos);
			}

		}));

		GLFW.glfwSetScrollCallback(window, (scrollCallback = new GLFWScrollCallback() {

			@Override
			public void invoke(long window, double xoffset, double yoffset) {
				setScrollOffset((float) yoffset);
			}
		}));
	}

	public void update() {
		setScrollOffset(0);
		pushedKeys.clear();
		releasedKeys.clear();
		pushedButtons.clear();
		releasedButtons.clear();
		GLFW.glfwPollEvents();
	}

	@Override
	public void shutdown() {
		keyCallback.free();
		cursorPosCallback.free();
		mouseButtonCallback.free();
		scrollCallback.free();
		framebufferSizeCallback.free();
	}

	public boolean isKeyPushed(int key) {
		return pushedKeys.contains(key);
	}

	public boolean isKeyReleased(int key) {
		return releasedKeys.contains(key);
	}

	public boolean isKeyHolding(int key) {
		return keysHolding.contains(key);
	}

	public boolean isButtonPushed(int key) {
		return pushedButtons.contains(key);
	}

	public boolean isButtonReleased(int key) {
		return releasedButtons.contains(key);
	}

	public boolean isButtonHolding(int key) {
		return buttonsHolding.contains(key);
	}

	public boolean isPause() {
		return pause;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}

	public Vector2f getCursorPosition() {
		return cursorPosition;
	}

	public void setCursorPosition(Vector2f cursorPosition, long window) {
		this.cursorPosition = cursorPosition;

		GLFW.glfwSetCursorPos(window, cursorPosition.x(), cursorPosition.y());
	}

	public Vector2f getLockedCursorPosition() {
		return lockedCursorPosition;
	}

	public void setLockedCursorPosition(Vector2f lockedCursorPosition) {
		this.lockedCursorPosition = lockedCursorPosition;
	}

	public Set<Integer> getPushedKeys() {
		return pushedKeys;
	}

	public Set<Integer> getButtonsHolding() {
		return buttonsHolding;
	}

	public float getScrollOffset() {
		return scrollOffset;
	}

	public void setScrollOffset(float scrollOffset) {
		this.scrollOffset = scrollOffset;
	}

	public Set<Integer> getKeysHolding() {
		return keysHolding;
	}

	public Set<Integer> getPushedButtons() {
		return pushedButtons;
	}

}

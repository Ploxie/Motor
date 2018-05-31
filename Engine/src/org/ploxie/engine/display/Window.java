package org.ploxie.engine.display;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.glfw.GLFWVulkan.*;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowIconifyCallback;
import org.lwjgl.glfw.GLFWWindowPosCallback;
import org.lwjgl.glfw.GLFWWindowRefreshCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.ploxie.engine.display.listeners.FocusListener;
import org.ploxie.engine.display.listeners.KeyListener;
import org.ploxie.engine.display.listeners.MouseButtonListener;
import org.ploxie.engine.display.listeners.MouseMovementListener;
import org.ploxie.engine.display.listeners.MouseScrollListener;
import org.ploxie.engine.display.listeners.WindowListener;
import org.ploxie.engine.input.Mouse;
import org.ploxie.utils.Color;
import org.ploxie.utils.math.vector.Vector2f;
import org.ploxie.utils.math.vector.Vector2i;

import lombok.Getter;
import lombok.Setter;

public class Window implements IDrawSurface {

	@Getter
	private Vector2i location = new Vector2i();
	@Getter
	private Vector2i dimensions;
	
	@Getter
	@Setter
	private Color clearColor = new Color(0,0,0,1);

	@Getter
	@Setter
	private WindowMode mode;

	@Getter
	private long handle = 0;

	@Getter
	private String title = "Window";

	@Getter
	private boolean vsync = false;
	@Getter
	private boolean focused = false;
	@Getter
	private boolean offscreen = false;
	@Getter
	private boolean alwaysOnTop = false;
	@Getter
	@Setter
	private int samples = 1;

	private boolean minimized = false;
	
	private Vector2i mouseLocation = new Vector2i(0, 0);
	private Vector2i oldMouseCoords = null;
	
	private List<WindowListener> windowListeners = new ArrayList<WindowListener>();

	private GLFWCursorPosCallback cursorPosCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	private GLFWScrollCallback scrollCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWCharCallback charCallback;

	private GLFWWindowSizeCallback windowSizeCallback;
	private GLFWWindowPosCallback windowPosCallback;
	private GLFWWindowCloseCallback windowCloseCallback;
	private GLFWWindowFocusCallback windowFocusCallback;
	private GLFWWindowIconifyCallback windowIconifyCallback;
	private GLFWWindowRefreshCallback windowRefreshCallback;

	@Getter
	private DrawingSurfaceEventHandler eventHandler = new DrawingSurfaceEventHandler();

	public Window() {
		this(new Vector2i(-1, -1), WindowMode.FULL_SCREEN);
	}

	public Window(Vector2i resolution) {
		this(resolution, WindowMode.BORDERLESS);
	}

	public Window(Vector2i resolution, final WindowMode mode) {
		if (resolution.x() == -1 || resolution.y() == -1) {
			final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			resolution.x(gd.getDisplayMode().getWidth());
			resolution.y(gd.getDisplayMode().getHeight());
		}
		this.dimensions = resolution;
		this.mode = mode;
	}

	@Override
	public void show() {
		if (handle != NULL) {
			destroy();
		}

		if (mode != WindowMode.BORDERLESS) {
			glfwWindowHint(GLFW_DECORATED, GLFW_TRUE);
		} else {
			glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
		}

		glfwWindowHint(GLFW_FLOATING, alwaysOnTop ? GLFW_TRUE : GLFW_FALSE);

		glfwWindowHint(GLFW_SAMPLES, samples);

		//if (graphicsLibrary == GraphicsLibrary.VULKAN) {
			//glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API);
		//}

		if (offscreen) {
			glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		}

		// use GL3.3
		// glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		// glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		// glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

		long monitor = NULL;

		if (mode == WindowMode.FULL_SCREEN) {
			monitor = glfwGetPrimaryMonitor();
		}

		System.out.println(dimensions + ", " + getTitle() + ", " + monitor);
		handle = glfwCreateWindow(dimensions.x(), dimensions.y(), getTitle(), monitor, NULL);

		// glfwSetInputMode(handle, GLFW_CURSOR, getMouseInputMode().opcode);

		cursorPosCallback = new GLFWCursorPosCallback() {

			@Override
			public void invoke(long windowHandle, double x, double y) {
				mouseLocation = new Vector2i((int) x, (int) y);
				Vector2i delta = new Vector2i();
				if (oldMouseCoords != null) {
					delta.xy(mouseLocation).subtract(oldMouseCoords);
				}
				oldMouseCoords = mouseLocation;
				List<MouseMovementListener> mouseMovementListeners = getMouseMovementListeners();
				synchronized (mouseMovementListeners) {
					for (MouseMovementListener mouseMovementListener : mouseMovementListeners) {
						mouseMovementListener.onMove(mouseLocation, delta);
					}
				}
			}

		};

		mouseButtonCallback = new GLFWMouseButtonCallback() {

			@Override
			public void invoke(long windowHandle, int buttonID, int action, int mods) {
				Mouse mouse = getMouse();
				List<MouseButtonListener> mouseButtonListeners = getMouseButtonListeners();
				synchronized (mouseButtonListeners) {
					for (MouseButtonListener listener : mouseButtonListeners) {
						if (action == GLFW_PRESS) {
							listener.onPress(buttonID, mouse.getLocation());
						} else if (action == GLFW_RELEASE) {
							listener.onRelease(buttonID, mouse.getLocation());
						} else {
							System.err.println("Unknown mouse button action: " + action);
						}
					}
				}
			}

		};

		scrollCallback = new GLFWScrollCallback() {

			@Override
			public void invoke(long windowHandle, double xOffset, double yOffset) {
				List<MouseScrollListener> mouseScrollListeners = getMouseScrollListeners();
				synchronized (mouseScrollListeners) {
					for (MouseScrollListener listener : mouseScrollListeners) {
						listener.onScroll(new Vector2f((float) xOffset, (float) yOffset));
					}
				}
			}

		};

		keyCallback = new GLFWKeyCallback() {

			@Override
			public void invoke(long windowHandle, int key, int scancode, int action, int mods) {
				List<KeyListener> keyListeners = getKeyInputListeners();

				synchronized (keyListeners) {
					for (KeyListener listener : keyListeners) {
						if (action == GLFW_PRESS) {
							listener.onPress(key, mods, false);
						} else if (action == GLFW_REPEAT) {
							listener.onPress(key, mods, true);
						} else if (action == GLFW_RELEASE) {
							listener.onRelease(key, mods);
						}
					}
				}
			}

		};

		charCallback = new GLFWCharCallback() {

			@Override
			public void invoke(long windowHandle, int character) {
				List<KeyListener> keyListeners = getKeyInputListeners();

				synchronized (keyListeners) {
					for (KeyListener listener : keyListeners) {
						listener.onType(character);
					}
				}
			}

		};

		windowSizeCallback = new GLFWWindowSizeCallback() {

			@Override
			public void invoke(long windowHandle, int width, int height) {
				dimensions.xy(width, height);
				synchronized (windowListeners) {
					for (WindowListener listener : windowListeners) {
						listener.onResize(dimensions);
					}
				}
			}

		};

		windowPosCallback = new GLFWWindowPosCallback() {

			@Override
			public void invoke(long windowHandle, int x, int y) {
				location.xy(x, y);
				synchronized (windowListeners) {
					for (WindowListener listener : windowListeners) {
						listener.onMove(location);
					}
				}
			}

		};

		windowCloseCallback = new GLFWWindowCloseCallback() {

			@Override
			public void invoke(long windowHandle) {
				System.out.println("CLOSE");
				synchronized (windowListeners) {
					for (WindowListener listener : windowListeners) {
						listener.onClose();
					}
				}
			}

		};

		windowFocusCallback = new GLFWWindowFocusCallback() {
			@Override
			public void invoke(long windowHandle, boolean focusedD) {
				focused = focusedD;
				synchronized (windowListeners) {
					List<FocusListener> focusListeners = getFocusChangeListeners();
					for (FocusListener listener : focusListeners) {
						if (focused) {
							listener.onFocusGained();
						} else {
							listener.onFocusLost();
						}
					}
				}
			}
		};

		windowIconifyCallback = new GLFWWindowIconifyCallback() {
			// iconified - GL11.GL_TRUE if the window was iconified, or
			// GL11.GL_FALSE if it was restored
			@Override
			public void invoke(long windowHandle, boolean iconified) {
				minimized = iconified;
				synchronized (windowListeners) {
					for (WindowListener listener : windowListeners) {
						listener.onMinimized(minimized);
					}
				}
			}
		};

		windowRefreshCallback = new GLFWWindowRefreshCallback() {

			@Override
			public void invoke(long windowHandle) {
				synchronized (windowListeners) {
					for (WindowListener listener : windowListeners) {
						listener.onRefresh();
					}
				}
			}
		};

		glfwSetCursorPosCallback(handle, cursorPosCallback);
		glfwSetMouseButtonCallback(handle, mouseButtonCallback);
		glfwSetScrollCallback(handle, scrollCallback);
		glfwSetKeyCallback(handle, keyCallback);
		glfwSetCharCallback(handle, charCallback);

		glfwSetWindowSizeCallback(handle, windowSizeCallback);
		glfwSetWindowPosCallback(handle, windowPosCallback);
		glfwSetWindowCloseCallback(handle, windowCloseCallback);
		glfwSetWindowFocusCallback(handle, windowFocusCallback);
		glfwSetWindowIconifyCallback(handle, windowIconifyCallback);
		glfwSetWindowRefreshCallback(handle, windowRefreshCallback);

		glfwMakeContextCurrent(handle);

		glfwSwapInterval(vsync ? 1 : 0);
		if (!offscreen) {
			glfwShowWindow(handle);
		}

		//if (graphicsLibrary == GraphicsLibrary.OPENGL) {
			//GL.createCapabilities();
		//} else {
			// TODO vulkan
		//}
	}

	public void setAlwaysOnTop(final boolean top) {
		this.alwaysOnTop = top;
		if (handle != NULL) {
			glfwWindowHint(GLFW_FLOATING, top ? GLFW_TRUE : GLFW_FALSE);
		}
	}
	
	public void setTitle(final String title) {
		this.title = title;
		if (handle != NULL) {
			glfwSetWindowTitle(handle, title);
		}
	}
	
	public void setDimensions(final Vector2i resolution) {
		this.dimensions = resolution;
		if (handle != NULL) {
			glfwSetWindowSize(handle, resolution.x(), resolution.y());
		}
	}
	
	public void setLocation(final Vector2i location) {
		this.location = location;
		if (handle != NULL) {
			glfwSetWindowPos(handle, location.x(), location.y());
		}
	}
	
	private void destroy() {
		cursorPosCallback.free();
		mouseButtonCallback.free();
		scrollCallback.free();
		keyCallback.free();
		charCallback.free();
		glfwDestroyWindow(handle);
		focused = false;
		minimized = false;
	}

	@Override
	public void preRender() {
	
	}
	
	@Override
	public void pollEvents() {
		glfwPollEvents();
	}

	@Override
	public void setVsync(boolean vsync) {
		this.vsync = vsync;
		if (handle != NULL) {
			glfwSwapInterval(vsync ? 1 : 0);
		}
	}

	@Override
	public boolean isDestroyed() {
		return glfwWindowShouldClose(handle);
	}

}
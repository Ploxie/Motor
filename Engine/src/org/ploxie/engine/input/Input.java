package org.ploxie.engine.input;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;

import org.ploxie.opengl.display.Window;

public class Input {

	private static Input instance;
	private Keyboard keyboard;
	private Mouse mouse;

	public static Input create(Window window) {
		if (instance != null) {
			throw new RuntimeException("Input instance already created");
		}

		instance = new Input();
		instance.keyboard = new Keyboard(window);
		instance.mouse = new Mouse(window);

		return instance;
	}
	
	public void update() {
		mouse.update();
		keyboard.update();
		
		glfwPollEvents();
	}

	private static Input getInstance() {
		if (instance == null) {
			throw new RuntimeException("Input is not created");
		}
		return instance;
	}

	public static Keyboard getKeyboard() {
		return getInstance().keyboard;
	}

	public static Mouse getMouse() {

		return getInstance().mouse;
	}

}

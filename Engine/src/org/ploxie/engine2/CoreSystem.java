package org.ploxie.engine2;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.ploxie.engine2.context.EngineContext;
import org.ploxie.engine2.display.Window;
import org.ploxie.engine2.input.Input;

import lombok.Getter;
import lombok.Setter;

public class CoreSystem {

	@Setter
	private RenderEngine renderEngine;

	@Getter
	private Window window;

	@Getter
	private Input input;	

	private GLFWErrorCallback errorCallback;
	
	public void initialize() {

		GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

		window = EngineContext.getInstance().getWindow();
		input = EngineContext.getInstance().getInput();

		window.create();
		input.create(window.getHandle());
		renderEngine.initialize();

	}

	public void update() {
		input.update();
		renderEngine.update();
	}

	public void preRender() {
		window.preRender();
	}
	
	public void render() {
		renderEngine.render();
		window.draw();
	}

	public void shutdown() {
		window.shutdown();
		input.shutdown();
		renderEngine.shutdown();
		errorCallback.free();
		GLFW.glfwTerminate();
	}

}

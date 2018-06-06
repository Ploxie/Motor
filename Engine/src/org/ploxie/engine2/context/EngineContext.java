package org.ploxie.engine2.context;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.ploxie.engine2.display.Window;
import org.ploxie.engine2.input.GLFWInput;
import org.ploxie.engine2.input.Input;

public class EngineContext {

	protected static Window window;
	protected static Input input;
	protected static Configuration configuration;
	protected static GraphicsToolkit graphicsToolkit;
	
	public static void initialize() {
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!GLFW.glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");			
		}
		
		input = new GLFWInput();
		configuration = new Configuration();
	}
	
	public static Window getWindow() {
		return window;
	}
	
	public static Input getInput() {
		return input;
	}
	
	public static Configuration getConfiguration() {
		return configuration;
	}	
	
	public static GraphicsToolkit getGraphicsToolkit(){
		return graphicsToolkit;
	}
	
}

package org.ploxie.engine2.context;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.ploxie.engine2.display.Window;
import org.ploxie.engine2.input.GLFWInput;
import org.ploxie.engine2.input.Input;
import org.ploxie.engine2.scenegraph.SceneGraph;
import org.ploxie.engine2.scenegraph.component.ComponentManager;

public class EngineContext {

	protected static EngineContext instance = null;
	
	protected Window window;
	protected Input input;
	protected Configuration configuration;
	protected GraphicsToolkit graphicsToolkit;
	protected ComponentManager componentManager;
	
	protected EngineContext() {
		
	}	

	public static EngineContext getInstance() {
		return instance;
	}
	
	protected void initialize() {
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!GLFW.glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");			
		}
		
		instance.input = new GLFWInput();
		instance.configuration = new Configuration();
		instance.componentManager = new ComponentManager();
	}
	
	public Window getWindow() {
		return window;
	}
	
	public Input getInput() {
		return input;
	}
	
	public Configuration getConfiguration() {
		return configuration;
	}	
	
	public GraphicsToolkit getGraphicsToolkit(){
		return graphicsToolkit;
	}
	
	public ComponentManager getComponentManager() {
		return componentManager;
	}

	
}

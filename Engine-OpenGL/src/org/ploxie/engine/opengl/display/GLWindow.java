package org.ploxie.engine.opengl.display;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.ploxie.engine.opengl.context.GLContext;
import org.ploxie.engine2.context.EngineContext;
import org.ploxie.engine2.display.Window;

public class GLWindow extends Window{

	public GLWindow() {
		super(GLContext.getInstance().getConfiguration().getDisplayTitle()+ " (OpenGL)",
			  GLContext.getInstance().getConfiguration().getWindowWidth(),
			  GLContext.getInstance().getConfiguration().getWindowHeight());		
	}

	@Override
	public void create() {
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);	
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);	
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);	
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);	
		
		setHandle(GLFW.glfwCreateWindow(getWidth(), getHeight(), getTitle(), 0, 0));
				
		if(getHandle() == 0) {
		    throw new RuntimeException("Failed to create window");
		}
		
		
				
		GLFW.glfwMakeContextCurrent(getHandle());
		GLFW.glfwSwapInterval(0);
		GLFW.glfwShowWindow(getHandle());
		
	}

	@Override
	public void draw() {
		GLFW.glfwSwapBuffers(getHandle());
	}

	@Override
	public void shutdown() {
		GLFW.glfwDestroyWindow(getHandle());
	}

	@Override
	public boolean isCloseRequested() {
		return GLFW.glfwWindowShouldClose(getHandle());
	}

	@Override
	public void resize(int width, int height) {
		GLFW.glfwSetWindowSize(getHandle(), width, height);
		setHeight(height);
		setWidth(width);
		EngineContext.getInstance().getConfiguration().setWindowWidth(width);
		EngineContext.getInstance().getConfiguration().setWindowHeight(height);
		// TODO set camera projection
	}
	

}

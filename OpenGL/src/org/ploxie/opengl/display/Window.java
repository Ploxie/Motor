package org.ploxie.opengl.display;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import org.lwjgl.opengl.GL;
import org.ploxie.opengl.utilities.OpenGLObject;

import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class Window implements OpenGLObject{

	private long handle;
	private int width;
	private int height;

	private Window() {

	}

	protected void finalize() throws Throwable{
		delete();
	}
	
	public void swapBuffers() {
		glfwSwapBuffers(handle);
	}

	public boolean isCloseRequested() {
		return glfwWindowShouldClose(handle);
	}

	public void setWindowSize(int width, int height) {
		glfwSetWindowSize(handle, width, height);
		setWidth(width);
		setHeight(height);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public long getHandle() {
		return handle;
	}

	public static Window create(int width, int height, String title) {
		Window window = new Window();

		window.setWidth(width);
		window.setHeight(height);

		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

		window.handle = glfwCreateWindow(width, height, title, 0, 0);

		if (window.handle == 0) {
			throw new RuntimeException("Failed to create Window");
		}

		glfwMakeContextCurrent(window.handle);
		GL.createCapabilities();
		glfwShowWindow(window.handle);

		return window;
	}

	@Override
	public void delete() {
		glfwDestroyWindow(handle);		
	}

}

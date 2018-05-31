package org.vulkantest;

import static org.lwjgl.glfw.GLFW.GLFW_CLIENT_API;
import static org.lwjgl.glfw.GLFW.GLFW_NO_API;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFWVulkan.glfwVulkanSupported;

import org.lwjgl.glfw.GLFW;
import org.ploxie.engine.Engine;
import org.ploxie.engine.display.Window;
import org.ploxie.engine.display.WindowMode;
import org.ploxie.utils.math.vector.Vector2i;

public class Test {

	public static void main(String[] args) {

		if (!GLFW.glfwInit()) {
			throw new RuntimeException("Failed to initialize GLFW");
		}
		

		
		
		Engine engine = new Engine();		
		Window window = new Window(new Vector2i(800, 600), WindowMode.WINDOWED);
		
		
	//	engine.setRenderEngine(new VulkanRenderer());
		
		glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API);
		window.setTitle("Vulkan");
		//window.show();

		
		
	}

}

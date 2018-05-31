package org.ploxie.engine.vulkan.display;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.vulkan.VK10;
import org.ploxie.engine.vulkan.context.VulkanContext;
import org.ploxie.engine2.display.Window;
import org.ploxie.vulkan.device.VulkanLogicalDevice;
import org.ploxie.vulkan.math.VulkanExtent2D;
import org.ploxie.vulkan.surface.VulkanSurfacePresentMode;
import org.ploxie.vulkan.swapchain.VulkanSwapChain;
import org.ploxie.vulkan.synchronization.VulkanSemaphore;

public class VulkanWindow extends Window{

	private VulkanLogicalDevice logicalDevice;
	private VulkanSwapChain swapchain;
	
	public VulkanWindow() {
		super(VulkanContext.getConfiguration().getDisplayTitle(),
			  VulkanContext.getConfiguration().getScreenResolutionX(),
			  VulkanContext.getConfiguration().getScreenResolutionY());		
	}

	@Override
	public void create() {
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, VK10.VK_TRUE);
		GLFW. glfwWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_NO_API);
        setHandle(GLFW.glfwCreateWindow(getWidth(), getHeight(), getTitle(), 0, 0));
        
        if(getHandle() == 0) {
		    throw new RuntimeException("Failed to create window");
		}
        
        logicalDevice = VulkanContext.getLogicalDevice();
        
        VulkanExtent2D extent = new VulkanExtent2D(getWidth(), getHeight());
        swapchain = logicalDevice.createSwapChain(getHandle(), extent, VulkanSurfacePresentMode.MAILBOX, null);
        
        
        GLFW.glfwShowWindow(getHandle());        
	}

	@Override
	public void draw() {
		
	}

	@Override
	public void shutdown() {
		GLFW.glfwDestroyWindow(getHandle());
	}

	@Override
	public boolean isCloseRequested() {
		return  GLFW.glfwWindowShouldClose(getHandle());
	}

	@Override
	public void resize(int x, int y) {
		
	}

}

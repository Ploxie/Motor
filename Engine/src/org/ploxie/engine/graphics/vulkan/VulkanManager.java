package org.ploxie.engine.graphics.vulkan;

import static org.lwjgl.glfw.GLFW.GLFW_CLIENT_API;
import static org.lwjgl.glfw.GLFW.GLFW_NO_API;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFWVulkan.glfwVulkanSupported;
import static org.lwjgl.vulkan.KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME;

import java.util.Arrays;

import org.ploxie.engine.display.GraphicsLibrary;
import org.ploxie.engine.display.IDrawSurface;
import org.ploxie.engine.graphics.IGraphicsManager;
import org.ploxie.engine.graphics.IRenderer;
import org.ploxie.vulkan.Vulkan;
import org.ploxie.vulkan.VulkanApiVersion;
import org.ploxie.vulkan.VulkanApplicationInfo;
import org.ploxie.vulkan.VulkanInstance;
import org.ploxie.vulkan.VulkanInstanceProperties;
import org.ploxie.vulkan.debug.DefaultVulkanDebugReportCallback;
import org.ploxie.vulkan.debug.VulkanDebugReportType;
import org.ploxie.vulkan.device.VulkanLogicalDevice;
import org.ploxie.vulkan.device.VulkanPhysicalDevice;
import org.ploxie.vulkan.queue.VulkanDeviceQueueCreateInfo;
import org.ploxie.vulkan.queue.VulkanQueue;
import org.ploxie.vulkan.queue.VulkanQueueFamilyProperties;
import org.ploxie.vulkan.queue.VulkanQueueFamilyPropertiesList;

import lombok.Getter;
import lombok.Setter;

public class VulkanManager implements IGraphicsManager {

	@Getter
	private VulkanLogicalDevice device;
	private VulkanRenderer renderer;
	
	@Getter
	private VulkanQueue queue;
	
	@Getter
	@Setter
	private IDrawSurface drawSurface;
	
	@Override
	public boolean initialize(IDrawSurface drawSurface) {
		this.drawSurface = drawSurface;
		
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		if (!glfwVulkanSupported()) {
			throw new AssertionError("GLFW failed to find the Vulkan loader");
		}
		
		glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API);

		VulkanApplicationInfo appInfo = new VulkanApplicationInfo("Vulkan Test", "Engine", 1,VulkanApiVersion.create(1, 0, 2));
		VulkanInstanceProperties instanceProperties = Vulkan.createInstanceProperties(appInfo);
		VulkanInstance vulkanInstance = Vulkan.createInstance(instanceProperties);

		if (Vulkan.isValidation()) {
			vulkanInstance.setupDebugging(new DefaultVulkanDebugReportCallback(), VulkanDebugReportType.WARNING,VulkanDebugReportType.ERROR);
		}

		VulkanPhysicalDevice physicalDevice = vulkanInstance.getPhysicalDevices().get(0);
		VulkanQueueFamilyPropertiesList queueFamilyPropertiesList = physicalDevice.getQueueFamilyProperties();
		VulkanQueueFamilyProperties queueFamilyGraphics = queueFamilyPropertiesList.getFirstGraphicsQueue();

		device = physicalDevice.createDevice(Arrays.asList(VK_KHR_SWAPCHAIN_EXTENSION_NAME),
				new VulkanDeviceQueueCreateInfo(queueFamilyGraphics, new float[] { 1 }));

		queue = device.getDeviceQueue(queueFamilyGraphics.getIndex(), 0);
		
		renderer = new VulkanRenderer();		
		renderer.initialize(this);

		return true;
	}

	@Override
	public void update() {
		renderer.update();
		renderer.presentFrame();
	}

	@Override
	public IRenderer getRenderer() {
		return renderer;
	}
	
}

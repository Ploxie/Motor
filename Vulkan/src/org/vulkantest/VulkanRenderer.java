package org.vulkantest;

import static org.lwjgl.glfw.GLFWVulkan.glfwVulkanSupported;
import static org.lwjgl.vulkan.KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME;

import java.util.Arrays;

import org.ploxie.vulkan.Vulkan;
import org.ploxie.vulkan.VulkanApplicationInfo;
import org.ploxie.vulkan.VulkanInstance;
import org.ploxie.vulkan.VulkanInstanceProperties;
import org.ploxie.vulkan.command.VulkanCommandPool;
import org.ploxie.vulkan.debug.DefaultVulkanDebugReportCallback;
import org.ploxie.vulkan.debug.VulkanDebugReportType;
import org.ploxie.vulkan.device.VulkanLogicalDevice;
import org.ploxie.vulkan.device.VulkanPhysicalDevice;
import org.ploxie.vulkan.queue.VulkanDeviceQueueCreateInfo;
import org.ploxie.vulkan.queue.VulkanQueue;
import org.ploxie.vulkan.queue.VulkanQueueFamilyProperties;
import org.ploxie.vulkan.queue.VulkanQueueFamilyPropertiesList;

public class VulkanRenderer {

	private VulkanInstance vulkanInstance;
	private VulkanPhysicalDevice physicalDevice;
	private VulkanLogicalDevice device;
	private VulkanQueue presentQueue;
	private VulkanCommandPool graphicsCommandPool;
	
	public void initialize(VulkanApplicationInfo appInfo) {
		
		if (!glfwVulkanSupported()) {
			throw new AssertionError("GLFW failed to find the Vulkan loader");
		}
		
		VulkanInstanceProperties instanceProperties = Vulkan.createInstanceProperties(appInfo);
		vulkanInstance = Vulkan.createInstance(instanceProperties);
		
		if(Vulkan.isValidation()) {
			vulkanInstance.setupDebugging(new DefaultVulkanDebugReportCallback(), VulkanDebugReportType.WARNING,VulkanDebugReportType.ERROR );
		}
		
		physicalDevice = vulkanInstance.getPhysicalDevices().get(0);
		VulkanQueueFamilyPropertiesList queueFamilyPropertiesList = physicalDevice.getQueueFamilyProperties();
		VulkanQueueFamilyProperties queueFamilyGraphics = queueFamilyPropertiesList.getFirstGraphicsQueue();
		
		device = physicalDevice.createDevice(Arrays.asList(VK_KHR_SWAPCHAIN_EXTENSION_NAME),
				new VulkanDeviceQueueCreateInfo(queueFamilyGraphics, new float[] { 1 }));
		
		presentQueue = device.getDeviceQueue(queueFamilyGraphics.getIndex(), 0);		
		graphicsCommandPool = device.createCommandPool(queueFamilyGraphics.getIndex());
	}
	
	private void recreateSwapchain() {
		
	}
	
	public void render() {
		
	}
	
}

package org.ploxie.test;

import static org.lwjgl.glfw.GLFWVulkan.glfwVulkanSupported;
import static org.lwjgl.vulkan.KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME;

import java.util.Arrays;

import org.lwjgl.glfw.GLFW;
import org.ploxie.vulkan.VulkanApiVersion;
import org.ploxie.vulkan.VulkanApplicationInfo;
import org.ploxie.vulkan.VulkanInstance;
import org.ploxie.vulkan.VulkanInstanceProperties;
import org.ploxie.vulkan.device.VulkanLogicalDevice;
import org.ploxie.vulkan.device.VulkanPhysicalDevice;
import org.ploxie.vulkan.queue.VulkanDeviceQueueCreateInfo;
import org.ploxie.vulkan.queue.VulkanQueue;
import org.ploxie.vulkan.queue.VulkanQueueFamilyProperties;
import org.ploxie.vulkan.queue.VulkanQueueFamilyPropertiesList;

public class Main {

	public static void main(String[] args) {

		if (!GLFW.glfwInit()) { throw new RuntimeException("Failed to initialize GLFW"); }
		  if (!glfwVulkanSupported()) { throw new
		  AssertionError("GLFW failed to find the Vulkan loader"); }
		
		VulkanApplicationInfo appInfo = new VulkanApplicationInfo("Vulkan Test", "Engine", 1,
				VulkanApiVersion.create(1, 0, 2));

		VulkanInstance vulkanInstance = new VulkanInstance(appInfo);

		VulkanPhysicalDevice physicalDevice = vulkanInstance.getPhysicalDevices().get(0);
		VulkanQueueFamilyPropertiesList queueFamilyPropertiesList = physicalDevice.getQueueFamilyProperties();
		VulkanQueueFamilyProperties queueFamilyGraphics = queueFamilyPropertiesList.getFirstGraphicsQueue();
		
		VulkanLogicalDevice device = new VulkanLogicalDevice(physicalDevice, Arrays.asList(VK_KHR_SWAPCHAIN_EXTENSION_NAME),
				new VulkanDeviceQueueCreateInfo(queueFamilyGraphics, new float[] { 1 }));
		
		VulkanQueue queue = device.getDeviceQueue(queueFamilyGraphics.getIndex(), 0);
		
		System.out.println("Physical Device: " + physicalDevice.getName());
		
	}

}

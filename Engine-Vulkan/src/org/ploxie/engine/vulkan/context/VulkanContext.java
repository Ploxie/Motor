package org.ploxie.engine.vulkan.context;

import static org.lwjgl.vulkan.KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME;

import java.util.Arrays;

import org.lwjgl.glfw.GLFWVulkan;
import org.ploxie.engine.vulkan.display.VulkanWindow;
import org.ploxie.engine2.context.EngineContext;
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


public class VulkanContext extends EngineContext {
	
	@Getter
	private static VulkanInstance vulkanInstance;
	@Getter
	private static VulkanPhysicalDevice physicalDevice;
	@Getter
	private static VulkanLogicalDevice logicalDevice;
	@Getter
	private static VulkanQueue graphicsQueue;
	@Getter
	private static DescriptorPoolManager descriptorPoolManager;
	
	public static void initialize() {
		EngineContext.initialize();
		
		if (!GLFWVulkan.glfwVulkanSupported()) {
			throw new AssertionError("GLFW failed to find the Vulkan loader");
		}

		VulkanApplicationInfo appInfo = new VulkanApplicationInfo(VulkanContext.getConfiguration().getDisplayTitle(), "Engine", 1, VulkanApiVersion.create(1, 0, 2));
		VulkanInstanceProperties instanceProperties = Vulkan.createInstanceProperties(appInfo);

		vulkanInstance = Vulkan.createInstance(instanceProperties);
		
		if (Vulkan.isValidation()) {
			vulkanInstance.setupDebugging(new DefaultVulkanDebugReportCallback(), VulkanDebugReportType.WARNING,VulkanDebugReportType.ERROR);
		}
		
		physicalDevice = vulkanInstance.getPhysicalDevices().get(0);
		
		VulkanQueueFamilyPropertiesList queueFamilyPropertiesList = physicalDevice.getQueueFamilyProperties();
		VulkanQueueFamilyProperties queueFamilyGraphics = queueFamilyPropertiesList.getFirstGraphicsQueue();
		
		logicalDevice = physicalDevice.createDevice(Arrays.asList(VK_KHR_SWAPCHAIN_EXTENSION_NAME),	new VulkanDeviceQueueCreateInfo(queueFamilyGraphics, new float[] { 1 }));
		
		window = new VulkanWindow();
		
		graphicsToolkit = new VulkanToolkit();
		int graphicsFamilyIndex = physicalDevice.getQueueFamilyProperties().getFirstGraphicsQueue().getIndex();
		graphicsQueue = logicalDevice.getDeviceQueue(graphicsFamilyIndex, 0);
		
		descriptorPoolManager = new DescriptorPoolManager();
		
	}

	public static VulkanWindow getWindow() {
		return (VulkanWindow) window;
	}
	

}

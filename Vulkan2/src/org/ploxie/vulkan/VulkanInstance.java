package org.ploxie.vulkan;

import static org.lwjgl.glfw.GLFWVulkan.glfwGetRequiredInstanceExtensions;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.vulkan.EXTDebugReport.VK_EXT_DEBUG_REPORT_EXTENSION_NAME;
import static org.lwjgl.vulkan.EXTDebugReport.VK_STRUCTURE_TYPE_DEBUG_REPORT_CALLBACK_CREATE_INFO_EXT;
import static org.lwjgl.vulkan.EXTDebugReport.vkCreateDebugReportCallbackEXT;
import static org.lwjgl.vulkan.EXTDebugReport.vkDestroyDebugReportCallbackEXT;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_APPLICATION_INFO;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_SUCCESS;
import static org.lwjgl.vulkan.VK10.vkCreateInstance;
import static org.lwjgl.vulkan.VK10.vkEnumeratePhysicalDevices;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkApplicationInfo;
import org.lwjgl.vulkan.VkDebugReportCallbackCreateInfoEXT;
import org.lwjgl.vulkan.VkDebugReportCallbackEXT;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkInstanceCreateInfo;
import org.lwjgl.vulkan.VkPhysicalDevice;
import org.ploxie.vulkan.debug.DefaultVulkanDebugReportCallback;
import org.ploxie.vulkan.debug.VulkanDebugReportCallback;
import org.ploxie.vulkan.debug.VulkanDebugReportType;
import org.ploxie.vulkan.device.VulkanPhysicalDevice;
import org.ploxie.vulkan.utils.BufferUtils;
import org.ploxie.vulkan.utils.VKUtil;

import lombok.Getter;

public class VulkanInstance {

	private static final boolean validation = Boolean.parseBoolean(System.getProperty("vulkan.validation", "true"));
	
	@Getter
	private final VkInstance handle;
	
	private long debugCallbackHandle;
	
	public VulkanInstance(VulkanApplicationInfo vulkanApplicationInfo) {
		List<String> extensions = new ArrayList<>();
		PointerBuffer requiredExtensions = glfwGetRequiredInstanceExtensions();
		if (requiredExtensions == null) {
			throw new AssertionError("Failed to find list of required Vulkan extensions");
		}
		while (requiredExtensions.hasRemaining()) {
			extensions.add(requiredExtensions.getStringASCII());
		}
		
		List<String> layers = new ArrayList<>();
		
		if (validation) {
			extensions.add(VK_EXT_DEBUG_REPORT_EXTENSION_NAME);
			layers.add("VK_LAYER_LUNARG_standard_validation");
		}
		
		/*VulkanInstanceProperties vulkanInstanceProperties = new VulkanInstanceProperties();
		vulkanInstanceProperties.setApplicationInfo(vulkanApplicationInfo);
		vulkanInstanceProperties.setExtensions(extensions);
		vulkanInstanceProperties.setLayers(layers);		*/
		
		try (MemoryStack stack = stackPush()) {
			
			VulkanApplicationInfo applicationInfo = vulkanApplicationInfo;
			ByteBuffer appName = BufferUtils.wrap(applicationInfo.getApplicationName() + "\0", stack);
			ByteBuffer engineName = BufferUtils.wrap(applicationInfo.getEngineName() + "\0", stack);
			
			PointerBuffer extensionsBuffer = stack.mallocPointer(extensions.size());
			for(String extension : extensions) {
				extensionsBuffer.put(BufferUtils.wrap(extension + "\0", stack));
			}
			extensionsBuffer.flip();
			
			PointerBuffer layersBuffer = stack.mallocPointer(layers.size());
			for(String layer : layers) {
				layersBuffer.put(BufferUtils.wrap(layer + "\0", stack));
			}
			layersBuffer.flip();
			
			VkApplicationInfo info = VkApplicationInfo.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_APPLICATION_INFO)
					.pNext(NULL)
					.pApplicationName(appName)
					.pEngineName(engineName)
					.engineVersion(applicationInfo.getEngineVersion())
					.apiVersion(applicationInfo.getApiVersion().getVersion());
			
			VkInstanceCreateInfo instanceCreateInfo = VkInstanceCreateInfo
					.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO)
					.pNext(NULL)
					.flags(0)
					.pApplicationInfo(info)
					.ppEnabledExtensionNames(extensionsBuffer)
					.ppEnabledLayerNames(layersBuffer);
			
			PointerBuffer pInstance = stack.mallocPointer(1);
			int err = vkCreateInstance(instanceCreateInfo, null, pInstance);

			if (err != VK_SUCCESS) {
				throw new AssertionError("Failed to create VkInstance: "
						+ VKUtil.translateVulkanResult(err));
			}

			long internal = pInstance.get(0);
			this.handle = new VkInstance(internal, instanceCreateInfo);			
			this.debugCallbackHandle = setupDebugging();
		}	
		
	}
	
	public List<VulkanPhysicalDevice> getPhysicalDevices() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer deviceCountBuffer = stack.mallocInt(1);
			int err = vkEnumeratePhysicalDevices(getHandle(), deviceCountBuffer, null);
			if (err != VK_SUCCESS) {
				throw new AssertionError(
						"Failed to get number of physical devices: " + VKUtil.translateVulkanResult(err));
			}

			int deviceCount = deviceCountBuffer.get(0);
			PointerBuffer physicalDevicesBuffer = stack.mallocPointer(deviceCount);
			vkEnumeratePhysicalDevices(getHandle(), deviceCountBuffer, physicalDevicesBuffer);

			List<VulkanPhysicalDevice> devices = new ArrayList<>(deviceCount);
			for (int i = 0; i < deviceCount; i++) {
				long physicalDeviceHandle = physicalDevicesBuffer.get(i);
				devices.add(new VulkanPhysicalDevice(this, new VkPhysicalDevice(physicalDeviceHandle, getHandle())));
			}
			return devices;
		}
	}
	
	private long setupDebugging() {
		
		if(debugCallbackHandle != -1) {
			vkDestroyDebugReportCallbackEXT(getHandle(), debugCallbackHandle, null);
		}
		
		VulkanDebugReportCallback callback = new DefaultVulkanDebugReportCallback();
		
		final VkDebugReportCallbackEXT debugCallback = new VkDebugReportCallbackEXT() {

			@Override
			public int invoke(int flags, int objectType, long object,
					long location, int messageCode, long pLayerPrefix,
					long pMessage, long pUserData) {
				VulkanDebugReportType type = VulkanDebugReportType.get(flags);
				String message = VkDebugReportCallbackEXT.getString(pMessage);
				return callback.invoke(type, objectType, object, location, messageCode, pLayerPrefix, pMessage, message, pUserData);
			}
			
		};
		
		VulkanDebugReportType[] types = new VulkanDebugReportType[] {VulkanDebugReportType.WARNING,VulkanDebugReportType.ERROR};
				
		int flags = 0;
		for(VulkanDebugReportType type : types) {
			flags |= type.getBitMask();
		}
		
		VkDebugReportCallbackCreateInfoEXT dbgCreateInfo = VkDebugReportCallbackCreateInfoEXT
				.calloc()
				.sType(VK_STRUCTURE_TYPE_DEBUG_REPORT_CALLBACK_CREATE_INFO_EXT)
				.pNext(NULL).pfnCallback(debugCallback).pUserData(NULL).flags(flags);
		
		LongBuffer pCallback = memAllocLong(1);
		int err = vkCreateDebugReportCallbackEXT(getHandle(), dbgCreateInfo, null, pCallback);
		long callbackHandle = pCallback.get(0);
		memFree(pCallback);
		dbgCreateInfo.free();
		if(err != VK_SUCCESS) {
			throw new AssertionError("Failed to create debug callback: "+VKUtil.translateVulkanResult(err));
		}
		
		return callbackHandle;		
	}
	
	public void destroy(){
		
		vkDestroyDebugReportCallbackEXT(handle, debugCallbackHandle, null);
		VK10.vkDestroyInstance(handle, null);
	}
	
}

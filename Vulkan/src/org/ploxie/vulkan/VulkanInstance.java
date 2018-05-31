package org.ploxie.vulkan;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkDebugReportCallbackCreateInfoEXT;
import org.lwjgl.vulkan.VkDebugReportCallbackEXT;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkPhysicalDevice;
import org.ploxie.vulkan.debug.VulkanDebugReportCallback;
import org.ploxie.vulkan.debug.VulkanDebugReportType;
import org.ploxie.vulkan.device.VulkanPhysicalDevice;
import org.ploxie.vulkan.surface.VulkanSurface;
import org.ploxie.vulkan.utils.VKUtil;

import static org.lwjgl.glfw.GLFWVulkan.glfwCreateWindowSurface;
import static org.lwjgl.vulkan.EXTDebugReport.*;
import static org.lwjgl.vulkan.VK10.*;
import static org.lwjgl.system.MemoryUtil.*;

import lombok.Data;

@Data
public class VulkanInstance {

	private final VkInstance internal;
	private final VulkanInstanceProperties properties;
	
	private long debugCallbackHandle = -1;

	private VulkanSurface surface = null;

	protected VulkanInstance(VkInstance internal, VulkanInstanceProperties properties) {
		this.internal = internal;
		this.properties = properties;
	}

	public List<VulkanPhysicalDevice> getPhysicalDevices() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer deviceCountBuffer = stack.mallocInt(1);
			int err = vkEnumeratePhysicalDevices(getInternal(), deviceCountBuffer, null);
			if (err != VK_SUCCESS) {
				throw new AssertionError(
						"Failed to get number of physical devices: " + VKUtil.translateVulkanResult(err));
			}

			int deviceCount = deviceCountBuffer.get(0);
			PointerBuffer physicalDevicesBuffer = stack.mallocPointer(deviceCount);
			vkEnumeratePhysicalDevices(getInternal(), deviceCountBuffer, physicalDevicesBuffer);

			List<VulkanPhysicalDevice> devices = new ArrayList<>(deviceCount);
			for (int i = 0; i < deviceCount; i++) {
				long physicalDeviceHandle = physicalDevicesBuffer.get(i);
				devices.add(new VulkanPhysicalDevice(this, new VkPhysicalDevice(physicalDeviceHandle, getInternal())));
			}
			return devices;
		}
	}

	public synchronized VulkanSurface getWindowSurface(long windowHandle) {
		if (surface == null) {
			try (MemoryStack stack = MemoryStack.stackPush()) {
				LongBuffer pSurface = stack.mallocLong(1);
				int err = glfwCreateWindowSurface(getInternal(), windowHandle, null, pSurface);
				if (err != VK_SUCCESS) {
					throw new AssertionError("Failed to create surface: " + VKUtil.translateVulkanResult(err));
				}
				long surfaceHandle = pSurface.get(0);
				surface = new VulkanSurface(surfaceHandle);
			}
		}
		return surface;
	}

	public void setupDebugging(VulkanDebugReportCallback callback, VulkanDebugReportType... types) {
		if(debugCallbackHandle != -1) {
			vkDestroyDebugReportCallbackEXT(getInternal(), debugCallbackHandle, null);
		}
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
				
		int flags = 0;
		for(VulkanDebugReportType type : types) {
			flags |= type.getBitMask();
		}
		debugCallbackHandle = setupDebugging(flags, debugCallback);
	}
	
	private long setupDebugging(int flags, VkDebugReportCallbackEXT callback) {
		VkDebugReportCallbackCreateInfoEXT dbgCreateInfo = VkDebugReportCallbackCreateInfoEXT
				.calloc()
				.sType(VK_STRUCTURE_TYPE_DEBUG_REPORT_CALLBACK_CREATE_INFO_EXT)
				.pNext(NULL).pfnCallback(callback).pUserData(NULL).flags(flags);
		
		LongBuffer pCallback = memAllocLong(1);
		int err = vkCreateDebugReportCallbackEXT(getInternal(), dbgCreateInfo, null, pCallback);
		long callbackHandle = pCallback.get(0);
		memFree(pCallback);
		dbgCreateInfo.free();
		if(err != VK_SUCCESS) {
			throw new AssertionError("Failed to create debug callback: "+VKUtil.translateVulkanResult(err));
		}
		
		return callbackHandle;
	}
	
}

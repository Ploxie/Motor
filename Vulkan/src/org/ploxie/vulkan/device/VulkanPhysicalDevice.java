package org.ploxie.vulkan.device;

import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkDeviceCreateInfo;
import org.lwjgl.vulkan.VkDeviceQueueCreateInfo;
import org.lwjgl.vulkan.VkExtensionProperties;
import org.lwjgl.vulkan.VkMemoryHeap;
import org.lwjgl.vulkan.VkMemoryType;
import org.lwjgl.vulkan.VkPhysicalDevice;
import org.lwjgl.vulkan.VkPhysicalDeviceMemoryProperties;
import org.lwjgl.vulkan.VkPhysicalDeviceProperties;
import org.lwjgl.vulkan.VkQueueFamilyProperties;
import org.lwjgl.vulkan.VkSurfaceCapabilitiesKHR;
import org.lwjgl.vulkan.VkSurfaceFormatKHR;
import org.ploxie.engine2.util.BufferUtils;
import org.ploxie.vulkan.VulkanInstance;
import org.ploxie.vulkan.memory.VulkanMemoryHeap;
import org.ploxie.vulkan.memory.VulkanMemoryType;
import org.ploxie.vulkan.memory.VulkanPhysicalDeviceMemoryProperties;
import org.ploxie.vulkan.queue.VulkanDeviceQueueCreateInfo;
import org.ploxie.vulkan.queue.VulkanQueue;
import org.ploxie.vulkan.queue.VulkanQueueFamilyProperties;
import org.ploxie.vulkan.queue.VulkanQueueFamilyPropertiesList;
import org.ploxie.vulkan.surface.VulkanSurface;
import org.ploxie.vulkan.surface.VulkanSurfaceCapabilities;
import org.ploxie.vulkan.surface.VulkanSurfaceFormat;
import org.ploxie.vulkan.surface.VulkanSurfacePresentMode;
import org.ploxie.vulkan.utils.VKUtil;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static org.lwjgl.vulkan.VK10.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.vulkan.KHRSurface.*;
import static org.lwjgl.vulkan.KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;


@RequiredArgsConstructor
public class VulkanPhysicalDevice {

	@Getter
	private final VulkanInstance instance;
	@Getter
	private final VkPhysicalDevice internal;

	private VkPhysicalDeviceProperties deviceProperties;
	private VulkanPhysicalDeviceMemoryProperties memoryProperties;

	private Set<String> extensionsSupported = null;

	public String getName() {
		ensureLoadDeviceProperties();
		return deviceProperties.deviceNameString();
	}

	public synchronized boolean isExtensionSupported(String name) {
		if (this.extensionsSupported == null) {
			try (MemoryStack stack = MemoryStack.stackPush()) {
				IntBuffer pPropertyCount = stack.mallocInt(1);
				int err = vkEnumerateDeviceExtensionProperties(internal, (ByteBuffer)null, pPropertyCount, null);
				if(err != VK_SUCCESS) {
					throw new AssertionError("Failed to get extensions supported: "+VKUtil.translateVulkanResult(err));
				}
				
				VkExtensionProperties.Buffer extensionProperties = VkExtensionProperties.callocStack(pPropertyCount.get(0), stack);
				err = vkEnumerateDeviceExtensionProperties(internal, (ByteBuffer)null, pPropertyCount,  extensionProperties);
				if(err != VK_SUCCESS) {
					throw new AssertionError("Failed to get extensions supported (2): " + VKUtil.translateVulkanResult(err));
				}
				
				Set<String> extensionsSupported = new HashSet<>();
				for(VkExtensionProperties  e : extensionProperties) {
					extensionsSupported.add(e.extensionNameString());
				}
				
				this.extensionsSupported = extensionsSupported;
			}
		}
		return extensionsSupported.contains(name);
	}

	public VulkanQueueFamilyPropertiesList getQueueFamilyProperties() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer countBuffer = stack.mallocInt(1);
			vkGetPhysicalDeviceQueueFamilyProperties(internal, countBuffer, null);
			int queueFamilyCount = countBuffer.get(0);

			VkQueueFamilyProperties.Buffer queueFamilyProperties = VkQueueFamilyProperties.calloc(queueFamilyCount);
			vkGetPhysicalDeviceQueueFamilyProperties(internal, countBuffer, queueFamilyProperties);

			VulkanQueueFamilyPropertiesList familyPropertiesList = new VulkanQueueFamilyPropertiesList();
			for (int i = 0; i < queueFamilyCount; i++) {
				VkQueueFamilyProperties internal = queueFamilyProperties.get(i);
				VulkanQueueFamilyProperties familyProperties = new VulkanQueueFamilyProperties(i, this, internal);
				familyPropertiesList.add(familyProperties);
			}

			return familyPropertiesList;
		}
	}
		
	public VulkanLogicalDevice createDevice(List<String> extensions, VulkanDeviceQueueCreateInfo... queueFamilyProperties) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			VkDeviceQueueCreateInfo.Buffer internalQueueCreateInfos = VkDeviceQueueCreateInfo
					.callocStack(queueFamilyProperties.length, stack);
			for (int i = 0; i < queueFamilyProperties.length; i++) {
				VulkanDeviceQueueCreateInfo deviceQueueCreateInfo = queueFamilyProperties[i];
				VkDeviceQueueCreateInfo internalDeviceQueueCreateInfo = internalQueueCreateInfos.get(i);

				float[] priorities = deviceQueueCreateInfo.getPriorities();

				int availableQueues = deviceQueueCreateInfo.getProperties().getQueueCount();
				if (priorities.length > availableQueues) {
					throw new AssertionError("Device queue limit exceeded, available: " + availableQueues
							+ ", requested: " + priorities.length);
				}

				FloatBuffer prioritiesBuffer = stack.floats(priorities);

				internalDeviceQueueCreateInfo.sType(VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO).pNext(NULL).flags(0)
						.queueFamilyIndex(deviceQueueCreateInfo.getProperties().getIndex())
						.pQueuePriorities(prioritiesBuffer);
			}

			PointerBuffer extensionsBuffer = stack.mallocPointer(extensions.size());
			for (String extension : extensions) {
				if (!isExtensionSupported(extension)) {
					throw new AssertionError("Extension not supported: " + extension);
				}
				extensionsBuffer.put(BufferUtils.wrap(extension + "\0", stack));
			}
			extensionsBuffer.flip();
			
			VkDeviceCreateInfo vkDeviceCreateInfo = VkDeviceCreateInfo.callocStack(stack);
			
			vkDeviceCreateInfo
			.sType(VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO)
			.pNext(NULL)
			.flags(0)
			.pQueueCreateInfos(internalQueueCreateInfos)
			.ppEnabledExtensionNames(extensionsBuffer);
			
			PointerBuffer devicePointerBuffer = stack.mallocPointer(1);
			int err = vkCreateDevice(internal, vkDeviceCreateInfo, null, devicePointerBuffer);
			if(err != VK_SUCCESS) {
				throw new AssertionError("Failed to create device: "+VKUtil.translateVulkanResult(err));
			}
			
			long deviceHandle = devicePointerBuffer.get(0);
			VkDevice internalDevice = new VkDevice(deviceHandle, internal, vkDeviceCreateInfo);
			return new VulkanLogicalDevice(this, internalDevice);
		}
	}
	
	public VulkanSurfaceCapabilities getSurfaceCapabilities(MemoryStack stack, VulkanSurface surface) {
		VkSurfaceCapabilitiesKHR surfaceCapabilitiesKHR = VkSurfaceCapabilitiesKHR.callocStack(stack);
		int err = vkGetPhysicalDeviceSurfaceCapabilitiesKHR(internal, surface.getHandle(), surfaceCapabilitiesKHR);
		if(err != VK_SUCCESS) {
			throw new AssertionError("Failed to get physical device surface capabilities: "+VKUtil.translateVulkanResult(err));
		}
		return new VulkanSurfaceCapabilities(surfaceCapabilitiesKHR);		
	}

	public List<VulkanSurfacePresentMode> getSurfacePresentModes(VulkanSurface surface){
		List<VulkanSurfacePresentMode> presentModes = new ArrayList<>();
		try(MemoryStack stack = MemoryStack.stackPush()){
			IntBuffer pPresentModeCount = stack.mallocInt(1);
			vkGetPhysicalDeviceSurfacePresentModesKHR(internal, surface.getHandle(), pPresentModeCount, null);
			
			IntBuffer modes = stack.mallocInt(pPresentModeCount.get(0));
			vkGetPhysicalDeviceSurfacePresentModesKHR(internal, surface.getHandle(), pPresentModeCount, modes);
			while(modes.hasRemaining()) {
				presentModes.add(VulkanSurfacePresentMode.get(modes.get()));
			}
		}
		return presentModes;
	}
	
	public List<VulkanSurfaceFormat> getSurfaceFormats(VulkanSurface surface) {
		List<VulkanSurfaceFormat> formats = new ArrayList<>();
		try(MemoryStack stack = MemoryStack.stackPush()){
			IntBuffer pSurfaceFormatCount = stack.mallocInt(1);
			vkGetPhysicalDeviceSurfaceFormatsKHR(internal, surface.getHandle(), pSurfaceFormatCount, null);
			
			int count = pSurfaceFormatCount.get(0);
			VkSurfaceFormatKHR.Buffer surfaceFormats = VkSurfaceFormatKHR.callocStack(count, stack);
			vkGetPhysicalDeviceSurfaceFormatsKHR(internal, surface.getHandle(), pSurfaceFormatCount, surfaceFormats);
			while(surfaceFormats.hasRemaining()) {
				VkSurfaceFormatKHR format = surfaceFormats.get();
				formats.add(new VulkanSurfaceFormat(format.format(), format.colorSpace()));
			}			
		}
		return formats;
	}
	
	public VulkanPhysicalDeviceMemoryProperties getDeviceMemoryProperties() {
		if(memoryProperties == null) {
			try(MemoryStack stack = MemoryStack.stackPush()){
				VkPhysicalDeviceMemoryProperties memoryPropertiesInternal = VkPhysicalDeviceMemoryProperties.callocStack(stack);
				vkGetPhysicalDeviceMemoryProperties(internal, memoryPropertiesInternal);
				
				VulkanMemoryType[] memoryTypes = new VulkanMemoryType[memoryPropertiesInternal.memoryTypeCount()];
				VulkanMemoryHeap[] memoryHeaps = new VulkanMemoryHeap[memoryPropertiesInternal.memoryHeapCount()];
				
				for (int i = 0; i < memoryTypes.length; i++) {
					VkMemoryType memoryType = memoryPropertiesInternal.memoryTypes(i);
					memoryTypes[i] = new VulkanMemoryType(i, memoryType.propertyFlags(), memoryType.heapIndex());
				}
				
				for (int i = 0; i < memoryHeaps.length; i++) {
					VkMemoryHeap memoryHeap = memoryPropertiesInternal.memoryHeaps(i);
					memoryHeaps[i] = new VulkanMemoryHeap(i, memoryHeap.size(), memoryHeap.flags());
				}
				
				memoryProperties = new VulkanPhysicalDeviceMemoryProperties(memoryTypes, memoryHeaps);
			}
		}
		
		return memoryProperties;
	}
	
	private synchronized void ensureLoadDeviceProperties() {
		if (deviceProperties != null) {
			return;
		}
		deviceProperties = VkPhysicalDeviceProperties.calloc();
		vkGetPhysicalDeviceProperties(internal, deviceProperties);
	}

	
}

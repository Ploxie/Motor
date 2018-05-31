package org.ploxie.vulkan.device;

import static org.lwjgl.vulkan.VK10.VK_SUCCESS;
import static org.lwjgl.vulkan.VK10.vkEnumerateDeviceExtensionProperties;
import static org.lwjgl.vulkan.VK10.vkGetPhysicalDeviceProperties;
import static org.lwjgl.vulkan.VK10.vkGetPhysicalDeviceQueueFamilyProperties;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Set;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkExtensionProperties;
import org.lwjgl.vulkan.VkPhysicalDevice;
import org.lwjgl.vulkan.VkPhysicalDeviceProperties;
import org.lwjgl.vulkan.VkQueueFamilyProperties;
import org.ploxie.vulkan.VulkanInstance;
import org.ploxie.vulkan.queue.VulkanQueueFamilyProperties;
import org.ploxie.vulkan.queue.VulkanQueueFamilyPropertiesList;
import org.ploxie.vulkan.utils.VKUtil;

import lombok.Data;
import lombok.Getter;

public class VulkanPhysicalDevice {

	@Getter
	private final VulkanInstance instance;
	@Getter
	private final VkPhysicalDevice handle;
	
	private VkPhysicalDeviceProperties deviceProperties;
	private Set<String> extensionsSupported = null;
	
	public VulkanPhysicalDevice(VulkanInstance vulkanInstance, VkPhysicalDevice internal) {
		this.instance = vulkanInstance;
		this.handle = internal;
		
		deviceProperties = VkPhysicalDeviceProperties.calloc();
		vkGetPhysicalDeviceProperties(internal, deviceProperties);
	}
	
	public synchronized boolean isExtensionSupported(String name) {
		if (this.extensionsSupported == null) {
			try (MemoryStack stack = MemoryStack.stackPush()) {
				IntBuffer pPropertyCount = stack.mallocInt(1);
				int err = vkEnumerateDeviceExtensionProperties(handle, (ByteBuffer)null, pPropertyCount, null);
				if(err != VK_SUCCESS) {
					throw new AssertionError("Failed to get extensions supported: "+VKUtil.translateVulkanResult(err));
				}
				
				VkExtensionProperties.Buffer extensionProperties = VkExtensionProperties.callocStack(pPropertyCount.get(0), stack);
				err = vkEnumerateDeviceExtensionProperties(handle, (ByteBuffer)null, pPropertyCount,  extensionProperties);
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
			vkGetPhysicalDeviceQueueFamilyProperties(handle, countBuffer, null);
			int queueFamilyCount = countBuffer.get(0);

			VkQueueFamilyProperties.Buffer queueFamilyProperties = VkQueueFamilyProperties.calloc(queueFamilyCount);
			vkGetPhysicalDeviceQueueFamilyProperties(handle, countBuffer, queueFamilyProperties);

			VulkanQueueFamilyPropertiesList familyPropertiesList = new VulkanQueueFamilyPropertiesList();
			for (int i = 0; i < queueFamilyCount; i++) {
				VkQueueFamilyProperties internal = queueFamilyProperties.get(i);
				VulkanQueueFamilyProperties familyProperties = new VulkanQueueFamilyProperties(i, this, internal);
				familyPropertiesList.add(familyProperties);
			}

			return familyPropertiesList;
		}
	}
	
	public String getName() {
		return deviceProperties.deviceNameString();
	}
}

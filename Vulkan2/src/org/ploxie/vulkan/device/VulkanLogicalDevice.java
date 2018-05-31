package org.ploxie.vulkan.device;

import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_SUCCESS;
import static org.lwjgl.vulkan.VK10.vkCreateDevice;
import static org.lwjgl.vulkan.VK10.vkGetDeviceQueue;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkDeviceCreateInfo;
import org.lwjgl.vulkan.VkDeviceQueueCreateInfo;
import org.lwjgl.vulkan.VkQueue;
import org.ploxie.vulkan.queue.VulkanDeviceQueueCreateInfo;
import org.ploxie.vulkan.queue.VulkanQueue;
import org.ploxie.vulkan.utils.BufferUtils;
import org.ploxie.vulkan.utils.VKUtil;

import lombok.Getter;

public class VulkanLogicalDevice {

	@Getter
	private final VkDevice handle;
	
	private Map<String, VulkanQueue> vulkanQueues = new HashMap<>();
		
	public VulkanLogicalDevice(VulkanPhysicalDevice physicalDevice, List<String> extensions, VulkanDeviceQueueCreateInfo... queueFamilyProperties) {
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
				if (!physicalDevice.isExtensionSupported(extension)) {
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
			int err = vkCreateDevice(physicalDevice.getHandle(), vkDeviceCreateInfo, null, devicePointerBuffer);
			if(err != VK_SUCCESS) {
				throw new AssertionError("Failed to create device: "+VKUtil.translateVulkanResult(err));
			}
			
			long deviceHandle = devicePointerBuffer.get(0);
			handle = new VkDevice(deviceHandle, physicalDevice.getHandle(), vkDeviceCreateInfo);			
		}		
	}
	
	public VulkanQueue getDeviceQueue(int queueFamilyIndex, int queueIndex) {
		String cacheKey = queueFamilyIndex + ":"+queueIndex;
		VulkanQueue queue = vulkanQueues.get(cacheKey);
		if(queue == null) {
			try (MemoryStack stack = MemoryStack.stackPush()){
				PointerBuffer pQueue = stack.mallocPointer(1);
				vkGetDeviceQueue(handle, queueFamilyIndex, queueIndex, pQueue);
				long queueHandle = pQueue.get(0);
				queue = new VulkanQueue(this, new VkQueue(queueHandle, handle));
				vulkanQueues.put(cacheKey, queue);
			}
		}			
		return queue;
	}
	
}

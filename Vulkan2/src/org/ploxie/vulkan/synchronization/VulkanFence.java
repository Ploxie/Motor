package org.ploxie.vulkan.synchronization;

import java.nio.LongBuffer;

import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkFenceCreateInfo;
import org.ploxie.vulkan.device.VulkanLogicalDevice;
import org.ploxie.vulkan.utils.VKUtil;

import lombok.Getter;

public class VulkanFence {

	@Getter
	private long handle;
	
	private LongBuffer pHandle;
	private VulkanLogicalDevice logicalDevice;
	
	public VulkanFence(VulkanLogicalDevice logicalDevice) {
		this.logicalDevice = logicalDevice;
		
		VkFenceCreateInfo createInfo = VkFenceCreateInfo.calloc()
				.sType(VK10.VK_STRUCTURE_TYPE_FENCE_CREATE_INFO)
				.pNext(0)
				.flags(VK10.VK_FENCE_CREATE_SIGNALED_BIT);
		
		pHandle = MemoryUtil.memAllocLong(1);
		
		int err = VK10.vkCreateFence(logicalDevice.getHandle(), createInfo, null, pHandle);
		if(err != VK10.VK_SUCCESS) {
			throw new AssertionError("Failed to create Fence: "+VKUtil.translateVulkanResult(err));
		}
		
		handle = pHandle.get(0);
		
		createInfo.free();
	}
	
	public void reset() {
		int err = VK10.vkResetFences(logicalDevice.getHandle(), handle);
		if(err != VK10.VK_SUCCESS) {
			throw new AssertionError("Failed to reset Fence: "+VKUtil.translateVulkanResult(err));
		}
	}
	
	public void waitForFence(){		
		int err = VK10.vkWaitForFences(logicalDevice.getHandle(), pHandle, true, 1000000000l);
		if(err != VK10.VK_SUCCESS) {
			throw new AssertionError("Failed to reset Fence: "+VKUtil.translateVulkanResult(err));
		}
	}
	
	public void destroy(){		
		VK10.vkDestroyFence(logicalDevice.getHandle(), handle, null);		
	}
	
}

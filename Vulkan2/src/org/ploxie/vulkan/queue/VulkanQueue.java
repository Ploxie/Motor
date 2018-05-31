package org.ploxie.vulkan.queue;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkPresentInfoKHR;
import org.lwjgl.vulkan.VkQueue;
import org.lwjgl.vulkan.VkSubmitInfo;
import org.ploxie.vulkan.buffer.VulkanCommandBuffer;
import org.ploxie.vulkan.command.VulkanSubmitInfo;
import org.ploxie.vulkan.device.VulkanLogicalDevice;
import org.ploxie.vulkan.utils.VKUtil;

import static org.lwjgl.vulkan.KHRSwapchain.*;
import static org.lwjgl.vulkan.VK10.*;
import static org.lwjgl.system.MemoryStack.*;

import lombok.Data;

@Data
public class VulkanQueue {

	private final VulkanLogicalDevice device;
	private final VkQueue internal;
	
	
	public void submit(VulkanCommandBuffer... commandBuffers) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			PointerBuffer commandPointerBuffer = stack.mallocPointer(commandBuffers.length);
			for(VulkanCommandBuffer buffer : commandBuffers) {
				commandPointerBuffer.put(buffer.getHandle());
			}
			
			commandPointerBuffer.flip();
			VulkanSubmitInfo submitInfo = new VulkanSubmitInfo();
			submitInfo.setCommandBuffers(commandPointerBuffer);						
			submit(submitInfo);
		}
	}
	
	public void submit(VulkanSubmitInfo submitInfo) {
		int err = vkQueueSubmit(internal, submitInfo.getInternal(), VK_NULL_HANDLE);
		if (err != VK_SUCCESS) {
			throw new AssertionError("Failed to submit render queue: "+ VKUtil.translateVulkanResult(err));
		}
	}
	
	public void present(VkPresentInfoKHR presentInfoKHR) {
		int err = vkQueuePresentKHR(internal, presentInfoKHR);
		if (err != VK_SUCCESS) {
			throw new AssertionError("Failed to present the swapchain image: "+ VKUtil.translateVulkanResult(err));
		}
	}

	public void waitIdle() {
		int err = vkQueueWaitIdle(internal);
		if (err != VK_SUCCESS) {
			throw new AssertionError("Failed to wait idle: "+ VKUtil.translateVulkanResult(err));
		}
	}
	
}

package org.ploxie.vulkan.command;

import static org.lwjgl.system.MemoryUtil.memAllocPointer;

import java.nio.IntBuffer;
import java.nio.LongBuffer;

import org.lwjgl.PointerBuffer;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkSubmitInfo;
import org.ploxie.vulkan.buffer.VulkanCommandBuffer;
import org.ploxie.vulkan.queue.VulkanQueue;
import org.ploxie.vulkan.synchronization.VulkanFence;
import org.ploxie.vulkan.utils.VKUtil;

import lombok.Getter;

public class VulkanSubmitInfo {

	@Getter
	private VkSubmitInfo internal;

	private VulkanFence fence;
	
	private PointerBuffer pCommandBuffer;
	
	public VulkanSubmitInfo() {
		internal = VkSubmitInfo.calloc().sType(VK10.VK_STRUCTURE_TYPE_SUBMIT_INFO).pNext(0);
		
		pCommandBuffer = memAllocPointer(1);
	}


	public void setCommandBuffers(PointerBuffer buffers) {	
		internal.pCommandBuffers(buffers);
	}
	
	public void setCommandBuffer(VulkanCommandBuffer commandBuffer) {
		pCommandBuffer.put(0, commandBuffer.getInternal());
		internal.pCommandBuffers(pCommandBuffer);
	}
	

	public void setWaitSemaphores(LongBuffer semaphores) {

		internal.waitSemaphoreCount(semaphores.remaining());
		internal.pWaitSemaphores(semaphores);
	}

	public void setSignalSemaphores(LongBuffer semaphores) {

		internal.pSignalSemaphores(semaphores);
	}

	public void setWaitDstStageMask(IntBuffer waitDstStageMasks) {

		internal.pWaitDstStageMask(waitDstStageMasks);
	}

	public void submit(VulkanQueue queue) {

		if (fence != null) {
			fence.reset();
		}

		int err = VK10.vkQueueSubmit(queue.getInternal(), internal, fence == null ? VK10.VK_NULL_HANDLE : fence.getHandle());
		if(err != VK10.VK_SUCCESS) {
			throw new AssertionError("Failed to submit to queue: "+VKUtil.translateVulkanResult(err));
		}
	}

}

package org.ploxie.vulkan.command;

import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.vulkan.KHRSwapchain.VK_STRUCTURE_TYPE_PRESENT_INFO_KHR;

import java.nio.IntBuffer;
import java.nio.LongBuffer;

import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.VkPresentInfoKHR;
import org.ploxie.vulkan.swapchain.VulkanSwapChain;
import org.ploxie.vulkan.synchronization.VulkanSemaphore;
import lombok.Getter;

public class VulkanPresentInfo {

	@Getter
	private VkPresentInfoKHR internal;
	
	private LongBuffer pSwapchain;
	private IntBuffer pImageIndices;

	public VulkanPresentInfo() {
		internal = VkPresentInfoKHR.calloc().sType(VK_STRUCTURE_TYPE_PRESENT_INFO_KHR).pNext(NULL).pResults(null);			
		pSwapchain = MemoryUtil.memAllocLong(1);
		pImageIndices = MemoryUtil.memAllocInt(1);
		internal.swapchainCount(1);
		internal.pSwapchains(pSwapchain);
		internal.pImageIndices(pImageIndices);
	}


	public void setWaitSemaphore(VulkanSemaphore semaphore) {
		internal.pWaitSemaphores(semaphore.getHandlePointer());
	}

	public void setSwapchain(VulkanSwapChain swapchain) {
		pSwapchain.put(0, swapchain.getHandle());
	}
	
	
	public void setImageIndices(int indices) {
		pImageIndices.put(0, indices);
	}


}

package org.ploxie.engine.graphics.vulkan;

import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.vulkan.KHRSwapchain.VK_STRUCTURE_TYPE_PRESENT_INFO_KHR;

import java.nio.IntBuffer;
import java.nio.LongBuffer;

import org.lwjgl.vulkan.VkPresentInfoKHR;
import org.ploxie.engine.display.IDrawSurface;
import org.ploxie.engine.graphics.IGraphicsManager;
import org.ploxie.engine.graphics.IRenderer;
import org.ploxie.vulkan.device.VulkanLogicalDevice;
import org.ploxie.vulkan.math.VulkanExtent2D;
import org.ploxie.vulkan.queue.VulkanQueue;
import org.ploxie.vulkan.surface.VulkanSurfacePresentMode;
import org.ploxie.vulkan.swapchain.VulkanSwapChain;
import org.ploxie.vulkan.synchronization.VulkanSemaphore;

import lombok.Getter;
import lombok.Setter;

public class VulkanRenderer implements IRenderer{
	
	private VulkanManager manager;
	private VulkanSwapChain swapChain;
	private IDrawSurface drawSurface;
	private VulkanLogicalDevice device;
	private VulkanQueue queue;
	
	IntBuffer pImageIndex;
	LongBuffer pSwapchains;
	LongBuffer pRenderCompleteSemaphore;
	LongBuffer pImageAcquiredSemaphore;
	VkPresentInfoKHR presentInfoKHR;
	VulkanSemaphore renderCompleteSemaphore;
	VulkanSemaphore imageAcquireSemaphore;
	
	private boolean recreateSwapchain = true;

	@Override
	public void initialize(IGraphicsManager manager) {
		this.manager = (VulkanManager)manager;
		this.drawSurface = manager.getDrawSurface();
		
		this.queue = this.manager.getQueue();
		this.device = this.manager.getDevice();
		
		pImageAcquiredSemaphore = memAllocLong(1);
		pRenderCompleteSemaphore = memAllocLong(1);		
		pSwapchains = memAllocLong(1);
		pImageIndex = memAllocInt(1);
		
		presentInfoKHR = VkPresentInfoKHR.calloc()
				.sType(VK_STRUCTURE_TYPE_PRESENT_INFO_KHR)
				.pNext(NULL)
				.pWaitSemaphores(pRenderCompleteSemaphore)
				.swapchainCount(pSwapchains.remaining())
				.pSwapchains(pSwapchains)
				.pImageIndices(pImageIndex)
				.pResults(null);
		
		VulkanLogicalDevice device = this.manager.getDevice();
		
		imageAcquireSemaphore = device.createSemaphore();
		pImageAcquiredSemaphore.put(0, imageAcquireSemaphore.getHandle());
		
		renderCompleteSemaphore = device.createSemaphore();
		pRenderCompleteSemaphore.put(0, renderCompleteSemaphore.getHandle());
		
	}
	
	@Override
	public void presentFrame() {
		
		int imageIndex = device.acquireNextImageIndexKHR(swapChain, -1, imageAcquireSemaphore);
		pImageIndex.put(0, imageIndex);
		
		pSwapchains.put(0, swapChain.getHandle());
		queue.present(presentInfoKHR);
	}
	
	public void update() {
		if(recreateSwapchain) {
			recreateSwapchain();
			recreateSwapchain = false;
		}
	}

	private void recreateSwapchain() {
		VulkanExtent2D extent = new VulkanExtent2D(drawSurface.getDimensions().x, drawSurface.getDimensions().y);
		
		VulkanSwapChain oldSwapChain = swapChain;
		
		swapChain = device.createSwapChain(drawSurface.getHandle(), extent, VulkanSurfacePresentMode.MAILBOX,oldSwapChain);
		//queue.waitIdle();
		device.createImageViewsForSwapChain(swapChain);
	}

}

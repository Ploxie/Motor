package org.ploxie.engine.vulkan.display;

import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memAllocPointer;
import static org.lwjgl.vulkan.KHRSwapchain.VK_STRUCTURE_TYPE_PRESENT_INFO_KHR;
import static org.lwjgl.vulkan.VK10.VK_FORMAT_D32_SFLOAT;
import static org.lwjgl.vulkan.VK10.VK_FORMAT_R32G32B32_SFLOAT;
import static org.lwjgl.vulkan.VK10.VK_FORMAT_R32G32_SFLOAT;
import static org.lwjgl.vulkan.VK10.VK_FORMAT_R32_UINT;
import static org.lwjgl.vulkan.VK10.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkPresentInfoKHR;
import org.ploxie.engine.vulkan.context.VulkanContext;
import org.ploxie.engine2.display.Window;
import org.ploxie.utils.Color;
import org.ploxie.utils.FileUtils;
import org.ploxie.vulkan.buffer.VulkanBuffer;
import org.ploxie.vulkan.buffer.VulkanBufferUsageFlag;
import org.ploxie.vulkan.buffer.VulkanCommandBuffer;
import org.ploxie.vulkan.buffer.VulkanFrameBuffer;
import org.ploxie.vulkan.buffer.vertex.AttributeDescription;
import org.ploxie.vulkan.buffer.vertex.BindingDescription;
import org.ploxie.vulkan.buffer.vertex.VertexInputInfo;
import org.ploxie.vulkan.command.VulkanCommandPool;
import org.ploxie.vulkan.command.VulkanPresentInfo;
import org.ploxie.vulkan.command.VulkanSubmitInfo;
import org.ploxie.vulkan.descriptor.VulkanDescriptorPool;
import org.ploxie.vulkan.descriptor.VulkanDescriptorSet;
import org.ploxie.vulkan.device.VulkanLogicalDevice;
import org.ploxie.vulkan.image.VulkanImage;
import org.ploxie.vulkan.image.VulkanImageAspectMask;
import org.ploxie.vulkan.image.VulkanImageLayout;
import org.ploxie.vulkan.image.VulkanImageUsageFlag;
import org.ploxie.vulkan.image.VulkanImageView;
import org.ploxie.vulkan.math.VulkanExtent2D;
import org.ploxie.vulkan.math.VulkanOffset2D;
import org.ploxie.vulkan.math.VulkanRect2D;
import org.ploxie.vulkan.memory.VulkanMemoryAllocation;
import org.ploxie.vulkan.memory.VulkanMemoryPropertyFlag;
import org.ploxie.vulkan.pipeline.VulkanGraphicsPipeline;
import org.ploxie.vulkan.pipeline.VulkanGraphicsPipelineProperties;
import org.ploxie.vulkan.queue.VulkanQueue;
import org.ploxie.vulkan.render.VulkanRenderPass;
import org.ploxie.vulkan.shader.VulkanShaderModule;
import org.ploxie.vulkan.shader.VulkanShaderModules;
import org.ploxie.vulkan.surface.VulkanSurfacePresentMode;
import org.ploxie.vulkan.swapchain.VulkanSwapChain;
import org.ploxie.vulkan.synchronization.VulkanSemaphore;
import org.ploxie.vulkan.utils.BufferUtils;
import org.ploxie.vulkan.viewport.VulkanViewportProperties;

import lombok.Getter;

public class VulkanWindow extends Window {

	private VulkanLogicalDevice logicalDevice;
	@Getter
	private VulkanSwapChain swapchain;
	private VulkanQueue graphicsQueue;
	@Getter
	private VulkanExtent2D extent;

	private VulkanPresentInfo presentInfo;
	//private VulkanSemaphore renderCompleteSemaphore;
	private VulkanSemaphore imageAcquireSemaphore;
	
	
	@Getter
	private int imageIndex;
	
	private VulkanCommandPool graphicsCommandPool;

	private VulkanSubmitInfo submitInfo;

	private VulkanRenderPass renderPass;
	private VulkanViewportProperties viewport;
	private VulkanRect2D scissor;
	private VulkanGraphicsPipelineProperties pipelineProperties;
	private VulkanGraphicsPipeline graphicsPipeline;

	private VulkanImage depthBufferImage = null;
	private VulkanImageView depthBufferImageView = null;
	private VulkanFrameBuffer[] frameBuffers;	
	

	public VulkanWindow() {
		super(VulkanContext.getConfiguration().getDisplayTitle(),
				VulkanContext.getConfiguration().getWindowWidth(),
				VulkanContext.getConfiguration().getWindowHeight());
	}

	@Override
	public void create() {
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, VK10.VK_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_NO_API);
		setHandle(GLFW.glfwCreateWindow(getWidth(), getHeight(), getTitle(), 0, 0));

		if (getHandle() == 0) {
			throw new RuntimeException("Failed to create window");
		}

		logicalDevice = VulkanContext.getLogicalDevice();

		int graphicsFamilyIndex = logicalDevice.getPhysicalDevice().getQueueFamilyProperties().getFirstGraphicsQueue().getIndex();
		graphicsQueue = logicalDevice.getDeviceQueue(graphicsFamilyIndex, 0);
		graphicsCommandPool = logicalDevice.getCommandPool(graphicsFamilyIndex);

		//renderCompleteSemaphore = logicalDevice.createSemaphore();
		imageAcquireSemaphore = logicalDevice.createSemaphore();

		presentInfo = new VulkanPresentInfo();
		presentInfo.setWaitSemaphore(imageAcquireSemaphore);
				
		resize(getWidth(), getHeight());
		GLFW.glfwShowWindow(getHandle());
	}
	
	@Override
	public void preRender() {
		imageIndex = logicalDevice.acquireNextImageIndexKHR(swapchain, -1, imageAcquireSemaphore);
		presentInfo.setImageIndices(imageIndex);
	}
	
	@Override
	public void draw() {		
		graphicsQueue.present(presentInfo);
		graphicsQueue.waitIdle();
	}

	@Override
	public void shutdown() {
		GLFW.glfwDestroyWindow(getHandle());
	}

	@Override
	public boolean isCloseRequested() {
		return GLFW.glfwWindowShouldClose(getHandle());
	}

	@Override
	public void resize(int x, int y) {
		extent = new VulkanExtent2D(x, y);

		swapchain = logicalDevice.createSwapChain(getHandle(), extent, VulkanSurfacePresentMode.MAILBOX, swapchain);
		logicalDevice.createImageViewsForSwapChain(swapchain);
		renderPass = logicalDevice.createRenderPass(swapchain.getImageFormat().getColorFormat(), VK_FORMAT_D32_SFLOAT);
		
		int depthFormat = VK_FORMAT_D32_SFLOAT;		
		depthBufferImage = logicalDevice.createImage2D(1, depthFormat, extent.getWidth(), extent.getHeight(), VulkanImageUsageFlag.DEPTH_STENCIL);
		logicalDevice.allocateMemory(depthBufferImage, VulkanMemoryPropertyFlag.DEVICE_LOCAL);
		depthBufferImageView = logicalDevice.createImageView(depthBufferImage, depthFormat, VulkanImageAspectMask.DEPTH);
		logicalDevice.setImageLayout(graphicsCommandPool, depthBufferImage, VulkanImageLayout.DEPTH_STENCIL_ATTACHMENT_OPTIMAL);
		
		frameBuffers = logicalDevice.createFrameBuffers(extent, swapchain, depthBufferImageView, renderPass);		
		presentInfo.setSwapchain(swapchain);
	}
	
	public VulkanFrameBuffer getCurrentFrameBuffer() {
		return frameBuffers[imageIndex];
	}

}

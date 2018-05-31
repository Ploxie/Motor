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

public class VulkanWindow extends Window {

	private VulkanLogicalDevice logicalDevice;
	private VulkanSwapChain swapchain;
	private VulkanQueue graphicsQueue;
	private VulkanCommandPool graphicsCommandPool;

	private VulkanSubmitInfo submitInfo;
	private VulkanPresentInfo presentInfo;
	private VulkanSemaphore renderCompleteSemaphore;
	private VulkanSemaphore imageAcquireSemaphore;

	private VulkanRenderPass renderPass;
	private VulkanViewportProperties viewport;
	private VulkanRect2D scissor;
	private VulkanGraphicsPipelineProperties pipelineProperties;
	private VulkanGraphicsPipeline graphicsPipeline;

	private VulkanImage depthBufferImage = null;
	private VulkanImageView depthBufferImageView = null;
	private VulkanFrameBuffer[] frameBuffers;	
	
	private VulkanCommandBuffer[] frameCommandBuffers;
	
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

		renderCompleteSemaphore = logicalDevice.createSemaphore();
		imageAcquireSemaphore = logicalDevice.createSemaphore();

		presentInfo = new VulkanPresentInfo();
		presentInfo.setWaitSemaphore(renderCompleteSemaphore);
		
		IntBuffer pWaitDstStageMask = memAllocInt(1);
		pWaitDstStageMask.put(0, VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT);
		
		submitInfo = new VulkanSubmitInfo();
		submitInfo.setWaitSemaphores(imageAcquireSemaphore.getHandlePointer());
		submitInfo.setWaitDstStageMask(pWaitDstStageMask);
		submitInfo.setSignalSemaphores(renderCompleteSemaphore.getHandlePointer());	

		ByteBuffer triangleVertCode = null;
		ByteBuffer triangleFragCode = null;
		try {
			triangleVertCode = BufferUtils.wrap(FileUtils.getFileToBytes("res/vert.spv"));
			triangleFragCode = BufferUtils.wrap(FileUtils.getFileToBytes("res/frag.spv"));
		} catch (IOException e) {
			e.printStackTrace();
		}		
				
		VulkanShaderModule triangleVertShader = logicalDevice.loadShader(triangleVertCode);
		VulkanShaderModule triangleFragShader = logicalDevice.loadShader(triangleFragCode);
		
		VulkanShaderModules shaderModules = VulkanShaderModules.builder().vertex(triangleVertShader).fragment(triangleFragShader).build();
		
		pipelineProperties = new VulkanGraphicsPipelineProperties(shaderModules);
		
		int stride = 2 * 4 + 3 * 4;
		BindingDescription bindingDescription = new BindingDescription(0, stride);
		AttributeDescription[] attributeDescriptions = new AttributeDescription[] {
				new AttributeDescription(0, 0, VK_FORMAT_R32G32_SFLOAT, 0),
				new AttributeDescription(1, 0, VK_FORMAT_R32G32B32_SFLOAT, 8),
		};
		
		VertexInputInfo vertexInputInfo = new VertexInputInfo(bindingDescription, attributeDescriptions);
		pipelineProperties.setVertexInputInfo(vertexInputInfo);		
		
		resize(getWidth(), getHeight());
		GLFW.glfwShowWindow(getHandle());
	}

	private void createCommandBuffers() {
		
		ByteBuffer vertexBytes = memAlloc(8 * 4 + 12 * 4);
		FloatBuffer fb = vertexBytes.asFloatBuffer();
		fb.put(-0.5f).put(-0.5f) .put(1).put(0).put(0);
		fb.put(0.5f).put(-0.5f)  .put(0).put(1).put(0);
		fb.put(0.5f).put(0.5f).put(0).put(0).put(1);
		fb.put(-0.5f).put(0.5f).put(0).put(1).put(1);
		fb.flip();
		
		int indexCount = 6 * 4;
		ByteBuffer indexBytes = memAlloc(indexCount);
		IntBuffer ib = indexBytes.asIntBuffer();
		
		ib.put(0).put(3).put(2).put(0).put(2).put(1);
		ib.flip();
		
		VulkanBuffer vertexStagingBuffer = logicalDevice.createBuffer(vertexBytes.remaining(), true, VulkanBufferUsageFlag.TRANSFER_SRC);
		VulkanMemoryAllocation vertexStagingMemoryAllocation = logicalDevice.allocateMemory(vertexStagingBuffer, VulkanMemoryPropertyFlag.HOST_VISIBLE, VulkanMemoryPropertyFlag.HOST_COHERENT);
		logicalDevice.fillBuffer(vertexStagingMemoryAllocation, vertexStagingBuffer, vertexBytes);
		
		VulkanBuffer vertexBuffer = logicalDevice.createBuffer(vertexBytes.remaining(), true, VulkanBufferUsageFlag.TRANSFER_DST, VulkanBufferUsageFlag.VERTEX);
		VulkanMemoryAllocation vertexMemoryAllocation = logicalDevice.allocateMemory(vertexBuffer,  VulkanMemoryPropertyFlag.DEVICE_LOCAL);
		VulkanCommandBuffer stageVertexCommandBuffer = logicalDevice.copyBuffer(graphicsCommandPool, vertexStagingBuffer, vertexBuffer, vertexBytes.remaining());
		graphicsQueue.submit(stageVertexCommandBuffer);
		
		VulkanBuffer indexStagingBuffer = logicalDevice.createBuffer(indexBytes.remaining(), true, VulkanBufferUsageFlag.TRANSFER_SRC);
		VulkanMemoryAllocation indexStagingMemoryAllocation = logicalDevice.allocateMemory(indexStagingBuffer, VulkanMemoryPropertyFlag.HOST_VISIBLE, VulkanMemoryPropertyFlag.HOST_COHERENT);
		logicalDevice.fillBuffer(indexStagingMemoryAllocation, indexStagingBuffer, indexBytes);
		
		VulkanBuffer indexBuffer = logicalDevice.createBuffer(indexBytes.remaining(), true, VulkanBufferUsageFlag.TRANSFER_DST, VulkanBufferUsageFlag.INDEX);
		VulkanMemoryAllocation indexMemoryAllocation = logicalDevice.allocateMemory(indexBuffer, VulkanMemoryPropertyFlag.DEVICE_LOCAL);
		VulkanCommandBuffer stageIndexCommandBuffer = logicalDevice.copyBuffer(graphicsCommandPool, indexStagingBuffer, indexBuffer, indexBytes.remaining());
		graphicsQueue.submit(stageIndexCommandBuffer);
		
		VulkanDescriptorPool descriptorPool = logicalDevice.createDescriptorPool(1,1);
		VulkanDescriptorSet descriptorSet = descriptorPool.allocateDescriptorSet(graphicsPipeline.getDescriptorSetLayouts()[0]);
		
		frameCommandBuffers = new VulkanCommandBuffer[frameBuffers.length];
		for(int i = 0 ; i < frameBuffers.length;i++) {
			frameCommandBuffers[i] = logicalDevice.createCommandBuffer(graphicsCommandPool, true);
			
			
			frameCommandBuffers[i].begin(true);			
			frameCommandBuffers[i].beginRenderPass(renderPass, frameBuffers[i], scissor, new Color(0.47f, 0.47f, 0.47f, 1.0f));							
			frameCommandBuffers[i].setViewport(viewport);
			frameCommandBuffers[i].setScissor(scissor);				
			frameCommandBuffers[i].bindPipeline(graphicsPipeline);		
			frameCommandBuffers[i].bindVertexBuffers(vertexBuffer);
			frameCommandBuffers[i].bindIndexBuffer(indexBuffer, VK_FORMAT_R32_UINT);			
			frameCommandBuffers[i].bindDescriptorSets(graphicsPipeline.getLayout(), descriptorSet);				
			frameCommandBuffers[i].drawIndexed(indexCount, 1, 0, 0, 0);			
			frameCommandBuffers[i].endRenderPass();
		
			frameCommandBuffers[i].end();
		}
		
	}
	
	@Override
	public void draw() {
		int imageIndex = logicalDevice.acquireNextImageIndexKHR(swapchain, -1, imageAcquireSemaphore);
		presentInfo.setImageIndices(imageIndex);

		submitInfo.setCommandBuffer(frameCommandBuffers[imageIndex]);
		
		graphicsQueue.submit(submitInfo);
		
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
		VulkanExtent2D extent = new VulkanExtent2D(x, y);

		swapchain = logicalDevice.createSwapChain(getHandle(), extent, VulkanSurfacePresentMode.MAILBOX, swapchain);
		logicalDevice.createImageViewsForSwapChain(swapchain);
		renderPass = logicalDevice.createRenderPass(swapchain.getImageFormat().getColorFormat(), VK_FORMAT_D32_SFLOAT);
		viewport = VulkanViewportProperties.builder().dimensions(extent).build();
		scissor = new VulkanRect2D(new VulkanOffset2D(0,0), extent);
		graphicsPipeline = logicalDevice.createGraphicsPipeline(renderPass, pipelineProperties);
		
		int depthFormat = VK_FORMAT_D32_SFLOAT;
		
		depthBufferImage = logicalDevice.createImage2D(1, depthFormat, extent.getWidth(), extent.getHeight(), VulkanImageUsageFlag.DEPTH_STENCIL);
		VulkanMemoryAllocation depthBufferMemoryAllocation = logicalDevice.allocateMemory(depthBufferImage, VulkanMemoryPropertyFlag.DEVICE_LOCAL);
		depthBufferImageView = logicalDevice.createImageView(depthBufferImage, depthFormat, VulkanImageAspectMask.DEPTH);
		logicalDevice.setImageLayout(graphicsCommandPool, depthBufferImage, VulkanImageLayout.DEPTH_STENCIL_ATTACHMENT_OPTIMAL);
		
		frameBuffers = logicalDevice.createFrameBuffers(extent, swapchain, depthBufferImageView, renderPass);
		
		createCommandBuffers();
		presentInfo.setSwapchain(swapchain);
	}

}

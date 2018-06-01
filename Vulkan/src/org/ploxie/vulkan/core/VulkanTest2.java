package org.ploxie.vulkan.core;


import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.vulkan.VK10.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.vulkan.KHRSwapchain.*;
import static org.lwjgl.vulkan.EXTDebugReport.*;
import static org.lwjgl.glfw.GLFW.GLFW_CLIENT_API;
import static org.lwjgl.glfw.GLFW.GLFW_NO_API;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFWVulkan.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.Arrays;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkDescriptorBufferInfo;
import org.lwjgl.vulkan.VkDescriptorImageInfo;
import org.lwjgl.vulkan.VkPresentInfoKHR;
import org.lwjgl.vulkan.VkSubmitInfo;
import org.lwjgl.vulkan.VkWriteDescriptorSet;
import org.ploxie.engine.display.GraphicsLibrary;
import org.ploxie.engine.display.Window;
import org.ploxie.engine.display.WindowMode;
import org.ploxie.utils.Color;
import org.ploxie.utils.FileUtils;
import org.ploxie.utils.math.vector.Vector2i;
import org.ploxie.vulkan.Vulkan;
import org.ploxie.vulkan.VulkanApiVersion;
import org.ploxie.vulkan.VulkanApplicationInfo;
import org.ploxie.vulkan.VulkanInstance;
import org.ploxie.vulkan.VulkanInstanceProperties;
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
import org.ploxie.vulkan.debug.DefaultVulkanDebugReportCallback;
import org.ploxie.vulkan.debug.VulkanDebugReportType;
import org.ploxie.vulkan.descriptor.VulkanDescriptorPool;
import org.ploxie.vulkan.descriptor.VulkanDescriptorSet;
import org.ploxie.vulkan.descriptor.VulkanUniformBufferDescriptor;
import org.ploxie.vulkan.device.VulkanLogicalDevice;
import org.ploxie.vulkan.device.VulkanPhysicalDevice;
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
import org.ploxie.vulkan.queue.VulkanDeviceQueueCreateInfo;
import org.ploxie.vulkan.queue.VulkanQueue;
import org.ploxie.vulkan.queue.VulkanQueueFamilyProperties;
import org.ploxie.vulkan.queue.VulkanQueueFamilyPropertiesList;
import org.ploxie.vulkan.render.VulkanRenderPass;
import org.ploxie.vulkan.shader.VulkanShaderModule;
import org.ploxie.vulkan.shader.VulkanShaderModules;
import org.ploxie.vulkan.surface.VulkanSurfacePresentMode;
import org.ploxie.vulkan.swapchain.VulkanSwapChain;
import org.ploxie.vulkan.synchronization.VulkanSemaphore;
import org.ploxie.vulkan.utils.BufferUtils;
import org.ploxie.vulkan.utils.VKUtil;
import org.ploxie.vulkan.viewport.VulkanViewportProperties;

public class VulkanTest2 {

	private static boolean recreateSwapchain = true;

	public static void main(String[] args) throws IOException {
		
		//System.in.read();
		
		
		  if (!GLFW.glfwInit()) { throw new RuntimeException("Failed to initialize GLFW"); }
		  if (!glfwVulkanSupported()) { throw new
		  AssertionError("GLFW failed to find the Vulkan loader"); }
		 

		Window window = new Window(new Vector2i(800, 600), WindowMode.WINDOWED);
		//window.setGraphicsLibrary(GraphicsLibrary.VULKAN);
		glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API);
		window.setTitle("Vulkan");
		window.show();

		VulkanApplicationInfo appInfo = new VulkanApplicationInfo("Vulkan Test", "Engine", 1, VulkanApiVersion.create(1, 0, 2));
		VulkanInstanceProperties instanceProperties = Vulkan.createInstanceProperties(appInfo);
		VulkanInstance vulkanInstance = Vulkan.createInstance(instanceProperties);
		
		if(Vulkan.isValidation()) {
			vulkanInstance.setupDebugging(new DefaultVulkanDebugReportCallback(), VulkanDebugReportType.WARNING,VulkanDebugReportType.ERROR );
		}
		
		VulkanPhysicalDevice physicalDevice = vulkanInstance.getPhysicalDevices().get(0);
		VulkanQueueFamilyPropertiesList queueFamilyPropertiesList = physicalDevice.getQueueFamilyProperties();
		VulkanQueueFamilyProperties queueFamilyGraphics = queueFamilyPropertiesList.getFirstGraphicsQueue();

		VulkanLogicalDevice device = physicalDevice.createDevice(Arrays.asList(VK_KHR_SWAPCHAIN_EXTENSION_NAME),
				new VulkanDeviceQueueCreateInfo(queueFamilyGraphics, new float[] { 1 }));
		VulkanQueue queue = device.getDeviceQueue(queueFamilyGraphics.getIndex(), 0);

		System.out.println("Physical Device: " + physicalDevice.getName());

		ByteBuffer triangleVertCode = BufferUtils.wrap(FileUtils.getFileToBytes("res/vert.spv"));
		ByteBuffer triangleFragCode = BufferUtils.wrap(FileUtils.getFileToBytes("res/frag.spv"));
				
		VulkanShaderModule triangleVertShader = device.loadShader(triangleVertCode);
		VulkanShaderModule triangleFragShader = device.loadShader(triangleFragCode);
		
		VulkanShaderModules shaderModules = VulkanShaderModules.builder().vertex(triangleVertShader).fragment(triangleFragShader).build();
		
		VulkanGraphicsPipelineProperties pipelineProperties = new VulkanGraphicsPipelineProperties(shaderModules);
		
		VulkanCommandPool pool = device.createCommandPool(queueFamilyGraphics.getIndex());
		
		
		int stride = 3 * 4 + 3 * 4;
		BindingDescription bindingDescription = new BindingDescription(0, stride);
		AttributeDescription[] attributeDescriptions = new AttributeDescription[] {
				new AttributeDescription(0, 0, VK_FORMAT_R32G32B32_SFLOAT, 0),
				new AttributeDescription(1, 0, VK_FORMAT_R32G32B32_SFLOAT, 12),
		};
		
		VertexInputInfo vertexInputInfo = new VertexInputInfo(bindingDescription, attributeDescriptions);
		pipelineProperties.setVertexInputInfo(vertexInputInfo);
	
		
		ByteBuffer vertexBytes = memAlloc(12 * 4 + 12 * 4);
		FloatBuffer fb = vertexBytes.asFloatBuffer();
		fb.put(-0.5f) .put(-0.5f) .put(1).put(1).put(0).put(0);
		fb.put( 0.5f) .put(-0.5f) .put(1).put(0).put(1).put(0);
		fb.put( 0.5f) .put( 0.5f) .put(1).put(0).put(0).put(1);
		fb.put(-0.5f) .put( 0.5f) .put(1).put(0).put(1).put(1);
		fb.flip();
		
		ByteBuffer indexBytes = memAlloc(6 * 4);
		IntBuffer ib = indexBytes.asIntBuffer();
		
		ib.put(0).put(3).put(2).put(0).put(2).put(1);
		ib.flip();
		
		VulkanBuffer vertexStagingBuffer = device.createBuffer(vertexBytes.remaining(), true, VulkanBufferUsageFlag.TRANSFER_SRC);
		VulkanMemoryAllocation vertexStagingMemoryAllocation = device.allocateMemory(vertexStagingBuffer, VulkanMemoryPropertyFlag.HOST_VISIBLE, VulkanMemoryPropertyFlag.HOST_COHERENT);
		device.fillBuffer(vertexStagingMemoryAllocation, vertexStagingBuffer, vertexBytes);
		
		VulkanBuffer vertexBuffer = device.createBuffer(vertexBytes.remaining(), true, VulkanBufferUsageFlag.TRANSFER_DST, VulkanBufferUsageFlag.VERTEX);
		VulkanMemoryAllocation vertexMemoryAllocation = device.allocateMemory(vertexBuffer,  VulkanMemoryPropertyFlag.DEVICE_LOCAL);
		VulkanCommandBuffer stageVertexCommandBuffer = device.copyBuffer(pool, vertexStagingBuffer, vertexBuffer, vertexBytes.remaining());
		queue.submit(stageVertexCommandBuffer);
		
		VulkanBuffer indexStagingBuffer = device.createBuffer(indexBytes.remaining(), true, VulkanBufferUsageFlag.TRANSFER_SRC);
		VulkanMemoryAllocation indexStagingMemoryAllocation = device.allocateMemory(indexStagingBuffer, VulkanMemoryPropertyFlag.HOST_VISIBLE, VulkanMemoryPropertyFlag.HOST_COHERENT);
		device.fillBuffer(indexStagingMemoryAllocation, indexStagingBuffer, indexBytes);
		
		VulkanBuffer indexBuffer = device.createBuffer(indexBytes.remaining(), true, VulkanBufferUsageFlag.TRANSFER_DST, VulkanBufferUsageFlag.INDEX);
		VulkanMemoryAllocation indexMemoryAllocation = device.allocateMemory(indexBuffer, VulkanMemoryPropertyFlag.DEVICE_LOCAL);
		VulkanCommandBuffer stageIndexCommandBuffer = device.copyBuffer(pool, indexStagingBuffer, indexBuffer, indexBytes.remaining());
		queue.submit(stageIndexCommandBuffer);
				
		renderLoop(window, device, pool, queue, pipelineProperties, vertexBuffer, indexBuffer, 6 * 4);
		
	}

	private static void renderLoop(Window window, VulkanLogicalDevice device,VulkanCommandPool pool,VulkanQueue queue ,VulkanGraphicsPipelineProperties pipelineProperties, VulkanBuffer vertexBuffer, VulkanBuffer indexBuffer, int indexCount) {
		
		VulkanSwapChain swapChain = null;
		
		
		//LongBuffer pRenderCompleteSemaphore = memAllocLong(1);
		//LongBuffer pImageAcquiredSemaphore = memAllocLong(1);
		//LongBuffer pSwapchains = memAllocLong(1);
		//PointerBuffer pCommandBuffers = memAllocPointer(1);
		
		//IntBuffer pImageIndex = memAllocInt(1);
		IntBuffer pWaitDstStageMask = memAllocInt(1);
		pWaitDstStageMask.put(0, VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT);
		
		VulkanSemaphore imageAcquireSemaphore = device.createSemaphore();
		VulkanSemaphore renderCompleteSemaphore = device.createSemaphore();
		
		VulkanSubmitInfo submitInfo = new VulkanSubmitInfo();
		submitInfo.setWaitSemaphores(imageAcquireSemaphore.getHandlePointer());
		submitInfo.setWaitDstStageMask(pWaitDstStageMask);
		//submitInfo.setCommandBuffers(pCommandBuffers);
		submitInfo.setSignalSemaphores(renderCompleteSemaphore.getHandlePointer());				
				
		VulkanPresentInfo presentInfo = new VulkanPresentInfo();
		presentInfo.setWaitSemaphore(renderCompleteSemaphore);
		
		/*VkPresentInfoKHR presentInfoKHR = VkPresentInfoKHR.calloc()
				.sType(VK_STRUCTURE_TYPE_PRESENT_INFO_KHR)
				.pNext(NULL)
				.pWaitSemaphores(renderCompleteSemaphore.getHandlePointer())
				.swapchainCount(pSwapchains.remaining())
				.pSwapchains(pSwapchains)
				.pImageIndices(pImageIndex)
				.pResults(null);*/
		
		/*VulkanSemaphore imageAcquireSemaphore = device.createSemaphore();
		pImageAcquiredSemaphore.put(0, imageAcquireSemaphore.getHandle());
		
		VulkanSemaphore renderCompleteSemaphore = device.createSemaphore();
		pRenderCompleteSemaphore.put(0, renderCompleteSemaphore.getHandle());*/
		
	
		
		VulkanRenderPass renderPass = null;
		VulkanViewportProperties viewport = null;
		VulkanRect2D scissor = null;
		VulkanGraphicsPipeline graphicsPipeline = null;
		
		VulkanImage depthBufferImage = null;
		VulkanMemoryAllocation depthBufferMemoryAllocation = null;
		VulkanImageView depthBufferImageView = null;
		VulkanFrameBuffer[] framebuffers = null;
		
		int uniformBufferSize = 16 * 4;
		VulkanUniformBufferDescriptor uniformBufferDescriptor = device.createUniformBuffer(uniformBufferSize);
		VulkanDescriptorSet descriptorSet = null;
		
		while (!window.isDestroyed()) {
			window.pollEvents();
			if (recreateSwapchain) {
				VulkanSwapChain oldSwapChain = swapChain;
				
				VulkanExtent2D extent = new VulkanExtent2D(window.getDimensions().x, window.getDimensions().y);
				swapChain = device.createSwapChain(window.getHandle(), extent, VulkanSurfacePresentMode.MAILBOX,oldSwapChain);
				System.out.println(swapChain.getSwapImages().length);
				queue.waitIdle();
				device.createImageViewsForSwapChain(swapChain);
				
				int depthFormat = VK_FORMAT_D32_SFLOAT;
				
				depthBufferImage = device.createImage2D(1, depthFormat, extent.getWidth(), extent.getHeight(), VulkanImageUsageFlag.DEPTH_STENCIL);
				depthBufferMemoryAllocation = device.allocateMemory(depthBufferImage, VulkanMemoryPropertyFlag.DEVICE_LOCAL);
				depthBufferImageView = device.createImageView(depthBufferImage, depthFormat, VulkanImageAspectMask.DEPTH);
				device.setImageLayout(pool, depthBufferImage, VulkanImageLayout.DEPTH_STENCIL_ATTACHMENT_OPTIMAL);
				
				renderPass = device.createRenderPass(swapChain.getImageFormat().getColorFormat(), VK_FORMAT_D32_SFLOAT);
				viewport = VulkanViewportProperties.builder().dimensions(extent).build();
				scissor = new VulkanRect2D(new VulkanOffset2D(0,0), extent);
				graphicsPipeline = device.createGraphicsPipeline(renderPass, pipelineProperties);
				
				framebuffers = device.createFrameBuffers(extent, swapChain, depthBufferImageView, renderPass);
				
				queue.waitIdle();
				
				try(MemoryStack stack = MemoryStack.stackPush()){
					VulkanDescriptorPool descriptorPool = device.createDescriptorPool(1,1);
					descriptorSet = descriptorPool.allocateDescriptorSet(graphicsPipeline.getDescriptorSetLayouts()[0]);
					
					
					
					/*VkDescriptorBufferInfo.Buffer bufferInfo = VkDescriptorBufferInfo
							.callocStack(1, stack)
							.buffer(uniformBufferDescriptor.getBuffer().getHandle())
							.offset(0)
							.range(uniformBufferSize);
					
					VkWriteDescriptorSet.Buffer descriptorWrite = VkWriteDescriptorSet.callocStack(1, stack);
					descriptorWrite.get(0)
						.sType(VK_STRUCTURE_TYPE_WRITE_DESCRIPTOR_SET)
						.dstSet(descriptorSet.getHandle())
						.dstBinding(0)
						.dstArrayElement(0)
						.descriptorType(VK_DESCRIPTOR_TYPE_UNIFORM_BUFFER)
						.pBufferInfo(bufferInfo)
						.pImageInfo(null)
						.pTexelBufferView(null);
					
					vkUpdateDescriptorSets(device.getInternal(), descriptorWrite, null);*/
						
				}
				
				recreateSwapchain = false;
			}
			
			int imageIndex = device.acquireNextImageIndexKHR(swapChain, -1, imageAcquireSemaphore);
			presentInfo.setImageIndices(imageIndex);
			//pImageIndex.put(0, imageIndex);
			
			VulkanCommandBuffer commandBuffer = device.createCommandBuffer(pool, true);
			setupCommandBuffers(renderPass, scissor, commandBuffer, framebuffers[imageIndex], graphicsPipeline, viewport, vertexBuffer, indexBuffer, descriptorSet, indexCount);
			
			updateUniformBuffer(window.getDimensions(), device, uniformBufferDescriptor);
						
			//pCommandBuffers.put(0, commandBuffer.getInternal());
			submitInfo.setCommandBuffer(commandBuffer);
			queue.submit(submitInfo);

			//pSwapchains.put(0, swapChain.getHandle());
			presentInfo.setSwapchain(swapChain);
			queue.present(presentInfo);
			
			queue.waitIdle();
			
			commandBuffer.free();
			
			
		}
	}
	
	private static void updateUniformBuffer(Vector2i viewportDimensions, VulkanLogicalDevice device, VulkanUniformBufferDescriptor uniformBufferDescriptor) {
		/*VulkanMemoryAllocation memoryAllocation = uniformBufferDescriptor.getMemoryAllocation();
		
		PointerBuffer pData = memAllocPointer(1);
		int err = vkMapMemory(device.getInternal(), memoryAllocation.getHandle(), 0, 16 * 4, 0, pData);
		long data = pData.get(0);
		memFree(pData);
		

		if (err != VK_SUCCESS) {
			throw new AssertionError("Failed to map UBO memory: " + VKUtil.translateVulkanResult(err));
		}

		
		vkUnmapMemory(device.getInternal(), memoryAllocation.getHandle());*/
	}
	
	private static void setupCommandBuffers(VulkanRenderPass renderPass,VulkanRect2D renderArea, VulkanCommandBuffer commandBuffer,VulkanFrameBuffer framebuffer, VulkanGraphicsPipeline graphicsPipeline,VulkanViewportProperties viewport, VulkanBuffer vertexBuffer,VulkanBuffer indexBuffer,VulkanDescriptorSet descriptorSet,int indexCount) {
		
		commandBuffer.begin();
		
			commandBuffer.beginRenderPass(renderPass, framebuffer,true, renderArea, new Color(0.47f, 0.47f, 0.47f, 1.0f));
			
				// update viewport and scissor
				
				commandBuffer.setViewport(viewport);
				commandBuffer.setScissor(renderArea);
				
				commandBuffer.bindPipeline(graphicsPipeline);
		
				commandBuffer.bindVertexBuffers(vertexBuffer);
				commandBuffer.bindIndexBuffer(indexBuffer, VK_FORMAT_R32_UINT);
			
				commandBuffer.bindDescriptorSets(graphicsPipeline.getLayout(), descriptorSet);
				
				commandBuffer.drawIndexed(indexCount, 1, 0, 0, 0);
			
			commandBuffer.endRenderPass();
		
		commandBuffer.end();
		
	}

}

package org.ploxie.vulkan.swapchain;

import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memAllocPointer;
import static org.lwjgl.vulkan.KHRSwapchain.VK_STRUCTURE_TYPE_PRESENT_INFO_KHR;
import static org.lwjgl.vulkan.VK10.VK_DESCRIPTOR_TYPE_UNIFORM_BUFFER;
import static org.lwjgl.vulkan.VK10.VK_FORMAT_D32_SFLOAT;
import static org.lwjgl.vulkan.VK10.VK_FORMAT_R32G32B32_SFLOAT;
import static org.lwjgl.vulkan.VK10.VK_FORMAT_R32G32_SFLOAT;
import static org.lwjgl.vulkan.VK10.VK_FORMAT_R32_UINT;
import static org.lwjgl.vulkan.VK10.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_SUBMIT_INFO;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_WRITE_DESCRIPTOR_SET;
import static org.lwjgl.vulkan.VK10.vkUpdateDescriptorSets;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.KHRSurface;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkDescriptorBufferInfo;
import org.lwjgl.vulkan.VkPresentInfoKHR;
import org.lwjgl.vulkan.VkSubmitInfo;
import org.lwjgl.vulkan.VkWriteDescriptorSet;
import org.ploxie.engine2.model.Mesh;
import org.ploxie.engine2.model.Vertex.VertexLayout;
import org.ploxie.engine2.util.BufferUtil;
import org.ploxie.engine2.util.MeshGenerator;
import org.ploxie.utils.Color;
import org.ploxie.utils.FileUtils;
import org.ploxie.vulkan.buffer.VulkanBuffer;
import org.ploxie.vulkan.buffer.VulkanBufferUsageFlag;
import org.ploxie.vulkan.buffer.VulkanCommandBuffer;
import org.ploxie.vulkan.buffer.VulkanFrameBuffer;
import org.ploxie.vulkan.buffer.vertex.AttributeDescription;
import org.ploxie.vulkan.buffer.vertex.BindingDescription;
import org.ploxie.vulkan.buffer.vertex.VertexInputInfo;
import org.ploxie.vulkan.command.VulkanSubmitInfo;
import org.ploxie.vulkan.descriptor.VulkanDescriptorPool;
import org.ploxie.vulkan.descriptor.VulkanDescriptorSet;
import org.ploxie.vulkan.descriptor.VulkanUniformBufferDescriptor;
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
import org.ploxie.vulkan.surface.VulkanSurface;
import org.ploxie.vulkan.surface.VulkanSurfaceFormat;
import org.ploxie.vulkan.synchronization.VulkanSemaphore;
import org.ploxie.vulkan.utils.BufferUtils;
import org.ploxie.vulkan.viewport.VulkanViewportProperties;

import lombok.Data;

@Data
public class VulkanSwapChain {

	private final VulkanLogicalDevice logicalDevice;
	private final long handle;
	private final LongBuffer handlePointer;
	private final VulkanExtent2D windowDimensions;
	private final VulkanSurface surface;
	private final VulkanImage[] swapImages;
	private final VulkanSurfaceFormat imageFormat;
	
	private VulkanImageView[] swapImageViews;
	
	private VulkanFrameBuffer[] frameBuffers;
	private List<VulkanCommandBuffer> renderCommandBuffers;
	
	private VulkanSemaphore imageAcquiredSemaphore;
	private VulkanSemaphore renderCompleteSemaphore;
	private VulkanSubmitInfo submitInfo;
	private VkPresentInfoKHR presentInfo;
	
	private IntBuffer pImageIndex;
	
	private VulkanRenderPass renderPass;	
	private Color color = new Color(0.5f, 0.5f, 0.55f, 1.0f); 
	
	public VulkanSwapChain(VulkanLogicalDevice logicalDevice, LongBuffer handlePointer, VulkanExtent2D windowDimensions, VulkanSurface surface, VulkanImage[] swapImages, VulkanSurfaceFormat imageFormat) {
		this.logicalDevice = logicalDevice;
		this.handle = handlePointer.get(0);
		this.handlePointer = handlePointer;
		this.windowDimensions = windowDimensions;
		this.surface = surface;
		this.swapImages = swapImages;
		this.imageFormat = imageFormat;
		
		createSwapchain();
	}
	
	private void createSwapchain() {
					
		logicalDevice.createImageViewsForSwapChain(this);
		
		imageAcquiredSemaphore = logicalDevice.createSemaphore();
		renderCompleteSemaphore = logicalDevice.createSemaphore();
		pImageIndex = memAllocInt(1);
		
		IntBuffer pWaitDstStageMask = memAllocInt(1);
        pWaitDstStageMask.put(0, VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT);
		
        submitInfo = new VulkanSubmitInfo();
        submitInfo.setWaitDstStageMask(pWaitDstStageMask);
        submitInfo.setWaitSemaphores(imageAcquiredSemaphore.getHandlePointer());
        submitInfo.setSignalSemaphores(renderCompleteSemaphore.getHandlePointer());		
        
        presentInfo = VkPresentInfoKHR.calloc()
				.sType(VK_STRUCTURE_TYPE_PRESENT_INFO_KHR)
				.pNext(NULL)
				.pWaitSemaphores(renderCompleteSemaphore.getHandlePointer())
				.swapchainCount(1)
				.pSwapchains(handlePointer)
				.pImageIndices(pImageIndex)
				.pResults(null);
        
        Mesh fullScreenQuad = MeshGenerator.NDCQuad2D();
        ByteBuffer vertexBufferBytes = BufferUtil.createByteBuffer(fullScreenQuad.getVertices(), VertexLayout.POS_UV);
        ByteBuffer indexBufferBytes = BufferUtil.createByteBuffer(fullScreenQuad.getIndices());
        
        int indexCount = indexBufferBytes.remaining();
        
        VulkanBuffer vertexStagingBuffer = logicalDevice.createBuffer(vertexBufferBytes.remaining(), true, VulkanBufferUsageFlag.TRANSFER_SRC);
		VulkanMemoryAllocation vertexStagingMemoryAllocation = logicalDevice.allocateMemory(vertexStagingBuffer, VulkanMemoryPropertyFlag.HOST_VISIBLE, VulkanMemoryPropertyFlag.HOST_COHERENT);
		logicalDevice.fillBuffer(vertexStagingMemoryAllocation, vertexStagingBuffer, vertexBufferBytes);
        		        
		VulkanBuffer vertexBuffer = logicalDevice.createBuffer(vertexBufferBytes.remaining(), true, VulkanBufferUsageFlag.TRANSFER_DST, VulkanBufferUsageFlag.VERTEX);
		VulkanMemoryAllocation vertexMemoryAllocation = logicalDevice.allocateMemory(vertexBuffer,  VulkanMemoryPropertyFlag.DEVICE_LOCAL);
		VulkanCommandBuffer stageVertexCommandBuffer = logicalDevice.copyBuffer(logicalDevice.getGraphicsCommandPool(), vertexStagingBuffer, vertexBuffer, vertexBufferBytes.remaining());
		logicalDevice.getGraphicsQueue().submit(stageVertexCommandBuffer);		
        
		VulkanBuffer indexStagingBuffer = logicalDevice.createBuffer(indexBufferBytes.remaining(), true, VulkanBufferUsageFlag.TRANSFER_SRC);
		VulkanMemoryAllocation indexStagingMemoryAllocation = logicalDevice.allocateMemory(indexStagingBuffer, VulkanMemoryPropertyFlag.HOST_VISIBLE, VulkanMemoryPropertyFlag.HOST_COHERENT);
		logicalDevice.fillBuffer(indexStagingMemoryAllocation, indexStagingBuffer, indexBufferBytes);
		
		VulkanBuffer indexBuffer = logicalDevice.createBuffer(indexBufferBytes.remaining(), true, VulkanBufferUsageFlag.TRANSFER_DST, VulkanBufferUsageFlag.INDEX);
		VulkanMemoryAllocation indexMemoryAllocation = logicalDevice.allocateMemory(indexBuffer, VulkanMemoryPropertyFlag.DEVICE_LOCAL);
		VulkanCommandBuffer stageIndexCommandBuffer = logicalDevice.copyBuffer(logicalDevice.getGraphicsCommandPool(), indexStagingBuffer, indexBuffer, indexBufferBytes.remaining());
		logicalDevice.getGraphicsQueue().submit(stageIndexCommandBuffer);	
		
		renderPass = logicalDevice.createRenderPass(getImageFormat().getColorFormat(), VK_FORMAT_D32_SFLOAT);
			
		int depthFormat = VK_FORMAT_D32_SFLOAT;
				
		VulkanImage depthBufferImage = logicalDevice.createImage2D(1, depthFormat, windowDimensions.getWidth(), windowDimensions.getHeight(), VulkanImageUsageFlag.DEPTH_STENCIL);
		VulkanMemoryAllocation depthBufferMemoryAllocation = logicalDevice.allocateMemory(depthBufferImage, VulkanMemoryPropertyFlag.DEVICE_LOCAL);
		VulkanImageView  depthBufferImageView = logicalDevice.createImageView(depthBufferImage, depthFormat, VulkanImageAspectMask.DEPTH);
		logicalDevice.setImageLayout(logicalDevice.getGraphicsCommandPool(), depthBufferImage, VulkanImageLayout.DEPTH_STENCIL_ATTACHMENT_OPTIMAL);
		
		frameBuffers = logicalDevice.createFrameBuffers(windowDimensions, this, depthBufferImageView, renderPass);		
		
        ByteBuffer triangleVertCode = null;
        ByteBuffer triangleFragCode = null;
		try {
			triangleVertCode = BufferUtils.wrap(FileUtils.getFileToBytes("res/quad.vert.spv"));
			triangleFragCode = BufferUtils.wrap(FileUtils.getFileToBytes("res/quad.frag.spv"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
				
		VulkanShaderModule triangleVertShader = logicalDevice.loadShader(triangleVertCode);
		VulkanShaderModule triangleFragShader = logicalDevice.loadShader(triangleFragCode);
		
		VulkanShaderModules shaderModules = VulkanShaderModules.builder().vertex(triangleVertShader).fragment(triangleFragShader).build();
		VulkanGraphicsPipelineProperties pipelineProperties = new VulkanGraphicsPipelineProperties(shaderModules);
		
		VulkanRect2D renderArea = new VulkanRect2D(new VulkanOffset2D(0, 0), windowDimensions);		
		VulkanViewportProperties viewport = VulkanViewportProperties.builder().dimensions(windowDimensions).build();
		VulkanGraphicsPipeline graphicsPipeline = logicalDevice.createGraphicsPipeline(renderPass, pipelineProperties);
		
		int stride = 3 * 4 + 2 * 4;
		BindingDescription bindingDescription = new BindingDescription(0, stride);
		AttributeDescription[] attributeDescriptions = new AttributeDescription[] {
				new AttributeDescription(0, 0, VK_FORMAT_R32G32B32_SFLOAT, 0),
				new AttributeDescription(1, 0, VK_FORMAT_R32G32_SFLOAT, 12),
		};
		
		VertexInputInfo vertexInputInfo = new VertexInputInfo(bindingDescription, attributeDescriptions);
		pipelineProperties.setVertexInputInfo(vertexInputInfo);
		
		int uniformBufferSize = 16 * 4;
		VulkanUniformBufferDescriptor uniformBufferDescriptor = logicalDevice.createUniformBuffer(uniformBufferSize);
		VulkanDescriptorSet descriptorSet = null;
		
		try(MemoryStack stack = MemoryStack.stackPush()){
			VulkanDescriptorPool descriptorPool = logicalDevice.createDescriptorPool(1,1);
			descriptorSet = descriptorPool.allocateDescriptorSet(graphicsPipeline.getDescriptorSetLayouts()[0]);
			
			VkDescriptorBufferInfo.Buffer bufferInfo = VkDescriptorBufferInfo
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
			
			vkUpdateDescriptorSets(logicalDevice.getInternal(), descriptorWrite, null);
				
		}
		
		renderCommandBuffers = new ArrayList<>();		
		for (VulkanFrameBuffer frameBuffer : frameBuffers){
			
			VulkanCommandBuffer commandBuffer = logicalDevice.createCommandBuffer(logicalDevice.getGraphicsCommandPool(), true);
			commandBuffer.begin();
				commandBuffer.beginRenderPass(renderPass, frameBuffer, renderArea, color);
				
					commandBuffer.setViewport(viewport);
					commandBuffer.setScissor(renderArea);				
					commandBuffer.bindPipeline(graphicsPipeline);		
					commandBuffer.bindVertexBuffers(vertexBuffer);
					commandBuffer.bindIndexBuffer(indexBuffer, VK_FORMAT_R32_UINT);			
					//commandBuffer.bindDescriptorSets(graphicsPipeline.getLayout(), descriptorSet);				
					commandBuffer.drawIndexed(indexCount, 1, 0, 0, 0);
					
				commandBuffer.endRenderPass();
			commandBuffer.end();
			
			renderCommandBuffers.add(commandBuffer);
		}
		
	}
	
	public void draw(VulkanQueue queue) {
		int imageIndex = logicalDevice.acquireNextImageIndexKHR(this, -1, imageAcquiredSemaphore);
		pImageIndex.put(0, imageIndex);
		
		//pCommandBuffers.put(0, commandBuffer.getInternal());
		queue.submit(submitInfo);

		//pSwapchains.put(0, swapChain.getHandle());
		queue.present(presentInfo);
	}
	
}

package org.ploxie.engine.vulkan;

import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.vulkan.VK10.VK_FORMAT_D32_SFLOAT;
import static org.lwjgl.vulkan.VK10.VK_FORMAT_R32G32B32_SFLOAT;
import static org.lwjgl.vulkan.VK10.VK_FORMAT_R32G32_SFLOAT;
import static org.lwjgl.vulkan.VK10.VK_FORMAT_R32_UINT;
import static org.lwjgl.vulkan.VK10.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkDescriptorBufferInfo;
import org.lwjgl.vulkan.VkWriteDescriptorSet;
import org.ploxie.engine.vulkan.context.VulkanContext;
import org.ploxie.engine.vulkan.display.VulkanWindow;
import org.ploxie.utils.Color;
import org.ploxie.utils.FileUtils;
import org.ploxie.utils.math.matrix.Matrix4f;
import org.ploxie.utils.math.vector.Vector2i;
import org.ploxie.utils.math.vector.Vector3f;
import org.ploxie.vulkan.buffer.VulkanBuffer;
import org.ploxie.vulkan.buffer.VulkanBufferUsageFlag;
import org.ploxie.vulkan.buffer.VulkanCommandBuffer;
import org.ploxie.vulkan.buffer.VulkanFrameBuffer;
import org.ploxie.vulkan.buffer.vertex.AttributeDescription;
import org.ploxie.vulkan.buffer.vertex.BindingDescription;
import org.ploxie.vulkan.buffer.vertex.VertexInputInfo;
import org.ploxie.vulkan.command.VulkanCommandPool;
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
import org.ploxie.vulkan.synchronization.VulkanSemaphore;
import org.ploxie.vulkan.utils.BufferUtils;
import org.ploxie.vulkan.utils.VKUtil;
import org.ploxie.vulkan.viewport.VulkanViewportProperties;

import lombok.Getter;
import lombok.Setter;

public class TestObject {

	private VulkanLogicalDevice logicalDevice;
	private VulkanQueue graphicsQueue;
	private VulkanCommandPool graphicsCommandPool;

	private VulkanSubmitInfo submitInfo;

	private VulkanRenderPass renderPass;
	private VulkanViewportProperties viewport;
	private VulkanRect2D scissor;
	private VulkanGraphicsPipelineProperties pipelineProperties;
	private VulkanGraphicsPipeline graphicsPipeline;
	
	private VulkanSemaphore renderCompleteSemaphore;
		
	private int indexCount;
	private VulkanWindow window;
	
	private VulkanBuffer vertexBuffer;
	private VulkanBuffer indexBuffer;
	private VulkanDescriptorSet descriptorSet;
	
	private VulkanCommandBuffer commandBuffer;
	private VulkanUniformBufferDescriptor uniformBufferDescriptor;
	
	@Getter
	@Setter
	private Vector3f position;
	
	public TestObject() {
		position = new Vector3f();
		logicalDevice = VulkanContext.getLogicalDevice();
		window = VulkanContext.getWindow();

		int graphicsFamilyIndex = logicalDevice.getPhysicalDevice().getQueueFamilyProperties().getFirstGraphicsQueue().getIndex();
		graphicsQueue = logicalDevice.getDeviceQueue(graphicsFamilyIndex, 0);
		graphicsCommandPool = logicalDevice.getCommandPool(graphicsFamilyIndex);
		
		IntBuffer pWaitDstStageMask = memAllocInt(1);
		pWaitDstStageMask.put(0, VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT);
		
		renderCompleteSemaphore = logicalDevice.createSemaphore();
		
		submitInfo = new VulkanSubmitInfo();
		submitInfo.setWaitSemaphores(renderCompleteSemaphore.getHandlePointer());
		
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
		
		int stride = 3 * 4 + 3 * 4;
		BindingDescription bindingDescription = new BindingDescription(0, stride);
		AttributeDescription[] attributeDescriptions = new AttributeDescription[] {
				new AttributeDescription(0, 0, VK_FORMAT_R32G32B32_SFLOAT, 0),
				new AttributeDescription(1, 0, VK_FORMAT_R32G32B32_SFLOAT, 12),
		};
		
		VertexInputInfo vertexInputInfo = new VertexInputInfo(bindingDescription, attributeDescriptions);
		pipelineProperties.setVertexInputInfo(vertexInputInfo);	
		
		renderPass = logicalDevice.createRenderPass(window.getSwapchain().getImageFormat().getColorFormat(), VK_FORMAT_D32_SFLOAT);
		viewport = VulkanViewportProperties.builder().dimensions(window.getExtent()).build();
		scissor = new VulkanRect2D(new VulkanOffset2D(0,0), window.getExtent());
		graphicsPipeline = logicalDevice.createGraphicsPipeline(renderPass, pipelineProperties);		
		
		ByteBuffer vertexBytes = memAlloc(stride*4);
		FloatBuffer fb = vertexBytes.asFloatBuffer();
		fb.put(-0.5f).put(-0.5f) .put(0).put(1).put(0).put(0);
		fb.put(0.5f).put(-0.5f)  .put(0).put(0).put(1).put(0);
		fb.put(0.5f).put(0.5f).put(0).put(0).put(0).put(1);
		fb.put(-0.5f).put(0.5f).put(0).put(0).put(1).put(1);		
		
		fb.flip();
		
		indexCount = 6;
		ByteBuffer indexBytes = memAlloc(indexCount * 4);
		IntBuffer ib = indexBytes.asIntBuffer();
		
		ib.put(0).put(3).put(2).put(0).put(2).put(1);
		ib.flip();
		
		VulkanBuffer vertexStagingBuffer = logicalDevice.createBuffer(vertexBytes.remaining(), true, VulkanBufferUsageFlag.TRANSFER_SRC);
		VulkanMemoryAllocation vertexStagingMemoryAllocation = logicalDevice.allocateMemory(vertexStagingBuffer, VulkanMemoryPropertyFlag.HOST_VISIBLE, VulkanMemoryPropertyFlag.HOST_COHERENT);
		logicalDevice.fillBuffer(vertexStagingMemoryAllocation, vertexStagingBuffer, vertexBytes);
		
		vertexBuffer = logicalDevice.createBuffer(vertexBytes.remaining(), true, VulkanBufferUsageFlag.TRANSFER_DST, VulkanBufferUsageFlag.VERTEX);
		VulkanMemoryAllocation vertexMemoryAllocation = logicalDevice.allocateMemory(vertexBuffer,  VulkanMemoryPropertyFlag.DEVICE_LOCAL);
		VulkanCommandBuffer stageVertexCommandBuffer = logicalDevice.copyBuffer(graphicsCommandPool, vertexStagingBuffer, vertexBuffer, vertexBytes.remaining());
		graphicsQueue.submit(stageVertexCommandBuffer);
		
		VulkanBuffer indexStagingBuffer = logicalDevice.createBuffer(indexBytes.remaining(), true, VulkanBufferUsageFlag.TRANSFER_SRC);
		VulkanMemoryAllocation indexStagingMemoryAllocation = logicalDevice.allocateMemory(indexStagingBuffer, VulkanMemoryPropertyFlag.HOST_VISIBLE, VulkanMemoryPropertyFlag.HOST_COHERENT);
		logicalDevice.fillBuffer(indexStagingMemoryAllocation, indexStagingBuffer, indexBytes);
		
		indexBuffer = logicalDevice.createBuffer(indexBytes.remaining(), true, VulkanBufferUsageFlag.TRANSFER_DST, VulkanBufferUsageFlag.INDEX);
		VulkanMemoryAllocation indexMemoryAllocation = logicalDevice.allocateMemory(indexBuffer, VulkanMemoryPropertyFlag.DEVICE_LOCAL);
		VulkanCommandBuffer stageIndexCommandBuffer = logicalDevice.copyBuffer(graphicsCommandPool, indexStagingBuffer, indexBuffer, indexBytes.remaining());
		graphicsQueue.submit(stageIndexCommandBuffer);
		
		VulkanDescriptorPool descriptorPool = logicalDevice.createDescriptorPool(1,1);
		descriptorSet = descriptorPool.allocateDescriptorSet(graphicsPipeline.getDescriptorSetLayouts()[0]);
		
		uniformBufferDescriptor = logicalDevice.createUniformBuffer(16 * 4);
		
		try(MemoryStack stack = MemoryStack.stackPush()){
			VkDescriptorBufferInfo.Buffer bufferInfo = VkDescriptorBufferInfo
				.callocStack(1, stack)
				.buffer(uniformBufferDescriptor.getBuffer().getHandle())
				.offset(0)
				.range(16*4);
	
			VkWriteDescriptorSet.Buffer descriptorWrite = VkWriteDescriptorSet.callocStack(1, stack);
			descriptorWrite.get(0)
				.sType(VK10.VK_STRUCTURE_TYPE_WRITE_DESCRIPTOR_SET)
				.dstSet(descriptorSet.getHandle())
				.dstBinding(0)
				.dstArrayElement(0)
				.descriptorType(VK10.VK_DESCRIPTOR_TYPE_UNIFORM_BUFFER)
				.pBufferInfo(bufferInfo)
				.pImageInfo(null)
				.pTexelBufferView(null);
	
			VK10.vkUpdateDescriptorSets(logicalDevice.getInternal(), descriptorWrite, null);
		}
		
		commandBuffer = logicalDevice.createCommandBuffer(graphicsCommandPool, false);	
		
		
	}
	
	public VulkanCommandBuffer record() {
		commandBuffer.reset();			
		commandBuffer.beginSecondary(renderPass, renderPass.getSubPasses().get(0), window.getCurrentFrameBuffer(), false);					
		commandBuffer.setViewport(viewport);
		commandBuffer.setScissor(scissor);				
		commandBuffer.bindPipeline(graphicsPipeline);		
		commandBuffer.bindVertexBuffers(vertexBuffer);
		commandBuffer.bindIndexBuffer(indexBuffer, VK_FORMAT_R32_UINT);			
		commandBuffer.bindDescriptorSets(graphicsPipeline.getLayout(), descriptorSet);				
		commandBuffer.drawIndexed(indexCount, 1, 0, 0, 0);			
		commandBuffer.end();
		
		updateUniformBuffer();
		return commandBuffer;
	}
	
	private void updateUniformBuffer() {
		VulkanMemoryAllocation memoryAllocation = uniformBufferDescriptor.getMemoryAllocation();
		
		Matrix4f matrix = new Matrix4f();
				
		matrix.setScale(0.5f, 0.5f, 1);
		matrix.setTranslation(position);

		PointerBuffer pData = MemoryUtil.memAllocPointer(1);
		int err = VK10.vkMapMemory(logicalDevice.getInternal(), memoryAllocation.getHandle(), 0, 16 * 4, 0, pData);
		long data = pData.get(0);
		MemoryUtil.memFree(pData);
		

		if (err != VK10.VK_SUCCESS) {
			throw new AssertionError(
					"Failed to map UBO memory: " + VKUtil.translateVulkanResult(err));
		}
		

		ByteBuffer matrixBuffer = MemoryUtil.memByteBuffer(data, 16 * 4);
		matrix.fillBuffer(matrixBuffer);
		
		VK10.vkUnmapMemory(logicalDevice.getInternal(), memoryAllocation.getHandle());
		//device.fillBuffer(memoryAllocation, buffer, dataBytes);
	}
	
	
	
}

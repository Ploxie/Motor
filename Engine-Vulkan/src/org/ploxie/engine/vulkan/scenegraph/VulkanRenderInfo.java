package org.ploxie.engine.vulkan.scenegraph;

import static org.lwjgl.vulkan.VK10.VK_FORMAT_D32_SFLOAT;
import static org.lwjgl.vulkan.VK10.VK_FORMAT_R32_UINT;

import java.nio.ByteBuffer;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkDescriptorBufferInfo;
import org.lwjgl.vulkan.VkWriteDescriptorSet;
import org.ploxie.engine.vulkan.context.VulkanContext;
import org.ploxie.engine.vulkan.context.VulkanToolkit;
import org.ploxie.engine.vulkan.pipeline.shader.VulkanCameraUniformBuffer;
import org.ploxie.engine2.model.Mesh;
import org.ploxie.engine2.pipeline.Pipeline;
import org.ploxie.engine2.scenegraph.RenderInfo;
import org.ploxie.engine2.util.BufferUtils;
import org.ploxie.utils.math.matrix.Matrix4f;
import org.ploxie.utils.math.vector.Vector3f;
import org.ploxie.vulkan.buffer.VulkanBuffer;
import org.ploxie.vulkan.buffer.VulkanBufferUsageFlag;
import org.ploxie.vulkan.buffer.VulkanCommandBuffer;
import org.ploxie.vulkan.command.VulkanCommandPool;
import org.ploxie.vulkan.descriptor.VulkanDescriptorPool;
import org.ploxie.vulkan.descriptor.VulkanDescriptorSet;
import org.ploxie.vulkan.descriptor.VulkanUniformBufferDescriptor;
import org.ploxie.vulkan.device.VulkanLogicalDevice;
import org.ploxie.vulkan.math.VulkanOffset2D;
import org.ploxie.vulkan.math.VulkanRect2D;
import org.ploxie.vulkan.memory.VulkanMemoryAllocation;
import org.ploxie.vulkan.pipeline.VulkanGraphicsPipeline;
import org.ploxie.vulkan.pipeline.VulkanGraphicsPipelineProperties;
import org.ploxie.vulkan.queue.VulkanQueue;
import org.ploxie.vulkan.render.VulkanRenderPass;
import org.ploxie.vulkan.utils.VKUtil;
import org.ploxie.vulkan.utils.VulkanBufferUtils;
import org.ploxie.vulkan.viewport.VulkanViewportProperties;

import lombok.Getter;
import lombok.Setter;

public class VulkanRenderInfo extends RenderInfo {

	private VulkanCommandBuffer commandBuffer;
	
	private VulkanBuffer vertexBuffer;
	private VulkanBuffer indexBuffer;
	
	private VulkanRenderPass renderPass;
	private VulkanViewportProperties viewport;
	private VulkanRect2D scissor;
	private VulkanGraphicsPipeline graphicsPipeline;
	private VulkanDescriptorSet descriptorSet;
	private VulkanUniformBufferDescriptor uniformBufferDescriptor;
	
	private VulkanCameraUniformBuffer uniBuffer;
	
	@Getter
	@Setter
	private Vector3f position = new Vector3f();
	
	private Matrix4f camera = new Matrix4f();

	public VulkanRenderInfo(Mesh mesh, VulkanGraphicsPipelineProperties pipeline) {
		super(mesh, pipeline);
		
		VulkanLogicalDevice logicalDevice = VulkanContext.getLogicalDevice();
		int graphicsFamilyIndex = logicalDevice.getPhysicalDevice().getQueueFamilyProperties().getFirstGraphicsQueue().getIndex();
		VulkanCommandPool commandPool = logicalDevice.getCommandPool(graphicsFamilyIndex);
		VulkanQueue queue = logicalDevice.getDeviceQueue(graphicsFamilyIndex, 0);		
		
		vertexBuffer = VulkanBufferUtils.createDeviceLocalBuffer(logicalDevice, BufferUtils.createByteBuffer(mesh.getVertices(), mesh.getVertexLayout()), queue, commandPool, VulkanBufferUsageFlag.VERTEX);
		indexBuffer = VulkanBufferUtils.createDeviceLocalBuffer(logicalDevice, BufferUtils.createByteBuffer(mesh.getIndices()), queue, commandPool, VulkanBufferUsageFlag.INDEX);		
		
		renderPass = logicalDevice.createRenderPass(VulkanContext.getWindow().getSwapchain().getImageFormat().getColorFormat(), VK_FORMAT_D32_SFLOAT);
		viewport = VulkanViewportProperties.builder().dimensions(VulkanContext.getWindow().getExtent()).build();
		scissor = new VulkanRect2D(new VulkanOffset2D(0,0), VulkanContext.getWindow().getExtent());		
		
		pipeline.setVertexInputInfo(mesh.getVertexInputInfo());
		graphicsPipeline = logicalDevice.createGraphicsPipeline(renderPass, pipeline);		
				
		commandBuffer = logicalDevice.createCommandBuffer(commandPool, false);						
		
		uniBuffer = new VulkanCameraUniformBuffer(0, graphicsPipeline.getDescriptorSetLayouts()[0]);		
		camera.setScale(new Vector3f(0.5f, 0.5f, 1.0f));		
		uniBuffer.setMVP(camera);
		
		uniBuffer.updateUniform();
		//updateUniformBuffer();
	}
	
	public VulkanCommandBuffer record() {
		commandBuffer.beginSecondary(renderPass, renderPass.getSubPasses().get(0), VulkanContext.getWindow().getCurrentFrameBuffer(), false);					
		commandBuffer.setViewport(viewport);
		commandBuffer.setScissor(scissor);				
		commandBuffer.bindPipeline(graphicsPipeline);		
		commandBuffer.bindVertexBuffers(vertexBuffer);
		commandBuffer.bindIndexBuffer(indexBuffer, VK_FORMAT_R32_UINT);			
		//commandBuffer.bindDescriptorSets(layout, descriptorSets);(uniBuffer.getDescriptorLayout(), uniBuffer.getDescriptorSet());
		commandBuffer.bindDescriptorSets(graphicsPipeline.getLayout(), uniBuffer.getDescriptorSet());				
		commandBuffer.drawIndexed(indexBuffer.getSize(), 1, 0, 0, 0);			
		commandBuffer.end();
		
		return commandBuffer;
	}
	
	

}
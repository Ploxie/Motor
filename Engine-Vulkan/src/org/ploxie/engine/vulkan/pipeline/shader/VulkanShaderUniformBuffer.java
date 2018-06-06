package org.ploxie.engine.vulkan.pipeline.shader;

import java.nio.ByteBuffer;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkDescriptorBufferInfo;
import org.lwjgl.vulkan.VkWriteDescriptorSet;
import org.ploxie.engine.vulkan.context.VulkanContext;
import org.ploxie.engine2.pipeline.shader.ShaderUniformBuffer;
import org.ploxie.utils.math.matrix.Matrix4f;
import org.ploxie.vulkan.descriptor.VulkanDescriptorLayout;
import org.ploxie.vulkan.descriptor.VulkanDescriptorPool;
import org.ploxie.vulkan.descriptor.VulkanDescriptorSet;
import org.ploxie.vulkan.descriptor.VulkanUniformBufferDescriptor;
import org.ploxie.vulkan.device.VulkanLogicalDevice;
import org.ploxie.vulkan.memory.VulkanMemoryAllocation;
import org.ploxie.vulkan.utils.VKUtil;

import lombok.Getter;

public abstract class VulkanShaderUniformBuffer extends ShaderUniformBuffer{

	@Getter
	private VulkanDescriptorSet descriptorSet;
	private VulkanUniformBufferDescriptor descriptor;
	private int totalSize;
	
	@Getter
	protected VulkanDescriptorLayout descriptorLayout;
	
	public VulkanShaderUniformBuffer(int binding, VulkanDescriptorLayout descriptorLayout, UniformBufferType... types) {
		super(binding, types);
		this.descriptorLayout = descriptorLayout;
		
		VulkanLogicalDevice logicalDevice = VulkanContext.getLogicalDevice();
		
		int uniforms = 1;
		int samplers = 1;
		for(UniformBufferType type : types) {
			totalSize += type.getSize();
			if(type == UniformBufferType.MATRIX4) {
				uniforms++;
			}
		}
		
		VulkanDescriptorPool descriptorPool = logicalDevice.createDescriptorPool(uniforms,samplers);
		this.descriptorSet = descriptorPool.allocateDescriptorSet(descriptorLayout);
		
		this.descriptor = logicalDevice.createUniformBuffer(totalSize);
		
		try(MemoryStack stack = MemoryStack.stackPush()){
			VkDescriptorBufferInfo.Buffer bufferInfo = VkDescriptorBufferInfo
				.callocStack(1, stack)
				.buffer(descriptor.getBuffer().getHandle())
				.offset(0)
				.range(totalSize);
	
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
	}
	
	protected abstract void fillData(ByteBuffer buffer);
	
	public void updateUniform() {
		VulkanLogicalDevice logicalDevice = VulkanContext.getLogicalDevice();
		VulkanMemoryAllocation memoryAllocation = descriptor.getMemoryAllocation();
		
		PointerBuffer pData = MemoryUtil.memAllocPointer(1);
		int err = VK10.vkMapMemory(logicalDevice.getInternal(), memoryAllocation.getHandle(), 0, totalSize, 0, pData);
		long data = pData.get(0);
		MemoryUtil.memFree(pData);		

		if (err != VK10.VK_SUCCESS) {
			throw new AssertionError(
					"Failed to map UBO memory: " + VKUtil.translateVulkanResult(err));
		}
		
		ByteBuffer buffer = MemoryUtil.memByteBuffer(data, totalSize);
		fillData(buffer);	
		
		VK10.vkUnmapMemory(logicalDevice.getInternal(), memoryAllocation.getHandle());
	}
	
}

package org.ploxie.vulkan.descriptor;

import org.ploxie.vulkan.buffer.VulkanBuffer;
import org.ploxie.vulkan.memory.VulkanMemoryAllocation;

import lombok.Data;

@Data
public class VulkanUniformBufferDescriptor {

	private final VulkanMemoryAllocation memoryAllocation;
	private final VulkanBuffer buffer;
	private final long offers;
	private final long range;
	
}

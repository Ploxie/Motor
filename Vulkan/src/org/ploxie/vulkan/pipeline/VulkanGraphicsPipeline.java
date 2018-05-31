package org.ploxie.vulkan.pipeline;

import org.ploxie.vulkan.descriptor.VulkanDescriptorLayout;

import lombok.Data;

@Data
public class VulkanGraphicsPipeline {

	private final long handle;
	private final VulkanDescriptorLayout[] descriptorSetLayouts;
	private final VulkanGraphicsPipelineLayout layout;
	
}

package org.ploxie.vulkan.pipeline;

import org.ploxie.engine2.pipeline.Pipeline;
import org.ploxie.vulkan.shader.VulkanShaderModules;

public class VulkanGraphicsPipelineProperties extends Pipeline{
		
	public VulkanGraphicsPipelineProperties(VulkanShaderModules shaderModules) {
		super(shaderModules);
	}

	@Override
	public VulkanShaderModules getShaderModules() {
		return (VulkanShaderModules) super.getShaderModules();
	}
	
}

package org.ploxie.vulkan.pipeline;

import java.util.ArrayList;
import java.util.List;

import org.ploxie.engine2.pipeline.Pipeline;
import org.ploxie.engine2.pipeline.uniformbuffers.UniformBuffer;
import org.ploxie.vulkan.shader.VulkanShaderModules;

public class VulkanGraphicsPipelineProperties extends Pipeline{
		
	public VulkanGraphicsPipelineProperties(Pipeline pipeline) {
		super(pipeline.getShaderModules());
		setUniformBuffers(pipeline.getUniformBuffers());
	}
	
	public VulkanGraphicsPipelineProperties(VulkanShaderModules shaderModules) {
		super(shaderModules);
	}

	@Override
	public VulkanShaderModules getShaderModules() {
		return (VulkanShaderModules) super.getShaderModules();
	}
	
}

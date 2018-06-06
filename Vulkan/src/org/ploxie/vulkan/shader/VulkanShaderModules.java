package org.ploxie.vulkan.shader;

import org.ploxie.engine2.pipeline.shader.ShaderModules;

import lombok.Builder;

@Builder
public class VulkanShaderModules extends ShaderModules{

	private VulkanShaderModule vertex;
	private VulkanShaderModule fragment;	

	public int getShaderCount() {
		int count = 0;
		if (vertex != null) {
			count++;
		}
		if (fragment != null) {
			count++;
		}
		return count;
	}

	@Override
	public VulkanShaderModule getVertex() {
		return vertex;
	}

	@Override
	public VulkanShaderModule getFragment() {
		return fragment;
	}
	
}

package org.ploxie.vulkan.shader;

import org.ploxie.engine2.pipeline.shader.ShaderModule;

import lombok.Data;

@Data
public class VulkanShaderModule extends ShaderModule{

	private final long handle;
	
}

package org.ploxie.vulkan.render;

import org.ploxie.vulkan.device.VulkanLogicalDevice;

import lombok.Data;

@Data
public class VulkanRenderPass {

	private final long handle;
	private final VulkanLogicalDevice device;
	
}

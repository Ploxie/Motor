package org.ploxie.vulkan.render;

import java.util.List;

import org.ploxie.vulkan.device.VulkanLogicalDevice;

import lombok.Data;

@Data
public class VulkanRenderPass {

	private final long handle;
	private final List<VulkanSubpass> subPasses;
	private final VulkanLogicalDevice device;
	
}

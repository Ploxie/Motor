package org.ploxie.vulkan.buffer;

import org.ploxie.vulkan.device.VulkanLogicalDevice;

import lombok.Data;

@Data
public class VulkanBuffer {

	private final VulkanLogicalDevice device;
	private final long handle;
	private final int size;
	
	@Override
	protected void finalize() throws Throwable {
		//device.cleanup(this);
		super.finalize();
	}
	
}

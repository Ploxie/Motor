package org.ploxie.vulkan.queue;

import lombok.Data;

@Data
public class VulkanDeviceQueueCreateInfo {

	private final VulkanQueueFamilyProperties properties;
	private final float[] priorities;
	
}

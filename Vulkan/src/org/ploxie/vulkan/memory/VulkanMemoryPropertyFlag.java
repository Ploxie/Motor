package org.ploxie.vulkan.memory;

import static org.lwjgl.vulkan.VK10.*;

import lombok.Getter;

public enum VulkanMemoryPropertyFlag {

	DEVICE_LOCAL(VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT),
	HOST_VISIBLE(VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT),
	HOST_COHERENT(VK_MEMORY_PROPERTY_HOST_COHERENT_BIT),
	HOST_CACHED(VK_MEMORY_PROPERTY_HOST_CACHED_BIT),
	LAZILY_ALLOCATED(VK_MEMORY_PROPERTY_LAZILY_ALLOCATED_BIT);
		
	@Getter
	private int bitMask;

	VulkanMemoryPropertyFlag(int bitMask) {
		this.bitMask = bitMask;
	}
	
}

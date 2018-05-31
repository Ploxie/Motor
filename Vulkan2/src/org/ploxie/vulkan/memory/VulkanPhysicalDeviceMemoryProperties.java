package org.ploxie.vulkan.memory;

import lombok.Data;

@Data
public class VulkanPhysicalDeviceMemoryProperties {

	private final VulkanMemoryType[] memoryTypes;
	private final VulkanMemoryHeap[] memoryHeaps;
	
	public VulkanMemoryType getType(VulkanMemoryPropertyFlag...flags) {
		for(VulkanMemoryType type: memoryTypes) {
			if(type.hasFlags(flags)) {
				return type;
			}
		}
		return null;
	}
	
}

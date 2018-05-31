package org.ploxie.vulkan.memory;

import lombok.Data;

@Data
public class VulkanMemoryType {

	private final int index;
	private final int propertyFlags;
	private final int heapIndex;
	
	public boolean hasFlags(VulkanMemoryPropertyFlag...flags) {
		for(VulkanMemoryPropertyFlag flag: flags) {
			if(!hasFlag(flag)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean hasFlag(VulkanMemoryPropertyFlag flag) {
		return (propertyFlags & flag.getBitMask()) != 0;
	}
	
}

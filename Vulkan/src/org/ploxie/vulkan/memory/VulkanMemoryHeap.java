package org.ploxie.vulkan.memory;

import lombok.Data;
import static org.lwjgl.vulkan.VK10.*;

@Data
public class VulkanMemoryHeap {

	private final int index;
	
	/**
	 * Size in bytes
	 */
	
	private final long size;
	private final int flags;
	
	public boolean isLocal() {
		return (flags & VK_MEMORY_HEAP_DEVICE_LOCAL_BIT) != 0;
	}
	
}

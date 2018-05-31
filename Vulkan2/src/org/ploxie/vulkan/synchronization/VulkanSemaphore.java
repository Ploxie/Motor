package org.ploxie.vulkan.synchronization;

import java.nio.LongBuffer;

import lombok.Data;

@Data
public class VulkanSemaphore {

	private final long handle;
	private final LongBuffer handlePointer;

}

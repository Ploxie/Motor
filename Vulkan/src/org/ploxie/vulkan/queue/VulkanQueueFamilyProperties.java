package org.ploxie.vulkan.queue;

import org.lwjgl.vulkan.VkQueueFamilyProperties;
import org.ploxie.vulkan.device.VulkanPhysicalDevice;

import lombok.Data;

import static org.lwjgl.vulkan.VK10.*;

@Data
public class VulkanQueueFamilyProperties {

	private final int index;
	private final VulkanPhysicalDevice physicalDevice;
	private final VkQueueFamilyProperties internal;	
	
	public int getQueueCount() {
		return internal.queueCount();
	}
	
	private boolean hasBit(int bit) {
		return (internal.queueFlags() & bit) != 0;
	}
	
	public boolean hasGraphicsCapabilities() {
		return hasBit(VK_QUEUE_GRAPHICS_BIT);
	}
	
	public boolean hasComputeCapabilities() {
		return hasBit(VK_QUEUE_COMPUTE_BIT);
	}
	
	public boolean hasTransferCapabilities() {
		return hasBit(VK_QUEUE_TRANSFER_BIT) || hasGraphicsCapabilities()
				|| hasComputeCapabilities();
	}
	
	public boolean hasSparseBindingCapabilities() {
		return hasBit(VK_QUEUE_SPARSE_BINDING_BIT);
	}
	
	@Override
	public String toString() {
		String capabilities = "[VulkanQueueFamilyProperties:\n";
		capabilities += "\tIndex: " + getIndex() + "\n";
		capabilities += "\tCount: " + getQueueCount() + "\n";
		capabilities += "\tCapabilities: {";
		if (hasGraphicsCapabilities()) {
			capabilities += "GRAPHICS, ";
		}
		if (hasComputeCapabilities()) {
			capabilities += "COMPUTE, ";
		}
		if (hasTransferCapabilities()) {
			capabilities += "TRANSFER, ";
		}
		if (hasSparseBindingCapabilities()) {
			capabilities += "SPARSE_BINDING, ";
		}
		if (capabilities.endsWith(", ")) {
			capabilities = capabilities.substring(0, capabilities.length() - 2);
		}
		capabilities += "}\n]\n";
		return capabilities;
	}
}

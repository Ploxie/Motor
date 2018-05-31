package org.ploxie.vulkan.queue;

import java.util.ArrayList;

public class VulkanQueueFamilyPropertiesList extends ArrayList<VulkanQueueFamilyProperties>{

	public VulkanQueueFamilyProperties getFirstGraphicsQueue() {
		for (VulkanQueueFamilyProperties p : this) {
			if (p.hasGraphicsCapabilities()) {
				return p;
			}
		}
		return null;
	}
	
	public VulkanQueueFamilyProperties getFirstComputeQueue() {
		for (VulkanQueueFamilyProperties p : this) {
			if (p.hasComputeCapabilities()) {
				return p;
			}
		}
		return null;
	}
		
}

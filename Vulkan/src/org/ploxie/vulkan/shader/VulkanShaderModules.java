package org.ploxie.vulkan.shader;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VulkanShaderModules {

	private VulkanShaderModule vertex;
	private VulkanShaderModule fragment;	

	public int getShaderCount() {
		int count = 0;
		if (vertex != null) {
			count++;
		}
		if (fragment != null) {
			count++;
		}
		return count;
	}
	
}

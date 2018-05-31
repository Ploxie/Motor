package org.ploxie.vulkan;

import java.util.List;

import lombok.Data;

@Data
public class VulkanInstanceProperties {

	private VulkanApplicationInfo applicationInfo;
	private List<String> extensions;
	private List<String> layers;

}

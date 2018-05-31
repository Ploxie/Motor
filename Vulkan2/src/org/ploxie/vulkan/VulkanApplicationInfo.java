package org.ploxie.vulkan;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VulkanApplicationInfo {

	private String applicationName;
	private String engineName;
	private int engineVersion;
	private VulkanApiVersion apiVersion;	
	
}

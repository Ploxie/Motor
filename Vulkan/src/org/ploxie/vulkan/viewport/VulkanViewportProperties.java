package org.ploxie.vulkan.viewport;

import org.ploxie.vulkan.math.VulkanExtent2D;
import org.ploxie.vulkan.math.VulkanOffset2D;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VulkanViewportProperties {

	@Builder.Default
	private VulkanOffset2D offset = new VulkanOffset2D(0,0);
	
	private VulkanExtent2D dimensions;
	
	@Builder.Default
	private float minDepth = 0;
	
	@Builder.Default
	private float maxDepth = 1;
}

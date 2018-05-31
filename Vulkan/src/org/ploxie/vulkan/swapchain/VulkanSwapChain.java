package org.ploxie.vulkan.swapchain;

import java.nio.LongBuffer;
import java.util.List;

import org.ploxie.vulkan.buffer.VulkanCommandBuffer;
import org.ploxie.vulkan.buffer.VulkanFrameBuffer;
import org.ploxie.vulkan.device.VulkanLogicalDevice;
import org.ploxie.vulkan.image.VulkanImage;
import org.ploxie.vulkan.image.VulkanImageView;
import org.ploxie.vulkan.math.VulkanExtent2D;
import org.ploxie.vulkan.surface.VulkanSurface;
import org.ploxie.vulkan.surface.VulkanSurfaceFormat;
import lombok.Data;

@Data
public class VulkanSwapChain {

	private final long handle;
	private final VulkanExtent2D windowDimensions;
	private final VulkanSurface surface;
	private final VulkanImage[] swapImages;
	private final VulkanSurfaceFormat imageFormat;
	private final LongBuffer handlePointer;
	
	private VulkanImageView[] swapImageViews;
	
	private VulkanFrameBuffer[] frameBuffers;
	private List<VulkanCommandBuffer> renderCommandBuffers;	
	
}

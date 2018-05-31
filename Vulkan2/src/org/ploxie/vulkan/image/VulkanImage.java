package org.ploxie.vulkan.image;

import static org.lwjgl.vulkan.VK10.*;

import lombok.Data;

@Data
public class VulkanImage {

	private final long handle;
	private final int width;
	private final int height;
	private final int depth;
	
	private VulkanImageLayout layout;
	private int imageType  = VK_IMAGE_TYPE_2D;
	
}

package org.ploxie.vulkan.image;

import static org.lwjgl.vulkan.VK10.*;

import lombok.Getter;

public enum VulkanImageAspectMask {

	COLOR(VK_IMAGE_ASPECT_COLOR_BIT),
	DEPTH(VK_IMAGE_ASPECT_DEPTH_BIT),
	STENCIL(VK_IMAGE_ASPECT_STENCIL_BIT),
	METADATA(VK_IMAGE_ASPECT_METADATA_BIT);
	
	@Getter
	private int bitMask;

	VulkanImageAspectMask(int bitMask) {
		this.bitMask = bitMask;
	}
	
}

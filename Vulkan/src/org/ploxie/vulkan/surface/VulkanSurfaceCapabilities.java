package org.ploxie.vulkan.surface;

import org.lwjgl.vulkan.VkExtent2D;
import org.lwjgl.vulkan.VkSurfaceCapabilitiesKHR;
import org.ploxie.vulkan.math.VulkanExtent2D;

import lombok.Data;

@Data
public class VulkanSurfaceCapabilities {

	private final VkSurfaceCapabilitiesKHR internal;
	
	public int getMinImageCount() {
		return internal.minImageCount();
	}

	public int getMaxImageCount() {
		return internal.maxImageCount();
	}

	public int getMaxImageLayers() {
		return internal.maxImageArrayLayers();
	}
	
	public int getCurrentTransform() {
		return internal.currentTransform();
	}
	
	public int getSupportedTransforms() {
		return internal.supportedTransforms();
	}
	
	public VulkanExtent2D getCurrentExtent() {
		VkExtent2D extent2d = internal.currentExtent();
		return new VulkanExtent2D(extent2d.width(), extent2d.height());
	}
	
	public VulkanExtent2D getMinImageExtent() {
		VkExtent2D extent2d = internal.minImageExtent();
		return new VulkanExtent2D(extent2d.width(), extent2d.height());
	}
	
	public VulkanExtent2D getMaxImageExtent() {
		VkExtent2D extent2d = internal.maxImageExtent();
		return new VulkanExtent2D(extent2d.width(), extent2d.height());
	}
}

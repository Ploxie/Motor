package org.ploxie.vulkan;

import static org.lwjgl.vulkan.VK10.VK_MAKE_VERSION;

public class VulkanApiVersion {

	private final int major;
	private final int minor;
	private final int patch;
	
	private VulkanApiVersion(int major, int minor, int patch) {
		this.major = major;
		this.minor = minor;
		this.patch = patch;
	}
	
	public int getVersion() {
		return VK_MAKE_VERSION(major, minor, patch);
	}
	
	public static VulkanApiVersion create(int major, int minor, int patch) {
		return new VulkanApiVersion(major, minor, patch);
	}
	
}

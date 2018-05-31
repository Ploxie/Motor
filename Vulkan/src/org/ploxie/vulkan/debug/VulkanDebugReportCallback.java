package org.ploxie.vulkan.debug;

public interface VulkanDebugReportCallback {

	public int invoke(VulkanDebugReportType type, int objectType, long object, long location, int messageCode, long pLayerPrefix, long pMessage, String message, long pUserData);
	
}

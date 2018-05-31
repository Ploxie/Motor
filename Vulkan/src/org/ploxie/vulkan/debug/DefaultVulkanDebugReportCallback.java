package org.ploxie.vulkan.debug;

public class DefaultVulkanDebugReportCallback implements VulkanDebugReportCallback {

	@Override
	public int invoke(VulkanDebugReportType type, int objectType, long object, long location, int messageCode, long pLayerPrefix, long pMessage, String message, long pUserData) {
		System.err.println("DefaultVulkanDebugReportCallback: " + type + ":" + messageCode + ":" + message);
		return 0;
	}

}

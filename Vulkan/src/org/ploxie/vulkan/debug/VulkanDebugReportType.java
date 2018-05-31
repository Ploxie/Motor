package org.ploxie.vulkan.debug;

import lombok.Getter;

import static org.lwjgl.vulkan.EXTDebugReport.*;

public enum VulkanDebugReportType {

	INFORMATION(VK_DEBUG_REPORT_INFORMATION_BIT_EXT),
	WARNING(VK_DEBUG_REPORT_WARNING_BIT_EXT),
	PERFORMANCE_WARNING(VK_DEBUG_REPORT_PERFORMANCE_WARNING_BIT_EXT),
	ERROR(VK_DEBUG_REPORT_ERROR_BIT_EXT),
	DEBUG(VK_DEBUG_REPORT_DEBUG_BIT_EXT);

	@Getter
	private int bitMask;

	VulkanDebugReportType(int bitMask) {
		this.bitMask = bitMask;
	}

	public static VulkanDebugReportType get(int bitMask) {
		for (VulkanDebugReportType type : values()) {
			if (type.getBitMask() == bitMask) {
				return type;
			}
		}
		return null;
	}

}

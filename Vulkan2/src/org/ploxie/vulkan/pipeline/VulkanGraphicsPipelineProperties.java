package org.ploxie.vulkan.pipeline;

import org.ploxie.vulkan.buffer.vertex.VertexInputInfo;
import org.ploxie.vulkan.shader.VulkanShaderModules;

import static org.lwjgl.vulkan.VK10.*;

import lombok.Data;

@Data
public class VulkanGraphicsPipelineProperties {

	private int topology = VK_PRIMITIVE_TOPOLOGY_TRIANGLE_LIST;
	private int polygonDrawMode = VK_POLYGON_MODE_FILL;
	private int frontFaceVertexWinding = VK_FRONT_FACE_COUNTER_CLOCKWISE;
	private int cullFaceSide = VK_CULL_MODE_BACK_BIT;
	
	private float lineWidth = 1.0f;
	
	private boolean depthClampEnabled = false;
	private boolean rasterizerDiscardEnabled = false;
	
	private boolean colorWriteMaskR = true;
	private boolean colorWriteMaskG = true;
	private boolean colorWriteMaskB = true;
	private boolean colorWriteMaskA = true;
	
	private boolean dynamicViewport = true;
	private boolean dynamicScissor = true;
	private boolean dynamicBlendConstants = false;
	
	private VertexInputInfo vertexInputInfo;
	private final VulkanShaderModules shaderModules;
	
	public int getDynamicStatesCount() {
		int count = 0;
		if (dynamicViewport) {
			count++;
		}
		if (dynamicScissor) {
			count++;
		}
		if (dynamicBlendConstants) {
			count++;
		}
		return count;
	}
}

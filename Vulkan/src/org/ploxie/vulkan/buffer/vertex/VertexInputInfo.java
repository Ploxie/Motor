package org.ploxie.vulkan.buffer.vertex;

import lombok.Data;

@Data
public class VertexInputInfo {

	private final BindingDescription bindingDescription;
	private final AttributeDescription[] attributeDescriptions;
	
}

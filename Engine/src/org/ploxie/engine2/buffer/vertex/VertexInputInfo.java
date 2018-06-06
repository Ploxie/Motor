package org.ploxie.engine2.buffer.vertex;

import lombok.Data;

@Data
public class VertexInputInfo {
	
	private final BindingDescription bindingDescription;
	private final AttributeDescription[] attributeDescriptions;

}
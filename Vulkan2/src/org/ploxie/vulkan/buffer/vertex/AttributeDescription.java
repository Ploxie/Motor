package org.ploxie.vulkan.buffer.vertex;

import lombok.Data;

@Data
public class AttributeDescription {

	private final int location;
	private final int binding;
	private final int format;
	private final int offset;

}

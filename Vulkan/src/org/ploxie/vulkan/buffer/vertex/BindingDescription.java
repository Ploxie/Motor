package org.ploxie.vulkan.buffer.vertex;

import lombok.Data;

@Data
public class BindingDescription {

	private final int binding;
	private final int stride;
	private boolean instanced = false;
	
}

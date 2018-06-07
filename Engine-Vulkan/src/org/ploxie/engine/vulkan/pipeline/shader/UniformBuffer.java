package org.ploxie.engine.vulkan.pipeline.shader;

import org.ploxie.utils.math.matrix.Matrix4f;

import lombok.Getter;

public class UniformBuffer {

	@Getter
	private Matrix4f matrix = new Matrix4f();
	
	public int getSize() {
		return 16 * 4;
	}
	
}

package org.ploxie.engine2.pipeline.shader;

import lombok.Data;

@Data
public abstract class ShaderUniformBuffer {

	public enum UniformBufferType{
		MATRIX4(16 * 4);
		
		int size;
		UniformBufferType(int size) {
			this.size = size;
		}
		
		public int getSize() {
			return size;
		}
	}
	
	protected int binding;
	protected UniformBufferType[] types;
	
	public ShaderUniformBuffer(int binding, UniformBufferType... types) {
		this.binding = binding;
		this.types = types;
	}
	
}

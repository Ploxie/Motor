package org.ploxie.engine2.pipeline;

import java.nio.ByteBuffer;

import org.ploxie.utils.math.matrix.Matrix4f;

import lombok.Getter;

public class UniformBuffer {

	@Getter
	private Matrix4f matrix = new Matrix4f();
	
	public int getSize() {
		return 16 * 4;
	}
	
	public ByteBuffer fillBuffer(ByteBuffer buffer) {
		buffer.clear();
		matrix.fillBuffer(buffer);
		return buffer;
	}
	
}

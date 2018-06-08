package org.ploxie.engine2.pipeline.uniformbuffers;

import java.nio.ByteBuffer;

import org.ploxie.utils.math.vector.Vector3f;

import lombok.Data;

@Data
public class TestBuffer extends UniformBuffer{

	private Vector3f vec = new Vector3f();
	
	@Override
	public int getSize() {
		return 3 * 4;
	}

	@Override
	public ByteBuffer fillBuffer(ByteBuffer buffer) {
		return vec.fillBuffer(buffer);
	}

	
}

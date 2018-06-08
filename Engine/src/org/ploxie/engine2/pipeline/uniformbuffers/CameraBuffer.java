package org.ploxie.engine2.pipeline.uniformbuffers;

import java.nio.ByteBuffer;

import org.ploxie.utils.math.matrix.Matrix4f;

import lombok.Data;

@Data
public class CameraBuffer extends UniformBuffer{

	private Matrix4f mvp = new Matrix4f();
	
	@Override
	public int getSize() {
		return 16 * 4;
	}

	@Override
	public ByteBuffer fillBuffer(ByteBuffer buffer) {
		return mvp.fillBuffer(buffer);
	}

}

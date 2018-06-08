package org.ploxie.engine2.pipeline.uniformbuffers;

import java.nio.ByteBuffer;

import org.ploxie.utils.math.matrix.Matrix4f;

import lombok.Getter;

public abstract class UniformBuffer {
	
	public abstract int getSize();
	
	public abstract ByteBuffer fillBuffer(ByteBuffer buffer);
	
}

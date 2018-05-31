package org.ploxie.utils;

import org.ploxie.utils.math.vector.Vector2f;
import org.ploxie.utils.math.vector.Vector3f;

public class VertexStream {

	private float[] buffer;
	private int index;
	
	public VertexStream(final int capacity) {
		this.buffer = new float[capacity];
	}
	
	public VertexStream(final float[] payload) {
		this.buffer = payload;
	}
	
	public void append(final Vector3f vertex) {
		buffer[index++] = vertex.x;
		buffer[index++] = vertex.y;
		buffer[index++] = vertex.z;
	}	
	
	public void append(final Vector2f vertex) {
		buffer[index++] = vertex.x;
		buffer[index++] = vertex.y;
	}	
	
	public void append(final VertexStream stream) {
		int length = stream.buffer.length;
		System.arraycopy(stream.buffer, 0, buffer, index, length);
	}
	
	public float[] getBuffer() {
		return buffer;
	}	
		
}

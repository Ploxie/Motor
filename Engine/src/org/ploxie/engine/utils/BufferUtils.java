package org.ploxie.engine.utils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.ploxie.utils.math.matrix.Matrix4f;

public class BufferUtils {

	public static FloatBuffer createFloatBuffer(int size) {
		return org.lwjgl.BufferUtils.createFloatBuffer(size);
	}

	public static FloatBuffer createFlippedBuffer(float... values) {
		FloatBuffer buffer = createFloatBuffer(values.length);
		buffer.put(values);
		buffer.flip();

		return buffer;
	}

	public static IntBuffer createIntBuffer(int size) {
		return org.lwjgl.BufferUtils.createIntBuffer(size);
	}

	public static IntBuffer createFlippedBuffer(int... values) {
		IntBuffer buffer = createIntBuffer(values.length);
		buffer.put(values);
		buffer.flip();

		return buffer;
	}

	public static FloatBuffer createFlippedBuffer(Matrix4f matrix) {
		FloatBuffer buffer = createFloatBuffer(16);
		matrix.fillBuffer(buffer);
		buffer.flip();

		return buffer;
	}

	public static ByteBuffer createByteBuffer(int size) {
		return org.lwjgl.BufferUtils.createByteBuffer(size);
	}
	
	public static ByteBuffer createFlippedBuffer(byte... values) {
		ByteBuffer buffer = createByteBuffer(values.length);
		buffer.put(values);
		buffer.flip();

		return buffer;
	}

}

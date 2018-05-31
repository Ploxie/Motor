package org.ploxie.vulkan.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.StandardCharsets;

import org.lwjgl.system.MemoryStack;

public class BufferUtils {

	public static ByteBuffer wrap(String string) {
		return wrap(string.getBytes(StandardCharsets.UTF_8));
	}

	public static ByteBuffer wrap(String string, MemoryStack stack) {
		return stack.bytes(string.getBytes(StandardCharsets.UTF_8));
	}

	public static ByteBuffer wrap(byte[] val) {
		ByteBuffer fb = createByteBuffer(val.length);
		fb.put(val);
		fb.rewind();
		return fb;
	}

	public static ByteBuffer wrap(byte[] val, int off, int len) {
		ByteBuffer fb = createByteBuffer(len);
		fb.put(val, off, len);
		fb.rewind();
		return fb;
	}

	public static IntBuffer wrap(int[] val) {
		IntBuffer fb = createIntBuffer(val.length);
		fb.put(val);
		fb.rewind();
		return fb;
	}

	public static LongBuffer wrap(long[] val) {
		LongBuffer fb = createLongBuffer(val.length);
		fb.put(val);
		fb.rewind();
		return fb;
	}

	public static IntBuffer wrap(int[] val, int off, int len) {
		IntBuffer fb = createIntBuffer(len);
		fb.put(val, off, len);
		fb.rewind();
		return fb;
	}

	public static FloatBuffer wrap(float[] val) {
		FloatBuffer fb = createFloatBuffer(val.length);
		fb.put(val);
		fb.rewind();
		return fb;
	}

	public static FloatBuffer wrap(float[] val, int off, int len) {
		FloatBuffer fb = createFloatBuffer(len);
		fb.put(val, off, len);
		fb.rewind();
		return fb;
	}

	public static ByteBuffer createByteBuffer(int size) {
		ByteBuffer buf = ByteBuffer.allocateDirect(size)
				.order(ByteOrder.nativeOrder());
		buf.clear();
		return buf;
	}

	public static FloatBuffer createFloatBuffer(int size) {
		FloatBuffer buffer = ByteBuffer.allocateDirect(4 * size)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		buffer.clear();
		return buffer;
	}

	public static FloatBuffer createFloatBuffer(float... data) {
		if (data == null) {
			return null;
		}
		FloatBuffer buff = createFloatBuffer(data.length);
		buff.clear();
		buff.put(data);
		buff.flip();
		return buff;
	}


	public static ShortBuffer createShortBuffer(int size) {
		ShortBuffer buffer = ByteBuffer.allocateDirect(2 * size)
				.order(ByteOrder.nativeOrder()).asShortBuffer();
		buffer.clear();
		return buffer;
	}

	public static ShortBuffer createShortBuffer(short... data) {
		if (data == null) {
			return null;
		}
		ShortBuffer buff = createShortBuffer(data.length);
		buff.clear();
		buff.put(data);
		buff.flip();
		return buff;
	}

	public static IntBuffer createIntBuffer(int size) {
		IntBuffer buffer = ByteBuffer.allocateDirect(4 * size)
				.order(ByteOrder.nativeOrder()).asIntBuffer();
		buffer.clear();
		return buffer;
	}

	public static LongBuffer createLongBuffer(int size) {
		LongBuffer buffer = ByteBuffer.allocateDirect(8 * size)
				.order(ByteOrder.nativeOrder()).asLongBuffer();
		buffer.clear();
		return buffer;
	}

	public static IntBuffer createIntBuffer(int... data) {
		if (data == null) {
			return null;
		}
		IntBuffer buff = createIntBuffer(data.length);
		buff.clear();
		buff.put(data);
		buff.flip();
		return buff;
	}

	public static DoubleBuffer createDoubleBuffer(int size) {
		DoubleBuffer buffer = ByteBuffer.allocateDirect(8 * size)
				.order(ByteOrder.nativeOrder()).asDoubleBuffer();
		buffer.clear();
		return buffer;
	}

}
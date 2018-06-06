package org.ploxie.engine2.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.StandardCharsets;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.ploxie.engine2.model.Vertex;
import org.ploxie.engine2.model.Vertex.VertexLayout;

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
		ByteBuffer buf = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
		buf.clear();
		return buf;
	}

	public static FloatBuffer createFloatBuffer(int size) {
		FloatBuffer buffer = ByteBuffer.allocateDirect(4 * size).order(ByteOrder.nativeOrder()).asFloatBuffer();
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
	
	public static FloatBuffer createFlippedBufferAOS(Vertex[] vertices)
	{
		FloatBuffer buffer = createFloatBuffer(vertices.length * Vertex.FLOATS);
		
		for(int i = 0; i < vertices.length; i++)
		{
			buffer.put(vertices[i].getPosition().x());
			buffer.put(-vertices[i].getPosition().y());
			buffer.put(vertices[i].getPosition().z());
			buffer.put(vertices[i].getNormal().x());
			buffer.put(vertices[i].getNormal().y());
			buffer.put(vertices[i].getNormal().z());
			buffer.put(vertices[i].getTextureCoord().x());
			buffer.put(vertices[i].getTextureCoord().y());
			
			if (vertices[i].getTangent() != null && vertices[i].getBitangent() != null){
				buffer.put(vertices[i].getTangent().x());
				buffer.put(vertices[i].getTangent().y());
				buffer.put(vertices[i].getTangent().z());
				buffer.put(vertices[i].getBitangent().x());
				buffer.put(vertices[i].getBitangent().y());
				buffer.put(vertices[i].getBitangent().z());
			}
		}
		
		buffer.flip();
		
		return buffer;
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
	
	public static ByteBuffer createByteBuffer(int... values){
		
		ByteBuffer byteBuffer = MemoryUtil.memAlloc(Integer.BYTES * values.length);
		IntBuffer intBuffer = byteBuffer.asIntBuffer();
		intBuffer.put(values);
		intBuffer.flip();
		
		return byteBuffer;
	}
	
	public static ByteBuffer createByteBuffer(Vertex[] vertices, VertexLayout layout){
		
		ByteBuffer byteBuffer = allocateVertexByteBuffer(layout, vertices.length);
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();

		for(int i = 0; i < vertices.length; i++)
		{
			floatBuffer.put(vertices[i].getPosition().x());
			floatBuffer.put(vertices[i].getPosition().y());
			floatBuffer.put(vertices[i].getPosition().z());
			
			if (layout == VertexLayout.POS_NORMAL ||
				layout == VertexLayout.POS_NORMAL_UV ||
				layout == VertexLayout.POS_NORMAL_UV_TAN_BITAN){
				
				floatBuffer.put(vertices[i].getNormal().x());
				floatBuffer.put(vertices[i].getNormal().y());
				floatBuffer.put(vertices[i].getNormal().z());
			}
			
			if (layout == VertexLayout.POS_NORMAL_UV ||
				layout == VertexLayout.POS_UV ||
				layout == VertexLayout.POS_NORMAL_UV_TAN_BITAN){
				
				floatBuffer.put(vertices[i].getTextureCoord().x());
				floatBuffer.put(vertices[i].getTextureCoord().y());
			}
			
			if (layout == VertexLayout.POS_NORMAL_UV_TAN_BITAN){
				
				floatBuffer.put(vertices[i].getTangent().x());
				floatBuffer.put(vertices[i].getTangent().y());
				floatBuffer.put(vertices[i].getTangent().z());
				floatBuffer.put(vertices[i].getBitangent().x());
				floatBuffer.put(vertices[i].getBitangent().y());
				floatBuffer.put(vertices[i].getBitangent().z());
			}
		}
		
		return byteBuffer;
	}

	public static ByteBuffer allocateVertexByteBuffer(VertexLayout layout, int vertexCount){
		
		ByteBuffer byteBuffer;
		
		switch(layout){
			case POS:
				byteBuffer = MemoryUtil.memAlloc(Float.BYTES * 3 * vertexCount);
				break;
			case POS_UV:
				byteBuffer =  MemoryUtil.memAlloc(Float.BYTES * 5 * vertexCount);
				break;
			case POS_NORMAL:
				byteBuffer =  MemoryUtil.memAlloc(Float.BYTES * 6 * vertexCount);
				break;
			case POS_NORMAL_UV:
				byteBuffer =  MemoryUtil.memAlloc(Float.BYTES * 8 * vertexCount);
				break;
			case POS_NORMAL_UV_TAN_BITAN:
				byteBuffer =  MemoryUtil.memAlloc(Float.BYTES * 14 * vertexCount);
				break;
			default:
				byteBuffer =  MemoryUtil.memAlloc(0);
		}
		return byteBuffer;
	}

}
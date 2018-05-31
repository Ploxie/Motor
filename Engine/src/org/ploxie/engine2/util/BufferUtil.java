package org.ploxie.engine2.util;

import static org.lwjgl.system.MemoryUtil.memAlloc;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.ploxie.engine2.model.Vertex;
import org.ploxie.engine2.model.Vertex.VertexLayout;
import org.ploxie.utils.math.matrix.Matrix4f;
import org.ploxie.utils.math.vector.Vector2f;
import org.ploxie.utils.math.vector.Vector3f;

public class BufferUtil {

	public static FloatBuffer createFloatBuffer(int size)
	{
		return BufferUtils.createFloatBuffer(size);
	}
	
	public static IntBuffer createIntBuffer(int size)
	{
		return BufferUtils.createIntBuffer(size);
	}
	
	public static DoubleBuffer createDoubleBuffer(int size)
	{
		return BufferUtils.createDoubleBuffer(size);
	}
	
	public static IntBuffer createFlippedBuffer(int... values)
	{
		IntBuffer buffer = createIntBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer createFlippedBuffer(float... values)
	{
		FloatBuffer buffer = createFloatBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		
		return buffer;
	}
	
	public static DoubleBuffer createFlippedBuffer(double... values)
	{
		DoubleBuffer buffer = createDoubleBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer createFlippedBufferAOS(Vertex[] vertices)
	{
		FloatBuffer buffer = createFloatBuffer(vertices.length * Vertex.FLOATS);
		
		for(int i = 0; i < vertices.length; i++)
		{
			buffer.put(vertices[i].getPosition().x());
			buffer.put(vertices[i].getPosition().y());
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
	
	public static FloatBuffer createFlippedBufferSOA(Vertex[] vertices)
	{
		FloatBuffer buffer = createFloatBuffer(vertices.length * Vertex.FLOATS);
		
		for(int i = 0; i < vertices.length; i++)
		{
			buffer.put(vertices[i].getPosition().x());
			buffer.put(vertices[i].getPosition().y());
			buffer.put(vertices[i].getPosition().z());
		}
		
		for(int i = 0; i < vertices.length; i++)
		{
			buffer.put(vertices[i].getNormal().x());
			buffer.put(vertices[i].getNormal().y());
			buffer.put(vertices[i].getNormal().z());
		}
			
		for(int i = 0; i < vertices.length; i++)
		{
			buffer.put(vertices[i].getTextureCoord().x());
			buffer.put(vertices[i].getTextureCoord().y());
		}	
		
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer createFlippedBuffer(Vector3f[] vector)
	{
		FloatBuffer buffer = createFloatBuffer(vector.length * Float.BYTES * 3);
		
		for (int i = 0; i < vector.length; i++)
		{
			buffer.put(vector[i].x());
			buffer.put(vector[i].y());
			buffer.put(vector[i].z());
		}
		
		buffer.flip();
		
		return buffer;
	}
	
/*	public static FloatBuffer createFlippedBuffer(Quaternion[] vector)
	{
		FloatBuffer buffer = createFloatBuffer(vector.length * Float.BYTES * 4);
		
		for (int i = 0; i < vector.length; i++)
		{
			buffer.put(vector[i].x());
			buffer.put(vector[i].y());
			buffer.put(vector[i].z());
			buffer.put(vector[i].getW());
		}
		
		buffer.flip();
		
		return buffer;
	}*/
	
	public static FloatBuffer createFlippedBuffer(Vector3f vector)
	{
		FloatBuffer buffer = createFloatBuffer(Float.BYTES * 3);
		
		buffer.put(vector.x());
		buffer.put(vector.y());
		buffer.put(vector.z());
		
		buffer.flip();
		
		return buffer;
	}
	
	/*public static FloatBuffer createFlippedBuffer(Quaternion vector)
	{
		FloatBuffer buffer = createFloatBuffer(Float.BYTES * 4);
		
		buffer.put(vector.x());
		buffer.put(vector.y());
		buffer.put(vector.z());
		buffer.put(vector.getW());
		
		buffer.flip();
		
		return buffer;
	}*/
	
	public static FloatBuffer createFlippedBuffer(Vector2f[] vector)
	{
		FloatBuffer buffer = createFloatBuffer(vector.length * Float.BYTES * 2);
		
		for (int i = 0; i < vector.length; i++)
		{
			buffer.put(vector[i].x());
			buffer.put(vector[i].y());	
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer createFlippedBuffer(Matrix4f matrix)
	{
		FloatBuffer buffer = createFloatBuffer(4 * 4);
		
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				buffer.put(matrix.get(i, j));
		
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer createFlippedBuffer(Matrix4f[] matrices)
	{
		FloatBuffer buffer = createFloatBuffer(4 * 4 * matrices.length);
		
		for (Matrix4f matrix : matrices){
			for (int i = 0; i < 4; i++)
				for (int j = 0; j < 4; j++)
					buffer.put(matrix.get(i, j));
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	public static ByteBuffer createByteBuffer(Matrix4f matrix){
		
		ByteBuffer byteBuffer = memAlloc(Float.BYTES * 16);
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		floatBuffer.put(BufferUtil.createFlippedBuffer(matrix));
		
		return byteBuffer;
	}
	
	public static ByteBuffer createByteBuffer(Vector2f[] vertices){
		
		ByteBuffer byteBuffer = memAlloc(Float.BYTES * 2 * vertices.length);
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		
		for(int i = 0; i < vertices.length; i++) {
			floatBuffer.put(vertices[i].x());
			floatBuffer.put(vertices[i].y());
		}

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
	
	public static ByteBuffer createByteBuffer(int... values){
		
		ByteBuffer byteBuffer = memAlloc(Integer.BYTES * values.length);
		IntBuffer intBuffer = byteBuffer.asIntBuffer();
		intBuffer.put(values);
		intBuffer.flip();
		
		return byteBuffer;
	}
	
	public static ByteBuffer createByteBuffer(float... values){
		
		ByteBuffer byteBuffer = memAlloc(Float.BYTES * values.length);
		FloatBuffer intBuffer = byteBuffer.asFloatBuffer();
		intBuffer.put(values);
		intBuffer.flip();
		
		return byteBuffer;
	}
	
	public static ByteBuffer createByteBuffer(FloatBuffer floatBuffer){
		
		ByteBuffer byteBuffer = memAlloc(Float.BYTES * floatBuffer.limit());
		FloatBuffer intBuffer = byteBuffer.asFloatBuffer();
		intBuffer.put(floatBuffer);
		intBuffer.flip();
		
		return byteBuffer;
	}
	
	public static ByteBuffer allocateVertexByteBuffer(VertexLayout layout, int vertexCount){
		
		ByteBuffer byteBuffer;
		
		switch(layout){
			case POS:
				byteBuffer = memAlloc(Float.BYTES * 3 * vertexCount);
				break;
			case POS_UV:
				byteBuffer = memAlloc(Float.BYTES * 5 * vertexCount);
				break;
			case POS_NORMAL:
				byteBuffer = memAlloc(Float.BYTES * 6 * vertexCount);
				break;
			case POS_NORMAL_UV:
				byteBuffer = memAlloc(Float.BYTES * 8 * vertexCount);
				break;
			case POS_NORMAL_UV_TAN_BITAN:
				byteBuffer = memAlloc(Float.BYTES * 14 * vertexCount);
				break;
			default:
				byteBuffer = memAlloc(0);
		}
		return byteBuffer;
	}
	
}

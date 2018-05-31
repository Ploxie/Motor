package org.ploxie.opengl.buffer;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.ploxie.opengl.utilities.BufferUtils;
import org.ploxie.opengl.utilities.OpenGLObject;

public class VBO implements OpenGLObject{

	public enum BufferType {
		VERTEX, UV, COLOR
	}
	
	private class VAO {
		public final int id;
		public final int index;

		public VAO(int id, int index) {
			this.id = id;
			this.index = index;
		}
	}

	private int vaoID;
	private int indexBufferID;
	private Map<BufferType, VAO> buffers;
	private int bufferIndex;
	private int size;

	public VBO() {
		vaoID = glGenVertexArrays();
		indexBufferID = glGenBuffers();
		buffers = new HashMap<BufferType, VAO>();
	}
	
	protected void finalize() throws Throwable{
		System.out.println("ASD");
		delete();
	}
		
	public int getSize() {
		return size;
	}

	public void setIndexBufferData(int[] data, int usage) {

		size = data.length;

		glBindVertexArray(vaoID);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createFlippedBuffer(data), usage);

		glBindVertexArray(0);
	}

	public void setBufferData(BufferType type, FloatBuffer buffer, int size, int usage) {

		VAO vao = buffers.get(type);
		if (vao == null) {
			vao = new VAO(glGenBuffers(), bufferIndex++);
			buffers.put(type, vao);
		}

		glBindVertexArray(vaoID);

		glBindBuffer(GL_ARRAY_BUFFER, vao.id);
		glBufferData(GL_ARRAY_BUFFER, buffer, usage);
		glVertexAttribPointer(vao.index, size, GL_FLOAT, false, 0, 0);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	public void setBufferData(BufferType type, ByteBuffer buffer, int size, int usage) {

		VAO vao = buffers.get(type);
		if (vao == null) {
			vao = new VAO(glGenBuffers(), bufferIndex++);
			buffers.put(type, vao);
		}

		glBindVertexArray(vaoID);

		glBindBuffer(GL_ARRAY_BUFFER, vao.id);
		glBufferData(GL_ARRAY_BUFFER, buffer, usage);
		
		glVertexAttribPointer(vao.index, size, GL_FLOAT, false, 0, 0);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	public void draw() {		
		glBindVertexArray(vaoID);

		for (VAO vao : buffers.values()) {
			glEnableVertexAttribArray(vao.index);
		}

		glDrawElements(GL_TRIANGLES, size, GL_UNSIGNED_INT, 0);

		for (VAO vao : buffers.values()) {
			glDisableVertexAttribArray(vao.index);
		}

		glBindVertexArray(0);
	}

	@Override
	public void delete() {
		glBindVertexArray(vaoID);
		for (VAO vao : buffers.values()) {
			glDeleteBuffers(vao.id);
		}
		glDeleteVertexArrays(vaoID);
		glBindVertexArray(0);
	}

}

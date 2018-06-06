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

import org.ploxie.engine2.model.Mesh;
import org.ploxie.engine2.model.Vertex;
import org.ploxie.engine2.util.BufferUtils;
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
	private int vboID;
	private int indexBufferID;
	private Map<BufferType, VAO> buffers;
	private int bufferIndex;
	private int size;

	public VBO() {
		vaoID = glGenVertexArrays();
		vboID = glGenBuffers();
		indexBufferID = glGenBuffers();
		buffers = new HashMap<BufferType, VAO>();
	}
	
	protected void finalize() throws Throwable{
		delete();
	}
		
	public int getSize() {
		return size;
	}
	
	public void setData(Mesh mesh) {
		size = mesh.getIndices().length;
	
		glBindVertexArray(vaoID);
		
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFlippedBufferAOS(mesh.getVertices()), GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createIntBuffer(mesh.getIndices()), GL_STATIC_DRAW);
		
		if (mesh.isTangentSpace()){
			glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.BYTES, 0);
			glVertexAttribPointer(1, 3, GL_FLOAT, false, Vertex.BYTES, Float.BYTES * 3);
			glVertexAttribPointer(2, 2, GL_FLOAT, false, Vertex.BYTES, Float.BYTES * 6);
			glVertexAttribPointer(3, 3, GL_FLOAT, false, Vertex.BYTES, Float.BYTES * 8);
			glVertexAttribPointer(4, 3, GL_FLOAT, false, Vertex.BYTES, Float.BYTES * 11);
		}
		else{
			glVertexAttribPointer(0, 3, GL_FLOAT, false, Float.BYTES * 8, 0);
			glVertexAttribPointer(1, 3, GL_FLOAT, false, Float.BYTES * 8, Float.BYTES * 3);
			glVertexAttribPointer(2, 2, GL_FLOAT, false, Float.BYTES * 8, Float.BYTES * 6);
		}
		
		glBindVertexArray(0);
	}

	/*public void setIndexBufferData(int[] data, int usage) {

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
	}*/

	/*public void setBufferData(BufferType type, ByteBuffer buffer, int size, int usage) {

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
	}*/

	public void draw() {		
		glBindVertexArray(vaoID);

		/*for (VAO vao : buffers.values()) {
			glEnableVertexAttribArray(vao.index);
		}

		glDrawElements(GL_TRIANGLES, size, GL_UNSIGNED_INT, 0);

		for (VAO vao : buffers.values()) {
			glDisableVertexAttribArray(vao.index);
		}*/
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		/*if (hasTangentsBitangents){
			glEnableVertexAttribArray(3);
			glEnableVertexAttribArray(4);
		}	*/	
			glDrawElements(GL_TRIANGLES, size, GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		/*if (hasTangentsBitangents){
			glDisableVertexAttribArray(3);
			glDisableVertexAttribArray(4);
		}*/

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

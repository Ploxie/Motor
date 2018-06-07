package org.ploxie.opengl.buffer;

import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL15.glUnmapBuffer;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL15.glMapBuffer;
import static org.lwjgl.opengl.GL30.glBindBufferBase;
import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;

import java.nio.ByteBuffer;

public class UniformBufferObject {

	private int ubo;

	public UniformBufferObject() {
		ubo = glGenBuffers();
	}

	public void create(int binding, int size) {
		glBindBufferBase(GL_UNIFORM_BUFFER, binding, ubo);

		glBindBuffer(GL_UNIFORM_BUFFER, ubo);
		glBufferData(GL_UNIFORM_BUFFER, size, GL_DYNAMIC_DRAW);		
	}

	public void addData(ByteBuffer buffer) {
		glBindBuffer(GL_UNIFORM_BUFFER, ubo);
		glBufferData(GL_UNIFORM_BUFFER, buffer, GL_DYNAMIC_DRAW);
	}

	public void update(ByteBuffer buffer, int size) {

		glBindBuffer(GL_UNIFORM_BUFFER, ubo);

		ByteBuffer mappedBuffer = glMapBuffer(GL_UNIFORM_BUFFER, GL_WRITE_ONLY, size, null);
		mappedBuffer.clear();

		for (int i = 0; i < size; i++) {
			mappedBuffer.put(buffer.get(i));
		}
		
		mappedBuffer.flip();

		glUnmapBuffer(GL_UNIFORM_BUFFER);
	}
}

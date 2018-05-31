package org.ploxie.opengl.buffer.primitive;

import org.ploxie.opengl.buffer.VBO;
import org.ploxie.opengl.utilities.BufferUtils;
import org.ploxie.utils.VertexStream;
import org.ploxie.utils.math.vector.Vector2f;

import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class Quad extends VBO{

	public Quad() {
		create(0,0,1,1);
	}
	
	public Quad(float width, float height) {
		create(0,0, width, height);
	}
	
	private void create(float x, float y, float width, float height) {
		VertexStream stream = new VertexStream(6 * 2);
		
		stream.append(new Vector2f(x,y));
		stream.append(new Vector2f(x,y+height));
		stream.append(new Vector2f(x+width,y));
		stream.append(new Vector2f(x+width,y+height));
		
		int[] indices = new int[] {0,1,2, 2,1,3};
		
		VertexStream uvStream = new VertexStream(6 * 2);
		uvStream.append(new Vector2f(0, 0));
		uvStream.append(new Vector2f(0, 1));
		uvStream.append(new Vector2f(1, 0));
		uvStream.append(new Vector2f(1, 1));
		
		setIndexBufferData(indices, GL_STATIC_DRAW);
		setBufferData(BufferType.VERTEX, BufferUtils.createFlippedBuffer(stream.getBuffer()), 2, GL_STATIC_DRAW);
		setBufferData(BufferType.UV, BufferUtils.createFlippedBuffer(uvStream.getBuffer()), 2, GL_STATIC_DRAW);
	}
	
}

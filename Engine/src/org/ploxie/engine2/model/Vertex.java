package org.ploxie.engine2.model;


import org.ploxie.utils.math.vector.Vector2f;
import org.ploxie.utils.math.vector.Vector3f;

import lombok.Getter;

@Getter
public class Vertex {

	public static final int BYTES = 14 * Float.BYTES;
	public static final int FLOATS = 14;
	
	private Vector3f position;
	private Vector3f normal;
	private Vector2f textureCoord;
	private Vector3f tangent;
	private Vector3f bitangent;
	
	public enum VertexLayout{
		
		POS_NORMAL_UV_TAN_BITAN(56),
		POS_NORMAL(24),
		POS_UV(20),
		POS(12),
		POS_NORMAL_UV(32),
		POS2D(8);
		
		int stride;
		VertexLayout(int stride){
			this.stride = stride;
		}
		public int getStride() {
			return stride;
		}
	}
	
	public Vertex(){	
	}
	
	public Vertex(Vector3f pos)
	{
		this.setPosition(pos);
		this.setTextureCoord(new Vector2f(0,0));
		this.setNormal(new Vector3f(0,0,0));
	}
	
	public Vertex(Vector3f pos, Vector2f texture)
	{
		this.setPosition(pos);
		this.setTextureCoord(texture);
		this.setNormal(new Vector3f(0,0,0));
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f pos) {
		this.position = pos;
	}

	public Vector2f getTextureCoord() {
		return textureCoord;
	}

	public void setTextureCoord(Vector2f texture) {
		this.textureCoord = texture;
	}


	public Vector3f getNormal() {
		return normal;
	}

	public void setNormal(Vector3f normal) {
		this.normal = normal;
	}

	public Vector3f getTangent() {
		return tangent;
	}

	public void setTangent(Vector3f tangent) {
		this.tangent = tangent;
	}

	public Vector3f getBitangent() {
		return bitangent;
	}

	public void setBitangent(Vector3f bitangent) {
		this.bitangent = bitangent;
	}
}

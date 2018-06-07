package org.ploxie.engine.model;

import org.ploxie.engine.model.materials.Material;
import org.ploxie.opengl.buffer.VertexBufferObject;

public class Model {

	protected VertexBufferObject vbo;
	protected Material material;
		
	public Model(VertexBufferObject vbo, Material material) {
		this.vbo = vbo;
		this.material = material;
	}
	
	public VertexBufferObject getVBO() {
		return vbo;
	}
	
	public Material getMaterial() {
		return material;
	}
}

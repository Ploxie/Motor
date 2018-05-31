package org.ploxie.engine.model;

import org.ploxie.engine.model.materials.Material;
import org.ploxie.opengl.buffer.VBO;

public class Model {

	protected VBO vbo;
	protected Material material;
		
	public Model(VBO vbo, Material material) {
		this.vbo = vbo;
		this.material = material;
	}
	
	public VBO getVBO() {
		return vbo;
	}
	
	public Material getMaterial() {
		return material;
	}
}

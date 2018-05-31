package org.ploxie.engine.model.materials;

import org.ploxie.engine.shaders.Shader;

public abstract class Material {
	
	public abstract void applyShader(Shader shader);
		
}

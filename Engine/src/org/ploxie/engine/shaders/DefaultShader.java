package org.ploxie.engine.shaders;

import org.ploxie.utils.ResourceLoader;
import org.ploxie.utils.math.vector.Vector3f;

public class DefaultShader extends Shader {

	@Override
	protected void initialize() {


		addVertexShader(ResourceLoader.loadShader("Default_vertex.glsl"));
		addFragmentShader(ResourceLoader.loadShader("Default_fragment.glsl"));

		compileShader();

		getUniform("color");
	}

	@Override
	protected void setDefaultUniforms() {
		setUniform("color", new Vector3f(1, 0, 1));
	}
	
}

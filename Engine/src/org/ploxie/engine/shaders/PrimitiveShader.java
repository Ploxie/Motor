package org.ploxie.engine.shaders;

import org.ploxie.engine.camera.Camera;
import org.ploxie.utils.math.matrix.Matrix4f;
import org.ploxie.utils.ResourceLoader;

public class PrimitiveShader extends Shader{

	@Override
	protected void initialize() {
		addVertexShader(ResourceLoader.loadShader("primitive_vert.glsl"));
		addFragmentShader(ResourceLoader.loadShader("primitive_frag.glsl"));

		compileShader();

		getUniform("modelMatrix");
		getUniform("viewMatrix");
		getUniform("projectionMatrix");
	}
	
	public void setUniforms(Camera camera,Matrix4f transform) {
		setUniform("modelMatrix", transform);
		setUniform("viewMatrix", camera.getViewMatrix());
		setUniform("projectionMatrix", camera.getProjectionMatrix());
		
	}

}

package org.ploxie.engine.scene.components;

import org.ploxie.engine.Engine;
import org.ploxie.engine.camera.Camera;
import org.ploxie.engine.model.Model;
import org.ploxie.engine.shaders.Shader;

public class GUIRenderer extends Renderer{

	public GUIRenderer(Model model, Shader shader) {
		super(model, shader);
	}
	
	@Override
	public void render() {
		Camera camera = Engine.getCamera2D();
		
		shader.bind();
		shader.setUniform("projectionMatrix", camera.getProjectionMatrix());
		shader.setUniform("modelMatrix", transform.getWorldMatrix());
		
		model.getMaterial().applyShader(shader);
		model.getVBO().draw();
		
	}

}

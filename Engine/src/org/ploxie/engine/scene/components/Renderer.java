package org.ploxie.engine.scene.components;

import org.ploxie.engine.Engine;
import org.ploxie.engine.camera.Camera;
import org.ploxie.engine.camera.Camera3D;
import org.ploxie.engine.model.Model;
import org.ploxie.engine.scene.decorations.Renderable;
import org.ploxie.engine.shaders.Shader;
import org.ploxie.utils.Color;
import org.ploxie.utils.math.vector.Vector2f;
import org.ploxie.utils.math.vector.Vector3f;

public class Renderer extends Component implements Renderable{

	protected Model model;
	protected Shader shader;
	
	protected Transform transform;
	
	public Renderer(Model model, Shader shader) {		
		setModel(model);
		this.shader = shader;
	}
		
	@Override
	public void awake() {
		transform = getGameObject().getComponent(Transform.class);
	}	
	
	public void setModel(Model model) {
		this.model = model;
	}		
		
	@Override
	public void render() {
		
		Camera camera = Engine.getCamera3D();
		
		shader.bind();
		shader.setUniform("projectionMatrix", camera.getProjectionMatrix());
		shader.setUniform("viewMatrix", camera.getViewMatrix());
		shader.setUniform("modelMatrix", transform.getWorldMatrix());
		
		model.getMaterial().applyShader(shader);
		model.getVBO().draw();
	}

}

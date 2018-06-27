package org.ploxie.game.scene;

import org.ploxie.engine2.context.EngineContext;
import org.ploxie.engine2.model.Mesh;
import org.ploxie.engine2.pipeline.Pipeline;
import org.ploxie.engine2.pipeline.shader.ShaderManager;
import org.ploxie.engine2.pipeline.shader.ShaderModules;
import org.ploxie.engine2.pipeline.uniformbuffers.CameraBuffer;
import org.ploxie.engine2.pipeline.uniformbuffers.TestBuffer;
import org.ploxie.engine2.scenegraph.GameObject;
import org.ploxie.engine2.scenegraph.SceneGraph;
import org.ploxie.engine2.scenegraph.component.Component;
import org.ploxie.engine2.scenegraph.component.RenderComponent;
import org.ploxie.engine2.util.MeshGenerator;
import org.ploxie.game.components.TestComponent;
import org.ploxie.game.components.TestComponent2;

public class GameScene extends SceneGraph{

	@Override
	public void initialize() {
		GameObject object = new GameObject();		
		
		Mesh mesh = MeshGenerator.NDCQuad2D();
		Pipeline pipeline = new Pipeline(ShaderManager.loadShader("shader"));
		pipeline.getUniformBuffers().add(new CameraBuffer());
		pipeline.getUniformBuffers().add(new TestBuffer());
		
		RenderComponent renderComponent = new RenderComponent(mesh, pipeline);
		TestComponent component = new TestComponent();
		
		object.addComponent(renderComponent);
		object.addComponent(component);
		
		addObject(object);
		
		
	}

}

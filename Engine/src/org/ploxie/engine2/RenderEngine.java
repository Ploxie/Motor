package org.ploxie.engine2;

import org.ploxie.engine2.scenegraph.SceneGraph;

import lombok.Getter;

public abstract class RenderEngine {
	
	@Getter
	protected SceneGraph sceneGraph;
	
	public void initialize() {
		sceneGraph = new SceneGraph();
	}
	
	public void update() {
		sceneGraph.update();
	}
	
	public abstract void render();

	public void shutdown() {
		sceneGraph.shutdown();
	}

}

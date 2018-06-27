package org.ploxie.engine2;

import org.ploxie.engine2.scenegraph.SceneGraph;

import lombok.Getter;
import lombok.Setter;

public abstract class RenderEngine {
	
	@Getter@Setter
	protected SceneGraph sceneGraph;
		
	public void initialize() {
		sceneGraph.initialize();
	}
		
	public void update() {		
		sceneGraph.update();		
	}
	
	public abstract void render();

	public void shutdown() {
		sceneGraph.shutdown();
	}

}

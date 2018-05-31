package org.ploxie.engine2;

public abstract class RenderEngine {
	
	public abstract void render();

	public void initialize() {
		
	}
	
	public void update() {
		
	}

	public void shutdown() {

		// important to shutdown scenegraph before render-engine, since
		// thread safety of instancing clusters.
		// scenegraph sets isRunning to false, render-engine signals all
		// waiting threads to shutdown

		//sceneGraph.shutdown();
	}

}

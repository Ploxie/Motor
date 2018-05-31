package org.ploxie.engine;

import org.ploxie.engine.display.IDrawSurface;
import org.ploxie.engine.graphics.IGraphicsManager;
import org.ploxie.engine.states.GameStateMachine;


public class Engine extends GameStateMachine{

	private IDrawSurface drawSurface;
	private IGraphicsManager graphicsManager;

	private boolean initialized;
	private boolean running;

	public void init(IDrawSurface drawSurface, IGraphicsManager graphicsManager ) {		
		this.drawSurface = drawSurface;
		this.graphicsManager = graphicsManager;
		
		this.graphicsManager.initialize(drawSurface);
		
		initialized = true;
	}

	public void start() {
		if(!initialized) {
			throw new AssertionError("Engine is not initialized");
		}
		if (running) {
			return;
		}
				
		drawSurface.show();

		running = true;
		run();
	}

	private void stop() {
		if (!running) {
			return;
		}

		running = false;
	}

	private void run() {

		while (running) {
			if (drawSurface.isDestroyed()) {
				stop();
			}
			drawSurface.preRender();
			
			//RENDER
			update(0.0f);
			render();
			
			drawSurface.postRender();
			graphicsManager.update();
			drawSurface.pollEvents();
		}
	}		
	

}

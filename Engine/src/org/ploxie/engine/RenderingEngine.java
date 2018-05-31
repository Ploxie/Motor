package org.ploxie.engine;

import org.lwjgl.opengl.GL11;
import org.ploxie.engine.gui.WidgetManager;
import org.ploxie.engine.scene.GameObject;
import org.ploxie.engine.scene.Scene;
import org.ploxie.opengl.display.Window;

public class RenderingEngine {

	private Window window;

	private Scene scene;
	
	public void init(Window window) {
		this.window = window;
		
	}

	public void startScene(Scene scene) {
		this.scene = scene;
	}

	public void update() {
		GL11.glClearColor(0.5f, 0.5f, 0.55f, 0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		scene.update();
		scene.render();

		window.swapBuffers();
	}
	

}

package org.ploxie.engine.scene;

import org.ploxie.engine.Engine;
import org.ploxie.engine.camera.Camera2D;
import org.ploxie.engine.camera.Camera3D;
import org.ploxie.engine.camera.FPSCamera;
import org.ploxie.engine.scene.components.ComponentManager;

public abstract class Scene extends GameObject{

	public abstract void initialize();
	
	private GameObject camera;
	
	public Scene() {
		camera = new GameObject();
		camera.addComponent(new FPSCamera(Engine.getCamera3D()));
		addChild(camera);
	}
	
	public void update() {
		ComponentManager.getInstance().update();
	}
	
	public void render() {
		ComponentManager.getInstance().render();
	}
				
}

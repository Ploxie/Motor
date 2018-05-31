package org.ploxie.engine.camera;

import org.ploxie.engine.scene.components.Component;
import org.ploxie.engine.scene.components.Transform;


public class CameraComponent extends Component{

	protected Transform transform;
	protected Camera camera;
	
	public CameraComponent(Camera camera) {
		this.camera = camera;
	}
	
	@Override
	public void awake() {
		transform = getGameObject().getTransform();
	}
	
	public void setTransform(Transform transform) {
		this.transform = transform;
	}
	
}

package org.ploxie.engine.camera;

import org.ploxie.utils.math.matrix.Matrix4f;

public abstract class Camera {

	protected Matrix4f projectionMatrix;
	protected Matrix4f viewMatrix;
			
	public Matrix4f getProjectionMatrix(){
		return projectionMatrix;
	}
	
	public Matrix4f getViewMatrix(){
		return viewMatrix;
	}
	
}

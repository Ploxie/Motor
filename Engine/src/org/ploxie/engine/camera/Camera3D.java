package org.ploxie.engine.camera;

import org.ploxie.utils.math.matrix.Matrix4f;

public class Camera3D extends Camera{

	private float fovy;
	private float zNear;
	private float zFar;
	private float aspectRatio;

	public Camera3D(float fovy, float zNear, float zFar, float aspectRatio) {
		this.fovy = fovy;
		this.zNear = zNear;
		this.zFar = zFar;
		this.aspectRatio = aspectRatio;

		viewMatrix = new Matrix4f();
		projectionMatrix = new Matrix4f().makePerspective(fovy, aspectRatio, zNear, zFar);
	}

	public float getFovy() {
		return fovy;
	}

	public void setFovy(float fovy) {
		this.fovy = fovy;
	}

	public float getzNear() {
		return zNear;
	}

	public void setzNear(float zNear) {
		this.zNear = zNear;
	}

	public float getzFar() {
		return zFar;
	}

	public void setzFar(float zFar) {
		this.zFar = zFar;
	}

	public float getAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(float aspectRatio) {
		this.aspectRatio = aspectRatio;
	}



}

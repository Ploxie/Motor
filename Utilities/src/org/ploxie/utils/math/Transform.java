package org.ploxie.utils.math;

import org.ploxie.utils.math.matrix.Matrix4f;
import org.ploxie.utils.math.vector.Vector3f;

public class Transform {

	private Vector3f translation;
	private Vector3f rotation;
	private Vector3f scaling;
	
	private Vector3f localTranslation;
	private Vector3f localRotation;
	private Vector3f localScaling;
	
	public Transform()
	{
		setTranslation(new Vector3f(0,0,0));
		setRotation(new Vector3f(0,0,0));
		setScaling(new Vector3f(1,1,1));
		
		setLocalTranslation(new Vector3f(0,0,0));
		setLocalRotation(new Vector3f(0,0,0));
		setLocalScaling(new Vector3f(1,1,1));
	}
	
	public Matrix4f getWorldMatrix()
	{
		Matrix4f translationMatrix = new Matrix4f().Translation(translation);
		Matrix4f rotationMatrix = new Matrix4f().Rotation(rotation);
		Matrix4f scalingMatrix = new Matrix4f().Scaling(scaling);
		
		return translationMatrix.multiply(scalingMatrix.multiply(rotationMatrix));
	}
	
	public Matrix4f getModelMatrix()
	{
		Matrix4f rotationMatrix = new Matrix4f().Rotation(rotation);
		
		return rotationMatrix;
	}	
	
	/*public Matrix4f getModelViewProjectionMatrix()
	{
		return EngineContext.getCamera().getViewProjectionMatrix().mul(getWorldMatrix());
	}*/

	public Vector3f getTranslation() {
		return translation;
	}

	public void setTranslation(Vector3f translation) {
		this.translation = translation;
	}
	
	public void setTranslation(float x, float y, float z) {
		this.translation = new Vector3f(x, y, z);
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}
	
	public void setRotation(float x, float y, float z) {
		this.rotation = new Vector3f(x,y,z);
	}

	public Vector3f getScaling() {
		return scaling;
	}

	public void setScaling(Vector3f scaling) {
		this.scaling = scaling;
	}
	
	public void setScaling(float x, float y, float z) {
		this.scaling = new Vector3f(x, y, z);
	}

	public Vector3f getLocalTranslation() {
		return localTranslation;
	}

	public void setLocalTranslation(Vector3f localTranslation) {
		this.localTranslation = localTranslation;
	}
	
	public void setLocalTranslation(float x, float y, float z) {
		this.localTranslation = new Vector3f(x, y, z);
	}

	public Vector3f getLocalRotation() {
		return localRotation;
	}

	public void setLocalRotation(Vector3f localRotation) {
		this.localRotation = localRotation;
	}
	
	public void setLocalRotation(float x, float y, float z) {
		this.localRotation = new Vector3f(x, y, z);
	}

	public Vector3f getLocalScaling() {
		return localScaling;
	}

	public void setLocalScaling(Vector3f localScaling) {
		this.localScaling = localScaling;
	}	
	
	public void setLocalScaling(float x, float y, float z) {
		this.localScaling = new Vector3f(x, y, z);
	}	
	
}

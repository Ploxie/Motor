package org.ploxie.engine.scene.components;

import org.ploxie.engine.scene.decorations.Updatable;
import org.ploxie.utils.math.matrix.Matrix4f;
import org.ploxie.utils.math.vector.Vector3f;

public class Transform extends Component implements Updatable{

	private Matrix4f local;
	private Matrix4f world;	
	
	@Override
	public void awake() {
		local = new Matrix4f();
		world = new Matrix4f();
	}
	
	@Override
	public void update() {		
		world = local;
		
		if(gameObject.getParent() != null) {
			world = gameObject.getParent().getTransform().world.clone().multiply(world);
			
		}
	}
	
	public void setTranslation(Vector3f position) {
		setTranslation(position.x, position.y, position.z);
	}
	
	public void setTranslation(float x, float y, float z) {
		local.setTranslation(x,y,z);
	}
	
	public void setLocalMatrix(Matrix4f matrix) {
		this.local = matrix;
	}
		
	public Matrix4f getLocalMatrix() {
		return local;
	}
	
	public Matrix4f getWorldMatrix() {
		return world;
	}

}

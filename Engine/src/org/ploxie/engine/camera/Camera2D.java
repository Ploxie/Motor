package org.ploxie.engine.camera;

import org.ploxie.engine.Engine;
import org.ploxie.utils.Viewport;
import org.ploxie.utils.math.matrix.Matrix4f;

public class Camera2D extends Camera{

	public Camera2D() {
		Viewport viewport = Engine.getViewport();
		projectionMatrix = new Matrix4f().makeOrthoMatrix(viewport.getLocation().x, viewport.getDimensions().x, viewport.getDimensions().y, viewport.getLocation().y, -1, 1);
		viewMatrix = new Matrix4f();
	}
	


}

package org.ploxie.engine.camera;

import java.awt.Font;

import org.ploxie.engine.Engine;
import org.ploxie.engine.event.events.KeyEvent;
import org.ploxie.engine.event.events.KeyboardListener;
import org.ploxie.engine.event.events.MouseKeyEvent;
import org.ploxie.engine.event.events.MouseListener;
import org.ploxie.engine.event.events.MouseMoveEvent;
import org.ploxie.engine.font.TextMesh;
import org.ploxie.engine.gui.GUIText;
import org.ploxie.engine.input.Input;
import org.ploxie.engine.input.Keyboard;
import org.ploxie.engine.input.Mouse;
import org.ploxie.engine.scene.GameObject;
import org.ploxie.engine.scene.components.Component;
import org.ploxie.engine.scene.components.Renderer;
import org.ploxie.engine.scene.components.Transform;
import org.ploxie.engine.scene.decorations.Updatable;
import org.ploxie.utils.math.matrix.Matrix4f;
import org.ploxie.utils.math.vector.Vector2i;
import org.ploxie.utils.math.vector.Vector3f;
import org.ploxie.utils.texture.Bitmap;

public class FPSCamera extends CameraComponent implements KeyboardListener, MouseListener {

	private Vector3f speed;

	private boolean mouseDown;

	private Vector3f position;
	private float pitch;
	private float yaw;
	

	public FPSCamera(Camera camera) {
		super(camera);
		this.position = new Vector3f(0, 10, 0);
		this.speed = new Vector3f(0.1f, 0.1f, 0.1f);

		Engine.getEventManager().register((KeyboardListener) this);
		Engine.getEventManager().register((MouseListener) this);

		 pitch -= 90;		
	}
	
	@Override
	public void awake() {
		super.awake();
		updateTranslation(transform.getWorldMatrix().makeFPSViewMatrix(position, pitch, yaw));
	}

	@Override
	public void keyHeld(KeyEvent event) {

		float delta = Engine.getFPSCounter().getDelta();
		Vector3f forward = camera.getViewMatrix().getDirection();
		Vector3f left = camera.getViewMatrix().getLeft();

		if (event.getKey() == (Keyboard.KEY_W)) {
			position.add(forward.copy().multiply(-speed.z / delta));
		}

		if (event.getKey() == (Keyboard.KEY_S)) {
			position.add(forward.copy().multiply(speed.z / delta));
		}

		if (event.getKey() == (Keyboard.KEY_A)) {
			position.add(left.copy().multiply(-speed.x / delta));
		}

		if (event.getKey() == (Keyboard.KEY_D)) {
			position.add(left.copy().multiply(speed.x / delta));
		}

		if (event.getKey() == (Keyboard.KEY_SPACE)) {
			position.add(new Vector3f(0, 1, 0).multiply(speed.x / delta));
		}

		if (event.getKey() == (Keyboard.KEY_LEFT_SHIFT)) {
			position.add(new Vector3f(0, -1, 0).multiply(speed.x / delta));
		}

		if (event.getKey() == (Keyboard.KEY_ENTER)) {
			camera.viewMatrix.makeLookAtViewMatrix(position, new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
			position = camera.viewMatrix.getTranslation();
		} else {

		}
		
		if(event.getKey() == Keyboard.KEY_UP) {

			yaw -= 0.1f * 360 / delta;

		}
		if(event.getKey() == Keyboard.KEY_DOWN) {

			yaw += 0.1f * 360 / delta;

		}

	 updateTranslation(transform.getWorldMatrix().makeFPSViewMatrix(position, pitch, yaw));

	}

	public void updateTranslation(Matrix4f view) {
		camera.getViewMatrix().set(view);
		transform.setLocalMatrix(view);
		
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {

		if (!mouseDown) {
			return;
		}

		float delta = Engine.getFPSCounter().getDelta();
		Vector2i mouseDelta = event.getDelta();
		
		
		
		yaw -= mouseDelta.x * 45  / delta;
		pitch -= mouseDelta.y * 45 / delta;
		
		updateTranslation(transform.getWorldMatrix().makeFPSViewMatrix(position, pitch, yaw));
		
	}

	@Override
	public void keyPressed(KeyEvent event) {

	}

	@Override
	public void onMouseHeld(MouseKeyEvent event) {
		

	}

	@Override
	public void onMouseRelease(MouseKeyEvent event) {
		if (event.getButton() == Mouse.BUTTON_1) {
			mouseDown = false;
		}
	}

	@Override
	public void onMouseDown(MouseKeyEvent event) {
		if (event.getButton() == Mouse.BUTTON_1) {
			mouseDown = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {

	}

	


}

package org.ploxie.engine.scene.debug;

import java.awt.Font;

import org.ploxie.engine.Engine;
import org.ploxie.engine.event.events.KeyEvent;
import org.ploxie.engine.event.events.KeyboardListener;
import org.ploxie.engine.gui.GUIText;
import org.ploxie.engine.input.Keyboard;
import org.ploxie.engine.scene.GameObject;
import org.ploxie.engine.scene.components.Component;
import org.ploxie.engine.scene.decorations.Updatable;


public class DebugInfo extends Component implements Updatable, KeyboardListener{

	private GUIText fpsText;
	private GUIText forwardText;
	
	private boolean toggle = true;
	
	@Override
	public void awake() {
		GameObject fpsTextObject = new GameObject();
		fpsText = new GUIText(new Font("Arial",Font.BOLD, 16));
		fpsTextObject.addComponent(fpsText);
		getGameObject().addChild(fpsTextObject);
		
		
		
		Engine.getEventManager().register((KeyboardListener)this);

	}

	@Override
	public void update() {
		fpsText.setText("FPS: "+Engine.getFPSCounter().getFPS());
		
	}

	@Override
	public void keyPressed(KeyEvent event) {
		
		if(event.getKey() == Keyboard.KEY_F1) {
			toggle = !toggle;
			
			fpsText.getGameObject().setEnabled(toggle);
		}

	}

	@Override
	public void keyReleased(KeyEvent event) {
		
	}

	@Override
	public void keyHeld(KeyEvent event) {
		
	}

	

	
}

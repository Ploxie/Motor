package org.ploxie.engine.scene.components;

import org.ploxie.engine.scene.GameObject;

public abstract class Component {

	protected GameObject gameObject;
	protected boolean isEnabled;
	protected String name;
	
	public Component() {
		isEnabled = true;
		name = "Component";
	}
	
	public Component(String name) {
		isEnabled = true;
		this.name = name;
	}
	
	public GameObject getGameObject() {
		return gameObject;
	}
	
	public void setGameObject(GameObject parent) {
		this.gameObject = parent;
	}
	
	public void setEnabled(boolean enable) {
		isEnabled = enable;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}	
	
	public String getName() {
		return name;
	}
	
	public void awake() {
		
	}
	
	
}

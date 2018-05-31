package org.ploxie.engine.scene;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ploxie.engine.camera.Camera;
import org.ploxie.engine.scene.components.Component;
import org.ploxie.engine.scene.components.ComponentManager;
import org.ploxie.engine.scene.components.Transform;
import org.ploxie.engine.scene.decorations.Renderable;
import org.ploxie.engine.scene.decorations.Updatable;

public class GameObject {

	protected String name;
	protected GameObject parent;
	protected Transform transform;

	protected List<Component> components;
	protected List<GameObject> children;
	
	protected boolean isEnabled = true;

	public GameObject() {
		transform = new Transform();
		components = new ArrayList<Component>();
		children = new ArrayList<GameObject>();
		addComponent(transform);
	}

	public GameObject(String name) {
		this.name = name;
		transform = new Transform();
		components = new ArrayList<Component>();
		children = new ArrayList<GameObject>();
		addComponent(transform);
	}
	
	public void addChild(GameObject go) {
		children.add(go);
		go.parent = this;
	}

	public void addComponent(Component component) {

		component.setGameObject(this);
		components.add(component);
		ComponentManager.getInstance().addComponent(component);

		component.awake();
	}

	public void removeComponent(Component component) {
		components.remove(component);
		ComponentManager.getInstance().removeComponent(component);
	}

	public Component getComponent(String name) {
		for (Component c : components) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(Class<T> type) {
		for (Component c : components) {
			if (c.getClass().equals(type)) {
				return (T) c;
			}
		}
		return null;
	}

	public GameObject getParent() {
		return parent;
	}

	public Transform getTransform() {
		return transform;
	}
	
	public void setEnabled(boolean enabled) {
		this.isEnabled = enabled;
		for(Component c : components) {
			c.setEnabled(enabled);
		}
		for(GameObject c : children) {
			c.setEnabled(enabled);
		}
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}

}

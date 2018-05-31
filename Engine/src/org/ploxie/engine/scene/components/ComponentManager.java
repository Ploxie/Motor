package org.ploxie.engine.scene.components;

import java.util.ArrayList;
import java.util.List;

import org.ploxie.engine.scene.decorations.Renderable;
import org.ploxie.engine.scene.decorations.Updatable;

public class ComponentManager {

	private static ComponentManager instance;

	private List<Component> components;
	private List<Component> renderableComponents;
	private List<Component> guiComponents;

	private ComponentManager() {
		components = new ArrayList<Component>();
		renderableComponents = new ArrayList<Component>();
		guiComponents = new ArrayList<Component>();
	}

	public Component addComponent(Component component) {
		if (component instanceof GUIRenderer) {
			guiComponents.add(component);
		} else if (component instanceof Renderer) {

			renderableComponents.add(component);
		}

		components.add(component);

		return component;
	}

	public void removeComponent(Component component) {
		if (component instanceof GUIRenderer) {
			guiComponents.remove(component);
		} else if (component instanceof Renderer) {
			renderableComponents.remove(component);
		} else {
			components.remove(component);
		}
	}

	public void update() {
		for (Component c : components) {
			if (c instanceof Updatable) {
				if (c.isEnabled) {
					((Updatable) c).update();
				}
			}
		}
	}

	public void render() {
		for (Component c : renderableComponents) {
			if (c.isEnabled) {
				((Renderable) c).render();
			}
		}
		for (Component c : guiComponents) {
			if (c.isEnabled) {
				((GUIRenderer) c).render();
			}
		}
	}

	public static ComponentManager getInstance() {
		if (instance == null) {
			instance = new ComponentManager();
		}

		return instance;
	}

}

package org.ploxie.engine2.scenegraph.component;

import java.util.ArrayList;
import java.util.List;

import org.ploxie.engine2.scenegraph.component.interfaces.Renderable;
import org.ploxie.engine2.scenegraph.component.interfaces.Updatable;

import lombok.Getter;

public class ComponentManager {

	@Getter
	protected List<Renderable> renderables = new ArrayList<>();
	@Getter
	protected List<Updatable> updatables = new ArrayList<>();

	public void addComponent(Component component) {
		if(component instanceof Renderable) {
			renderables.add((Renderable) component);
		}
		if(component instanceof Updatable) {
			updatables.add((Updatable) component);
		}
	}
	
}

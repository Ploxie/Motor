package org.ploxie.engine2.scenegraph;

import java.util.ArrayList;
import java.util.List;

import org.ploxie.engine2.context.EngineContext;
import org.ploxie.engine2.scenegraph.component.Component;
import org.ploxie.engine2.scenegraph.component.ComponentManager;
import org.ploxie.engine2.scenegraph.component.interfaces.Renderable;
import org.ploxie.engine2.scenegraph.component.interfaces.Updatable;
import org.ploxie.utils.math.Transform;

import lombok.Getter;

public abstract class SceneGraph {

	private GameObject rootObject;
	private List<Updatable> updatables = new ArrayList<>();
	@Getter
	private List<Renderable> renderables = new ArrayList<>();

	public SceneGraph() {
		rootObject = new GameObject();
		rootObject.setWorldTransform(new Transform());
		updatables = new ArrayList<>();
		renderables = new ArrayList<>();
	}
	
	public abstract void initialize();

	public void render() {
		for(Renderable r : renderables) {
			r.getRenderInfo();
		}
	}
	
	public void update() {
		for(Updatable u : updatables) {
			u.update();
		}
	}

	public void shutdown() {
		//rootObject.shutdown();
	}

	public GameObject getRoot() {
		return rootObject;
	}

	public void addObject(GameObject object) {
		rootObject.setSceneGraph(this);
		rootObject.addChild(object);
		
	}
	
	protected void addComponent(Component component) {
		if(component instanceof Renderable) {
			renderables.add((Renderable) component);
		}
		if(component instanceof Updatable) {
			updatables.add((Updatable) component);
		}
		component.initialize();
	}
}

package org.ploxie.engine2.scenegraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.ploxie.engine2.context.EngineContext;
import org.ploxie.engine2.scenegraph.component.Component;
import org.ploxie.utils.math.Transform;

import lombok.Data;

@Data
public class GameObject {

	protected String id;
	protected GameObject parent;
	protected List<GameObject> children;
	protected Transform worldTransform;
	protected Transform localTransform;
	//protected List<Component> components;
	protected Map<Class<? extends Component>, List<Component>> components = new HashMap<>();
	protected SceneGraph sceneGraph;
	
	public GameObject() {
		id = UUID.randomUUID().toString();
		setWorldTransform(new Transform());
		setLocalTransform(new Transform());
		setChildren(new ArrayList<GameObject>());
	}

	public void addChild(GameObject child) {
		child.setParent(this);
		children.add(child);
				
	}
	
	public void setParent(GameObject parent) {
		this.parent = parent;
		setSceneGraph(parent.sceneGraph);
		for(List<Component> cs : components.values()) {
			for(Component c : cs) {
				sceneGraph.addComponent(c);
			}
		}
	}
	
	public void addComponent(Component component) {
		if(!components.containsKey(component.getClass())) {
			components.put(component.getClass(), new ArrayList<Component>());
		}
		components.get(component.getClass()).add(component);	
		
		if(sceneGraph != null) {
			sceneGraph.addComponent(component);
		}
		
		component.setParent(this);
	}
	
	public <T extends Component> T getComponent(Class<T> component){
		return getComponents(component).get(0);
	}
	
	public <T extends Component> List<T> getComponents(Class<T> component){
		return (List<T>) components.get(component);
	}

}

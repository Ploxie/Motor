package org.ploxie.engine2.scenegraph;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.ploxie.utils.math.Transform;

public class GameObject {

	protected String id;
	protected GameObject parent;
	protected List<GameObject> children;
	protected Transform worldTransform;
	protected Transform localTransform;
	
	
	public GameObject(){
		
		id = UUID.randomUUID().toString();
		setWorldTransform(new Transform());
		setLocalTransform(new Transform());
		setChildren(new ArrayList<GameObject>());
	}
	
	public void addChild(GameObject child)
	{
		child.setParent(this);
		children.add(child);
	}
	
	public void update()
	{
		/*getWorldTransform().setRotation(getWorldTransform().getLocalRotation().add(getParentNode().getWorldTransform().getRotation()));
		getWorldTransform().setTranslation(getWorldTransform().getLocalTranslation().add(getParentNode().getWorldTransform().getTranslation()));
		getWorldTransform().setScaling(getWorldTransform().getLocalScaling().multiply(getParentNode().getWorldTransform().getScaling()));
		
		for(GameObject child: children)
			child.update();*/
	}
	
	public void input()
	{
		for(GameObject child: children)
			child.input();
	}
	
	public void render()
	{
		for(GameObject child: children)
			child.render();
	}
	
	public void renderWireframe()
	{
		for(GameObject child: children)
			child.render();
	}
	
	public void renderShadows()
	{
		for(GameObject child: children)
			child.renderShadows();
	}
	
	/*public void record(RenderList renderList){

		for(GameObject child: children)
			child.record(renderList);
	}*/
	
	public void shutdown()
	{
		for(GameObject child: children)
			child.shutdown();
	}

	public GameObject getParentNode() {
		return parent;
	}
	
	public GameObject getParent(){		
		return parent;
	}

	public void setParent(GameObject parent) {
		this.parent = parent;
	}

	public List<GameObject> getChildren() {
		return children;
	}

	public void setChildren(List<GameObject> children) {
		this.children = children;
	}

	public Transform getWorldTransform() {
		return worldTransform;
	}

	public void setWorldTransform(Transform transform) {
		this.worldTransform = transform;
	}

	public Transform getLocalTransform() {
		return localTransform;
	}

	public void setLocalTransform(Transform localTransform) {
		this.localTransform = localTransform;
	}

	public String getId() {
		return id;
	}
}

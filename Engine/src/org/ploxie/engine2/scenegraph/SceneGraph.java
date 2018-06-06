package org.ploxie.engine2.scenegraph;

import org.ploxie.utils.math.Transform;

public class SceneGraph{

	private GameObject rootObject;

	public SceneGraph(){
		
		rootObject = new GameObject();
		rootObject.setWorldTransform(new Transform());
	}
	
	public void render(){
		
		rootObject.render();
	}
	
	
	public void renderShadows(){
		
		rootObject.renderShadows();
	}
	
	/*public void record(RenderList renderList){

		rootObject.record(renderList);
	}*/
	
	public void update(){

		rootObject.update();
	}
	
	public void input(){
		rootObject.input();
	}
	
	public void shutdown()
	{
		rootObject.shutdown();
	}

	public GameObject getRoot() {
		return rootObject;
	}
	
	public void addObject(GameObject object){
		rootObject.addChild(object);
	}

}

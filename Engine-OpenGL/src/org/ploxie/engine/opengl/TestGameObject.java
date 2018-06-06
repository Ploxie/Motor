package org.ploxie.engine.opengl;

import org.ploxie.engine.opengl.scenegraph.GLRenderInfo;
import org.ploxie.engine2.scenegraph.GameObject;

public class TestGameObject extends GameObject{

	private GLRenderInfo renderInfo;
	
	public TestGameObject(GLRenderInfo renderInfo) {
		this.renderInfo = renderInfo;
	}
	
	public void render() {
		
		renderInfo.render();
	}
	
}

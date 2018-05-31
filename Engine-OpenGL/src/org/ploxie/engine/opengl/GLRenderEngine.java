package org.ploxie.engine.opengl;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.ploxie.engine2.RenderEngine;

public class GLRenderEngine extends RenderEngine{

	@Override
	public void initialize() {
		super.initialize();
		
	}
	
	@Override
	public void render() {
		GL11.glClearColor(0.5f, 0.5f, 0.55f, 0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}

}

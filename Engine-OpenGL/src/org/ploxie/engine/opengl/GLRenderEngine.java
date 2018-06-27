package org.ploxie.engine.opengl;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.ploxie.engine.opengl.scenegraph.GLRenderInfo;
import org.ploxie.engine2.RenderEngine;
import org.ploxie.engine2.model.Mesh;
import org.ploxie.engine2.pipeline.uniformbuffers.CameraBuffer;
import org.ploxie.engine2.pipeline.uniformbuffers.TestBuffer;
import org.ploxie.engine2.pipeline.uniformbuffers.UniformBuffer;
import org.ploxie.engine2.scenegraph.component.interfaces.Renderable;
import org.ploxie.engine2.util.MeshGenerator;
import org.ploxie.opengl.pipeline.GLPipeline;
import org.ploxie.opengl.shader.GLShaderModule;
import org.ploxie.opengl.shader.GLShaderModules;

public class GLRenderEngine extends RenderEngine{
	
	@Override
	public void initialize() {
		GL.createCapabilities();				
		super.initialize();
	}
	
	@Override
	public void render() {
		GL11.glClearColor(0.5f, 0.5f, 0.55f, 0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		
		for(Renderable r : sceneGraph.getRenderables()) {
			r.getRenderInfo().record(null);
		}
		
	}

}

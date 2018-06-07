package org.ploxie.engine.opengl;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.ploxie.engine.opengl.scenegraph.GLRenderInfo;
import org.ploxie.engine2.RenderEngine;
import org.ploxie.engine2.model.Mesh;
import org.ploxie.engine2.pipeline.UniformBuffer;
import org.ploxie.engine2.util.MeshGenerator;
import org.ploxie.opengl.pipeline.GLPipeline;
import org.ploxie.opengl.shader.GLShaderModule;
import org.ploxie.opengl.shader.GLShaderModules;

public class GLRenderEngine extends RenderEngine{

	private TestGameObject object;
	
	@Override
	public void initialize() {
		super.initialize();
		GL.createCapabilities();
		
		Mesh mesh = MeshGenerator.NDCQuad2D();		
		
		GLShaderModule vertexShader = new GLShaderModule(GL20.GL_VERTEX_SHADER, "res/shader.vert");
		GLShaderModule fragmentShader = new GLShaderModule(GL20.GL_FRAGMENT_SHADER, "res/shader.frag");
		
		GLShaderModules shaderModules = GLShaderModules.builder().vertexShader(vertexShader).fragmentShader(fragmentShader).build().init();
		
		GLPipeline pipeline = new GLPipeline(shaderModules);
		pipeline.setUniformBuffer(new UniformBuffer());
		
		object = new TestGameObject(new GLRenderInfo(mesh, pipeline));
		
	}
	
	@Override
	public void render() {
		GL11.glClearColor(0.5f, 0.5f, 0.55f, 0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		object.render();	
		
	}

}

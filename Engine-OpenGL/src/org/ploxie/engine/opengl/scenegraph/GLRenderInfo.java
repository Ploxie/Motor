package org.ploxie.engine.opengl.scenegraph;

import org.ploxie.engine2.model.Mesh;
import org.ploxie.engine2.pipeline.Pipeline;
import org.ploxie.engine2.pipeline.shader.ShaderModules;
import org.ploxie.engine2.scenegraph.RenderInfo;
import org.ploxie.opengl.buffer.VBO;
import org.ploxie.opengl.pipeline.GLPipeline;
import org.ploxie.opengl.shader.GLShaderModules;

public class GLRenderInfo extends RenderInfo{

	private VBO vbo;
	private GLPipeline pipeline;
	
	public GLRenderInfo(Mesh mesh, GLPipeline pipeline) {
		super(mesh, pipeline);
		this.pipeline = pipeline;
		
		vbo = new VBO();		
		vbo.setData(mesh);	
	}
	
	public void render() {
		GLShaderModules shader = pipeline.getShaderModules();
		
		shader.bind();
		
		vbo.draw();
	}

}

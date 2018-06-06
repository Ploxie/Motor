package org.ploxie.opengl.pipeline;

import org.ploxie.engine2.pipeline.Pipeline;
import org.ploxie.engine2.pipeline.shader.ShaderModules;
import org.ploxie.opengl.shader.GLShaderModules;

public class GLPipeline extends Pipeline{

	public GLPipeline(GLShaderModules shaderModules) {
		super(shaderModules);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public GLShaderModules getShaderModules() {
		return (GLShaderModules) super.getShaderModules();
	}

}

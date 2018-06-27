package org.ploxie.opengl.pipeline;

import org.ploxie.engine2.pipeline.Pipeline;
import org.ploxie.engine2.pipeline.shader.ShaderModules;
import org.ploxie.opengl.shader.GLShaderModules;

public class GLPipeline extends Pipeline{

	public GLPipeline(Pipeline pipeline) {
		super(pipeline.getShaderModules());
		setUniformBuffers(pipeline.getUniformBuffers());
	}
	
	public GLPipeline(GLShaderModules shaderModules) {
		super(shaderModules);
	}
	
	@Override
	public GLShaderModules getShaderModules() {
		return (GLShaderModules) super.getShaderModules();
	}

}

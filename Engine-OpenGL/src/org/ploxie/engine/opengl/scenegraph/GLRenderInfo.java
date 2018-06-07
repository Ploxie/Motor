package org.ploxie.engine.opengl.scenegraph;

import java.nio.ByteBuffer;

import org.ploxie.engine.utils.BufferUtils;
import org.ploxie.engine2.model.Mesh;
import org.ploxie.engine2.pipeline.Pipeline;
import org.ploxie.engine2.pipeline.UniformBuffer;
import org.ploxie.engine2.pipeline.shader.ShaderModules;
import org.ploxie.engine2.scenegraph.RenderInfo;
import org.ploxie.opengl.buffer.UniformBufferObject;
import org.ploxie.opengl.buffer.VertexBufferObject;
import org.ploxie.opengl.pipeline.GLPipeline;
import org.ploxie.opengl.shader.GLShaderModules;
import org.ploxie.utils.math.vector.Vector3f;

public class GLRenderInfo extends RenderInfo{

	private GLPipeline pipeline;
	private VertexBufferObject vbo;
	private UniformBufferObject ubo;
	
	private UniformBuffer uniformBuffer;
	
	private GLShaderModules shader;
	
	public GLRenderInfo(Mesh mesh, GLPipeline pipeline) {
		super(mesh, pipeline);
		this.pipeline = pipeline;
		this.shader = pipeline.getShaderModules();	
		
		vbo = new VertexBufferObject();		
		vbo.setData(mesh);	
		
		ubo = new UniformBufferObject();		
		uniformBuffer = pipeline.getUniformBuffer();
		
		uniformBuffer.getMatrix().setScale(new Vector3f(0.5f, 0.5f, 1.0f));
		
		ubo.create(0, uniformBuffer.getSize());
	}
	
	public void render() {
		GLShaderModules shader = pipeline.getShaderModules();		
		shader.bind();
		
		ByteBuffer byteBuffer = BufferUtils.createByteBuffer(uniformBuffer.getSize());	
		ubo.update(uniformBuffer.fillBuffer(byteBuffer), uniformBuffer.getSize());
		
		vbo.draw();
	}

}

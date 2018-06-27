package org.ploxie.engine.opengl.scenegraph;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.ploxie.engine.utils.BufferUtils;
import org.ploxie.engine2.model.Mesh;
import org.ploxie.engine2.pipeline.Pipeline;
import org.ploxie.engine2.pipeline.uniformbuffers.UniformBuffer;
import org.ploxie.engine2.scenegraph.RenderInfo;
import org.ploxie.engine2.scenegraph.RenderList;
import org.ploxie.opengl.buffer.UniformBufferObject;
import org.ploxie.opengl.buffer.VertexBufferObject;
import org.ploxie.opengl.pipeline.GLPipeline;
import org.ploxie.opengl.shader.GLShaderModules;

public class GLRenderInfo extends RenderInfo{

	private GLPipeline pipeline;
	private VertexBufferObject vbo;
	private List<UniformBufferObject> uniformBufferObjects;
			
	public GLRenderInfo(Mesh mesh, Pipeline pipeline) {
		super(mesh, pipeline);
		this.pipeline = new GLPipeline(pipeline);
		
		vbo = new VertexBufferObject();		
		vbo.setData(mesh);	
		
		uniformBufferObjects = new ArrayList<>();
		
		for(int i = 0 ; i < pipeline.getUniformBuffers().size();i++) {
			UniformBuffer buffer = pipeline.getUniformBuffers().get(0);
			UniformBufferObject ubo = new UniformBufferObject();
			ubo.create(i, buffer.getSize());			
			uniformBufferObjects.add(ubo);
		}		
	}

	@Override
	public void record(RenderList renderList) {
		GLShaderModules shader = pipeline.getShaderModules();		
		shader.bind();	
		
		for(int i = 0 ; i < uniformBufferObjects.size();i++) {
			UniformBufferObject ubo = uniformBufferObjects.get(i);
			UniformBuffer buffer = pipeline.getUniformBuffers().get(i);
			
			ByteBuffer byteBuffer = BufferUtils.createByteBuffer(buffer.getSize());	
			ubo.update(buffer.fillBuffer(byteBuffer), buffer.getSize());
		}	
		
		vbo.draw();
	}

}

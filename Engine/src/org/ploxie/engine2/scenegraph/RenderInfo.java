package org.ploxie.engine2.scenegraph;

import org.ploxie.engine2.model.Mesh;
import org.ploxie.engine2.pipeline.Pipeline;

import lombok.Getter;

@Getter
public abstract class RenderInfo {

	private Mesh mesh;
	protected Pipeline pipeline;

	public RenderInfo(Mesh mesh, Pipeline pipeline) {
		this.mesh = mesh;
		this.pipeline = pipeline;
	}
	
	public abstract void record(RenderList renderList);
	
}

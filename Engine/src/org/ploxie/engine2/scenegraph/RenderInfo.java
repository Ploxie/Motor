package org.ploxie.engine2.scenegraph;

import org.ploxie.engine2.model.Mesh;
import org.ploxie.engine2.pipeline.Pipeline;

import lombok.Getter;

@Getter
public class RenderInfo {

	private Mesh mesh;
	private Pipeline pipeline;

	public RenderInfo(Mesh mesh, Pipeline pipeline) {
		this.mesh = mesh;
		this.pipeline = pipeline;
	}
	
}
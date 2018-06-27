package org.ploxie.engine2.context;

import org.ploxie.engine2.display.Window;
import org.ploxie.engine2.model.Mesh;
import org.ploxie.engine2.pipeline.Pipeline;
import org.ploxie.engine2.pipeline.shader.ShaderModule;
import org.ploxie.engine2.pipeline.shader.ShaderModules;
import org.ploxie.engine2.scenegraph.RenderInfo;

public abstract class GraphicsToolkit {

	public abstract RenderInfo createRenderInfo(Mesh mesh, Pipeline pipeline);
	
	public abstract ShaderModules loadShader(String name);
	
}

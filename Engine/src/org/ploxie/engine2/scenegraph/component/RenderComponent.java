package org.ploxie.engine2.scenegraph.component;

import org.ploxie.engine2.context.EngineContext;
import org.ploxie.engine2.model.Mesh;
import org.ploxie.engine2.pipeline.Pipeline;
import org.ploxie.engine2.scenegraph.RenderInfo;
import org.ploxie.engine2.scenegraph.component.interfaces.Renderable;

public class RenderComponent extends Component implements Renderable{
	
	private RenderInfo renderInfo;
	
	public RenderComponent(Mesh mesh, Pipeline pipeline) {
		renderInfo = EngineContext.getInstance().getGraphicsToolkit().createRenderInfo(mesh, pipeline);
	}
	
	@Override
	public RenderInfo getRenderInfo() {		
		return renderInfo;
	}

}

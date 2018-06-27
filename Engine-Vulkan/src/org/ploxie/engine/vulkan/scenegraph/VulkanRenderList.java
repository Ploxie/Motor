package org.ploxie.engine.vulkan.scenegraph;

import java.util.ArrayList;
import java.util.List;

import org.ploxie.engine2.scenegraph.RenderList;
import org.ploxie.engine2.scenegraph.SceneGraph;
import org.ploxie.engine2.scenegraph.component.RenderComponent;
import org.ploxie.engine2.scenegraph.component.interfaces.Renderable;
import org.ploxie.vulkan.buffer.VulkanCommandBuffer;

import lombok.Data;
import lombok.Getter;

public class VulkanRenderList extends RenderList{

	@Getter
	private List<VulkanCommandBuffer> commandBuffers = new ArrayList<>();
	
	public void clear() {
		commandBuffers.clear();
	}
	
	public void record(SceneGraph sceneGraph) {
		for(Renderable r : sceneGraph.getRenderables()) {
			if(r instanceof RenderComponent) {
				((RenderComponent)r).getRenderInfo().record(this);
			}
		}
	}
	
	public void record(VulkanCommandBuffer commandBuffer) {
		commandBuffers.add(commandBuffer);
	}
	
}

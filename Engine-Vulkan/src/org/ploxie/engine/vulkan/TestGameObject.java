package org.ploxie.engine.vulkan;

import org.ploxie.engine.vulkan.context.VulkanContext;
import org.ploxie.engine.vulkan.scenegraph.VulkanRenderInfo;
import org.ploxie.engine2.scenegraph.GameObject;
import org.ploxie.vulkan.buffer.VulkanCommandBuffer;

public class TestGameObject extends GameObject{

	private VulkanRenderInfo renderInfo;
	
	public TestGameObject(VulkanRenderInfo renderInfo) {
		this.renderInfo = renderInfo;
	}
	
	public VulkanCommandBuffer record() {
		return renderInfo.record();
	}
	
}

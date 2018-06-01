package org.ploxie.engine.vulkan;

import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.vulkan.VK10.VK_FORMAT_D32_SFLOAT;
import static org.lwjgl.vulkan.VK10.VK_FORMAT_R32_UINT;
import static org.lwjgl.vulkan.VK10.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT;

import java.nio.IntBuffer;

import org.ploxie.engine.vulkan.context.VulkanContext;
import org.ploxie.engine.vulkan.display.VulkanWindow;
import org.ploxie.engine2.RenderEngine;
import org.ploxie.utils.Color;
import org.ploxie.utils.math.vector.Vector3f;
import org.ploxie.vulkan.buffer.VulkanCommandBuffer;
import org.ploxie.vulkan.command.VulkanCommandPool;
import org.ploxie.vulkan.command.VulkanSubmitInfo;
import org.ploxie.vulkan.device.VulkanLogicalDevice;
import org.ploxie.vulkan.math.VulkanExtent2D;
import org.ploxie.vulkan.math.VulkanOffset2D;
import org.ploxie.vulkan.math.VulkanRect2D;
import org.ploxie.vulkan.queue.VulkanQueue;
import org.ploxie.vulkan.render.VulkanRenderPass;
import org.ploxie.vulkan.viewport.VulkanViewportProperties;

public class VulkanRenderEngine extends RenderEngine {

	private VulkanQueue graphicsQueue;
	private VulkanSubmitInfo submitInfo;
	private VulkanCommandBuffer primaryCommandBuffer;
	private VulkanWindow window;
	private VulkanRect2D renderArea;	
	private VulkanRenderPass renderPass;
	
	private TestObject object;
	private TestObject object2;
	
	@Override
	public void initialize() {
		super.initialize();
		
		object = new TestObject();		
		object2 = new TestObject();		
		VulkanLogicalDevice logicalDevice = VulkanContext.getLogicalDevice();
		window = VulkanContext.getWindow();

		int graphicsFamilyIndex = logicalDevice.getPhysicalDevice().getQueueFamilyProperties().getFirstGraphicsQueue().getIndex();
		graphicsQueue = logicalDevice.getDeviceQueue(graphicsFamilyIndex, 0);
		
		IntBuffer pWaitDstStageMask = memAllocInt(1);
		pWaitDstStageMask.put(0, VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT);
				
		submitInfo = new VulkanSubmitInfo();
		submitInfo.setWaitDstStageMask(pWaitDstStageMask);				
		
		renderPass = logicalDevice.createRenderPass(window.getSwapchain().getImageFormat().getColorFormat(), VK_FORMAT_D32_SFLOAT);
		renderArea = new VulkanRect2D(new VulkanOffset2D(0,0), window.getExtent());
		
		VulkanCommandPool pool = logicalDevice.getCommandPool(graphicsFamilyIndex);
			
		primaryCommandBuffer = logicalDevice.createCommandBuffer(pool, true);	
		
		object.setPosition(new Vector3f(0.5f, 0, 0));
		object2.setPosition(new Vector3f(-0.5f, 0, 0));
	}	
	
	@Override
	public void render() {
		
		primaryCommandBuffer.reset();
		primaryCommandBuffer.begin();			
		primaryCommandBuffer.beginRenderPass(renderPass, window.getCurrentFrameBuffer(), false, renderArea, new Color(0.47f, 0.47f, 0.47f, 1.0f));		
		primaryCommandBuffer.execute(object.record(), object2.record());
		primaryCommandBuffer.endRenderPass();	
		primaryCommandBuffer.end();
		
		submitInfo.setCommandBuffer(primaryCommandBuffer);		
		graphicsQueue.submit(submitInfo);		
	}
	
	
}

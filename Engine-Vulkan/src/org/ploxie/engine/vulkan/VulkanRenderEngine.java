package org.ploxie.engine.vulkan;

import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.vulkan.VK10.VK_FORMAT_D32_SFLOAT;
import static org.lwjgl.vulkan.VK10.VK_FORMAT_R32_UINT;
import static org.lwjgl.vulkan.VK10.VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.ploxie.engine.vulkan.context.VulkanContext;
import org.ploxie.engine.vulkan.display.VulkanWindow;
import org.ploxie.engine.vulkan.scenegraph.VulkanRenderInfo;
import org.ploxie.engine2.RenderEngine;
import org.ploxie.engine2.model.Mesh;
import org.ploxie.engine2.util.BufferUtils;
import org.ploxie.engine2.util.MeshGenerator;
import org.ploxie.utils.Color;
import org.ploxie.utils.FileUtils;
import org.ploxie.utils.math.vector.Vector3f;
import org.ploxie.vulkan.buffer.VulkanCommandBuffer;
import org.ploxie.vulkan.command.VulkanCommandPool;
import org.ploxie.vulkan.command.VulkanSubmitInfo;
import org.ploxie.vulkan.device.VulkanLogicalDevice;
import org.ploxie.vulkan.math.VulkanExtent2D;
import org.ploxie.vulkan.math.VulkanOffset2D;
import org.ploxie.vulkan.math.VulkanRect2D;
import org.ploxie.vulkan.pipeline.VulkanGraphicsPipelineProperties;
import org.ploxie.vulkan.queue.VulkanQueue;
import org.ploxie.vulkan.render.VulkanRenderPass;
import org.ploxie.vulkan.shader.VulkanShaderModule;
import org.ploxie.vulkan.shader.VulkanShaderModules;
import org.ploxie.vulkan.viewport.VulkanViewportProperties;

public class VulkanRenderEngine extends RenderEngine {

	private VulkanQueue graphicsQueue;
	private VulkanSubmitInfo submitInfo;
	private VulkanCommandBuffer primaryCommandBuffer;
	private VulkanWindow window;
	private VulkanRect2D renderArea;	
	private VulkanRenderPass renderPass;
	
	private TestGameObject object;
	
	@Override
	public void initialize() {
		super.initialize();
		VulkanLogicalDevice logicalDevice = VulkanContext.getLogicalDevice();
		
		Mesh mesh = MeshGenerator.NDCQuad2D();
		
		ByteBuffer triangleVertCode = null;
		ByteBuffer triangleFragCode = null;
		try {
			triangleVertCode = BufferUtils.wrap(FileUtils.getFileToBytes("res/vert.spv"));
			triangleFragCode = BufferUtils.wrap(FileUtils.getFileToBytes("res/frag.spv"));
		} catch (IOException e) {
			e.printStackTrace();
		}		
				
		VulkanShaderModule triangleVertShader = logicalDevice.loadShader(triangleVertCode);
		VulkanShaderModule triangleFragShader = logicalDevice.loadShader(triangleFragCode);
		
		VulkanShaderModules shaderModules = VulkanShaderModules.builder().vertex(triangleVertShader).fragment(triangleFragShader).build();		
		VulkanGraphicsPipelineProperties pipeline = new VulkanGraphicsPipelineProperties(shaderModules);
		
		object = new TestGameObject(new VulkanRenderInfo(mesh, pipeline));		
		
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
		
		//object.setPosition(new Vector3f(0.5f, 0, 0));
	}	
	
	@Override
	public void render() {
		
		primaryCommandBuffer.reset();
		primaryCommandBuffer.begin();			
		primaryCommandBuffer.beginRenderPass(renderPass, window.getCurrentFrameBuffer(), false, renderArea, new Color(0.47f, 0.47f, 0.47f, 1.0f));		
		primaryCommandBuffer.execute(object.record());
		primaryCommandBuffer.endRenderPass();	
		primaryCommandBuffer.end();
		
		submitInfo.setCommandBuffer(primaryCommandBuffer);		
		graphicsQueue.submit(submitInfo);		
	}
	
	
}

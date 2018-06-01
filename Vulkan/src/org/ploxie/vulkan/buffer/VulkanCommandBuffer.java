package org.ploxie.vulkan.buffer;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.CallbackI.V;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK;
import org.lwjgl.vulkan.VkBufferCopy;
import org.lwjgl.vulkan.VkClearValue;
import org.lwjgl.vulkan.VkCommandBuffer;
import org.lwjgl.vulkan.VkCommandBufferBeginInfo;
import org.lwjgl.vulkan.VkCommandBufferInheritanceInfo;
import org.lwjgl.vulkan.VkExtent2D;
import org.lwjgl.vulkan.VkImageMemoryBarrier;
import org.lwjgl.vulkan.VkOffset2D;
import org.lwjgl.vulkan.VkRect2D;
import org.lwjgl.vulkan.VkRenderPassBeginInfo;
import org.lwjgl.vulkan.VkViewport;
import org.ploxie.utils.Color;
import org.ploxie.vulkan.command.VulkanCommandPool;
import org.ploxie.vulkan.descriptor.VulkanDescriptorSet;
import org.ploxie.vulkan.device.VulkanLogicalDevice;
import org.ploxie.vulkan.image.VulkanImage;
import org.ploxie.vulkan.image.VulkanImageLayout;
import org.ploxie.vulkan.math.VulkanRect2D;
import org.ploxie.vulkan.pipeline.VulkanGraphicsPipeline;
import org.ploxie.vulkan.pipeline.VulkanGraphicsPipelineLayout;
import org.ploxie.vulkan.render.VulkanRenderPass;
import org.ploxie.vulkan.render.VulkanSubpass;
import org.ploxie.vulkan.utils.VKUtil;
import org.ploxie.vulkan.viewport.VulkanViewportProperties;

import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.vulkan.VK10.*;

import java.nio.LongBuffer;

import lombok.Data;

@Data
public class VulkanCommandBuffer {

	private final VulkanLogicalDevice device;
	private final VkCommandBuffer internal;
	private final VulkanCommandPool pool;
	private final PointerBuffer internalHandle;
	
	public void free() {
		device.cleanup(pool, this);
		
	}
	
	public void begin() {
		begin(false);
	}
	
	public void begin(boolean simultaneous) {
		try(MemoryStack stack = MemoryStack.stackPush()){
			VkCommandBufferBeginInfo beginInfo = VkCommandBufferBeginInfo
					.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO)
					.flags(simultaneous ? VK_COMMAND_BUFFER_USAGE_SIMULTANEOUS_USE_BIT  : VK_COMMAND_BUFFER_USAGE_ONE_TIME_SUBMIT_BIT)
					.pInheritanceInfo(null);
			
			int err = vkBeginCommandBuffer(internal, beginInfo);
			
			if (err != VK_SUCCESS) {
				throw new AssertionError("Failed to begin render command buffer: "+ VKUtil.translateVulkanResult(err));
			}
		}
	}
	
	public void beginSecondary(VulkanRenderPass renderPass, VulkanSubpass subpass, VulkanFrameBuffer framebuffer, boolean simultaneous) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			VkCommandBufferInheritanceInfo inheritanceInfo = VkCommandBufferInheritanceInfo
					.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_COMMAND_BUFFER_INHERITANCE_INFO)
					.pNext(NULL)
					.renderPass(renderPass.getHandle())
					.subpass(subpass.getIndex())
					.framebuffer(framebuffer.getHandle());
			
			
			//todo statistics : https://www.khronos.org/registry/vulkan/specs/1.1-extensions/man/html/VkQueryPipelineStatisticFlagBits.html
			
			VkCommandBufferBeginInfo beginInfo = VkCommandBufferBeginInfo
					.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO)
					.flags(VK_COMMAND_BUFFER_USAGE_RENDER_PASS_CONTINUE_BIT)
					.pInheritanceInfo(inheritanceInfo);
	
			int err = vkBeginCommandBuffer(internal, beginInfo);
			
			if (err != VK_SUCCESS) {
				throw new AssertionError(
						"Failed to begin render command buffer: " + VKUtil.translateVulkanResult(err));
			}
		}
	}
	
	public void end() {
		int err = vkEndCommandBuffer(internal);
		if(err != VK_SUCCESS) {
			throw new AssertionError("Failed to end render command buffer: "+VKUtil.translateVulkanResult(err));
		}
	}
	
	public void reset(){		
		int err = vkResetCommandBuffer(internal, VK_COMMAND_BUFFER_RESET_RELEASE_RESOURCES_BIT);
		if(err != VK_SUCCESS) {
			throw new AssertionError("Failed to reset command buffer: "+VKUtil.translateVulkanResult(err));
		}
	}
	
	public void copy(VulkanBuffer src, VulkanBuffer dest, long size) {
		copy(src, dest, 0, 0, size);
	}
	
	public void copy(VulkanBuffer src, VulkanBuffer dest,long srcOffset, long destOffset, long size) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			VkBufferCopy.Buffer copyRegion = VkBufferCopy.callocStack(1, stack)
					.srcOffset(srcOffset)
					.dstOffset(destOffset)
					.size(size);
			
			vkCmdCopyBuffer(internal, src.getHandle(), dest.getHandle(), copyRegion);
		}
	}
	
	public void beginRenderPass(VulkanRenderPass renderPass, VulkanFrameBuffer frameBuffer,boolean inlineCommands, VulkanRect2D renderArea, Color clearColor) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			VkClearValue.Buffer clearValues = VkClearValue.callocStack(2, stack);
			
			clearValues
				.get(0)
				.color()
				.float32(0, clearColor.getR())
				.float32(1, clearColor.getG())
				.float32(2, clearColor.getB())
				.float32(3, clearColor.getA());
			
			clearValues
				.get(1)
				.depthStencil()
				.depth(1)
				.stencil(0);
			
			VkRect2D rect2D = VkRect2D.callocStack(stack);
			VkOffset2D offset2D = VkOffset2D.callocStack(stack);
			VkExtent2D extent2D = VkExtent2D.callocStack(stack);
			
			offset2D.set(renderArea.getOffset().getX(), renderArea.getOffset().getY());
			extent2D.set(renderArea.getExtent().getWidth(), renderArea.getExtent().getHeight());
			
			rect2D.offset(offset2D);
			rect2D.extent(extent2D);
			
			VkRenderPassBeginInfo renderPassBeginInfo = VkRenderPassBeginInfo
						.callocStack(stack)
						.sType(VK_STRUCTURE_TYPE_RENDER_PASS_BEGIN_INFO)
						.pNext(NULL)
						.renderPass(renderPass.getHandle())
						.pClearValues(clearValues)
						.framebuffer(frameBuffer.getHandle())
						.renderArea(rect2D);
			
			vkCmdBeginRenderPass(internal, renderPassBeginInfo, inlineCommands ? VK_SUBPASS_CONTENTS_INLINE : VK_SUBPASS_CONTENTS_SECONDARY_COMMAND_BUFFERS);
						
		}
	}
	
	public void endRenderPass() {
		vkCmdEndRenderPass(internal);
	}
	
	public void setViewport(VulkanViewportProperties viewport) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			VkViewport.Buffer viewportInternal = VkViewport
					.callocStack(1, stack)
					.x(viewport.getOffset().getX())
					.y(viewport.getOffset().getY())
					.width(viewport.getDimensions().getWidth())
					.height(viewport.getDimensions().getHeight())
					.minDepth(viewport.getMinDepth())
					.maxDepth(viewport.getMaxDepth());
			
			vkCmdSetViewport(internal, 0, viewportInternal);
		}
	}
	
	public void setScissor(VulkanRect2D scissor) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			VkRect2D.Buffer rect2D = VkRect2D.callocStack(1, stack);
			VkOffset2D offset2D = VkOffset2D.callocStack(stack);
			VkExtent2D extent2D = VkExtent2D.callocStack(stack);
			
			offset2D.set(scissor.getOffset().getX(), scissor.getOffset().getY());
			extent2D.set(scissor.getExtent().getWidth(), scissor.getExtent().getHeight());
			
			rect2D.offset(offset2D);
			rect2D.extent(extent2D);
			
			vkCmdSetScissor(internal, 0 , rect2D);
		}
	}
	
	public void bindPipeline(VulkanGraphicsPipeline graphicsPipeline) {
		vkCmdBindPipeline(internal, VK_PIPELINE_BIND_POINT_GRAPHICS, graphicsPipeline.getHandle());
	}
	
	public void bindVertexBuffers(VulkanBuffer vertexBuffer) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			LongBuffer pBuffers = stack.mallocLong(1);
			pBuffers.put(0, vertexBuffer.getHandle());
			LongBuffer pOffsets = stack.mallocLong(1);
			pOffsets.put(0,0);
			vkCmdBindVertexBuffers(internal, 0, pBuffers, pOffsets);
		}
	}
	
	public void bindIndexBuffer(VulkanBuffer indexBuffer, int format) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			int indexType;
			if (format == VK_FORMAT_R16_UINT) {
				indexType = VK_INDEX_TYPE_UINT16;
			} else if (format == VK_FORMAT_R32_UINT) {
				indexType = VK_INDEX_TYPE_UINT32;
			} else {
				throw new AssertionError("Invalid index buffer format: "+ format + ", supported: VK_INDEX_TYPE_UINT16 , VK_INDEX_TYPE_UINT32");
			}
			
			
			vkCmdBindIndexBuffer(internal, indexBuffer.getHandle(), 0, indexType);
		}
	}
	
	public void bindDescriptorSets(VulkanGraphicsPipelineLayout layout, VulkanDescriptorSet... descriptorSets) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			LongBuffer pDescriptorSets = stack.mallocLong(descriptorSets.length);
			for(VulkanDescriptorSet descriptorSet : descriptorSets) {
				pDescriptorSets.put(descriptorSet.getHandle());
			}
			pDescriptorSets.flip();
			vkCmdBindDescriptorSets(internal, VK_PIPELINE_BIND_POINT_GRAPHICS, layout.getHandle(), 0, pDescriptorSets, null);
		}
	}
	
	public void draw(int vertexCount, int instanceCount, int firstVertex, int firstInstance) {
		vkCmdDraw(internal, vertexCount, instanceCount, firstVertex, firstInstance);
	}
	
	public void drawIndexed(int indexCount, int instanceCount, int firstIndex, int vertexOffset, int firstInstance) {
		vkCmdDrawIndexed(internal, indexCount, instanceCount, firstIndex, vertexOffset, firstInstance);
	}

	public void setImageLayout(VulkanImage image, VulkanImageLayout oldLayout, VulkanImageLayout newLayout) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			int sourceStage;
			int destinationStage;
			
			int aspectMask = VK_IMAGE_ASPECT_COLOR_BIT;
			
			VkImageMemoryBarrier.Buffer barrier = VkImageMemoryBarrier
					.callocStack(1, stack)
					.sType(VK_STRUCTURE_TYPE_IMAGE_MEMORY_BARRIER)
					.oldLayout(oldLayout.getBitMask())
					.newLayout(newLayout.getBitMask())
					.srcQueueFamilyIndex(VK_QUEUE_FAMILY_IGNORED)
					.dstQueueFamilyIndex(VK_QUEUE_FAMILY_IGNORED)
					.image(image.getHandle());
			
			if (oldLayout == VulkanImageLayout.UNDEFINED && newLayout == VulkanImageLayout.DEPTH_STENCIL_ATTACHMENT_OPTIMAL) {
				aspectMask = VK_IMAGE_ASPECT_DEPTH_BIT;
				//if has stencil, also add stencil aspectmask

				barrier.srcAccessMask(0);
				barrier.dstAccessMask(VK_ACCESS_DEPTH_STENCIL_ATTACHMENT_READ_BIT | VK_ACCESS_DEPTH_STENCIL_ATTACHMENT_WRITE_BIT);
				
				sourceStage = VK_PIPELINE_STAGE_TOP_OF_PIPE_BIT;
				destinationStage = VK_PIPELINE_STAGE_EARLY_FRAGMENT_TESTS_BIT;
			} else if (oldLayout == VulkanImageLayout.UNDEFINED && newLayout == VulkanImageLayout.TRANSFER_DST_OPTIMAL) {
				barrier.srcAccessMask(0);
				barrier.dstAccessMask(VK_ACCESS_TRANSFER_WRITE_BIT);
				
				sourceStage = VK_PIPELINE_STAGE_TOP_OF_PIPE_BIT;
				destinationStage = VK_PIPELINE_STAGE_TRANSFER_BIT;
			} else if(oldLayout == VulkanImageLayout.TRANSFER_DST_OPTIMAL && newLayout == VulkanImageLayout.SHADER_READ_ONLY_OPTIMAL) {
				barrier.srcAccessMask(VK_ACCESS_TRANSFER_WRITE_BIT);
				barrier.dstAccessMask(VK_ACCESS_SHADER_READ_BIT);
				
				sourceStage = VK_PIPELINE_STAGE_TRANSFER_BIT;
				destinationStage = VK_PIPELINE_STAGE_FRAGMENT_SHADER_BIT;
			} else {
				throw new AssertionError("Unsupported layout transition: " + oldLayout + " -> " + newLayout);
			}
			
			barrier.subresourceRange()
				.aspectMask(aspectMask)
				.baseMipLevel(0)
				.levelCount(1)
				.baseArrayLayer(0)
				.layerCount(1);
			
			vkCmdPipelineBarrier(internal, sourceStage, destinationStage, 0, null, null, barrier);
		}		
	}
	
	public void execute(VulkanCommandBuffer secondaryCommandBuffer) {
		vkCmdExecuteCommands(internal, secondaryCommandBuffer.getInternal());
	}

	public void execute(VulkanCommandBuffer... secondaryCommandBuffers) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			PointerBuffer pointerBuffer = stack.callocPointer(secondaryCommandBuffers.length);
			for (VulkanCommandBuffer buffer : secondaryCommandBuffers) {
				pointerBuffer.put(buffer.getInternal());
			}
			pointerBuffer.clear();
			vkCmdExecuteCommands(internal, pointerBuffer);
		}
	}
	
	
}

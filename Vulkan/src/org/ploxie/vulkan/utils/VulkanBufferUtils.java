package org.ploxie.vulkan.utils;

import java.nio.ByteBuffer;

import org.ploxie.vulkan.buffer.VulkanBuffer;
import org.ploxie.vulkan.buffer.VulkanBufferUsageFlag;
import org.ploxie.vulkan.buffer.VulkanCommandBuffer;
import org.ploxie.vulkan.command.VulkanCommandPool;
import org.ploxie.vulkan.device.VulkanLogicalDevice;
import org.ploxie.vulkan.memory.VulkanMemoryAllocation;
import org.ploxie.vulkan.memory.VulkanMemoryPropertyFlag;
import org.ploxie.vulkan.queue.VulkanQueue;

public class VulkanBufferUtils {

	public static VulkanBuffer createDeviceLocalBuffer(VulkanLogicalDevice logicalDevice, ByteBuffer byteBuffer, VulkanQueue queue, VulkanCommandPool commandPool, VulkanBufferUsageFlag usageFlag){

		VulkanBuffer stagingBuffer = logicalDevice.createBuffer(byteBuffer.remaining(), true, VulkanBufferUsageFlag.TRANSFER_SRC);
		VulkanMemoryAllocation stagingMemoryAllocation = logicalDevice.allocateMemory(stagingBuffer, VulkanMemoryPropertyFlag.HOST_VISIBLE, VulkanMemoryPropertyFlag.HOST_COHERENT);
		logicalDevice.fillBuffer(stagingMemoryAllocation, stagingBuffer, byteBuffer);
		
		VulkanBuffer buffer = logicalDevice.createBuffer(byteBuffer.remaining(), true, VulkanBufferUsageFlag.TRANSFER_DST, usageFlag);
		logicalDevice.allocateMemory(buffer,  VulkanMemoryPropertyFlag.DEVICE_LOCAL);
		VulkanCommandBuffer stageCommandBuffer = logicalDevice.copyBuffer(commandPool, stagingBuffer, buffer, byteBuffer.remaining());
		queue.submit(stageCommandBuffer);
		
		return buffer;
	}
	
}

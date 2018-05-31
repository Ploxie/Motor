package org.ploxie.vulkan.descriptor;

import static org.lwjgl.vulkan.VK10.VK_DESCRIPTOR_TYPE_UNIFORM_BUFFER;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_WRITE_DESCRIPTOR_SET;
import static org.lwjgl.vulkan.VK10.vkUpdateDescriptorSets;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkDescriptorBufferInfo;
import org.lwjgl.vulkan.VkWriteDescriptorSet;

import lombok.Data;

@Data
public class VulkanDescriptorSet {

	private final long handle;
	
/*	public void update(long buffer) {
		try(MemoryStack stack = MemoryStack.stackPush()){
			VkDescriptorBufferInfo.Buffer bufferInfo = VkDescriptorBufferInfo
					.callocStack(1, stack)
					.buffer(uniformBufferDescriptor.getBuffer().getHandle())
					.offset(0)
					.range(uniformBufferSize);
		
			VkWriteDescriptorSet.Buffer descriptorWrite = VkWriteDescriptorSet.callocStack(1, stack);
			descriptorWrite.get(0)
				.sType(VK_STRUCTURE_TYPE_WRITE_DESCRIPTOR_SET)
				.dstSet(descriptorSet.getHandle())
				.dstBinding(0)
				.dstArrayElement(0)
				.descriptorType(VK_DESCRIPTOR_TYPE_UNIFORM_BUFFER)
				.pBufferInfo(bufferInfo)
				.pImageInfo(null)
				.pTexelBufferView(null);
		
			vkUpdateDescriptorSets(device.getInternal(), descriptorWrite, null);
		}
	}*/
	
}

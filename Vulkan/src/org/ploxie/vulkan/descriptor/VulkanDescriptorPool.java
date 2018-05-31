package org.ploxie.vulkan.descriptor;

import java.nio.LongBuffer;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkDescriptorSetAllocateInfo;
import org.ploxie.vulkan.device.VulkanLogicalDevice;
import org.ploxie.vulkan.utils.VKUtil;

import static org.lwjgl.vulkan.VK10.*;

import lombok.Data;

@Data
public class VulkanDescriptorPool {

	private final VulkanLogicalDevice device;
	private final long handle;
	
	public VulkanDescriptorSet allocateDescriptorSet(VulkanDescriptorLayout layout) {
		return allocateDescriptorSets(layout)[0];
	}
	
	public VulkanDescriptorSet[] allocateDescriptorSets(VulkanDescriptorLayout...descriptorLayouts) {
		try(MemoryStack stack = MemoryStack.stackPush()){
			LongBuffer layoutHandles = stack.mallocLong(descriptorLayouts.length);
			
			for(VulkanDescriptorLayout layout : descriptorLayouts) {
				layoutHandles.put(layout.getHandle());
			}
			
			layoutHandles.flip();
			
			VkDescriptorSetAllocateInfo allocateInfo = VkDescriptorSetAllocateInfo
					.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_DESCRIPTOR_SET_ALLOCATE_INFO)
					.descriptorPool(handle)
					.pSetLayouts(layoutHandles);
			
			LongBuffer pDescriptorSets = stack.mallocLong(descriptorLayouts.length);
			int err = vkAllocateDescriptorSets(device.getInternal(), allocateInfo, pDescriptorSets);
			if(err != VK_SUCCESS) {
				throw new AssertionError("Failed to allocate descriptor sets: "+VKUtil.translateVulkanResult(err));
			}
			
			VulkanDescriptorSet[] descriptorSets = new VulkanDescriptorSet[descriptorLayouts.length];
			for(int i = 0 ; i < descriptorLayouts.length;i++) {
				descriptorSets[i] = new VulkanDescriptorSet(pDescriptorSets.get(i));
			}
			
			return descriptorSets;
		}
	}
	
}

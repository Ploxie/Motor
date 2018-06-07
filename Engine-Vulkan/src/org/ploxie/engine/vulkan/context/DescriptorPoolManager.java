package org.ploxie.engine.vulkan.context;

import java.util.HashMap;

import org.ploxie.vulkan.descriptor.VulkanDescriptorPool;

public class DescriptorPoolManager {

	public enum DescriptorPoolType{
		PRIMARY
	}
	
	private HashMap<DescriptorPoolType, VulkanDescriptorPool> descriptorPools;

	protected DescriptorPoolManager() {		
		descriptorPools = new HashMap<DescriptorPoolType, VulkanDescriptorPool>();
	}
	
	public void addDescriptorPool(DescriptorPoolType key, VulkanDescriptorPool descriptorPool){		
		descriptorPools.put(key, descriptorPool);
	}
	
	public VulkanDescriptorPool getDescriptorPool(DescriptorPoolType key){		
		return descriptorPools.get(key);
	}
	
}

package org.ploxie.engine.vulkan.pipeline.shader;

import java.nio.ByteBuffer;

import org.ploxie.utils.math.matrix.Matrix4f;
import org.ploxie.vulkan.descriptor.VulkanDescriptorLayout;

public class VulkanCameraUniformBuffer extends VulkanShaderUniformBuffer{

	private Matrix4f mvp;	
	
	public VulkanCameraUniformBuffer(int binding, VulkanDescriptorLayout descriptorLayout) {
		super(binding, descriptorLayout, UniformBufferType.MATRIX4);
	}

	@Override
	protected void fillData(ByteBuffer buffer) {
		mvp.fillBuffer(buffer);
	}
	
	public void setMVP(Matrix4f data) {
		this.mvp = data;
	}

}

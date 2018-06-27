package org.ploxie.engine.vulkan.context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

import org.ploxie.engine.vulkan.scenegraph.VulkanRenderInfo;
import org.ploxie.engine2.context.GraphicsToolkit;
import org.ploxie.engine2.model.Mesh;
import org.ploxie.engine2.pipeline.Pipeline;
import org.ploxie.engine2.pipeline.shader.ShaderModules;
import org.ploxie.engine2.scenegraph.RenderInfo;
import org.ploxie.engine2.util.BufferUtils;
import org.ploxie.utils.FileUtils;
import org.ploxie.vulkan.device.VulkanLogicalDevice;
import org.ploxie.vulkan.shader.VulkanShaderModule;
import org.ploxie.vulkan.shader.VulkanShaderModules;

public class VulkanToolkit extends GraphicsToolkit {

	@Override
	public RenderInfo createRenderInfo(Mesh mesh, Pipeline pipeline) {
		return new VulkanRenderInfo(mesh, pipeline);
	}

	@Override
	public ShaderModules loadShader(String name) {
		File folder = FileUtils.getFile("res");
		File[] listOfFiles = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File name) {
				return name.getName().contains(name.getName());
			}
		});

		try {

			ByteBuffer vert = null;
			ByteBuffer frag = null;

			for (int i = 0; i < listOfFiles.length; i++) {
				File file = listOfFiles[i];
				if (file.getName().endsWith(".vert.spv")) {
					vert = BufferUtils.wrap(FileUtils.getFileToBytes("res/" + file.getName()));
				}else if (file.getName().endsWith(".frag.spv")) {
					frag = BufferUtils.wrap(FileUtils.getFileToBytes("res/" + file.getName()));
				}
			}
			
			VulkanLogicalDevice logicalDevice = VulkanContext.getInstance().getLogicalDevice();
			VulkanShaderModule vertShader = logicalDevice.loadShader(vert);
			VulkanShaderModule fragShader = logicalDevice.loadShader(frag);
			
			VulkanShaderModules shaderModules = VulkanShaderModules.builder().vertex(vertShader).fragment(fragShader).build();	
			return shaderModules;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}

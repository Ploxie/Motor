package org.ploxie.vulkan;

import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_APPLICATION_INFO;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.VK_SUCCESS;
import static org.lwjgl.vulkan.VK10.vkCreateInstance;
import static org.lwjgl.glfw.GLFWVulkan.glfwGetRequiredInstanceExtensions;
import static org.lwjgl.vulkan.EXTDebugReport.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkApplicationInfo;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkInstanceCreateInfo;
import org.ploxie.vulkan.utils.BufferUtils;
import org.ploxie.vulkan.utils.VKUtil;

import lombok.Getter;

public class Vulkan {

	@Getter
	private static final boolean validation = Boolean.parseBoolean(System.getProperty("vulkan.validation", "true"));
	
	public static VulkanInstanceProperties createInstanceProperties(VulkanApplicationInfo vulkanApplicationInfo) {
		List<String> extensions = new ArrayList<>();
		PointerBuffer requiredExtensions = glfwGetRequiredInstanceExtensions();
		if (requiredExtensions == null) {
			throw new AssertionError("Failed to find list of required Vulkan extensions");
		}
		while (requiredExtensions.hasRemaining()) {
			extensions.add(requiredExtensions.getStringASCII());
		}
		
		if (validation) {
			extensions.add(VK_EXT_DEBUG_REPORT_EXTENSION_NAME);
		}

		List<String> layers = new ArrayList<>();
		if (validation) {
			layers.add("VK_LAYER_LUNARG_standard_validation");
		}
		
		VulkanInstanceProperties vulkanInstanceProperties = new VulkanInstanceProperties();
		vulkanInstanceProperties.setApplicationInfo(vulkanApplicationInfo);
		vulkanInstanceProperties.setExtensions(extensions);
		vulkanInstanceProperties.setLayers(layers);
		
		return vulkanInstanceProperties;
	}
	
	public static VulkanInstance createInstance(VulkanInstanceProperties properties) {
		
		try (MemoryStack stack = stackPush()) {
			
			VulkanApplicationInfo applicationInfo = properties.getApplicationInfo();
			ByteBuffer appName = BufferUtils.wrap(applicationInfo.getApplicationName() + "\0", stack);
			ByteBuffer engineName = BufferUtils.wrap(applicationInfo.getEngineName() + "\0", stack);
			
			List<String> extensions = properties.getExtensions();
			PointerBuffer extensionsBuffer = stack.mallocPointer(extensions.size());
			for(String extension : extensions) {
				extensionsBuffer.put(BufferUtils.wrap(extension + "\0", stack));
			}
			extensionsBuffer.flip();
			
			List<String> layers = properties.getLayers();
			PointerBuffer layersBuffer = stack.mallocPointer(layers.size());
			for(String layer : layers) {
				layersBuffer.put(BufferUtils.wrap(layer + "\0", stack));
			}
			layersBuffer.flip();
			
			VkApplicationInfo info = VkApplicationInfo.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_APPLICATION_INFO)
					.pNext(NULL)
					.pApplicationName(appName)
					.pEngineName(engineName)
					.engineVersion(applicationInfo.getEngineVersion())
					.apiVersion(applicationInfo.getApiVersion().getVersion());
			
			VkInstanceCreateInfo instanceCreateInfo = VkInstanceCreateInfo
					.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO)
					.pNext(NULL)
					.flags(0)
					.pApplicationInfo(info)
					.ppEnabledExtensionNames(extensionsBuffer)
					.ppEnabledLayerNames(layersBuffer);
			
			PointerBuffer pInstance = stack.mallocPointer(1);
			int err = vkCreateInstance(instanceCreateInfo, null, pInstance);

			if (err != VK_SUCCESS) {
				throw new AssertionError("Failed to create VkInstance: "
						+ VKUtil.translateVulkanResult(err));
			}

			long handle = pInstance.get(0);
			VkInstance instance = new VkInstance(handle, instanceCreateInfo);
			return new VulkanInstance(instance, properties);
		}		
	}
	
}

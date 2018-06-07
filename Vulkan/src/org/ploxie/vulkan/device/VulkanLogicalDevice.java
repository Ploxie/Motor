package org.ploxie.vulkan.device;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkAttachmentDescription;
import org.lwjgl.vulkan.VkAttachmentReference;
import org.lwjgl.vulkan.VkBufferCreateInfo;
import org.lwjgl.vulkan.VkCommandBuffer;
import org.lwjgl.vulkan.VkCommandBufferAllocateInfo;
import org.lwjgl.vulkan.VkCommandPoolCreateInfo;
import org.lwjgl.vulkan.VkDescriptorBufferInfo;
import org.lwjgl.vulkan.VkDescriptorPoolCreateInfo;
import org.lwjgl.vulkan.VkDescriptorPoolSize;
import org.lwjgl.vulkan.VkDescriptorSetLayoutBinding;
import org.lwjgl.vulkan.VkDescriptorSetLayoutCreateInfo;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkExtent2D;
import org.lwjgl.vulkan.VkFramebufferCreateInfo;
import org.lwjgl.vulkan.VkGraphicsPipelineCreateInfo;
import org.lwjgl.vulkan.VkImageCreateInfo;
import org.lwjgl.vulkan.VkImageViewCreateInfo;
import org.lwjgl.vulkan.VkMemoryAllocateInfo;
import org.lwjgl.vulkan.VkMemoryRequirements;
import org.lwjgl.vulkan.VkPipelineColorBlendAttachmentState;
import org.lwjgl.vulkan.VkPipelineColorBlendStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineDepthStencilStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineInputAssemblyStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineLayoutCreateInfo;
import org.lwjgl.vulkan.VkPipelineMultisampleStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineRasterizationStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineShaderStageCreateInfo;
import org.lwjgl.vulkan.VkPipelineVertexInputStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineViewportStateCreateInfo;
import org.lwjgl.vulkan.VkQueue;
import org.lwjgl.vulkan.VkRenderPassCreateInfo;
import org.lwjgl.vulkan.VkSemaphoreCreateInfo;
import org.lwjgl.vulkan.VkShaderModuleCreateInfo;
import org.lwjgl.vulkan.VkSubpassDescription;
import org.lwjgl.vulkan.VkSwapchainCreateInfoKHR;
import org.lwjgl.vulkan.VkVertexInputAttributeDescription;
import org.lwjgl.vulkan.VkVertexInputBindingDescription;
import org.lwjgl.vulkan.VkWriteDescriptorSet;
import org.ploxie.engine.display.Window;
import org.ploxie.engine.vulkan.context.VulkanContext;
import org.ploxie.engine2.buffer.vertex.AttributeDescription;
import org.ploxie.engine2.buffer.vertex.BindingDescription;
import org.ploxie.engine2.buffer.vertex.VertexInputInfo;
import org.ploxie.engine2.util.BufferUtils;
import org.ploxie.utils.math.FastMath;
import org.ploxie.utils.math.vector.Vector2i;
import org.ploxie.vulkan.VulkanInstance;
import org.ploxie.vulkan.buffer.VulkanBuffer;
import org.ploxie.vulkan.buffer.VulkanBufferUsageFlag;
import org.ploxie.vulkan.buffer.VulkanCommandBuffer;
import org.ploxie.vulkan.buffer.VulkanFrameBuffer;
import org.ploxie.vulkan.command.VulkanCommandPool;
import org.ploxie.vulkan.descriptor.VulkanDescriptorLayout;
import org.ploxie.vulkan.descriptor.VulkanDescriptorPool;
import org.ploxie.vulkan.descriptor.VulkanDescriptorSet;
import org.ploxie.vulkan.descriptor.VulkanUniformBufferDescriptor;
import org.ploxie.vulkan.image.VulkanImage;
import org.ploxie.vulkan.image.VulkanImageAspectMask;
import org.ploxie.vulkan.image.VulkanImageLayout;
import org.ploxie.vulkan.image.VulkanImageUsageFlag;
import org.ploxie.vulkan.image.VulkanImageView;
import org.ploxie.vulkan.math.VulkanExtent2D;
import org.ploxie.vulkan.memory.VulkanMemoryAllocation;
import org.ploxie.vulkan.memory.VulkanMemoryPropertyFlag;
import org.ploxie.vulkan.memory.VulkanMemoryType;
import org.ploxie.vulkan.memory.VulkanPhysicalDeviceMemoryProperties;
import org.ploxie.vulkan.pipeline.VulkanGraphicsPipeline;
import org.ploxie.vulkan.pipeline.VulkanGraphicsPipelineLayout;
import org.ploxie.vulkan.pipeline.VulkanGraphicsPipelineProperties;
import org.ploxie.vulkan.queue.VulkanDeviceQueueCreateInfo;
import org.ploxie.vulkan.queue.VulkanQueue;
import org.ploxie.vulkan.queue.VulkanQueueFamilyProperties;
import org.ploxie.vulkan.queue.VulkanQueueFamilyPropertiesList;
import org.ploxie.vulkan.render.VulkanRenderPass;
import org.ploxie.vulkan.render.VulkanSubpass;
import org.ploxie.vulkan.shader.VulkanShaderModule;
import org.ploxie.vulkan.shader.VulkanShaderModules;
import org.ploxie.vulkan.surface.VulkanSurface;
import org.ploxie.vulkan.surface.VulkanSurfaceCapabilities;
import org.ploxie.vulkan.surface.VulkanSurfaceFormat;
import org.ploxie.vulkan.surface.VulkanSurfacePresentMode;
import org.ploxie.vulkan.swapchain.VulkanSwapChain;
import org.ploxie.vulkan.synchronization.VulkanSemaphore;
import org.ploxie.vulkan.utils.VKUtil;
import org.lwjgl.vulkan.VkPipelineDynamicStateCreateInfo;

import static org.lwjgl.vulkan.VK10.*;
import static org.lwjgl.vulkan.KHRSurface.*;
import static org.lwjgl.vulkan.KHRSwapchain.*;
import static org.lwjgl.system.MemoryUtil.*;

import lombok.Data;

@Data
public class VulkanLogicalDevice {

	public final VulkanPhysicalDevice physicalDevice;
	public final VkDevice internal;
	
	private Map<String, VulkanQueue> queues = new HashMap<>();	
	private Map<Integer, VulkanCommandPool> commandPools = new HashMap<>();	
		
	public VulkanQueue getDeviceQueue(int queueFamilyIndex, int queueIndex) {
		String cacheKey = queueFamilyIndex + ":"+queueIndex;
		VulkanQueue queue = queues.get(cacheKey);
		if(queue == null) {
			try (MemoryStack stack = MemoryStack.stackPush()){
				PointerBuffer pQueue = stack.mallocPointer(1);
				vkGetDeviceQueue(internal, queueFamilyIndex, queueIndex, pQueue);
				long queueHandle = pQueue.get(0);
				queue = new VulkanQueue(this, new VkQueue(queueHandle, internal));
				queues.put(cacheKey, queue);
			}
		}
			
		return queue;
	}
	
	public VulkanSwapChain createSwapChain(long windowHandle, VulkanExtent2D windowDimensions, VulkanSurfacePresentMode surfacePresentMode, VulkanSwapChain oldChain) {
		VulkanInstance instance = getPhysicalDevice().getInstance();
		VulkanSurface surface = instance.getWindowSurface(windowHandle);
		return createSwapChain(surface, windowDimensions, surfacePresentMode, oldChain);
	}
	
	public VulkanSwapChain createSwapChain(VulkanSurface surface, VulkanExtent2D windowDimensions, VulkanSurfacePresentMode surfacePresentMode, VulkanSwapChain oldChain) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			VulkanSurfaceCapabilities surfaceCapabilities = physicalDevice.getSurfaceCapabilities(stack, surface);
			List<VulkanSurfacePresentMode> presentModes = physicalDevice.getSurfacePresentModes(surface);
			if (!presentModes.contains(surfacePresentMode)) {
				throw new AssertionError("Surface present mode not supported: " + surfacePresentMode);
			}
			List<VulkanSurfaceFormat> formats = physicalDevice.getSurfaceFormats(surface);
			VulkanSurfaceFormat format = pickFormat(formats);

			/**
			 * The first one is the number of images in the swap chain,
			 * essentially the queue length. The implementation specifies the
			 * minimum amount of images to function properly and we'll try to
			 * have one more than that to properly implement triple buffering.
			 * 
			 */
			int swapImageCount = surfaceCapabilities.getMinImageCount() + 1;
			if (surfaceCapabilities.getMaxImageCount() > 0
					&& swapImageCount > surfaceCapabilities
							.getMaxImageCount()) {
				swapImageCount = surfaceCapabilities.getMaxImageCount();
			}
			
			VulkanExtent2D swapExtent = chooseSwapExtent(surfaceCapabilities, windowDimensions);
			VkExtent2D internalSwapExtent = VkExtent2D.callocStack(stack).set(swapExtent.getWidth(), swapExtent.getHeight());
			/**
			 * The imageArrayLayers specifies the amount of layers each image
			 * consists of. This is always 1 unless you are developing a
			 * stereoscopic 3D application. The imageUsage bit field specifies
			 * what kind of operations we'll use the images in the swap chain
			 * for. In this tutorial we're going to render directly to them,
			 * which means that they're used as color attachment. It is also
			 * possible that you'll render images to a separate image first to
			 * perform operations like post-processing. In that case you may use
			 * a value like VK_IMAGE_USAGE_TRANSFER_DST_BIT instead and use a
			 * memory operation to transfer the rendered image to a swap chain
			 * image.
			 * 
			 * 
			 */
			
			int preTransform;
			if ((surfaceCapabilities.getSupportedTransforms()
					& VK_SURFACE_TRANSFORM_IDENTITY_BIT_KHR) != 0) {
				preTransform = VK_SURFACE_TRANSFORM_IDENTITY_BIT_KHR;
			} else {
				preTransform = surfaceCapabilities.getCurrentTransform();
			}
			
			VulkanQueueFamilyPropertiesList vulkanQueueFamilyPropertiesList = physicalDevice
					.getQueueFamilyProperties();
			
			IntBuffer supportsPresent = stack
					.mallocInt(vulkanQueueFamilyPropertiesList.size());
			
			for (VulkanQueueFamilyProperties properties : vulkanQueueFamilyPropertiesList) {
				int err = vkGetPhysicalDeviceSurfaceSupportKHR(
						physicalDevice.getInternal(), properties.getIndex(),
						surface.getHandle(), supportsPresent);
				if (err != VK_SUCCESS) {
					throw new AssertionError(
							"Failed to physical device surface support: "
									+ VKUtil.translateVulkanResult(err));
				}
				supportsPresent.position(supportsPresent.position() + 1);
			}
			
			VkSwapchainCreateInfoKHR swapchainCreate = VkSwapchainCreateInfoKHR.callocStack(stack);
			swapchainCreate
				.sType(VK_STRUCTURE_TYPE_SWAPCHAIN_CREATE_INFO_KHR)
				.pNext(NULL)
				.surface(surface.getHandle())
				.minImageCount(swapImageCount)
				.imageFormat(format.getColorFormat())
				.imageColorSpace(format.getColorSpace())
				.imageUsage(VK_IMAGE_USAGE_COLOR_ATTACHMENT_BIT)
				.preTransform(preTransform)
				.imageArrayLayers(1)
				.imageSharingMode(VK_SHARING_MODE_EXCLUSIVE)
				.pQueueFamilyIndices(null)
				.presentMode(surfacePresentMode.getID())
				.oldSwapchain(VK_NULL_HANDLE)
				.clipped(true)
				.compositeAlpha(VK_COMPOSITE_ALPHA_OPAQUE_BIT_KHR)
				.imageExtent(internalSwapExtent)
				.oldSwapchain(oldChain == null ? VK_NULL_HANDLE : oldChain.getHandle());
			
			LongBuffer swapChainPtrBuffer = stack.mallocLong(1);
			int err = vkCreateSwapchainKHR(internal, swapchainCreate, null, swapChainPtrBuffer);
			if (err != VK_SUCCESS) {
				throw new AssertionError("Failed to create swap chain: "
						+ VKUtil.translateVulkanResult(err));
			}
			
			if (oldChain != null) {
				// If we just re-created an existing swapchain, we should destroy the
				// old swapchain at this point.
				// Note: destroying the swapchain also cleans up all its associated
				// presentable images once the platform is done with them.
				cleanup(oldChain);
			}
			
			IntBuffer pSwapchainImageCount = stack.mallocInt(1);
			long swapchain = swapChainPtrBuffer.get(0);
			
			err = vkGetSwapchainImagesKHR(internal, swapchain, pSwapchainImageCount, null);
			if (err != VK_SUCCESS) {
				throw new AssertionError(
						"Failed to get number of swapchain images: "
								+ VKUtil.translateVulkanResult(err));
			}
			
			//device can provide more swap images than we ask for, thats why we need to query it again
			int actualSwapImageCount = pSwapchainImageCount.get(0);
			LongBuffer pSwapchainImages = stack.mallocLong(actualSwapImageCount);
			err = vkGetSwapchainImagesKHR(internal, swapchain, pSwapchainImageCount, pSwapchainImages);
			if (err != VK_SUCCESS) {
				throw new AssertionError("Failed to get swapchain images: "
						+ VKUtil.translateVulkanResult(err));
			}
						
			VulkanImage[] swapchainImages = new VulkanImage[actualSwapImageCount];
			for (int i = 0; i < actualSwapImageCount; i++) {
				swapchainImages[i] = new VulkanImage(
						pSwapchainImages.get(i),
						windowDimensions.getWidth(),
						windowDimensions.getHeight(),
						1
				);
			}
			
			/**
			 * With Vulkan it's possible that your swap chain becomes invalid or
			 * unoptimized while your application is running, for example
			 * because the window was resized. In that case the swap chain
			 * actually needs to be recreated from scratch and a reference to
			 * the old one must be specified in this field.
			 */
			return new VulkanSwapChain(swapchain, windowDimensions, surface, swapchainImages, format, swapChainPtrBuffer);
		}
	}
	
	public VulkanSemaphore createSemaphore() {
		try(MemoryStack stack = MemoryStack.stackPush()){
			VkSemaphoreCreateInfo semaphoreCreateInfo = VkSemaphoreCreateInfo
					.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_SEMAPHORE_CREATE_INFO)
					.pNext(NULL)
					.flags(0);
			LongBuffer pSemaphore = memAllocLong(1);
			int err = vkCreateSemaphore(internal, semaphoreCreateInfo, null, pSemaphore);
			if(err != VK_SUCCESS) {
				throw new AssertionError("Failed o create image semaphore: "+VKUtil.translateVulkanResult(err));
			}
			return new VulkanSemaphore(pSemaphore.get(0), pSemaphore);
		}
	}
	
	public VulkanRenderPass createRenderPass(int colorFormat, int depthFormat) {
		try(MemoryStack stack = MemoryStack.stackPush()){
			VkAttachmentDescription.Buffer attachments = VkAttachmentDescription.callocStack(2, stack);
			
			attachments.get(0)
			.format(colorFormat)
			.samples(VK_SAMPLE_COUNT_1_BIT)
			.loadOp(VK_ATTACHMENT_LOAD_OP_CLEAR)
			.storeOp(VK_ATTACHMENT_STORE_OP_STORE)
			.stencilLoadOp(VK_ATTACHMENT_LOAD_OP_DONT_CARE)
			.stencilStoreOp(VK_ATTACHMENT_STORE_OP_DONT_CARE)
			.initialLayout(VK_IMAGE_LAYOUT_UNDEFINED)
			.finalLayout(VK_IMAGE_LAYOUT_PRESENT_SRC_KHR);
			
			attachments.get(1)
			.format(depthFormat)
			.samples(VK_SAMPLE_COUNT_1_BIT)
			.loadOp(VK_ATTACHMENT_LOAD_OP_CLEAR)
			.storeOp(VK_ATTACHMENT_STORE_OP_DONT_CARE)
			.stencilLoadOp(VK_ATTACHMENT_LOAD_OP_DONT_CARE)
			.stencilStoreOp(VK_ATTACHMENT_STORE_OP_DONT_CARE)
			.initialLayout(VK_IMAGE_LAYOUT_UNDEFINED)
			.finalLayout(VK_IMAGE_LAYOUT_DEPTH_STENCIL_ATTACHMENT_OPTIMAL);
			
			VkAttachmentReference.Buffer colorReference = VkAttachmentReference
					.callocStack(1, stack)
					.attachment(0)
					.layout(VK_IMAGE_LAYOUT_COLOR_ATTACHMENT_OPTIMAL);
			
			VkAttachmentReference depthReference = VkAttachmentReference
					.callocStack(stack)
					.attachment(1)
					.layout(VK_IMAGE_LAYOUT_DEPTH_STENCIL_ATTACHMENT_OPTIMAL);
			
			VkSubpassDescription.Buffer subpass = VkSubpassDescription
					.calloc(1)
					.pipelineBindPoint(VK_PIPELINE_BIND_POINT_GRAPHICS)
					.flags(0)
					.pInputAttachments(null)
					.colorAttachmentCount(colorReference.remaining())
					.pColorAttachments(colorReference)
					.pResolveAttachments(null)
					.pDepthStencilAttachment(depthReference)
					.pPreserveAttachments(null);
			
			VkRenderPassCreateInfo renderPassCreateInfo = VkRenderPassCreateInfo
					.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_RENDER_PASS_CREATE_INFO)
					.pAttachments(attachments)
					.pSubpasses(subpass)
					.pDependencies(null);
			
			LongBuffer renderPassHandleBuffer = stack.mallocLong(1);
			int err = vkCreateRenderPass(internal, renderPassCreateInfo, null, renderPassHandleBuffer);
			if(err != VK_SUCCESS) {
				throw new AssertionError("Failed to create clear render pass: "+VKUtil.translateVulkanResult(err));
			}
			
			List<VulkanSubpass> subPasses = new ArrayList<VulkanSubpass>();
			subPasses.add(new VulkanSubpass(0));
			
			return new VulkanRenderPass(renderPassHandleBuffer.get(0), subPasses, this);			
		}		
	}
		
	public VulkanGraphicsPipeline createGraphicsPipeline(VulkanRenderPass renderPass, VulkanGraphicsPipelineProperties properties) {		
		try(MemoryStack stack = MemoryStack.stackPush()){
			VkVertexInputBindingDescription.Buffer bindingDescriptionInternal;
			VkVertexInputAttributeDescription.Buffer inputAttributeDescriptionsInternal;
			
			VertexInputInfo vertexInputInfo = properties.getVertexInputInfo();
			if(vertexInputInfo == null) {
				bindingDescriptionInternal = VkVertexInputBindingDescription.callocStack(0, stack);
				inputAttributeDescriptionsInternal = VkVertexInputAttributeDescription.callocStack(0, stack);
			}else {
				BindingDescription bindingDescription = vertexInputInfo.getBindingDescription();
				AttributeDescription[] attributeDescriptions = vertexInputInfo.getAttributeDescriptions();
				bindingDescriptionInternal = VkVertexInputBindingDescription.callocStack(1, stack);
				inputAttributeDescriptionsInternal = VkVertexInputAttributeDescription.callocStack(attributeDescriptions.length, stack);
				
				bindingDescriptionInternal
				.binding(bindingDescription.getBinding())
				.stride(bindingDescription.getStride())
				.inputRate(bindingDescription.isInstanced() ? VK_VERTEX_INPUT_RATE_INSTANCE : VK_VERTEX_INPUT_RATE_VERTEX);
				
				for(int i = 0; i < attributeDescriptions.length;i++) {
					AttributeDescription attributeDescription = attributeDescriptions[i];
					int format = VKUtil.getFormat(attributeDescription.getFormat());
					inputAttributeDescriptionsInternal
					.get(i)
					.location(attributeDescription.getLocation())
					.binding(attributeDescription.getBinding())
					.format(format)
					.offset(attributeDescription.getOffset());
				}
			}
			
			VkPipelineVertexInputStateCreateInfo vertexInputStateInfo = VkPipelineVertexInputStateCreateInfo
					.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_PIPELINE_VERTEX_INPUT_STATE_CREATE_INFO)
					.pNext(NULL)
					.pVertexBindingDescriptions(bindingDescriptionInternal)
					.pVertexAttributeDescriptions(inputAttributeDescriptionsInternal);
			
			int topology = VKUtil.getTopology(properties.getTopology());
			VkPipelineInputAssemblyStateCreateInfo inputAssemblyStateCreateInfo = VkPipelineInputAssemblyStateCreateInfo
					.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_PIPELINE_INPUT_ASSEMBLY_STATE_CREATE_INFO)
					.topology(topology)
					.primitiveRestartEnable(false);
			
			int polygonMode = VKUtil.getPolygonDrawMode(properties.getPolygonDrawMode());
			int frontFace = VKUtil.getFrontFaceVertexWinding(properties.getFrontFaceVertexWinding());
			int cullMode = VKUtil.getCullMode(properties.getCullFaceSide());
			
			VkPipelineRasterizationStateCreateInfo rasterizationStateCreateInfo = VkPipelineRasterizationStateCreateInfo
					.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_PIPELINE_RASTERIZATION_STATE_CREATE_INFO)
					.polygonMode(polygonMode)
					.frontFace(frontFace)
					.lineWidth(properties.getLineWidth())
					.depthClampEnable(properties.isDepthClampEnabled())
					.rasterizerDiscardEnable(properties.isRasterizerDiscardEnabled())
					.depthBiasEnable(false)
					.cullMode(cullMode);
			
			int colorMask = 0;
			if(properties.isColorWriteMaskR()) {
				colorMask |= VK_COLOR_COMPONENT_R_BIT;
			}
			if(properties.isColorWriteMaskG()) {
				colorMask |= VK_COLOR_COMPONENT_G_BIT;
			}
			if(properties.isColorWriteMaskB()) {
				colorMask |= VK_COLOR_COMPONENT_B_BIT;
			}
			if(properties.isColorWriteMaskA()) {
				colorMask |= VK_COLOR_COMPONENT_A_BIT;
			}
			
			VkPipelineColorBlendAttachmentState.Buffer colorWriteMask = VkPipelineColorBlendAttachmentState
					.callocStack(1, stack)
					.blendEnable(false)
					.colorWriteMask(colorMask);
			
			VkPipelineColorBlendStateCreateInfo colorBlendStateCreateInfo = VkPipelineColorBlendStateCreateInfo
					.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_PIPELINE_COLOR_BLEND_STATE_CREATE_INFO)
					.pNext(NULL)
					.pAttachments(colorWriteMask);
			
			VkPipelineMultisampleStateCreateInfo multisampleStateCreateInfo = VkPipelineMultisampleStateCreateInfo
					.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_PIPELINE_MULTISAMPLE_STATE_CREATE_INFO)
					.pSampleMask(null)
					.rasterizationSamples(VK_SAMPLE_COUNT_1_BIT);
			
			VkPipelineViewportStateCreateInfo viewportStateCreateInfo = VkPipelineViewportStateCreateInfo
					.calloc()
					.sType(VK_STRUCTURE_TYPE_PIPELINE_VIEWPORT_STATE_CREATE_INFO)
					.viewportCount(1)
					.scissorCount(1);
			
			VulkanShaderModules shaderModules = properties.getShaderModules();
			VkPipelineShaderStageCreateInfo.Buffer shaderStages = createShaderStages(stack, shaderModules);
					
			VkDescriptorSetLayoutBinding.Buffer layoutBinding = VkDescriptorSetLayoutBinding.callocStack(2, stack);
			
			layoutBinding
				.get(0)
				.binding(0)
				.descriptorType(VK_DESCRIPTOR_TYPE_UNIFORM_BUFFER)
				.descriptorCount(1)
				.stageFlags(VK_SHADER_STAGE_VERTEX_BIT)
				.pImmutableSamplers(null);
			
			layoutBinding
				.get(1)
				.binding(1)
				.descriptorType(VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER)
				.descriptorCount(1)
				.stageFlags(VK_SHADER_STAGE_FRAGMENT_BIT)
				.pImmutableSamplers(null);
			
			VkDescriptorSetLayoutCreateInfo layoutCreateInfo = VkDescriptorSetLayoutCreateInfo
					.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_DESCRIPTOR_SET_LAYOUT_CREATE_INFO)
					.pBindings(layoutBinding);
			
			LongBuffer descriptorSetLayoutHandleBuffer = stack.mallocLong(1);
			int err = vkCreateDescriptorSetLayout(internal, layoutCreateInfo, null, descriptorSetLayoutHandleBuffer);
			if(err != VK_SUCCESS) {
				throw new AssertionError("Failed to create descriptor set layout: "+VKUtil.translateVulkanResult(err));
			}
			
			VulkanDescriptorLayout[] descriptorSetLayouts = new VulkanDescriptorLayout[descriptorSetLayoutHandleBuffer.remaining()];
			for(int i = 0; i < descriptorSetLayoutHandleBuffer.remaining();i++) {
				descriptorSetLayouts[i] = new VulkanDescriptorLayout(descriptorSetLayoutHandleBuffer.get(i));
			}
			
			LongBuffer pipelineLayoutHandleBuffer = stack.mallocLong(1);
			
			VkPipelineLayoutCreateInfo pipelineLayoutCreateInfo = VkPipelineLayoutCreateInfo
					.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_PIPELINE_LAYOUT_CREATE_INFO)
					.pNext(NULL)
					.pSetLayouts(descriptorSetLayoutHandleBuffer);
			
			err = vkCreatePipelineLayout(internal, pipelineLayoutCreateInfo, null, pipelineLayoutHandleBuffer);
			if(err != VK_SUCCESS) {
				throw new AssertionError("Failed to create pipeline layout: "+VKUtil.translateVulkanResult(err));
			}
			
			VkPipelineDepthStencilStateCreateInfo depthStencilStateCreateInfo = VkPipelineDepthStencilStateCreateInfo
					.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_PIPELINE_DEPTH_STENCIL_STATE_CREATE_INFO)
					.depthTestEnable(true)
					.depthWriteEnable(true)
					.depthCompareOp(VK_COMPARE_OP_LESS)
					.depthBoundsTestEnable(false)
					.minDepthBounds(0)
					.maxDepthBounds(1);
			
			long layoutHandle = pipelineLayoutHandleBuffer.get(0);
			
			IntBuffer pDynamicStates = memAllocInt(properties.getDynamicStatesCount());
			if(properties.isDynamicViewport()) {
				pDynamicStates.put(VK_DYNAMIC_STATE_VIEWPORT);
			}
			if(properties.isDynamicScissor()) {
				pDynamicStates.put(VK_DYNAMIC_STATE_SCISSOR);
			}
			if(properties.isDynamicBlendConstants()) {
				pDynamicStates.put(VK_DYNAMIC_STATE_BLEND_CONSTANTS);
			}
			pDynamicStates.flip();
			
			VkPipelineDynamicStateCreateInfo dynamicState = VkPipelineDynamicStateCreateInfo
					.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_PIPELINE_DYNAMIC_STATE_CREATE_INFO)
					.pDynamicStates(pDynamicStates);
			
			LongBuffer pipelinesHandleBuffer = stack.mallocLong(1);
			VkGraphicsPipelineCreateInfo.Buffer pipelineCreateInfo = VkGraphicsPipelineCreateInfo
					.callocStack(1, stack)
					.sType(VK_STRUCTURE_TYPE_GRAPHICS_PIPELINE_CREATE_INFO)
					.layout(layoutHandle)
					.renderPass(renderPass.getHandle())
					.pVertexInputState(vertexInputStateInfo)					
					.pInputAssemblyState(inputAssemblyStateCreateInfo)
					.pRasterizationState(rasterizationStateCreateInfo)
					.pColorBlendState(colorBlendStateCreateInfo)
					.pMultisampleState(multisampleStateCreateInfo)
					.pViewportState(viewportStateCreateInfo)
					.pDepthStencilState(depthStencilStateCreateInfo)
					.pStages(shaderStages)
					.pDynamicState(dynamicState);
			
			err = vkCreateGraphicsPipelines(internal, VK_NULL_HANDLE,pipelineCreateInfo, null, pipelinesHandleBuffer);
			
			if(err != VK_SUCCESS) {
				throw new AssertionError("Failed to create pipeline: "+VKUtil.translateVulkanResult(err));
			}
			
			VulkanGraphicsPipelineLayout layout = new VulkanGraphicsPipelineLayout(layoutHandle);			
					
			return new VulkanGraphicsPipeline(pipelinesHandleBuffer.get(0), descriptorSetLayouts, layout);
		}		
	}
	
	public VulkanShaderModule loadShader(ByteBuffer shaderCode) {
		try(MemoryStack stack = MemoryStack.stackPush()){
			LongBuffer handleBuffer = stack.mallocLong(1);
			VkShaderModuleCreateInfo moduleCreateInfo = VkShaderModuleCreateInfo
					.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_SHADER_MODULE_CREATE_INFO)
					.pNext(NULL)
					.pCode(shaderCode)
					.flags(0);
			
			int err = vkCreateShaderModule(internal, moduleCreateInfo, null, handleBuffer);
			if(err != VK_SUCCESS) {
				throw new AssertionError("Failed to create shader module: "+VKUtil.translateVulkanResult(err));
			}
			
			return new VulkanShaderModule(handleBuffer.get(0));
		}
	}
	
	public VulkanCommandPool createCommandPool(int queueFamilyIndex) {
		try(MemoryStack stack = MemoryStack.stackPush()){
			VkCommandPoolCreateInfo commandPoolCreateInfo = VkCommandPoolCreateInfo
					.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_COMMAND_POOL_CREATE_INFO)
					.queueFamilyIndex(queueFamilyIndex)
					.flags(VK_COMMAND_POOL_CREATE_RESET_COMMAND_BUFFER_BIT);
			
			LongBuffer commandPoolHandleBuffer = stack.mallocLong(1);
			int err = vkCreateCommandPool(internal, commandPoolCreateInfo, null, commandPoolHandleBuffer);
			if(err != VK_SUCCESS) {
				throw new AssertionError("Failed to create command pool: "+VKUtil.translateVulkanResult(err));
			}
			long handle = commandPoolHandleBuffer.get(0);
					
			return new VulkanCommandPool(handle);
		}
	}
	
	public VulkanCommandPool getCommandPool(int queueFamilyIndex) {
		VulkanCommandPool pool = commandPools.get(queueFamilyIndex);
		if(pool != null) {
			return pool;
		}			
		return createCommandPool(queueFamilyIndex);
	}
	
	public VulkanCommandBuffer createCommandBuffer(VulkanCommandPool pool, boolean primary) {
		return createCommandBuffers(pool, primary, 1)[0];
	}
	
	public VulkanCommandBuffer[] createCommandBuffers(VulkanCommandPool pool, boolean primary, int count) {
		VulkanCommandBuffer[] commandBuffers = new VulkanCommandBuffer[count];
		try(MemoryStack stack = MemoryStack.stackPush()){
			VkCommandBufferAllocateInfo commandBufferAllocateInfo = VkCommandBufferAllocateInfo.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_COMMAND_BUFFER_ALLOCATE_INFO)
					.commandPool(pool.getHandle())
					.level(primary ? VK_COMMAND_BUFFER_LEVEL_PRIMARY : VK_COMMAND_BUFFER_LEVEL_SECONDARY)
					.commandBufferCount(count);
			
			PointerBuffer commandBufferHandleBuffer = stack.mallocPointer(count);
			int err = vkAllocateCommandBuffers(internal, commandBufferAllocateInfo, commandBufferHandleBuffer);
			if(err != VK_SUCCESS) {
				throw new AssertionError("Failed to allocate render command buffer: "+VKUtil.translateVulkanResult(err));
			}
			
			for (int i = 0; i < count; i++) {
				commandBuffers[i] = new VulkanCommandBuffer(this,new VkCommandBuffer(commandBufferHandleBuffer.get(i), internal), pool, commandBufferHandleBuffer);
			}
			
			return commandBuffers;
		}
	}
	
	public VulkanBuffer createBuffer(int size, boolean exclusive, VulkanBufferUsageFlag... usages) {
		try(MemoryStack stack = MemoryStack.stackPush()){
			int usageFlags = 0;
			for(VulkanBufferUsageFlag flag : usages) {
				usageFlags |= flag.getBitMask();
			}
			
			VkBufferCreateInfo bufferInfo = VkBufferCreateInfo
					.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_BUFFER_CREATE_INFO)
					.size(size)
					.usage(usageFlags)
					.sharingMode(exclusive ? VK_SHARING_MODE_EXCLUSIVE : VK_SHARING_MODE_CONCURRENT);
			
			LongBuffer buffer = stack.mallocLong(1);
			int err = vkCreateBuffer(internal, bufferInfo,  null, buffer);
			if(err != VK_SUCCESS) {
				throw new AssertionError("Failed to create buffer: "+VKUtil.translateVulkanResult(err));
			}
			
			return new VulkanBuffer(this, buffer.get(0), size);
		}
	}
	
	public VulkanMemoryAllocation allocateMemory(VulkanBuffer buffer, VulkanMemoryPropertyFlag... flags) {
		try(MemoryStack stack = MemoryStack.stackPush()){
			VkMemoryRequirements memoryRequirements = VkMemoryRequirements.callocStack(stack);
			vkGetBufferMemoryRequirements(internal,  buffer.getHandle(), memoryRequirements);
			
			System.out.println(memoryRequirements.size() + ": " + ": " + memoryRequirements.alignment() +":" + memoryRequirements.memoryTypeBits());
			
			VulkanPhysicalDeviceMemoryProperties memoryProperties = physicalDevice.getDeviceMemoryProperties();
			VulkanMemoryType memoryType = memoryProperties.getType(flags);
			
			VkMemoryAllocateInfo allocateInfo = VkMemoryAllocateInfo.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_MEMORY_ALLOCATE_INFO)
					.allocationSize(memoryRequirements.size())
					.memoryTypeIndex(memoryType.getIndex());
			
			LongBuffer longBuffer = stack.mallocLong(1);
			int err = vkAllocateMemory(internal, allocateInfo, null, longBuffer);
			if (err != VK_SUCCESS) {
				throw new AssertionError("Failed to allocate memory: "+ VKUtil.translateVulkanResult(err));
			}
			
			long memoryHandle = longBuffer.get(0);
			err = vkBindBufferMemory(internal, buffer.getHandle(), memoryHandle, 0);
			if (err != VK_SUCCESS) {
				throw new AssertionError("Failed to bind memory: "+ VKUtil.translateVulkanResult(err));
			}
			
			return new VulkanMemoryAllocation(memoryHandle);
		}
	}
	
	public VulkanMemoryAllocation allocateMemory(VulkanImage image, VulkanMemoryPropertyFlag... flags) {
		try(MemoryStack stack = MemoryStack.stackPush()){
			VkMemoryRequirements memoryRequirements = VkMemoryRequirements.callocStack(stack);
			vkGetImageMemoryRequirements(internal, image.getHandle(), memoryRequirements);
			
			System.out.println(memoryRequirements.size() + ": " + ": " + memoryRequirements.alignment() +":" + memoryRequirements.memoryTypeBits());
			
			VulkanPhysicalDeviceMemoryProperties memoryProperties = physicalDevice.getDeviceMemoryProperties();
			VulkanMemoryType memoryType = memoryProperties.getType(flags);
			
			VkMemoryAllocateInfo allocateInfo = VkMemoryAllocateInfo.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_MEMORY_ALLOCATE_INFO)
					.allocationSize(memoryRequirements.size())
					.memoryTypeIndex(memoryType.getIndex());
			
			LongBuffer longBuffer = stack.mallocLong(1);
			int err = vkAllocateMemory(internal, allocateInfo, null, longBuffer);
			if (err != VK_SUCCESS) {
				throw new AssertionError("Failed to allocate memory: "+ VKUtil.translateVulkanResult(err));
			}
			
			long memoryHandle = longBuffer.get(0);
			err = vkBindImageMemory(internal, image.getHandle(), memoryHandle, 0);
			if (err != VK_SUCCESS) {
				throw new AssertionError("Failed to bind memory: "+ VKUtil.translateVulkanResult(err));
			}
			
			return new VulkanMemoryAllocation(memoryHandle);
		}
	}
	
	public void fillBuffer(VulkanMemoryAllocation memoryAllocation, VulkanBuffer buffer, ByteBuffer data) {
		try(MemoryStack stack = MemoryStack.stackPush()){
			PointerBuffer pointerBuffer = stack.mallocPointer(1);
			int err = vkMapMemory(internal,memoryAllocation.getHandle(), 0, buffer.getSize(), 0, pointerBuffer);
			if (err != VK_SUCCESS) {
				throw new AssertionError("Failed to map memory: "+ VKUtil.translateVulkanResult(err));
			}
			
			long dataPointer = pointerBuffer.get(0);
			memCopy(memAddress(data), dataPointer, data.remaining());
			vkUnmapMemory(internal, memoryAllocation.getHandle());
		}
	}
	
	
	
	public VulkanCommandBuffer copyBuffer(VulkanCommandPool pool, VulkanBuffer src, VulkanBuffer dest, long size) {
		VulkanCommandBuffer commandBuffer = createCommandBuffer(pool, true);
		commandBuffer.begin();
		commandBuffer.copy(src, dest, size);
		commandBuffer.end();
		return commandBuffer;
	}
	
	
	public void cleanup(VulkanBuffer buffer) {
		vkDestroyBuffer(internal, buffer.getHandle(), null);
	}
	
	public void cleanup(VulkanSwapChain swapChain) {
		vkDestroySwapchainKHR(internal, swapChain.getHandle(), null);
	}
	
	public void cleanup(VulkanCommandPool commandPool, VulkanCommandBuffer commandBuffer) {
		vkFreeCommandBuffers(internal, commandPool.getHandle(), commandBuffer.getInternal());
	}
	
	public VulkanExtent2D chooseSwapExtent(VulkanSurfaceCapabilities surfaceCapabilities, VulkanExtent2D windowDimensions) {
		if(surfaceCapabilities.getCurrentExtent().getWidth() != -1) {
			return surfaceCapabilities.getCurrentExtent();
		}else {
			VulkanExtent2D minExtent = surfaceCapabilities.getMinImageExtent();
			VulkanExtent2D maxExtent = surfaceCapabilities.getMaxImageExtent();
			int w = FastMath.clamp(windowDimensions.getWidth(), minExtent.getWidth(), maxExtent.getWidth());
			int h = FastMath.clamp(windowDimensions.getHeight(), minExtent.getHeight(), maxExtent.getHeight());
			return new VulkanExtent2D(w,h);
		}
	}
	
	public VulkanSurfaceFormat pickFormat(List<VulkanSurfaceFormat> formats) {
		if (formats.size()==1) {
			VulkanSurfaceFormat format = formats.get(0);
			if (format.getColorFormat() == VK_FORMAT_UNDEFINED) {
				return new VulkanSurfaceFormat(VK_FORMAT_B8G8R8_UNORM, VK_COLOR_SPACE_SRGB_NONLINEAR_KHR);
			}
		}
		for(VulkanSurfaceFormat format : formats) {
			if (format.getColorFormat() == VK_FORMAT_B8G8R8A8_UNORM && format.getColorSpace() == VK_COLOR_SPACE_SRGB_NONLINEAR_KHR) {
				return format;
			}
		}
		return formats.get(0);
	}

	private VkPipelineShaderStageCreateInfo.Buffer createShaderStages(MemoryStack stack, VulkanShaderModules shaderModules){
		VkPipelineShaderStageCreateInfo.Buffer shaderStages = VkPipelineShaderStageCreateInfo.callocStack(shaderModules.getShaderCount(), stack);
		
		VulkanShaderModule vertexModule = shaderModules.getVertex();
		if(vertexModule != null) {
			shaderStages.get().set(getShaderStage(stack, vertexModule, VK_SHADER_STAGE_VERTEX_BIT));
		}
		
		VulkanShaderModule fragmentModule = shaderModules.getFragment();
		if(fragmentModule != null) {
			shaderStages.get().set(getShaderStage(stack, fragmentModule, VK_SHADER_STAGE_FRAGMENT_BIT));
		}
		shaderStages.flip();		
		return shaderStages;
	}
	
	
	private VkPipelineShaderStageCreateInfo getShaderStage(MemoryStack stack, VulkanShaderModule shaderModule, int stage) {
		return VkPipelineShaderStageCreateInfo
			.calloc()
			.sType(VK_STRUCTURE_TYPE_PIPELINE_SHADER_STAGE_CREATE_INFO)
			.stage(stage)
			.module(shaderModule.getHandle())
			.pName(BufferUtils.wrap("main\0", stack));
	}
	
	public VulkanFrameBuffer[] createFrameBuffers(VulkanExtent2D dimensions, VulkanSwapChain swapChain, VulkanImageView depthImageView, VulkanRenderPass renderPass) {
		VulkanImageView[] colorImageViews = swapChain.getSwapImageViews();
		if(colorImageViews == null) {
			throw new AssertionError("Create swap images because calling createFrameBuffers");
		}
		
		VulkanFrameBuffer[] buffers = new VulkanFrameBuffer[colorImageViews.length];
		for(int i = 0 ; i < buffers.length;i++) {
			buffers[i] = createFrameBuffer(dimensions, renderPass, colorImageViews[i], depthImageView);
		}
		
		return buffers;
	}
	
	public VulkanFrameBuffer createFrameBuffer(VulkanExtent2D dimensions, VulkanRenderPass renderPass, VulkanImageView... attachments) {
		try(MemoryStack stack = MemoryStack.stackPush()){
			LongBuffer frameBufferHandleBuffer = stack.mallocLong(1);
			LongBuffer attachmentsPointerBuffer = stack.mallocLong(attachments.length);
			for(VulkanImageView image : attachments) {
				attachmentsPointerBuffer.put(image.getHandle());
			}
			
			attachmentsPointerBuffer.flip();
			VkFramebufferCreateInfo fci = VkFramebufferCreateInfo.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_FRAMEBUFFER_CREATE_INFO)
					.pAttachments(attachmentsPointerBuffer)
					.flags(0)
					.height(dimensions.getHeight())
					.width(dimensions.getWidth())
					.layers(1)
					.pNext(NULL)
					.renderPass(renderPass.getHandle());
			
			int err = vkCreateFramebuffer(internal, fci, null, frameBufferHandleBuffer);
			if(err != VK_SUCCESS) {
				throw new AssertionError("Failed to create framebuffer: "+VKUtil.translateVulkanResult(err));
			}
			long handle = frameBufferHandleBuffer.get(0);
			return new VulkanFrameBuffer(handle);
		}		
	}

	public int acquireNextImageIndexKHR(VulkanSwapChain swapChain, int timeout, VulkanSemaphore semaphore) {
		try(MemoryStack stack = MemoryStack.stackPush()){
			IntBuffer imageIndexBuffer = stack.mallocInt(1);
			int err = vkAcquireNextImageKHR(internal, swapChain.getHandle(), timeout, semaphore.getHandle(), VK_NULL_HANDLE, imageIndexBuffer);
			if(err != VK_SUCCESS) {
				throw new AssertionError("Failed to acquire next swapchain image: "+VKUtil.translateVulkanResult(err));
			}
			return imageIndexBuffer.get(0);
		}
	}

	public VulkanDescriptorPool createDescriptorPool(int uniformBufferCount, int imageSamplerCount) {
		try(MemoryStack stack = MemoryStack.stackPush()){
			VkDescriptorPoolSize.Buffer poolSize = VkDescriptorPoolSize.callocStack(2, stack);
			
			poolSize.get(0)
				.type(VK_DESCRIPTOR_TYPE_UNIFORM_BUFFER)
				.descriptorCount(uniformBufferCount);
			
			poolSize.get(1)
				.type(VK_DESCRIPTOR_TYPE_COMBINED_IMAGE_SAMPLER)
				.descriptorCount(imageSamplerCount);
			
			VkDescriptorPoolCreateInfo poolCreateInfo = VkDescriptorPoolCreateInfo
					.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_DESCRIPTOR_POOL_CREATE_INFO)
					.pPoolSizes(poolSize)
					.maxSets(1);
			
			LongBuffer pDescriptorPool = stack.mallocLong(1);
			int err = vkCreateDescriptorPool(internal, poolCreateInfo, null, pDescriptorPool);
			if(err != VK_SUCCESS) {
				throw new AssertionError("Failed to create descriptor pool: "+VKUtil.translateVulkanResult(err));
			}
			
			long descriptorPoolHandle = pDescriptorPool.get(0);			
			
			return new VulkanDescriptorPool(this, descriptorPoolHandle);
		}
	}

	public VulkanUniformBufferDescriptor createUniformBuffer(int uniformBufferSize) {
		VulkanBuffer buffer = createBuffer(uniformBufferSize, true, VulkanBufferUsageFlag.UNIFORM);
		VulkanMemoryAllocation memoryAllocation = allocateMemory(buffer, VulkanMemoryPropertyFlag.HOST_VISIBLE, VulkanMemoryPropertyFlag.HOST_COHERENT);
		return new VulkanUniformBufferDescriptor(memoryAllocation, buffer, 0, uniformBufferSize);
	}
	
	public void updateDescriptorSet(VulkanDescriptorSet descriptorSet, long buffer, long range, long offset, int binding, int usage) {
		try(MemoryStack stack = MemoryStack.stackPush()){
			VkDescriptorBufferInfo.Buffer bufferInfo = VkDescriptorBufferInfo
				.callocStack(1, stack)
				.buffer(buffer)
				.offset(offset)
				.range(range);
	
			VkWriteDescriptorSet.Buffer descriptorWrite = VkWriteDescriptorSet.callocStack(1, stack);
			descriptorWrite.get(0)
				.sType(VK10.VK_STRUCTURE_TYPE_WRITE_DESCRIPTOR_SET)
				.dstSet(descriptorSet.getHandle())
				.dstBinding(binding)
				.dstArrayElement(0)
				.descriptorType(usage)
				.pBufferInfo(bufferInfo)
				.pImageInfo(null)
				.pTexelBufferView(null);
	
			vkUpdateDescriptorSets(getInternal(), descriptorWrite, null);			
		}
	}
	
	public void mapUniformBuffer(VulkanUniformBufferDescriptor descriptor, ByteBuffer buffer) {
		VulkanMemoryAllocation memoryAllocation = descriptor.getMemoryAllocation();
		
		PointerBuffer pData = MemoryUtil.memAllocPointer(1);
		int err = vkMapMemory(getInternal(), memoryAllocation.getHandle(), 0, descriptor.getRange(), 0, pData);
		long data = pData.get(0);
		MemoryUtil.memFree(pData);		

		if (err != VK_SUCCESS) {
			throw new AssertionError("Failed to map UBO memory: " + VKUtil.translateVulkanResult(err));
		}
		
		memCopy(memAddress(buffer), data, buffer.remaining());
		
		vkUnmapMemory(getInternal(), memoryAllocation.getHandle());
	}

	public void createImageViewsForSwapChain(VulkanSwapChain swapChain) {
		VulkanImage[] images = swapChain.getSwapImages();
		VulkanImageView[] imageViews = new VulkanImageView[images.length];
		int imageFormat = swapChain.getImageFormat().getColorFormat();
		for(int i = 0 ; i < images.length;i++) {
			imageViews[i] = createImageView(images[i], imageFormat, VK_IMAGE_ASPECT_COLOR_BIT);
		}
		
		swapChain.setSwapImageViews(imageViews);		
	}
	
	public VulkanImageView createImageView(VulkanImage image, int format, VulkanImageAspectMask... imageAspectMasks) {
		int internalFormat = format;
		int internalImageAspectMask = 0;
		for(VulkanImageAspectMask mask : imageAspectMasks) {
			internalImageAspectMask |= mask.getBitMask();
		}
		
		return createImageView(image, internalFormat, internalImageAspectMask);
	}
	
	public VulkanImageView createImageView(VulkanImage image, int format, int aspectMask) {
		try(MemoryStack stack = MemoryStack.stackPush()){
			int internalViewType = image.getImageType();
			
			VkImageViewCreateInfo createInfo = VkImageViewCreateInfo.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_IMAGE_VIEW_CREATE_INFO)
					.pNext(NULL)
					.image(image.getHandle())
					.format(format)
					.viewType(internalViewType)
					.flags(0);
			
			createInfo.subresourceRange()
				.aspectMask(aspectMask)
				.baseMipLevel(0)
				.levelCount(1)
				.baseArrayLayer(0)
				.layerCount(1);
			
			LongBuffer pBufferView = stack.mallocLong(1);
			int err = vkCreateImageView(internal, createInfo, null, pBufferView);
			if(err != VK_SUCCESS) {
				throw new AssertionError("Failed to create image view: "+VKUtil.translateVulkanResult(err));
			}
			
			return new VulkanImageView(pBufferView.get(0));
		}
	}

	public VulkanImage createImage2D(int mipLevels, int format, int width, int height, VulkanImageUsageFlag... usageFlags) {		
		return createImage(VK_IMAGE_TYPE_2D, mipLevels, 1, format, width, height, 1, usageFlags);
	}

	private VulkanImage createImage(int imageFormat, int mipLevels, int arrayLayers, int format, int width, int height, int depth,	VulkanImageUsageFlag... usageFlags) {
		VulkanImageLayout layout = VulkanImageLayout.UNDEFINED;
		int formatInternal = format;
		int usageFlagsInternal = 0;
		for(VulkanImageUsageFlag flag : usageFlags) {
			usageFlagsInternal |= flag.getBitMask();
		}
		
		int imageTypeInternal = imageFormat;
		try(MemoryStack stack = MemoryStack.stackPush()){
			VkImageCreateInfo imageCreateInfo = VkImageCreateInfo
					.callocStack(stack)
					.sType(VK_STRUCTURE_TYPE_IMAGE_CREATE_INFO)
					.imageType(imageTypeInternal)
					.mipLevels(mipLevels)
					.arrayLayers(arrayLayers)
					.format(formatInternal)
					.tiling(VK_IMAGE_TILING_OPTIMAL)
					.initialLayout(layout.getBitMask())
					.usage(usageFlagsInternal)
					.sharingMode(VK_SHARING_MODE_EXCLUSIVE)
					.samples(VK_SAMPLE_COUNT_1_BIT)
					.flags(0);
			
			imageCreateInfo.extent()
				.width(width)
				.height(height)
				.depth(depth);
			
			LongBuffer pImage = stack.mallocLong(1);
			int err = vkCreateImage(internal,imageCreateInfo, null, pImage);
			if(err != VK_SUCCESS) {
				throw new AssertionError("Failed to create image: "+VKUtil.translateVulkanResult(err));
			}
			
			VulkanImage image = new VulkanImage(pImage.get(0), width, height, depth);
			image.setLayout(layout);
			return image;
			
		}
	}

	public VulkanCommandBuffer setImageLayout(VulkanCommandPool pool, VulkanImage image, VulkanImageLayout newLayout) {
		VulkanCommandBuffer commandBuffer = createCommandBuffer(pool, true);
		commandBuffer.begin();
		commandBuffer.setImageLayout(image, image.getLayout(), newLayout);
		commandBuffer.end();
		image.setLayout(newLayout);
		return commandBuffer;
	}
	
}

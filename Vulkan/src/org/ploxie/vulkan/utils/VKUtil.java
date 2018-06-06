package org.ploxie.vulkan.utils;

import static org.lwjgl.vulkan.EXTDebugReport.*;       
import static org.lwjgl.vulkan.KHRDisplaySwapchain.*;     
import static org.lwjgl.vulkan.KHRSurface.*;      
import static org.lwjgl.vulkan.KHRSwapchain.*;        
import static org.lwjgl.vulkan.VK10.*;

import org.ploxie.engine2.buffer.vertex.AttributeDescription;
import org.ploxie.engine2.buffer.vertex.BindingDescription;
import org.ploxie.engine2.buffer.vertex.VertexInputInfo;
import org.ploxie.engine2.model.Mesh;
import org.ploxie.engine2.pipeline.data.CullFaceSide;
import org.ploxie.engine2.pipeline.data.Format;
import org.ploxie.engine2.pipeline.data.FrontFaceVertexWinding;
import org.ploxie.engine2.pipeline.data.ImageType;
import org.ploxie.engine2.pipeline.data.PolygonDrawMode;
import org.ploxie.engine2.pipeline.data.PrimitiveTopology;
import org.ploxie.utils.math.vector.Vector2f;
import org.ploxie.utils.math.vector.Vector3f;

public class VKUtil {

	public static VertexInputInfo getVertexInputInfo(Mesh mesh) {
		int stride = mesh.getVertexLayout().getStride();
				
		
		
		BindingDescription bindingDescription = new BindingDescription(0, stride);
		AttributeDescription[] attributeDescriptions = new AttributeDescription[] {
				new AttributeDescription(0, 0, Format.R32G32B32_SFLOAT, 0),
				new AttributeDescription(1, 0, Format.R32G32B32_SFLOAT, 12),
		};
		
		return null;
	}
	
	public static int getImageType(ImageType type) {
		switch (type) {
			case TYPE_1D:
				return VK_IMAGE_TYPE_1D;
			case TYPE_2D:
				return VK_IMAGE_TYPE_2D;
			case TYPE_3D:
				return VK_IMAGE_TYPE_3D;
			default:
				throw new UnsupportedOperationException();
		}
	}
	
	public static int getFormat(Format format) {
		switch (format) {
			case R32_SFLOAT:
				return VK_FORMAT_R32_SFLOAT;
			case R32G32_SFLOAT:
				return VK_FORMAT_R32G32_SFLOAT;
			case R32G32B32_SFLOAT:
				return VK_FORMAT_R32G32B32_SFLOAT;
			case R32G32B32A32_SFLOAT:
				return VK_FORMAT_R32G32B32A32_SFLOAT;

			case R32_SINT:
				return VK_FORMAT_R32_SINT;
			case R32G32_SINT:
				return VK_FORMAT_R32G32_SINT;
			case R32G32B32_SINT:
				return VK_FORMAT_R32G32B32_SINT;
			case R32G32B32A32_SINT:
				return VK_FORMAT_R32G32B32A32_SINT;

			case R8_UNORM:
				return VK_FORMAT_R8_UNORM;
			case R8G8_UNORM:
				return VK_FORMAT_R8G8_UNORM;
			case R8G8B8_UNORM:
				return VK_FORMAT_R8G8B8_UNORM;
			case R8G8B8A8_UNORM:
				return VK_FORMAT_R8G8B8A8_UNORM;
				
			case B8G8R8_UNORM:
				return VK_FORMAT_B8G8R8_UNORM;
			case B8G8R8A8_UNORM:
				return VK_FORMAT_B8G8R8A8_UNORM;
				
			case D32_SFLOAT:
				return VK_FORMAT_D32_SFLOAT;
			default:
				throw new UnsupportedOperationException();
		}
	}
	
	public static int getCullMode(CullFaceSide cullFaceSide) {
		switch (cullFaceSide) {
			case BACK:
				return VK_CULL_MODE_BACK_BIT;
			case FRONT:
				return VK_CULL_MODE_FRONT_BIT;
			case FRONT_AND_BACK:
				return VK_CULL_MODE_FRONT_AND_BACK;
			case NONE:
				return VK_CULL_MODE_NONE;
			default:
				throw new UnsupportedOperationException();
		}
	}
	
	public static int getFrontFaceVertexWinding(FrontFaceVertexWinding vertexWinding) {
		switch (vertexWinding) {
			case COUNTER_CLOCKWISE:
				return VK_FRONT_FACE_COUNTER_CLOCKWISE;
			case CLOCKWISE:
				return VK_FRONT_FACE_CLOCKWISE;
			default:
				throw new UnsupportedOperationException();
		}
	}

	public static int getPolygonDrawMode(PolygonDrawMode drawMode) {
		switch (drawMode) {
			case FILL:
				return VK_POLYGON_MODE_FILL;
			case LINE:
				return VK_POLYGON_MODE_LINE;
			case POINT:
				return VK_POLYGON_MODE_POINT;
			default:
				throw new UnsupportedOperationException();
		}
	}

	public static int getTopology(PrimitiveTopology topology) {
		switch (topology) {
			case POINT_LIST:
				return VK_PRIMITIVE_TOPOLOGY_POINT_LIST;
			case LINE_LIST:
				return VK_PRIMITIVE_TOPOLOGY_LINE_LIST;
			case LINE_STRIP:
				return VK_PRIMITIVE_TOPOLOGY_LINE_STRIP;
			case TRIANGLE_LIST:
				return VK_PRIMITIVE_TOPOLOGY_TRIANGLE_LIST;
			case TRIANGLE_STRIP:
				return VK_PRIMITIVE_TOPOLOGY_TRIANGLE_STRIP;
			case TRIANGLE_FAN:
				return VK_PRIMITIVE_TOPOLOGY_TRIANGLE_FAN;
			case LINE_LIST_WITH_ADJACENCY:
				return VK_PRIMITIVE_TOPOLOGY_LINE_LIST_WITH_ADJACENCY;
			case LINE_STRIP_WITH_ADJACENCY:
				return VK_PRIMITIVE_TOPOLOGY_LINE_STRIP_WITH_ADJACENCY;
			case TRIANGLE_LIST_WITH_ADJACENCY:
				return VK_PRIMITIVE_TOPOLOGY_TRIANGLE_LIST_WITH_ADJACENCY;
			case TRIANGLE_STRIP_WITH_ADJACENCY:
				return VK_PRIMITIVE_TOPOLOGY_TRIANGLE_STRIP_WITH_ADJACENCY;
			case PATCH_LIST:
				return VK_PRIMITIVE_TOPOLOGY_PATCH_LIST;
			default:
				throw new UnsupportedOperationException();
		}
	}
	
    /**
     * Translates a Vulkan {@code VkResult} value to a String describing the result.
     * 
     * @param result
     *            the {@code VkResult} value
     * 
     * @return the result description
     */
    public static String translateVulkanResult(int result) {
        switch (result) {
        // Success codes
        case VK_SUCCESS:
            return "Command successfully completed.";
        case VK_NOT_READY:
            return "A fence or query has not yet completed.";
        case VK_TIMEOUT:
            return "A wait operation has not completed in the specified time.";
        case VK_EVENT_SET:
            return "An event is signaled.";
        case VK_EVENT_RESET:
            return "An event is unsignaled.";
        case VK_INCOMPLETE:
            return "A return array was too small for the result.";
        case VK_SUBOPTIMAL_KHR:
            return "A swapchain no longer matches the surface properties exactly, but can still be used to present to the surface successfully.";

            // Error codes
        case VK_ERROR_OUT_OF_HOST_MEMORY:
            return "A host memory allocation has failed.";
        case VK_ERROR_OUT_OF_DEVICE_MEMORY:
            return "A device memory allocation has failed.";
        case VK_ERROR_INITIALIZATION_FAILED:
            return "Initialization of an object could not be completed for implementation-specific reasons.";
        case VK_ERROR_DEVICE_LOST:
            return "The logical or physical device has been lost.";
        case VK_ERROR_MEMORY_MAP_FAILED:
            return "Mapping of a memory object has failed.";
        case VK_ERROR_LAYER_NOT_PRESENT:
            return "A requested layer is not present or could not be loaded.";
        case VK_ERROR_EXTENSION_NOT_PRESENT:
            return "A requested extension is not supported.";
        case VK_ERROR_FEATURE_NOT_PRESENT:
            return "A requested feature is not supported.";
        case VK_ERROR_INCOMPATIBLE_DRIVER:
            return "The requested version of Vulkan is not supported by the driver or is otherwise incompatible for implementation-specific reasons.";
        case VK_ERROR_TOO_MANY_OBJECTS:
            return "Too many objects of the type have already been created.";
        case VK_ERROR_FORMAT_NOT_SUPPORTED:
            return "A requested format is not supported on this device.";
        case VK_ERROR_SURFACE_LOST_KHR:
            return "A surface is no longer available.";
        case VK_ERROR_NATIVE_WINDOW_IN_USE_KHR:
            return "The requested window is already connected to a VkSurfaceKHR, or to some other non-Vulkan API.";
        case VK_ERROR_OUT_OF_DATE_KHR:
            return "A surface has changed in such a way that it is no longer compatible with the swapchain, and further presentation requests using the "
                    + "swapchain will fail. Applications must query the new surface properties and recreate their swapchain if they wish to continue" + "presenting to the surface.";
        case VK_ERROR_INCOMPATIBLE_DISPLAY_KHR:
            return "The display used by a swapchain does not use the same presentable image layout, or is incompatible in a way that prevents sharing an" + " image.";
        case VK_ERROR_VALIDATION_FAILED_EXT:
            return "A validation layer found an error.";
        default:
            return String.format("%s [%d]", "Unknown", Integer.valueOf(result));
        }
    }

}
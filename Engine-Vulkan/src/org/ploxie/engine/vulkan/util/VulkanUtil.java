package org.ploxie.engine.vulkan.util;



import static org.lwjgl.vulkan.VK10.*;

import org.ploxie.engine2.pipeline.Pipeline;
import org.ploxie.engine2.pipeline.data.CullFaceSide;
import org.ploxie.engine2.pipeline.data.Format;
import org.ploxie.engine2.pipeline.data.FrontFaceVertexWinding;
import org.ploxie.engine2.pipeline.data.ImageType;
import org.ploxie.engine2.pipeline.data.PolygonDrawMode;
import org.ploxie.engine2.pipeline.data.PrimitiveTopology;
import org.ploxie.vulkan.pipeline.VulkanGraphicsPipelineProperties;

public class VulkanUtil {
	
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
	
}

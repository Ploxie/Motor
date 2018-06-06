package org.ploxie.engine2.model;

import static org.lwjgl.vulkan.VK10.VK_FORMAT_R32G32B32_SFLOAT;

import java.util.ArrayList;
import java.util.List;

import org.ploxie.engine2.buffer.vertex.AttributeDescription;
import org.ploxie.engine2.buffer.vertex.BindingDescription;
import org.ploxie.engine2.buffer.vertex.VertexInputInfo;
import org.ploxie.engine2.model.Vertex.VertexLayout;
import org.ploxie.engine2.pipeline.data.Format;

public class Mesh{

	private Vertex[] vertices;
	private int[] indices;
	private int instances;
	private VertexLayout vertexLayout;
	private boolean tangentSpace = false;
	private boolean instanced = false;
	private VertexInputInfo inputInfo;
	
	public Mesh(Vertex[] vertices, int[] indices)
	{
		this.vertices = vertices;
		this.indices = indices;
	}

	public Vertex[] getVertices() {
		return vertices;
	}

	public void setVertices(Vertex[] vertices) {
		this.vertices = vertices;
	}

	public int[] getIndices() {
		return indices;
	}

	public void setIndices(int[] indices) {
		this.indices = indices;
	}

	public boolean isTangentSpace() {
		return tangentSpace;
	}

	public void setTangentSpace(boolean tangentSpace) {
		this.tangentSpace = tangentSpace;
	}

	public boolean isInstanced() {
		return instanced;
	}

	public void setInstanced(boolean instanced) {
		this.instanced = instanced;
	}

	public int getInstances() {
		return instances;
	}

	public void setInstances(int instances) {
		this.instances = instances;
	}

	public VertexLayout getVertexLayout() {
		return vertexLayout;
	}

	public void setVertexLayout(VertexLayout vertexLayout) {
		this.vertexLayout = vertexLayout;
		
		int stride = vertexLayout.getStride();		
		BindingDescription bindingDescription = new BindingDescription(0, stride);
		AttributeDescription[] attributeDescriptions = null;
		
		switch(vertexLayout) {
		case POS:
			attributeDescriptions = new AttributeDescription[] {
					new AttributeDescription(0, 0, Format.R32G32B32_SFLOAT, 0),
			};
			break;
		case POS2D:
			attributeDescriptions = new AttributeDescription[] {
					new AttributeDescription(0, 0, Format.R32G32_SFLOAT, 0),
			};
			break;
		case POS_NORMAL:
			attributeDescriptions = new AttributeDescription[] {
					new AttributeDescription(0, 0, Format.R32G32B32_SFLOAT, 0),
					new AttributeDescription(1, 0, Format.R32G32B32_SFLOAT, 12),
			};
			break;
		case POS_NORMAL_UV:
			attributeDescriptions = new AttributeDescription[] {
					new AttributeDescription(0, 0, Format.R32G32B32_SFLOAT, 0),
					new AttributeDescription(1, 0, Format.R32G32B32_SFLOAT, 12),
					new AttributeDescription(2, 0, Format.R32G32_SFLOAT, 24),
			};
			break;
		case POS_NORMAL_UV_TAN_BITAN:
			attributeDescriptions = new AttributeDescription[] {
					new AttributeDescription(0, 0, Format.R32G32B32_SFLOAT, 0),
					new AttributeDescription(1, 0, Format.R32G32B32_SFLOAT, 12),
					new AttributeDescription(2, 0, Format.R32G32_SFLOAT, 24),
					new AttributeDescription(3, 0, Format.R32G32B32_SFLOAT, 32),
					new AttributeDescription(4, 0, Format.R32G32B32_SFLOAT, 44),
			};
			break;
		case POS_UV:
			attributeDescriptions = new AttributeDescription[] {
					new AttributeDescription(0, 0, Format.R32G32B32_SFLOAT, 0),
					new AttributeDescription(1, 0, Format.R32G32_SFLOAT, 12),
			};
			break;		
		}
		
		this.inputInfo = new VertexInputInfo(bindingDescription, attributeDescriptions);		
	}
	
	public VertexInputInfo getVertexInputInfo() {
		return inputInfo;
	}
}

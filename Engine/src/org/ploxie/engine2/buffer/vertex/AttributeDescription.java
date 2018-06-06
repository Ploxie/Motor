package org.ploxie.engine2.buffer.vertex;

import org.ploxie.engine2.pipeline.data.Format;

import lombok.Data;

@Data
public class AttributeDescription {

	/**
	 * location is the shader binding location number for this attribute.
	 */
	private final int location;
	/**
	 * binding is the binding number which this attribute takes its data from.
	 */
	private final int binding;
	/**
	 * format is the size and type of the vertex attribute data.
	 */
	private final Format format;
	/**
	 * offset is a byte offset of this attribute relative to the start of an
	 * element in the vertex input binding.
	 */
	private final int offset;

}

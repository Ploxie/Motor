package org.ploxie.engine2.pipeline.data;

public enum Format {

	R16_UINT,
	R32_UINT,
	
	//float
	R32_SFLOAT,
	//vec2
	R32G32_SFLOAT,
	//vec3
	R32G32B32_SFLOAT,
	//vec4
	R32G32B32A32_SFLOAT,

	//int
	R32_SINT,
	//ivec2
	R32G32_SINT,
	//ivec3
	R32G32B32_SINT,
	//ivec4
	R32G32B32A32_SINT,

	R8_UNORM,
	R8G8_UNORM,
	R8G8B8_UNORM,
	R8G8B8A8_UNORM,

	B8G8R8_UNORM,
	B8G8R8A8_UNORM,
	
	D32_SFLOAT

}

package org.ploxie.engine2.pipeline.shader;

public abstract class ShaderModules {

	public abstract ShaderModule getVertex();
	public abstract ShaderModule getFragment();

	public int getShaderCount() {
		int count = 0;
		if (getVertex() != null) {
			count++;
		}
		if (getFragment() != null) {
			count++;
		}
		return count;
	}

}

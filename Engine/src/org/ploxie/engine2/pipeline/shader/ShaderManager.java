package org.ploxie.engine2.pipeline.shader;

import java.util.HashMap;
import java.util.Map;

import org.ploxie.engine2.context.EngineContext;

public class ShaderManager {

	private static Map<String, ShaderModules> shaders = new HashMap<>();
	
	public static ShaderModules loadShader(String name) {
		return EngineContext.getInstance().getGraphicsToolkit().loadShader(name);
	}
	
}

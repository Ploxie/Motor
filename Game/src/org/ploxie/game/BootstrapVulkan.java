package org.ploxie.game;

import java.io.IOException;
import org.ploxie.engine.vulkan.VulkanRenderEngine;
import org.ploxie.engine.vulkan.context.VulkanContext;
import org.ploxie.engine2.CoreEngine;
import org.ploxie.engine2.CoreSystem;
import org.ploxie.engine2.RenderEngine;

public class BootstrapVulkan {

	public static void main(String[] args) throws IOException {
						
		boolean useRenderDoc = false;
		
		if(useRenderDoc) {
			System.in.read();
		}

		CoreEngine engine = new CoreEngine();
		CoreSystem system = new CoreSystem();
		RenderEngine renderEngine = null;
		
		VulkanContext.initialize();
		renderEngine = new VulkanRenderEngine();	
		
		system.setRenderEngine(renderEngine);
		engine.init(system);
		engine.start();
	}

}

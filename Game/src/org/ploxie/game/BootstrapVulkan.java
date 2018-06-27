package org.ploxie.game;

import java.io.IOException;
import org.ploxie.engine.vulkan.VulkanRenderEngine;
import org.ploxie.engine.vulkan.context.VulkanContext;
import org.ploxie.engine2.CoreEngine;
import org.ploxie.engine2.CoreSystem;
import org.ploxie.engine2.RenderEngine;
import org.ploxie.game.scene.GameScene;

public class BootstrapVulkan {

	public static void main(String[] args) throws IOException {
						
		boolean useRenderDoc = false;
		
		if(useRenderDoc) {
			System.in.read();
		}

		CoreEngine engine = new CoreEngine();
		CoreSystem system = new CoreSystem();
		
		VulkanContext.create();
		RenderEngine renderEngine = new VulkanRenderEngine();
		
		renderEngine.setSceneGraph(new GameScene());
		
		system.setRenderEngine(renderEngine);
		
		engine.initialize(system);
		engine.start();
	}

}

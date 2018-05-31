package org.ploxie.game;

import org.ploxie.engine.opengl.GLRenderEngine;
import org.ploxie.engine.opengl.context.GLContext;
import org.ploxie.engine.vulkan.VulkanRenderEngine;
import org.ploxie.engine.vulkan.context.VulkanContext;
import org.ploxie.engine2.CoreEngine;
import org.ploxie.engine2.CoreSystem;

public class Bootstrap {

	public static void main(String[] args) {
		/*
		 * Window window = new Window(); Engine engine = new Engine(); IGraphicsManager
		 * graphicsManager = new VulkanManager();
		 * 
		 * engine.init(window, graphicsManager);
		 * 
		 * 
		 * 
		 * window.setTitle("Game"); window.setMode(WindowMode.WINDOWED);
		 * window.setDimensions(new Vector2i(1024, 768)); window.setSamples(4);
		 * window.setVsync(false); window.setClearColor(new Color(1,0,0,1));
		 * 
		 * engine.pushGameState(new InGameState());
		 * 
		 * engine.start();
		 */

		boolean useGL = false;
		if (useGL) {
			GLContext.initialize();

			GLRenderEngine renderEngine = new GLRenderEngine();
			CoreEngine engine = new CoreEngine();

			CoreSystem system = new CoreSystem();
			system.setRenderEngine(renderEngine);
			engine.init(system);

			engine.start();
		} else {
			VulkanContext.initialize();

			VulkanRenderEngine renderEngine = new VulkanRenderEngine();
			CoreEngine engine = new CoreEngine();

			CoreSystem system = new CoreSystem();
			system.setRenderEngine(renderEngine);
			engine.init(system);

			engine.start();
		}
	}

}

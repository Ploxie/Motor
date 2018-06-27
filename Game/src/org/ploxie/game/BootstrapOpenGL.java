package org.ploxie.game;

import java.io.IOException;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.ploxie.engine.opengl.GLRenderEngine;
import org.ploxie.engine.opengl.context.GLContext;
import org.ploxie.engine.vulkan.VulkanRenderEngine;
import org.ploxie.engine.vulkan.context.VulkanContext;
import org.ploxie.engine2.CoreEngine;
import org.ploxie.engine2.CoreSystem;
import org.ploxie.engine2.RenderEngine;
import org.ploxie.game.scene.GameScene;

public class BootstrapOpenGL {

	public static void main(String[] args) throws IOException {
				
		boolean useRenderDoc = false;
		
		if(useRenderDoc) {
			System.in.read();
		}

		CoreEngine engine = new CoreEngine();
		CoreSystem system = new CoreSystem();
		RenderEngine renderEngine = null;
		
		GLContext.create();
		renderEngine = new GLRenderEngine();
		
		renderEngine.setSceneGraph(new GameScene());
		
		system.setRenderEngine(renderEngine);
		engine.initialize(system);
		engine.start();
	}

}

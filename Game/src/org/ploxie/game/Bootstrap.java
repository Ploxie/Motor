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

public class Bootstrap {

	public static void main(String[] args) throws IOException {
				
		boolean useRenderDoc = false;
		
		if(useRenderDoc) {
			System.in.read();
		}

		CoreEngine engine = new CoreEngine();
		CoreSystem system = new CoreSystem();
		RenderEngine renderEngine = null;
		
		boolean useGL = false;
		if (useGL) {
			GLContext.initialize();
			renderEngine = new GLRenderEngine();
		} else {
			VulkanContext.initialize();
			renderEngine = new VulkanRenderEngine();	
		}
		
		system.setRenderEngine(renderEngine);
		engine.init(system);
		engine.start();
	}

}

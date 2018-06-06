package org.ploxie.engine.opengl.context;

import org.lwjgl.opengl.GL;
import org.ploxie.engine.opengl.display.GLWindow;
import org.ploxie.engine2.context.EngineContext;

public class GLContext extends EngineContext{
	
	public static void initialize() {
		EngineContext.initialize();
		window = new GLWindow();		
	}
	
	public static GLWindow getWindow() {
		return (GLWindow) window;
	}
	
}

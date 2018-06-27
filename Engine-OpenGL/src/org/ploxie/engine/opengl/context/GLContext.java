package org.ploxie.engine.opengl.context;

import org.ploxie.engine.opengl.display.GLWindow;
import org.ploxie.engine2.context.EngineContext;

public class GLContext extends EngineContext {

	protected GLContext() {

	}

	public static GLContext create() {
		instance = new GLContext();
		getInstance().initialize();
		
		return (GLContext) instance;
	}

	public static GLContext getInstance() {
		return (GLContext) instance;
	}

	public void initialize() {
		super.initialize();
		window = new GLWindow();
		graphicsToolkit = new GLToolkit();
	}

	public GLWindow getWindow() {
		return (GLWindow) window;
	}

}

package org.ploxie.engine.graphics;

import org.ploxie.engine.display.IDrawSurface;

public interface IGraphicsManager {

	public boolean initialize(IDrawSurface drawSurface);
	
	public void update();
	
	public IRenderer getRenderer();
	
	public void setDrawSurface(IDrawSurface drawSurface);
	public IDrawSurface getDrawSurface();
	
}

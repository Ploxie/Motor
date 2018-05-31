package org.ploxie.engine.event.events;

import org.ploxie.engine.event.EventListener;

public interface ObjectRenderedListener extends EventListener{

	public void onObjectRender(ObjectRenderedEvent event);
	
}

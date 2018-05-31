package org.ploxie.engine.event.events;

import org.ploxie.engine.event.Event;
import org.ploxie.engine.event.EventListener;

public class ObjectRenderedEvent extends Event{

	private final int vertexCount;
	
	public ObjectRenderedEvent(int vertexCount) {
		this.vertexCount = vertexCount;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
	
	@Override
	protected void dispatch(EventListener listener) {
		if((listener instanceof ObjectRenderedListener)) {
			ObjectRenderedListener oListener = (ObjectRenderedListener) listener;
			oListener.onObjectRender(this);
		}
	}

}

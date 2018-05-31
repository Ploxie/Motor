package org.ploxie.utils;

import org.ploxie.utils.math.vector.Vector2i;

public class Viewport {

	private Vector2i location;
	private Vector2i dimensions;
	
	public Viewport(int left, int right, int top, int bottom) {
		location = new Vector2i(left, top);
		dimensions = new Vector2i(right, bottom);
	}
	
	public Vector2i getLocation() {
		return location;
	}
	
	public Vector2i getDimensions() {
		return dimensions;
	}
	
	public Vector2i pointToScreen(Vector2i pixel) {
		return pixel.clone().divide(dimensions);
	}
	
	public Vector2i screenToPoint(Vector2i screen) {
		return screen.clone().multiply(dimensions);
	}
	
}

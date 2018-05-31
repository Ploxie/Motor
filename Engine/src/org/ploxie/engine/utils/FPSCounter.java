package org.ploxie.engine.utils;

import org.lwjgl.glfw.GLFW;

public class FPSCounter {

	private int fpsCounter;
	private int fps = 1;
	private long lastFPS = System.nanoTime();
	private long lastFrame = System.nanoTime();
	private long delta;

	/**
	 * Updates average FPS every 1000 MS
	 */
	public void update() {
		
		long time = System.nanoTime();
		
		delta = (time - lastFrame) / 1000;
		
		if ((time - lastFPS) > 1000000000) {
			fps = fpsCounter;
			fpsCounter = 0;
			lastFPS = time;
		}
		fpsCounter++;
		lastFrame = time;
	}

	public long getTime() {
		return System.nanoTime();
	}
	
	public long getDelta() {
		return delta;
	}

	public int getFPS() {
		return fps;
	}
	

	
	
}

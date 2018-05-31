package org.ploxie.engine2;

import org.ploxie.engine2.context.EngineContext;

public class CoreEngine {

	private static final long NANOSECOND = 1000000000;

	private static int fps;
	private static float framerate = 200;
	private static float frameTime = 1.0f / framerate;
	private boolean isRunning;
	private CoreSystem system;

	public void init(CoreSystem system) {
		this.system = system;
		system.initialize();
	}

	public void start() {
		if (isRunning) {
			return;
		}

		run();
	}

	private void run() {
		isRunning = true;

		this.isRunning = true;

		int frames = 0;
		long frameCounter = 0;

		long lastTime = System.nanoTime();
		double unprocessedTime = 0;

		// Rendering Loop
		while (isRunning) {
			boolean render = false;

			long startTime = System.nanoTime();
			long passedTime = startTime - lastTime;
			lastTime = startTime;

			unprocessedTime += passedTime / (double) NANOSECOND;
			frameCounter += passedTime;

			while (unprocessedTime > frameTime) {
				render = true;
				unprocessedTime -= frameTime;

				if (EngineContext.getWindow().isCloseRequested()) {
					stop();
				}

				system.update();

				if (frameCounter >= NANOSECOND) {
					setFps(frames);
					frames = 0;
					frameCounter = 0;
				}
			}
			if (render) {
				// System.out.println(fps);
				system.render();
				frames++;
			} else {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		system.shutdown();

	}

	private void stop() {
		if (!isRunning)
			return;

		isRunning = false;
	}
	
	public static float getFrameTime() {
		return frameTime;
	}

	public static int getFps() {
		return fps;
	}

	public static void setFps(int fps) {
		CoreEngine.fps = fps;
	}

}

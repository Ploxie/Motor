package org.ploxie.engine2.display;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


public abstract class Window {

	@Getter
	private long handle;
	@Getter
	@Setter
	private int width;
	@Getter
	@Setter
	private int height;
	@Getter
	@Setter
	private String title;
	

	public abstract void create();

	public abstract void draw();

	public abstract void shutdown();

	public abstract boolean isCloseRequested();

	public abstract void resize(int x, int y);

	public Window(String title, int width, int height) {
		this.width = width;
		this.height = height;
		this.title = title;
	}
	
	protected void setHandle(long id) {
		this.handle = id;
	}	
	
}

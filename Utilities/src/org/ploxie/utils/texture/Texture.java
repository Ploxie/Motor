package org.ploxie.utils.texture;


public interface Texture {
	
	public void bind();

	public void delete();

	public void unbind();

	public void noFilter();

	public void bilinearFilter();

	public void trilinearFilter();

	public void anisotropicFilter();

	public void clampToEdge();

	public void repeat();

	public int getWidth();

	public int getHeight();
}

package org.ploxie.engine.model.materials;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.ploxie.engine.shaders.Shader;
import org.ploxie.utils.texture.Texture;

public class TextMaterial extends Material{

	private Texture texture;
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	@Override
	public void applyShader(Shader shader) {
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glActiveTexture(GL_TEXTURE0 );
		texture.bind();
		texture.bilinearFilter();
		shader.setUniform("bitmapSampler", 0);
	}

}

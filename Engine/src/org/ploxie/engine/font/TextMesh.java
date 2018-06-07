package org.ploxie.engine.font;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;

import org.ploxie.engine.model.Model;
import org.ploxie.engine.model.materials.TextMaterial;
import org.ploxie.engine.utils.BufferUtils;
import org.ploxie.opengl.buffer.VertexBufferObject;
import org.ploxie.opengl.buffer.VertexBufferObject.BufferType;
import org.ploxie.utils.VertexStream;
import org.ploxie.utils.math.vector.Vector2f;
import org.ploxie.utils.math.vector.Vector3f;
import org.ploxie.utils.texture.Bitmap;

public class TextMesh extends Model{
	
	private final Bitmap bitmap;

	public TextMesh(final Bitmap bitmap, final CharSequence text) {
		super(null, new TextMaterial());
		this.bitmap = bitmap;
		
		create(text, true);
	}
	
	public TextMesh(final Bitmap bitmap) {
		super(null, new TextMaterial());
		this.bitmap = bitmap;
				
		create("", false);
	}
	

	private void create(final CharSequence text, boolean drawStatic) {
		VertexStream vertexStream = new VertexStream(text.length() * 4 * 3);
		VertexStream uvStream = new VertexStream(text.length() * 4 * 2);
		List<Integer> indices = new ArrayList<Integer>();

		float fontHeight = bitmap.getFontMetrics().getHeight();

		float currentWidth = 0;
		float currentX = 0;
		float currentY = 0;

		for (int i = 0; i < text.length(); i++) {
			char character = text.charAt(i);
			currentWidth = bitmap.getFontMetrics().stringWidth(String.valueOf(character));
			addQuad(vertexStream, currentX, currentY, currentWidth, fontHeight);
			addQuadIndices(i, indices);
			addUV(uvStream, character, currentWidth, fontHeight);
			currentX += currentWidth;
		}

		int usage = drawStatic ? GL_STATIC_DRAW : GL_DYNAMIC_DRAW;
		
		vbo = new VertexBufferObject();
		vbo.setIndexBufferData(indices.stream().mapToInt(i -> i).toArray(), usage);
		vbo.setBufferData(BufferType.VERTEX, BufferUtils.createFlippedBuffer(vertexStream.getBuffer()), 3, usage);
		vbo.setBufferData(BufferType.UV, BufferUtils.createFlippedBuffer(uvStream.getBuffer()), 2, usage);
	}

	private VertexStream addQuad(VertexStream stream, final float x, final float y, final float width,
			final float height) {
		stream.append(new Vector3f(x, y, 0));
		stream.append(new Vector3f(x, y + height, 0));
		stream.append(new Vector3f(x + width, y, 0));
		stream.append(new Vector3f(x + width, y + height, 0));
		return stream;
	}

	private List<Integer> addQuadIndices(int quadIndex, List<Integer> currentList) {
		quadIndex *= 4;
		currentList.add(quadIndex);
		currentList.add(quadIndex + 1);
		currentList.add(quadIndex + 2);

		currentList.add(quadIndex + 2);
		currentList.add(quadIndex + 1);
		currentList.add(quadIndex + 3);

		return currentList;
	}

	private VertexStream addUV(VertexStream stream, final char character, float width, float height) {
		Vector2f texCoords = bitmap.getCharacterTextureCoordinates(character);
		Vector2f texDimensions = bitmap.getCharacterTextureDimensions(new Vector2f(width, height));		
		
		stream.append(new Vector2f(texCoords.x, texCoords.y));
		stream.append(new Vector2f(texCoords.x, texCoords.y + texDimensions.y));
		stream.append(new Vector2f(texCoords.x + texDimensions.x, texCoords.y));
		stream.append(new Vector2f(texCoords.x + texDimensions.x, texCoords.y + texDimensions.y));
		return stream;
	}
	
	public void setText(final CharSequence text) {
		create(text, false);
	}

}

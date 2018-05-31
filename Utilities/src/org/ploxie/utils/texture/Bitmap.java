package org.ploxie.utils.texture;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import org.ploxie.utils.CacheMap;
import org.ploxie.utils.math.vector.Vector2f;

public class Bitmap {

	public enum TextAlignment {
		TOP, BOTTOM, BASELINE, MIDDLE
	}

	private static final String DEFAULT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 .,/:'\"[]\\;-=|";
	private static CacheMap<String, Bitmap> BITMAP_CACHE = new CacheMap<String, Bitmap>(32);

	private final FontMetrics fontMetrics;
	private final String characters;
	private final boolean antiAliased;

	private final Vector2f characterDimensions;
	private final int charactersPerRow;

	private int descLine;
	private BufferedImage bitmapImage;

	public Bitmap(final FontMetrics fontMetrics, final String characters, final boolean antiAliased,
			final BufferedImage bitmapImage, final Vector2f characterDimensions) {
		this.fontMetrics = fontMetrics;
		this.characters = characters;
		this.antiAliased = antiAliased;
		this.characterDimensions = characterDimensions;
		this.bitmapImage = bitmapImage;
		this.charactersPerRow = (int) Math.ceil(Math.sqrt(characters.length()));
	}

	public Font getFont() {
		return fontMetrics.getFont();
	}

	public FontMetrics getFontMetrics() {
		return fontMetrics;
	}

	public int getDescLine() {
		return descLine;
	}

	public String getCharacters() {
		return characters;
	}

	public boolean isAntiAliased() {
		return antiAliased;
	}

	/**
	 * Gets coordinates of a character in texture-space [0..1]
	 * 
	 * @param c
	 * @return
	 */
	public Vector2f getCharacterTextureCoordinates(final char c) {
		final int index = characters.indexOf(c);
		final int row = (index / charactersPerRow);
		final int column = index - (row * charactersPerRow);

		final Vector2f pixelSpace = new Vector2f(column * characterDimensions.x(), row * characterDimensions.y());
		final Vector2f textureSpace = pixelSpace.divide(new Vector2f(bitmapImage.getWidth(), bitmapImage.getHeight()));
		return textureSpace;
	}

	/**
	 * Gets dimensions of a character in texture-space [0..1]
	 * 
	 * @param c
	 * @return
	 */
	public Vector2f getCharacterTextureDimensions() {
		final Vector2f pixelSpace = characterDimensions.clone();
		final Vector2f textureSpace = pixelSpace.divide(new Vector2f(bitmapImage.getWidth(), bitmapImage.getHeight()));
		return textureSpace;
	}

	public Vector2f getCharacterTextureDimensions(Vector2f dimensions) {
		final Vector2f pixelSpace = dimensions.clone();
		final Vector2f textureSpace = pixelSpace.divide(new Vector2f(bitmapImage.getWidth(), bitmapImage.getHeight()));
		return textureSpace;
	}

	public Vector2f getCharacterDimensions() {
		return characterDimensions;
	}

	public Vector2f getDistanceToOrigin(final TextAlignment alignment) {
		final int x = (int) Math.floor((characterDimensions.x() / 1.25f * 0.125f));
		int y = 0;
		if (alignment == TextAlignment.TOP) {
			y = 0;
		} else if (alignment == TextAlignment.BOTTOM) {
			y = getFontMetrics().getMaxAscent() + getFontMetrics().getMaxDescent();
		} else if (alignment == TextAlignment.BASELINE) {
			y = getFontMetrics().getMaxAscent();
		} else if (alignment == TextAlignment.MIDDLE) {
			y = (getFontMetrics().getMaxAscent() + getFontMetrics().getMaxDescent()) / 2;
		}
		return new Vector2f(x, y);
	}

	public static Bitmap create(final Font font, final boolean antiAliased) {
		return create(font, DEFAULT_CHARACTERS, antiAliased);
	}

	public static Bitmap create(final Font font, final CharSequence characters, final boolean antiAliased) {
		final String key = font.getFontName() + "_" + font.getSize2D() + "_" + font.getStyle() + "_" + characters + "_"
				+ antiAliased;
		final Bitmap cached = BITMAP_CACHE.get(key);
		if (cached != null) {
			BITMAP_CACHE.use(key);
			return cached;
		}

		final int amountOfCharacters = characters.length();
		final int charactersPerRow = (int) Math.ceil(Math.sqrt(amountOfCharacters));

		final int characterWidthPixels = (int) Math.ceil(font.getSize2D() * 1.25F);
		final int characterHeightPixels = characterWidthPixels;

		int originalDimensions = characterWidthPixels * charactersPerRow;
		int dimensions = originalDimensions;

		final BufferedImage image = new BufferedImage(dimensions, dimensions, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g = (Graphics2D) image.getGraphics();
		g.setFont(font);
		if (antiAliased) {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}

		int descLine = g.getFontMetrics().getMaxDescent();
				
		for (int i = 0; i < amountOfCharacters; i++) {
			int y = (i / charactersPerRow);
			int x = i - (y * charactersPerRow);

			final String toDraw = String.valueOf(characters.charAt(i));

			int xImage = (int) (x * characterWidthPixels /* + Math.floor((characterWidthPixels / 1.25f * 0.125f)) */);
			int yImage = (y + 1) * characterHeightPixels - descLine;

			g.setColor(Color.WHITE);
			g.drawString(toDraw, xImage, yImage);
		}

		final Bitmap bitmap = new Bitmap(g.getFontMetrics(), characters.toString(), antiAliased, image,
				new Vector2f(characterWidthPixels, characterHeightPixels));
		BITMAP_CACHE.put(key, bitmap);
		return bitmap;
	}
	
	public BufferedImage getImage() {
		return bitmapImage;
	}

}

package org.ploxie.utils;

import java.nio.FloatBuffer;

import org.ploxie.utils.math.vector.Vector3f;


public class Color {

	public static final Color WHITE = new Color(1,1,1,1);
	public static final Color RED = new Color(1,0,0,1);
	public static final Color GREEN = new Color(0,1,0,1);
	public static final Color BLUE = new Color(0,0,1,1);
	public static final Color NO_COLOR = new Color(0,0,0,0);
	public static final Color BLACK = new Color(0,0,0, 1);
	
	private Vector3f col = new Vector3f();
	private float a = 1;

	public Color(float r, float g, float b) {
		col.xyz(r, g, b);
	}

	public Color(Vector3f color) {
		col.xyz(color);
	}

	public Color(float r, float g, float b, float a) {
		col.xyz(r, g, b);
		this.a = a;
	}

	public Color(float r, float g, float b, boolean convert) {
		if (convert) {
			col.xyz(r / 255f, g / 255f, b / 255f);
		} else {
			col.xyz(r, g, b);
		}
	}

	public Color() {
	}

	public Vector3f getVector() {
		return col;
	}

	public byte[] getAsBytes() {
		int r = (int) (col.x * 255);
		int g = (int) (col.y * 255);
		int b = (int) (col.z * 255);
		int alpha = (int) (a * 255);
		return new byte[] { (byte) r, (byte) g, (byte) b, (byte) alpha };
	}

	/*public FloatBuffer getAsFloatBuffer() {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
		buffer.put(new float[] { col.x, col.y, col.z, a });
		buffer.flip();
		return buffer;
	}*/

	public float getR() {
		return col.x;
	}

	public float getG() {
		return col.y;
	}

	public float getB() {
		return col.z;
	}
	
	public float getA() {
		return a;
	}

	public Color duplicate() {
		return new Color(col.x, col.y, col.z);
	}

	public void multiplyBy(Color color) {
		this.col.x *= color.col.x;
		this.col.y *= color.col.y;
		this.col.z *= color.col.z;
	}

	public void setColor(float r, float g, float b) {
		col.xyz(r, g, b);
	}

	public void setColor(Vector3f color) {
		col.xyz(color);
	}

	public void setColor(Color color) {
		this.col.xyz(color.col);
	}

	public void setColor(float r, float g, float b, float a) {
		col.xyz(r, g, b);
		this.a = a;
	}

	public void setR(float r) {
		col.x = r;
	}

	public void setG(float g) {
		col.y = g;
	}

	public void setB(float b) {
		col.z = b;
	}

	public boolean isEqualTo(Color color) {
		return (col.x == color.col.x && col.y == color.col.y && col.z == color.col.z);
	}

	public Color scale(float value) {
		col.multiply(value);
		return this;
	}

	public String toString() {
		return ("(" + col.x + ", " + col.y + ", " + col.z + ")");
	}

	public static Color sub(Color colLeft, Color colRight, Color dest) {
		if (dest == null) {
			return new Color(colLeft.col.subtract(colRight.col));
		} else {
			dest.col = colLeft.col.subtract(colRight.col);
			return dest;
		}
	}

	public Color getUnit() {
		Color color = new Color();
		if (col.x == 0 && col.y == 0 && col.z == 0) {
			return color;
		}
		color.setColor(this);
		color.scale(1f / length());
		return color;
	}

	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}

	public float lengthSquared() {
		return col.lengthSquared();
	}

	public void setHsvColor(float hue, float sat, float value) {
		this.setColor(hsvToRgb(hue, sat, value));
	}

	public static Color hsvToRgb(float hue, float saturation, float value) {
		int h = (int) (hue * 6);
		float f = hue * 6 - h;
		float p = value * (1 - saturation);
		float q = value * (1 - f * saturation);
		float t = value * (1 - (1 - f) * saturation);
		switch (h) {
		case 0:
			return new Color(value, t, p);
		case 1:
			return new Color(q, value, p);
		case 2:
			return new Color(p, value, t);
		case 3:
			return new Color(p, q, value);
		case 4:
			return new Color(t, p, value);
		case 5:
			return new Color(value, p, q);
		default:
			return new Color();
		}
	}

	public static Color interpolateColors(Color color1, Color color2, float blend, Color dest) {
		float color1Weight = 1 - blend;
		float r = (color1Weight * color1.col.x) + (blend * color2.col.x);
		float g = (color1Weight * color1.col.y) + (blend * color2.col.y);
		float b = (color1Weight * color1.col.z) + (blend * color2.col.z);
		if (dest == null) {
			return new Color(r, g, b);
		} else {
			return dest;
		}
	}

	public static Color add(Color color1, Color color2, Color dest) {
		if (dest == null) {
			return new Color(color1.col.add(color2.col));
		} else {
			dest.col = color1.col.add(color2.col);
			return dest;
		}
	}

}

package org.ploxie.utils.math.vector;

public class Vector2f implements Cloneable {

	public float x, y;

	public Vector2f() {
		this(0, 0);
	}

	public Vector2f(float xy){
		this(xy,xy);
	}

	public Vector2f(Vector2i v){
		this(v.x(), v.y());
	}
	
	public Vector2f(Vector2f v){
		this(v.x(), v.y());
	}

	public Vector2f(final float x, final float y) {
		this.x = x;
		this.y = y;
	}

	public float x() {
		return x;
	}

	public float y() {
		return y;
	}

	public Vector2f x(final float x) {
		this.x = x;
		return this;
	}

	public Vector2f y(final float y) {
		this.y = y;
		return this;
	}
	
	public Vector2f xy(final float x, final float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Vector2f add(final Vector2f o) {
		x(x() + o.x());
		y(y() + o.y());
		return this;
	}
	
	public Vector2f subtract(final Vector2f o) {
		x(x() - o.x());
		y(y() - o.y());
		return this;
	}

	public Vector2f divide(final Vector2f o) {
		x(x() / o.x());
		y(y() / o.y());
		return this;
	}

	public Vector2f divide(final Vector2i o) {
		x(x() / o.x());
		y(y() / o.y());
		return this;
	}

	public Vector2f multiply(final Vector2f o) {
		x(x() * o.x());
		y(y() * o.y());
		return this;
	}

	public Vector2f multiply(final float s) {
		x(x() * s);
		y(y() * s);
		return this;
	}
	
	@Override
	public Vector2f clone() {
		return new Vector2f(x(), y());
	}

	@Override
	public String toString() {
		return "[x:" + x() + ", y:" + y() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Vector2f)) {
			return false;
		}
		Vector2f other = (Vector2f) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) {
			return false;
		}
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) {
			return false;
		}
		return true;
	}

}

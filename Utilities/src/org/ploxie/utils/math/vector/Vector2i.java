package org.ploxie.utils.math.vector;

import org.ploxie.utils.math.Math;

public class Vector2i implements Cloneable {

	public int x, y;

	public Vector2i() {
		this(0, 0);
	}

	public Vector2i(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public Vector2i x(final int x) {
		this.x = x;
		return this;
	}

	public Vector2i y(final int y) {
		this.y = y;
		return this;
	}

	public Vector2i xy(final int x, final int y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Vector2i xy(final Vector2i xy) {
		return xy(xy.x, xy.y);
	}

	public Vector2i add(final Vector2i o) {
		x(x() + o.x());
		y(y() + o.y());
		return this;
	}

	public Vector2i subtract(final Vector2i o) {
		x(x() - o.x());
		y(y() - o.y());
		return this;
	}

	public Vector2i divide(final Vector2i o) {
		x(x() / o.x());
		y(y() / o.y());
		return this;
	}

	public Vector2i multiply(final Vector2i o) {
		x(x() * o.x());
		y(y() * o.y());
		return this;
	}

	public float distance(Vector2i o) {
		float dX = x() - o.x();
		float dY = y() - o.y();
		return Math.sqrt(dX * dX + dY * dY);
	}

	@Override
	public Vector2i clone() {
		return new Vector2i(x(), y());
	}

	@Override
	public String toString() {
		return "[x:" + x() + ", y:" + y() + "]";
	}
	
	public Vector2f toFloat() {
		return new Vector2f(x, y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		if (!(obj instanceof Vector2i)) {
			return false;
		}
		Vector2i other = (Vector2i) obj;
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}

}

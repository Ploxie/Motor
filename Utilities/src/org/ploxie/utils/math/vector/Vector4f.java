package org.ploxie.utils.math.vector;

import org.ploxie.utils.math.Math;

public class Vector4f implements Cloneable {

	public float x, y, z, w;

	public Vector4f() {
		this(0, 0, 0, 0);
	}

	public Vector4f(final float x, final float y, final float z, final float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Vector4f(Vector3f xyz, float w) {
		this(xyz.x(), xyz.y(), xyz.z(), w);
	}

	public Vector4f(Vector2f xyz, float z, float w) {
		this(xyz.x(), xyz.y(), z, w);
	}

	public float x() {
		return x;
	}

	public float y() {
		return y;
	}

	public float z() {
		return z;
	}

	public float w() {
		return w;
	}

	public void x(final float x) {
		this.x = x;
	}

	public void y(final float y) {
		this.y = y;
	}

	public void z(final float z) {
		this.z = z;
	}

	public void w(final float w) {
		this.w = w;
	}

	public Vector3f xyz() {
		return new Vector3f(x, y, z);
	}

	public Vector4f add(final Vector4f o) {
		x(x() + o.x());
		y(y() + o.y());
		z(z() + o.z());
		w(w() + o.w());
		return this;
	}

	public Vector4f subtract(final Vector4f o) {
		x(x() - o.x());
		y(y() - o.y());
		z(z() - o.z());
		w(w() - o.w());
		return this;
	}

	public Vector4f divide(final Vector4f o) {
		x(x() / o.x());
		y(y() / o.y());
		z(z() / o.z());
		w(w() / o.w());
		return this;
	}

	public Vector4f multiply(final Vector4f o) {
		x(x() * o.x());
		y(y() * o.y());
		z(z() * o.z());
		w(w() * o.w());
		return this;
	}

	public float length() {
		return Math.sqrt(lengthSquared());
	}

	public float lengthSquared() {
		return x() * x() + y() * y() + z() * z() + w() * w();
	}

	public Vector4f normalize() {
		float len = length();
		x(x() / len);
		y(y() / len);
		z(z() / len);
		w(w() / len);
		return this;
	}

	@Override
	public Vector4f clone() {
		return new Vector4f(x(), y(), z(), w());
	}

	@Override
	public String toString() {
		return "[x:" + x() + ", y:" + y() + ", z:" + z() + ", w:" + w() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(w);
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
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
		if (!(obj instanceof Vector4f)) {
			return false;
		}
		Vector4f other = (Vector4f) obj;
		if (Float.floatToIntBits(w) != Float.floatToIntBits(other.w)) {
			return false;
		}
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) {
			return false;
		}
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) {
			return false;
		}
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z)) {
			return false;
		}
		return true;
	}

}

package org.ploxie.utils.math.vector;

import org.ploxie.utils.math.Math;

public class Vector3i implements Cloneable {

	public int x, y, z;

	public Vector3i() {
		this(0, 0, 0);
	}

	public Vector3i(final int x, final int y, final int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3i(int xyz) {
		this(xyz, xyz, xyz);
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public int z() {
		return z;
	}

	public void x(final int x) {
		this.x = x;
	}

	public void y(final int y) {
		this.y = y;
	}

	public void z(final int z) {
		this.z = z;
	}

	public Vector3i xyz(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Vector3i xyz(Vector3i location) {
		return xyz(location.x, location.y, location.z);
	}

	public Vector3i add(final Vector3i o) {
		x(x() + o.x());
		y(y() + o.y());
		z(z() + o.z());
		return this;
	}

	public Vector3i subtract(final Vector3i o) {
		x(x() - o.x());
		y(y() - o.y());
		z(z() - o.z());
		return this;
	}

	public Vector3i divide(final Vector3i o) {
		x(x() / o.x());
		y(y() / o.y());
		z(z() / o.z());
		return this;
	}

	public Vector3i multiply(int o) {
		x(x() * o);
		y(y() * o);
		z(z() * o);
		return this;
	}

	public Vector3i multiply(final Vector3i o) {
		x(x() * o.x());
		y(y() * o.y());
		z(z() * o.z());
		return this;
	}

	public float length() {
		return Math.sqrt(lengthSquared());
	}

	public int lengthSquared() {
		return x() * x() + y() * y() + z() * z();
	}

	public Vector3f normalize() {
		final Vector3f result = new Vector3f();
		float len = length();
		result.x(x() / len);
		result.y(y() / len);
		result.z(z() / len);
		return result;
	}

	@Override
	public Vector3i clone() {
		return new Vector3i(x(), y(), z());
	}

	@Override
	public String toString() {
		return "[x:" + x() + ", y:" + y() + ", z:" + z() + "]";
	}

	public Vector3i getCrossed(final Vector3i other) {
		final Vector3i result = new Vector3i();
		result.x = y * other.z - z * other.y;
		result.y = z * other.x - x * other.z;
		result.z = x * other.y - y * other.x;
		return result;
	}

	public int dot(final Vector3i other) {
		return x() * other.x() + y() * other.y() + z() * other.z();
	}

	public Vector3i min(Vector3i o) {
		x(Math.min(x(), o.x()));
		y(Math.min(y(), o.y()));
		z(Math.min(z(), o.z()));
		return this;
	}

	public Vector3i max(Vector3i o) {
		x(Math.max(x(), o.x()));
		y(Math.max(y(), o.y()));
		z(Math.max(z(), o.z()));
		return this;
	}

	public float distance(final Vector3i v) {
		return Math.sqrt(distanceSquared(v));
	}

	public float distanceSquared(final Vector3i v) {
		float dx = x - v.x;
		float dy = y - v.y;
		float dz = z - v.z;
		return dx * dx + dy * dy + dz * dz;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
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
		if (!(obj instanceof Vector3i)) {
			return false;
		}
		Vector3i other = (Vector3i) obj;
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		if (z != other.z) {
			return false;
		}
		return true;
	}

}

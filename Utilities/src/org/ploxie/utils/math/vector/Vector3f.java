package org.ploxie.utils.math.vector;

import org.ploxie.utils.math.Math;

public class Vector3f implements Cloneable {

	public static final Vector3f ZERO = new Vector3f();

	public float x, y, z;

	public Vector3f() {
		this(0, 0, 0);
	}

	public Vector3f(final float x, final float y, final float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3f(float xyz) {
		this(xyz, xyz, xyz);
	}

	public Vector3f(Vector3i xyz) {
		this(xyz.x(), xyz.y(), xyz.z());
	}
	
	public Vector3f copy() {
		return new Vector3f(x,y,z);
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

	public void x(final float x) {
		this.x = x;
	}

	public void y(final float y) {
		this.y = y;
	}

	public void z(final float z) {
		this.z = z;
	}

	public Vector2f xy() {
		return new Vector2f(x,y);
	}
	
	public Vector3f xyz(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Vector3f xyz(Vector3f location) {
		return xyz(location.x, location.y, location.z);
	}

	public Vector3f xyz(Vector3i location) {
		return xyz(location.x, location.y, location.z);
	}

	public Vector3f add(final Vector3i o) {
		x(x() + o.x());
		y(y() + o.y());
		z(z() + o.z());
		return this;
	}

	public Vector3f add(final Vector3f o) {
		x(x() + o.x());
		y(y() + o.y());
		z(z() + o.z());
		return this;
	}

	public Vector3f add(float x, float y, float z) {
		x(x() + x);
		y(y() + y);
		z(z() + z);
		return this;
	}

	public Vector3f subtract(final Vector3f o) {
		x(x() - o.x());
		y(y() - o.y());
		z(z() - o.z());
		return this;
	}

	public Vector3f subtract(final Vector3i o) {
		x(x() - o.x());
		y(y() - o.y());
		z(z() - o.z());
		return this;
	}

	public Vector3f divide(final float s) {
		x(x() / s);
		y(y() / s);
		z(z() / s);
		return this;
	}

	public Vector3f divide(final Vector3f o) {
		x(x() / o.x());
		y(y() / o.y());
		z(z() / o.z());
		return this;
	}

	public Vector3f multiply(final float s) {
		x(x() * s);
		y(y() * s);
		z(z() * s);
		return this;
	}

	public Vector3f multiply(final Vector3f o) {
		x(x() * o.x());
		y(y() * o.y());
		z(z() * o.z());
		return this;
	}

	public float distance(final Vector3i v3i) {
		return distance(new Vector3f(v3i));
	}

	public float distance(final Vector3f v3f) {
		return Math.sqrt(distanceSquared(v3f));
	}

	public float distanceSquared(final Vector3f v) {
		float dx = x - v.x;
		float dy = y - v.y;
		float dz = z - v.z;
		return dx * dx + dy * dy + dz * dz;
	}

	public float length() {
		return Math.sqrt(lengthSquared());
	}

	public float lengthSquared() {
		return x() * x() + y() * y() + z() * z();
	}

	public Vector3f normalize() {
		float len = length();
		if (len == 0) {
			x(0);
			y(0);
			z(0);
			return this;
		}
		x(x() / len);
		y(y() / len);
		z(z() / len);
		return this;
	}

	public Vector3i round() {
		return new Vector3i(Math.round(x), Math.round(y), Math.round(z));
	}

	public Vector3i floor() {
		return new Vector3i(Math.floor(x), Math.floor(y), Math.floor(z));
	}

	@Override
	public Vector3f clone() {
		return new Vector3f(x(), y(), z());
	}

	public Vector3f getCrossed(final Vector3f other) {
		final Vector3f result = new Vector3f();
		result.x = y * other.z - z * other.y;
		result.y = z * other.x - x * other.z;
		result.z = x * other.y - y * other.x;
		return result;
	}

	public float dot(final Vector3f other) {
		return x() * other.x() + y() * other.y() + z() * other.z();
	}

	public Vector3f clamp(final float min, final float max) {
		return clamp(new Vector3f(min), new Vector3f(max));
	}

	public Vector3f clamp(final Vector3f min, final Vector3f max) {
		this.x = Math.clamp(x, min.x, max.x);
		this.y = Math.clamp(y, min.y, max.y);
		this.z = Math.clamp(z, min.z, max.z);
		return this;
	}

	public Vector3f cross(final float otherX, final float otherY, final float otherZ) {
		float tempx = y * otherZ - z * otherY;
		float tempy = z * otherX - x * otherZ;
		z = x * otherY - y * otherX;
		x = tempx;
		y = tempy;
		return this;
	}

	public Vector3f cross(final Vector3f other) {
		return cross(other.x, other.y, other.z);
	}

	/**
	 * Creates a new Vector3f with the current instance as start, and the given
	 * end as an end of the interpolation with the given factor
	 *
	 * @param end
	 *            end point of the interpolation
	 * @param factor
	 *            factor of the interpolation mapped between [0..1]
	 * @return a new instance as an interpolated point between start and end
	 */
	public Vector3f interpolate(final Vector3f end, final float factor) {
		return new Vector3f().interpolate(this, end, factor);
	}

	/**
	 * Modifies the current instance to create an interpolated vector between
	 * <i>start</i> and <i>end</i> with given <i>factor</i>
	 *
	 * @param start
	 *            start point of the interpolation
	 * @param end
	 *            end point of the interpolation
	 * @param factor
	 *            factor of the interpolation mapped between [0..1]
	 * @return the current instance as an interpolated point between start and
	 *         end
	 */
	public Vector3f interpolate(final Vector3f start, final Vector3f end, final float factor) {
		x = start.x + factor * (end.x - start.x);
		y = start.y + factor * (end.y - start.y);
		z = start.z + factor * (end.z - start.z);
		return this;
	}

	public Vector3f toDegrees() {
		x = Math.toDegrees(x);
		y = Math.toDegrees(y);
		z = Math.toDegrees(z);
		return this;
	}

	public Vector3f toRadians() {
		x = Math.toRadians(x);
		y = Math.toRadians(y);
		z = Math.toRadians(z);
		return this;
	}

	public Vector3f abs() {
		x = Math.abs(x);
		y = Math.abs(y);
		z = Math.abs(z);
		return this;
	}

	@Override
	public String toString() {
		return "[x:" + x() + ", y:" + y() + ", z:" + z() + "]";
	}
	
	public String toFormatedString() {
		return "[x:" + String.format("%.3f",x()) + " y:" + String.format("%.3f",y()) + " z:" + String.format("%.3f",z()) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		if (!(obj instanceof Vector3f)) {
			return false;
		}
		Vector3f other = (Vector3f) obj;
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

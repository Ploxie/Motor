package org.ploxie.utils.math.matrix;

import java.nio.FloatBuffer;

import org.ploxie.utils.math.Math;
import org.ploxie.utils.math.vector.Vector2f;
import org.ploxie.utils.math.vector.Vector3f;

public class Matrix3f {

	private float[] values;

	public Matrix3f() {
		values = new float[9];
		loadIdentity();
	}

	public Matrix3f(Matrix3f m3f) {
		set(m3f);
	}

	public Matrix3f(float[] values) {
		this.values = values;
	}

	public Matrix3f set(Matrix3f m3f) {
		this.values = m3f.values.clone();
		return this;
	}

	public float[] getValues() {
		return values;
	}
	


	/**
	 * Sets matrix to:
	 * <p>
	 * <code>
	 * [1,0,0,0]<br>
	 * 	[0,1,0,0]<br>
	 * 	[0,0,1,0]<br>
	 * [0,0,0,1]</code>
	 */
	public Matrix3f loadIdentity() {
		values[1] = values[2] = values[3] = values[5] = values[6] = values[7] = 0;
		values[0] = values[4] = values[8] = 1;
		return this;
	}

	public Matrix3f load(FloatBuffer buffer) {
		buffer.get(values);
		return this;
	}

	public Matrix3f transposeLocal() {
		values[0] = values[0];
		values[1] = values[3];
		values[2] = values[6];
		values[3] = values[1];
		values[4] = values[4];
		values[5] = values[7];
		values[6] = values[2];
		values[7] = values[5];
		values[8] = values[8];
		return this;
	}

	public Matrix3f zero() {
		for (int i = 0; i < values.length; i++) {
			values[i] = 0.0f;
		}
		return this;
	}

	public Matrix3f invertLocal() {
		float det = determinant();
		if (Math.abs(det) <= Math.FLT_EPSILON) {
			return zero();
		}
		float f0 = values[4] * values[8] - values[5] * values[7];
		float f1 = values[2] * values[7] - values[1] * values[8];
		float f2 = values[1] * values[5] - values[2] * values[4];
		float f3 = values[5] * values[6] - values[3] * values[8];
		float f4 = values[0] * values[8] - values[2] * values[6];
		float f5 = values[2] * values[3] - values[0] * values[5];
		float f6 = values[3] * values[7] - values[4] * values[6];
		float f7 = values[1] * values[6] - values[0] * values[7];
		float f8 = values[0] * values[4] - values[1] * values[3];
		values[0] = f0;
		values[1] = f1;
		values[2] = f2;
		values[3] = f3;
		values[4] = f4;
		values[5] = f5;
		values[6] = f6;
		values[7] = f7;
		values[8] = f8;
		multLocal(1f / det);
		return this;
	}

	public Vector3f multiply(Vector3f vec) {
		float vx = vec.x(), vy = vec.y(), vz = vec.z();
		vec.x(values[0] * vx + values[1] * vy + values[2] * vz);
		vec.y(values[3] * vx + values[4] * vy + values[5] * vz);
		vec.z(values[6] * vx + values[7] * vy + values[8] * vz);
		return vec;
	}

	public Matrix3f negateLeft() {
		values[0] = -values[0];
		values[4] = -values[4];
		values[8] = -values[8];
		return this;
	}

	public Vector3f multiply(Vector3f vec, Vector3f store) {
		if (store == null) {
			store = new Vector3f();
		}
		float vx = vec.x(), vy = vec.y(), vz = vec.z();
		store.x(values[0] * vx + values[1] * vy + values[2] * vz);
		store.y(values[3] * vx + values[4] * vy + values[5] * vz);
		store.z(values[6] * vx + values[7] * vy + values[8] * vz);
		return store;
	}

	public Matrix3f invert(Matrix3f store) {
		float det = determinant();
		if (Math.abs(det) <= Math.FLT_EPSILON) {
			return store.zero();
		}

		store.values[0] = values[4] * values[8] - values[5] * values[7];
		store.values[1] = values[2] * values[7] - values[1] * values[8];
		store.values[2] = values[1] * values[5] - values[2] * values[4];
		store.values[3] = values[5] * values[6] - values[3] * values[8];
		store.values[4] = values[0] * values[8] - values[2] * values[6];
		store.values[5] = values[2] * values[3] - values[0] * values[5];
		store.values[6] = values[3] * values[7] - values[4] * values[6];
		store.values[7] = values[1] * values[6] - values[0] * values[7];
		store.values[8] = values[0] * values[4] - values[1] * values[3];

		store.multLocal(1f / det);
		return store;
	}

	public Matrix3f multLocal(float scalar) {
		for (int i = 0; i < values.length; i++) {
			values[i] *= scalar;
		}
		return this;
	}

	public float determinant() {
		float fCo00 = values[4] * values[8] - values[5] * values[7];
		float fCo10 = values[5] * values[6] - values[3] * values[8];
		float fCo20 = values[3] * values[7] - values[4] * values[6];
		float fDet = values[0] * fCo00 + values[1] * fCo10 + values[2] * fCo20;
		return fDet;
	}

	public Matrix3f negate() {
		for (int i = 0; i < values.length; i++) {
			values[i] = -values[i];
		}
		return this;
	}
	
	public void setTranslation(Vector2f translation) {
		setTranslation(translation.x, translation.y);
	}
	
	public void setTranslation(float x, float y) {
		values[6] = x;
		values[7] = y;
	}
	
	public Vector2f getTranslation() {
		return new Vector2f(values[6], values[7]);
	}
	
	public void setScale(Vector2f scale) {
		setScale(scale.x, scale.y);
	}
	
	public void setScale(float x, float y) {
		values[0] = x;
		values[4] = y;
	}
	
	public Vector2f getScale() {
		return new Vector2f(values[0], values[4]);
	}

	public FloatBuffer fillBuffer(final FloatBuffer floatBuffer) {
		floatBuffer.put(values);
		return floatBuffer;
	}
	
}
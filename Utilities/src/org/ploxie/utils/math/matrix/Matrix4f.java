package org.ploxie.utils.math.matrix;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.ploxie.utils.math.FastMath;
import org.ploxie.utils.math.Math;
import org.ploxie.utils.math.vector.Vector2f;
import org.ploxie.utils.math.vector.Vector3f;
import org.ploxie.utils.math.vector.Vector4f;

public class Matrix4f implements Cloneable {

	private float[] values;

	public Matrix4f() {
		values = new float[16];
		loadIdentity();
	}

	public Matrix4f(final Matrix4f m4f) {
		set(m4f);
	}

	public Matrix4f(final FloatBuffer matrixBuffer) {
		values = new float[16];
		set(matrixBuffer);
	}

	public Matrix4f(final float[] viewmatrix) {
		values = viewmatrix;

	}

	public float[] getValues() {
		return values;
	}

	@Override
	public Matrix4f clone() {
		return new Matrix4f(values.clone());
	}

	public Matrix4f(final Matrix3f modelView) {
		this();
		float[] mViewValus = modelView.getValues();
		values[0] = mViewValus[0];
		values[1] = mViewValus[1];
		values[2] = mViewValus[2];

		values[4] = mViewValus[3];
		values[5] = mViewValus[4];
		values[6] = mViewValus[5];

		values[8] = mViewValus[6];
		values[9] = mViewValus[7];
		values[10] = mViewValus[8];
	}

	public Matrix4f set(final Matrix4f m4f) {
		values = m4f.values.clone();
		return this;
	}

	public Matrix4f set(final FloatBuffer buf) {
		for (int i = 0; i < values.length; i++) {
			values[i] = buf.get();
		}
		return this;
	}

	public Matrix4f set(final int row, final int column, final float val) {
		values[row * 4 + column] = val;
		return this;
	}
	
	public float get(int x, int y)
	{
		return  this.values[x * 4 + y];
	}

	
	
	/**
	 * Sets matrix to:
	 * <p>
	 * <code>
	 * [1,0,0,0]<br>
	 * [0,1,0,0]<br>
	 * [0,0,1,0]<br>
	 * [0,0,0,1]</code>
	 */
	public Matrix4f loadIdentity() {
		/*
		 * m01 = m02 = m03 = 0.0f; m10 = m12 = m13 = 0.0f; m20 = m21 = m23 =
		 * 0.0f; m30 = m31 = m32 = 0.0f; m00 = m11 = m22 = m33 = 1.0f;
		 */
		values[1] = values[2] = values[3] = 0.0f;
		values[4] = values[6] = values[7] = 0.0f;
		values[8] = values[9] = values[11] = 0.0f;
		values[12] = values[13] = values[14] = 0.0f;
		values[0] = values[5] = values[10] = values[15] = 1.0f;
		return this;
	}

	public Matrix4f load(final FloatBuffer buffer1) {
		buffer1.get(values);
		return this;
	}

	public Matrix4f multiply(final Matrix4f m) {
		float[] c = values.clone();
		values[0] = c[0] * m.values[0] + c[1] * m.values[4] + c[2] * m.values[8] + c[3] * m.values[12];
		values[1] = c[0] * m.values[1] + c[1] * m.values[5] + c[2] * m.values[9] + c[3] * m.values[13];
		values[2] = c[0] * m.values[2] + c[1] * m.values[6] + c[2] * m.values[10] + c[3] * m.values[14];
		values[3] = c[0] * m.values[3] + c[1] * m.values[7] + c[2] * m.values[11] + c[3] * m.values[15];
		values[4] = c[4] * m.values[0] + c[5] * m.values[4] + c[6] * m.values[8] + c[7] * m.values[12];
		values[5] = c[4] * m.values[1] + c[5] * m.values[5] + c[6] * m.values[9] + c[7] * m.values[13];
		values[6] = c[4] * m.values[2] + c[5] * m.values[6] + c[6] * m.values[10] + c[7] * m.values[14];
		values[7] = c[4] * m.values[3] + c[5] * m.values[7] + c[6] * m.values[11] + c[7] * m.values[15];
		values[8] = c[8] * m.values[0] + c[9] * m.values[4] + c[10] * m.values[8] + c[11] * m.values[12];
		values[9] = c[8] * m.values[1] + c[9] * m.values[5] + c[10] * m.values[9] + c[11] * m.values[13];
		values[10] = c[8] * m.values[2] + c[9] * m.values[6] + c[10] * m.values[10] + c[11] * m.values[14];
		values[11] = c[8] * m.values[3] + c[9] * m.values[7] + c[10] * m.values[11] + c[11] * m.values[15];
		values[12] = c[12] * m.values[0] + c[13] * m.values[4] + c[14] * m.values[8] + c[15] * m.values[12];
		values[13] = c[12] * m.values[1] + c[13] * m.values[5] + c[14] * m.values[9] + c[15] * m.values[13];
		values[14] = c[12] * m.values[2] + c[13] * m.values[6] + c[14] * m.values[10] + c[15] * m.values[14];
		values[15] = c[12] * m.values[3] + c[13] * m.values[7] + c[14] * m.values[11] + c[15] * m.values[15];

		return this;
	}
	
	public Matrix3f toRotationMatrix(final Matrix3f m3f) {
		float[] values3 = m3f.getValues();
		values3[0] = values[0];
		values3[1] = values[1];
		values3[2] = values[2];
		values3[3] = values[4];
		values3[4] = values[5];
		values3[5] = values[6];
		values3[6] = values[8];
		values3[7] = values[9];
		values3[8] = values[10];
		return m3f;
	}

	public Matrix3f toRotationMatrix() {
		float[] values3 = new float[9];
		values3[0] = values[0];
		values3[1] = values[1];
		values3[2] = values[2];
		values3[3] = values[4];
		values3[4] = values[5];
		values3[5] = values[6];
		values3[6] = values[8];
		values3[7] = values[9];
		values3[8] = values[10];
		return new Matrix3f(values3);
	}

	public Matrix4f invertLocal() {

		float fA0 = values[0] * values[5] - values[1] * values[4];
		float fA1 = values[0] * values[6] - values[2] * values[4];
		float fA2 = values[0] * values[7] - values[3] * values[4];
		float fA3 = values[1] * values[6] - values[2] * values[5];
		float fA4 = values[1] * values[7] - values[3] * values[5];
		float fA5 = values[2] * values[7] - values[3] * values[6];
		float fB0 = values[8] * values[13] - values[9] * values[12];
		float fB1 = values[8] * values[14] - values[10] * values[12];
		float fB2 = values[8] * values[15] - values[11] * values[12];
		float fB3 = values[9] * values[14] - values[10] * values[13];
		float fB4 = values[9] * values[15] - values[11] * values[13];
		float fB5 = values[10] * values[15] - values[11] * values[14];
		float fDet = fA0 * fB5 - fA1 * fB4 + fA2 * fB3 + fA3 * fB2 - fA4 * fB1 + fA5 * fB0;

		if (java.lang.Math.abs(fDet) <= 0f) {
			return new Matrix4f().loadIdentity();
		}

		float f00 = +values[5] * fB5 - values[6] * fB4 + values[7] * fB3;
		float f10 = -values[4] * fB5 + values[6] * fB2 - values[7] * fB1;
		float f20 = +values[4] * fB4 - values[5] * fB2 + values[7] * fB0;
		float f30 = -values[4] * fB3 + values[5] * fB1 - values[6] * fB0;
		float f01 = -values[1] * fB5 + values[2] * fB4 - values[3] * fB3;
		float f11 = +values[0] * fB5 - values[2] * fB2 + values[3] * fB1;
		float f21 = -values[0] * fB4 + values[1] * fB2 - values[3] * fB0;
		float f31 = +values[0] * fB3 - values[1] * fB1 + values[2] * fB0;
		float f02 = +values[13] * fA5 - values[14] * fA4 + values[15] * fA3;
		float f12 = -values[12] * fA5 + values[14] * fA2 - values[15] * fA1;
		float f22 = +values[12] * fA4 - values[13] * fA2 + values[15] * fA0;
		float f32 = -values[12] * fA3 + values[13] * fA1 - values[14] * fA0;
		float f03 = -values[9] * fA5 + values[10] * fA4 - values[11] * fA3;
		float f13 = +values[8] * fA5 - values[10] * fA2 + values[11] * fA1;
		float f23 = -values[8] * fA4 + values[9] * fA2 - values[11] * fA0;
		float f33 = +values[8] * fA3 - values[9] * fA1 + values[10] * fA0;

		values[0] = f00;
		values[1] = f01;
		values[2] = f02;
		values[3] = f03;
		values[4] = f10;
		values[5] = f11;
		values[6] = f12;
		values[7] = f13;
		values[8] = f20;
		values[9] = f21;
		values[10] = f22;
		values[11] = f23;
		values[12] = f30;
		values[13] = f31;
		values[14] = f32;
		values[15] = f33;

		float fInvDet = 1.0f / fDet;
		multLocal(fInvDet);

		return this;
	}

	public void multLocal(final float mult) {
		for (int i = 0; i < 16; i++) {
			values[i] *= mult;
		}
	}

	public void fromFrustum(final float near, final float far, final float left, final float right, final float top, final float bottom) {
		values[0] = 2.0f * near / (right - left);
		values[5] = 2.0f * near / (top - bottom);
		values[14] = -1.0f;
		values[15] = -0.0f;

		// A
		values[3] = (right + left) / (right - left);

		// B
		values[6] = (top + bottom) / (top - bottom);

		// C
		values[10] = -(far + near) / (far - near);

		// D
		values[11] = -(2.0f * far * near) / (far - near);
	}

	public Vector3f getLeft() {
		return new Vector3f(values[0], values[4], values[8]);
	}

	public Vector3f getUp() {
		return new Vector3f(values[1], values[5], values[9]);
	}

	public Vector3f getDirection() {
		return new Vector3f(values[2], values[6], values[10]);
	}

	public Vector3f getTranslation() {
		return new Vector3f(values[12], values[13], values[14]);
	}

	public FloatBuffer fillBuffer(final FloatBuffer floatBuffer) {
		floatBuffer.put(values);
		return floatBuffer;
	}

	public Matrix4f makePerspective(final float fovy, final float aspect, final float zNear, final float zFar) {
		float radians = Math.toRadians(fovy / 2f);
		float deltaZ = zFar - zNear;
		float sine = Math.sin(radians);
		if (deltaZ == 0.0F || sine == 0.0F || aspect == 0.0F) {
			return this;
		}
		float cotangent = Math.cos(radians) / sine;
		loadIdentity();
		values[0] = cotangent / aspect;
		values[5] = cotangent;
		values[10] = -(zFar + zNear) / deltaZ;
		values[11] = -1.0f;
		values[14] = -2.0f * zNear * zFar / deltaZ;
		values[15] = 0.0f;
		// applyMatrix();
		return this;
	}

	public void put(final int row, final float x, final float y, final float z, final float a) {
		values[row * 4] = x;
		values[row * 4 + 1] = y;
		values[row * 4 + 2] = z;
		values[row * 4 + 3] = a;
	}

	public Matrix4f makeLookAtViewMatrix(final Vector3f eye, final Vector3f center, final Vector3f up) {
		Vector3f zaxis = eye.clone().subtract(center).normalize();
		Vector3f xaxis = up.getCrossed(zaxis).normalize();
		Vector3f yaxis = zaxis.getCrossed(xaxis);

		/*
		 * put(0 ,xaxis.x, yaxis.x, zaxis.x, 0); put(1, xaxis.y, yaxis.y,
		 * zaxis.y, 0); put(2, xaxis.z, yaxis.z, zaxis.z, 0); put(3,
		 * -xaxis.dot(eye), -yaxis.dot(eye), -zaxis.dot(eye), 1);
		 */

		values[0] = xaxis.x();
		values[1] = yaxis.x();
		values[2] = zaxis.x();
		values[3] = 0.0f;

		values[4] = xaxis.y();
		values[5] = yaxis.y();
		values[6] = zaxis.y();
		values[7] = 0.0f;

		values[8] = xaxis.z();
		values[9] = yaxis.z();
		values[10] = zaxis.z();
		values[11] = 0.0f;

		values[12] = -xaxis.dot(eye);
		values[13] = -yaxis.dot(eye);
		values[14] = -zaxis.dot(eye);
		values[15] = 1.0f;
		return this;
	}
	
	public Matrix4f makeFPSViewMatrix(Vector3f eye, float pitch, float yaw) {
		float cosPitch = Math.cos(FastMath.toRadians(pitch));
		float sinPitch = Math.sin(FastMath.toRadians(pitch));
		float cosYaw = Math.cos(FastMath.toRadians(yaw));
		float sinYaw = Math.sin(FastMath.toRadians(yaw));
		
		Vector3f xaxis = new Vector3f(cosYaw, 0, -sinYaw);
		Vector3f yaxis = new Vector3f(sinYaw * sinPitch, cosPitch, cosYaw * sinPitch);
		Vector3f zaxis = new Vector3f(sinYaw * cosPitch, -sinPitch, cosPitch * cosYaw);
		
		setRow(0, xaxis.x, yaxis.x, zaxis.x, 0);
		setRow(1, xaxis.y, yaxis.y, zaxis.y, 0);
		setRow(2, xaxis.z, yaxis.z, zaxis.z, 0);
		setRow(3, -xaxis.dot(eye), -yaxis.dot(eye), -zaxis.dot(eye), 1);
		//setTranslation(eye);
		return this;
	}

	public Matrix4f transpose() {
		float[] temp = values.clone();
		values[0] = temp[0];
		values[4] = temp[1];
		values[8] = temp[2];
		values[12] = temp[3];
		values[1] = temp[4];
		values[5] = temp[5];
		values[9] = temp[6];
		values[13] = temp[7];
		values[2] = temp[8];
		values[6] = temp[9];
		values[10] = temp[10];
		values[14] = temp[11];
		values[3] = temp[12];
		values[7] = temp[13];
		values[11] = temp[14];
		values[15] = temp[15];
		return this;
	}

	public Matrix4f makeOrthoMatrix(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar) {
		float tx = -(right + left) / (right - left);
		float ty = -(top + bottom) / (top - bottom);
		float tz = -(zFar + zNear) / (zFar - zNear);
		setRow(0, 2.0f / (right - left), 0.0f, 0.0f, 0.0f);
		setRow(1, 0.0f, 2.0f / (top - bottom), 0.0f, 0.0f);
		setRow(2, 0.0f, 0.0f, -2.0f / (zFar - zNear), 0.0f);
		setRow(3, tx, ty, tz, 1.0f);
		return this;
	}
		

	public void setRow(final int row, final float x, final float y, final float z, final float a) {
		// values[row * 4] = x;
		// values[row * 4 + 1] = y;
		// values[row * 4 + 2] = z;
		// values[row * 4 + 3] = a;
		values[row * 4] = x;
		values[row * 4 + 1] = y;
		values[row * 4 + 2] = z;
		values[row * 4 + 3] = a;
	}

	public Vector3f multiply(Vector3f in) {
		return multiply(in, new Vector3f());
	}
	
	public Vector3f multiply(Vector3f in, Vector3f store) {
		float vx = in.x, vy = in.y, vz = in.z, vw = 1;
		store.x = vx * values[0] + vy * values[4] + vz * values[8] + vw * values[12];
		store.y = vx * values[1] + vy * values[5] + vz * values[9] + vw * values[13];
		store.z = vx * values[2] + vy * values[6] + vz * values[10] + vw * values[14];
		//store.w = (vx * values[3] + vy * values[7] + vz * values[11] + vw * values[15]);
		return store;
	}
	
	public Vector4f multiply(Vector4f in) {
		return multiply(in, new Vector4f());
	}
	
	public Vector4f multiply(Vector4f in, Vector4f store) {
		float vx = in.x, vy = in.y, vz = in.z, vw = in.w;
		store.x = vx * values[0] + vy * values[4] + vz * values[8] + vw * values[12];
		store.y = vx * values[1] + vy * values[5] + vz * values[9] + vw * values[13];
		store.z = vx * values[2] + vy * values[6] + vz * values[10] + vw * values[14];
		store.w = (vx * values[3] + vy * values[7] + vz * values[11] + vw * values[15]);
		return store;
	}		
	
	public Matrix4f translate(Vector3f v3f) {
		return translate(v3f.x, v3f.y, v3f.z);
	}
	
	public Matrix4f translate(Vector2f v2f) {
		return translate(v2f.x, v2f.y, 0);
	}

	public Matrix4f translate(float x, float y, float z) {
		values[12] += x;
		values[13] += y;
		values[14] += z;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("Matrix4f\n[\n");
		for (int i = 1; i < 17; i++) {
			result.append(values[i - 1]);
			if ((double) i / 4 == 1 || (double) i / 4 == 2 || (double) i / 4 == 3 || (double) i / 4 == 4) {
				result.append(" \n");
			}
			result.append(" ");
		}

		result.append(" ]");
		return result.toString();
	}

	public void setTranslation(float x, float y, float z) {
		setRow(3, x, y, z, 1.0f);
	}
	
	public void setTranslation(Vector3f pos) {
		setRow(3, pos.x, pos.y, pos.z, 1.0f);
	}
	
	public void setTranslation(Vector2f pos) {
		setRow(3, pos.x, pos.y, values[14], 1.0f);
	}
		
	public Matrix4f rotate(float angle, Vector3f axis)
	{		
		float c = FastMath.cos(FastMath.toRadians(angle));
	    float s = FastMath.sin(FastMath.toRadians(angle));
	    float oneminusc = 1.0f - c;
	    float xy = axis.x * axis.y;
	    float yz = axis.y * axis.z;
	    float xz = axis.x * axis.z;
	    float xs = axis.x * s;
	    float ys = axis.y * s;
	    float zs = axis.z * s;

	    float f00 = axis.x * axis.x * oneminusc + c;
	    float f01 = xy * oneminusc + zs;
	    float f02 = xz * oneminusc - ys;

	    float f10 = xy * oneminusc - zs;
	    float f11 = axis.y * axis.y * oneminusc + c;
	    float f12 = yz * oneminusc + xs;

	    float f20 = xz * oneminusc + ys;
	    float f21 = yz * oneminusc - xs;
	    float f22 = axis.z * axis.z * oneminusc + c;

	    float t00 = values[0] * f00 + values[4] * f01 + values[8] * f02;
	    float t01 = values[1] * f00 + values[5] * f01 + values[9] * f02;
	    float t02 = values[2] * f00 + values[6] * f01 + values[10] * f02;
	    float t03 = values[3] * f00 + values[7] * f01 + values[11] * f02;

	    float t10 = values[0] * f10 + values[4] * f11 + values[8] * f12;
	    float t11 = values[1] * f10 + values[5] * f11 + values[9] * f12;
	    float t12 = values[2] * f10 + values[6] * f11 + values[10] * f12;
	    float t13 = values[3] * f10 + values[7] * f11 + values[11] * f12;

	    values[8] = values[0] * f20 + values[4] * f21 + values[8] * f22;
	    values[9] = values[1] * f20 + values[5] * f21 + values[9] * f22;
	    values[10] = values[2] * f20 + values[6] * f21 + values[10] * f22;
	    values[11] = values[3] * f20 + values[7] * f21 + values[11] * f22;

	    values[0] = t00;
	    values[1] = t01;
	    values[2] = t02;
	    values[3] = t03;
	    values[4] = t10;
	    values[5] = t11;
	    values[6] = t12;
	    values[7] = t13;
	
		
        return this;
	}

	public void setScale(Vector2f scale) {
		values[0] = scale.x;
		values[5] = scale.y;
	}
	
	public void setScale(Vector3f scale) {
		values[0] = scale.x;
		values[5] = scale.y;
		values[10] = scale.z;		
	}
	
	public void setScale(float x, float y, float z) {
		values[0] = x;
		values[5] = y;
		values[10] = z;
	}

	public Vector3f getScale() {
		return new Vector3f(values[0], values[5], values[10]);
	}

	
}
package org.ploxie.utils.math;

public class FastMath {

	private static float[] SIN_TABLE;
	public static boolean fastMode = true;
	public static final float f_HALFPI = (float) java.lang.Math.PI / 2f;
	public static final float f_PI = (float) java.lang.Math.PI;
	public static final float f_2PI = (float) (2.0d * java.lang.Math.PI);
	public static final float ANG2RAD = f_PI / 180.0f;
	public static final float RAD2ANG = 180.0f / f_PI;
	public static final float ONE_THIRD = 1f / 3f;
	/** A "close to zero" double epsilon value for use */
	public static final double DBL_EPSILON = 2.220446049250313E-16d;
	/** A "close to zero" float epsilon value for use */
	public static final float FLT_EPSILON = 1.1920928955078125E-7f;
	/** A "close to zero" float epsilon value for use */
	public static final float ZERO_TOLERANCE = 0.0001f;

	private static long negativeZeroFloatBits = Float.floatToIntBits(-0.0f);

	public static final float sin(float f) {
		if (fastMode)
			return SIN_TABLE[(int) (f * 10430.38F) & 0xffff];
		return (float) java.lang.Math.sin(f);
	}

	public static final float cos(float f) {
		if (fastMode)
			return SIN_TABLE[(int) (f * 10430.38F + 16384F) & 0xffff];
		return (float) java.lang.Math.cos(f);
	}

	public static float tan(float f) {
		return sin(f) / cos(f);
	}

	public static float clamp(float value, float min, float max) {
		return value < min ? min : value > max ? max : value;
	}
	
	public static int clamp(int value, int min, int max) {
		return value < min ? min : value > max ? max : value;
	}

	public static float toRadians(float angleDegrees) {
		return angleDegrees * ANG2RAD;
	}

	public static float toDegrees(float angleRadians) {
		return angleRadians * RAD2ANG;
	}

	public static float simplifyRadians(float rad) {
		while (rad <= -f_PI) {
			rad += f_2PI;
		}
		while (rad > f_PI) {
			rad -= f_2PI;
		}
		return rad;
	}

	public static float convertHalfToFloat(int half) {
		switch (half) {
		case 0x0000:
			return 0f;
		case 0x8000:
			return -0f;
		case 0x7c00:
			return Float.POSITIVE_INFINITY;
		case 0xfc00:
			return Float.NEGATIVE_INFINITY;
			// TODO: Support for NaN?
		default:
			return Float.intBitsToFloat(((half & 0x8000) << 16)
					| (((half & 0x7c00) + 0x1C000) << 13)
					| ((half & 0x03FF) << 13));
		}
	}

	public static int colorAsIntABGR(byte x, byte y, byte z, byte a) {
		int abgr = ((a * 255 & 0xFF) << 24) | ((z * 255 & 0xFF) << 16)
				| ((y * 255 & 0xFF) << 8) | ((x * 255 & 0xFF));
		return abgr;
	}

	public static byte[] intAsByteArray(int abgr) {
		return new byte[] { (byte) ((abgr & 0x000000ff) / 255f),
				(byte) (((abgr & 0x0000ff00) >> 8) / 255f),
				(byte) (((abgr & 0x00ff0000) >> 16) / 255f),
				(byte) ((((abgr & 0xff000000) >> 24) & 0x000000ff) / 255f) };
	}

	public static short convertFloatToHalf(float flt) {
		if (Float.isNaN(flt)) {
			throw new UnsupportedOperationException(
					"NaN to half conversion not supported!");
		} else if (flt == Float.POSITIVE_INFINITY) {
			return (short) 0x7c00;
		} else if (flt == Float.NEGATIVE_INFINITY) {
			return (short) 0xfc00;
		} else if (flt == 0f) {
			return (short) 0x0000;
		} else if (flt == -0f) {
			return (short) 0x8000;
		} else if (flt > 65504f) {
			return 0x7bff;
		} else if (flt < -65504f) {
			return (short) (0x7bff | 0x8000);
		} else if (flt > 0f && flt < 5.96046E-8f) {
			return 0x0001;
		} else if (flt < 0f && flt > -5.96046E-8f) {
			return (short) 0x8001;
		}

		int f = Float.floatToIntBits(flt);
		return (short) (((f >> 16) & 0x8000)
				| ((((f & 0x7f800000) - 0x38000000) >> 13) & 0x7c00) | ((f >> 13) & 0x03ff));
	}

	public static float interpolateLinear(float scale, float startValue,
			float endValue) {
		if (startValue == endValue) {
			return startValue;
		}
		if (scale <= 0f) {
			return startValue;
		}
		if (scale >= 1f) {
			return endValue;
		}
		return ((1f - scale) * startValue) + (scale * endValue);
	}

	public static float min(float a, float b) {
		if (a != a)
			return a; // a is NaN
		if ((a == 0.0f) && (b == 0.0f)
				&& (Float.floatToIntBits(b) == negativeZeroFloatBits)) {
			return b;
		}
		return (a <= b) ? a : b;
	}

	public static int min(int a, int b) {
		return (a <= b) ? a : b;
	}

	public static float max(float a, float b) {
		if (a != a)
			return a; // a is NaN
		if ((a == 0.0f) && (b == 0.0f)
				&& (Float.floatToIntBits(b) == negativeZeroFloatBits)) {
			return a;
		}
		return (a <= b) ? b : a;
	}

	public static int max(int a, int b) {
		return (a >= b) ? a : b;
	}

	public static float lerpf(float x1, float x2, float p) {
		return x1 * (1.0f - p) + x2 * p;
	}

	public static float pow(float fBase, float fExponent) {
		return (float) java.lang.Math.pow(fBase, fExponent);
	}

	public static float invSqrt(float fValue) {
		return (float) (1.0f / java.lang.Math.sqrt(fValue));
	}

	public static float sqrt(float f) {
		return (float) java.lang.Math.sqrt(f);
	}

	public static float abs(float fValue) {
		if (fValue < 0) {
			return -fValue;
		}
		return fValue;
	}

	public static int floor(float val) {
		int i = (int) val;
		return (val < 0 && val != i) ? i - 1 : i;
	}

	public static int ceil(float f) {
		return floor(f + 1);
	}

	public static float acos(float fValue) {
		if (-1.0f < fValue) {
			if (fValue < 1.0f) {
				return (float) java.lang.Math.acos(fValue);
			}
			return 0.0f;
		}
		return f_PI;
	}

	public static int round(float a) {
		if (a != 0x1.fffffep-2f) // greatest float value less than 0.5
			return floor(a + 0.5f);
		return 0;
	}

	public static float atan2(float fY, float fX) {
		return (float) java.lang.Math.atan2(fY, fX);
	}

	public static float asin(float fValue) {
		if (-1.0f < fValue) {
			if (fValue < 1.0f) {
				return (float) java.lang.Math.asin(fValue);
			}

			return f_HALFPI;
		}

		return -f_HALFPI;
	}

	public static float atan(float fValue) {
		return (float) java.lang.Math.atan(fValue);
	}

	static {
		SIN_TABLE = new float[65536];
		for (int i = 0; i < 65536; i++) {
			SIN_TABLE[i] = (float) java.lang.Math.sin((i * f_PI * 2D) / 65536D);
		}
	}

	public static int abs(int i) {
		return (i >= 0) ? i : -i;
	}

	public static int roundUp(int num, int divisor) {
		return (num + divisor - 1) / divisor;
	}

	public static boolean is_power_of_two(int a) {
		return a == (-a & a);
	}

}
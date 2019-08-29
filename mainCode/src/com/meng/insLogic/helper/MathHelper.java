package com.meng.insLogic.helper;

import com.badlogic.gdx.math.Vector2;

public class MathHelper {

	public static Vector2 polarToCartesian(float length, float radians) {
		return new Vector2((float) (length * Math.cos(radians)), (float) (length * Math.sin(radians)));
	}

	public static void polarToCartesian(Vector2 polar) {
		float x = (float) (polar.x * Math.cos(polar.y));
		float y = (float) (polar.x * Math.sin(polar.y));
		polar.x = x;
		polar.y = y;
	}

	public static Vector2 cartesianToPolar(float x, float y) {
		float length = (float) Math.sqrt(x * x + y * y);
		float radians = 0;
		if (x == 0) {
			if (y > 0) {
				radians = ((float) Math.PI) / 2;
			} else if (y < 0) {
				radians = 3 * ((float) Math.PI) / 2;
			}
		} else {
			radians = (float) Math.atan(y / x);
		}
		return new Vector2(length, radians);
	}

	public static void cartesianToPolar(Vector2 cartesian) {
		float length = (float) Math.sqrt(cartesian.x * cartesian.x + cartesian.y * cartesian.y);
		float radians = 0;
		if (cartesian.x == 0) {
			if (cartesian.y > 0) {
				radians = ((float) Math.PI) / 2;
			} else if (cartesian.y < 0) {
				radians = 3 * ((float) Math.PI) / 2;
			}
		} else {
			radians = (float) Math.atan(cartesian.y / cartesian.x);
		}
		cartesian.x = length;
		cartesian.y = radians;
	}
}

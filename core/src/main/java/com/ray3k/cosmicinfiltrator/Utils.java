package com.ray3k.cosmicinfiltrator;

import com.badlogic.gdx.math.Vector2;

public class Utils {
    private static final Vector2 vector2 = new Vector2();

    public static float degDifference(float sourceAngle, float targetAngle) {
        var angle = targetAngle - sourceAngle;
        angle = mod((angle + 180), 360) - 180;
        return angle;
    }

    private static float mod(float a, float n) {
        return (a % n + n) % n;
    }

    public static float approach(float start, float target, float increment) {
        increment = Math.abs(increment);
        if (start < target) {
            start += increment;

            if (start > target) {
                start = target;
            }
        } else {
            start -= increment;

            if (start < target) {
                start = target;
            }
        }
        return start;
    }

    /**
     * Determine if a polygon is wound clockwise. It may be concave or convex.
     * @param points
     * @return
     */
    public static boolean isClockwise(float[] points) {
        var sum = 0;
        for (int i = 0; i + 3 < points.length; i += 2) {
            sum += (points[i + 2] - points[i]) * (points[i + 3] + points[i + 1]);
        }
        sum += (points[0] - points[points.length - 2]) * (points[1] + points[points.length - 1]);
        return sum > 0;
    }

    public static float[] reverseVertecies(float[] points) {
        var newPoints = new float[points.length];
        for (int i = 0; i < points.length; i += 2) {
            var x = points[i];
            var y = points[i + 1];

            newPoints[points.length - i - 2] = x;
            newPoints[points.length - i - 1] = y;
        }
        return newPoints;
    }

    public static float pointDistance(float x1, float y1, float x2, float y2) {
        vector2.set(x1, y1);
        return vector2.dst(x2, y2);
    }

    public static float pointDirection(float x1, float y1, float x2, float y2) {
        vector2.set(x2, y2);
        vector2.sub(x1, y1);
        return vector2.angleDeg();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.utils;

import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author RemalKoil
 */
public class Matrix2fUtil {

    public static final Matrix2f createRotation(float angle) {
        Matrix2f matrix = new Matrix2f();
        float cos = MathUtils.cos(angle);
        float sin = MathUtils.sin(angle);

        matrix.m00 = cos;
        matrix.m10 = sin;
        matrix.m01 = -sin;
        matrix.m11 = cos;
        return matrix;
    }

    public static final void rotate(Matrix2f matrix, float angle) {
        float cos = MathUtils.cos(angle);
        float sin = MathUtils.sin(angle);

        matrix.m00 = matrix.m00 * cos + matrix.m01 * sin;
        matrix.m10 = matrix.m10 * cos + matrix.m11 * sin;
        matrix.m01 = -matrix.m00 * sin + matrix.m01 * cos;
        matrix.m11 = -matrix.m10 * sin + matrix.m11 * cos;
    }

    public static final Matrix2f createScale(float scalex, float scaley) {
        Matrix2f matrix = new Matrix2f();
        matrix.m00 = scalex;
        matrix.m11 = scaley;
        return matrix;
    }

    public static final void scalate(Matrix2f matrix, float scalex, float scaley) {
        matrix.m00 *= scalex;
        matrix.m01 *= scalex;
        matrix.m10 *= scaley;
        matrix.m11 *= scaley;
    }
}

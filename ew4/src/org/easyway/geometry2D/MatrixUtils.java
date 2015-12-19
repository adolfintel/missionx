/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.geometry2D;

import org.easyway.utils.MathUtils;
import org.lwjgl.util.vector.Matrix3f;

/**
 *
 * @author RemalKoil
 */
public class MatrixUtils {

    public static Matrix3f scale(float scaleX, float scaleY, Matrix3f matrix) {
        Matrix3f scaleMatrix = new Matrix3f();
        scaleMatrix.m00 = scaleX;
        scaleMatrix.m11 = scaleY;
        return Matrix3f.mul(matrix, scaleMatrix, scaleMatrix);
    }

    public static Matrix3f translate(float x, float y, Matrix3f matrix) {
        Matrix3f translate = new Matrix3f();
        translate.m02 = x;
        translate.m12 = y;
        return Matrix3f.mul(matrix, translate, translate);
    }

    public static Matrix3f rotate(float alpha, Matrix3f matrix) {
        float cosA = MathUtils.cos(alpha);
        float sinA = MathUtils.sin(alpha);
        Matrix3f rotation = new Matrix3f();
        rotation.m11 = rotation.m00 = cosA;
        rotation.m01 = -sinA;
        rotation.m10 = sinA;
        return Matrix3f.mul(matrix, rotation, rotation);
    }
    
}

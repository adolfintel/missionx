/* EasyWay Game Engine
 * Copyright (C) 2006 Daniele Paggi.
 *  
 * Written by: 2006 Daniele Paggi<dshnt@hotmail.com>
 *   
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 * 
 * You should have received a copy of the GNU Library General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.easyway.utils;

import org.easyway.system.Core;
import org.lwjgl.util.vector.Vector3f;

/**
 * A very-little collection of mathematical functions
 * 
 * @author Daniele Paggi
 * 
 */
public final class MathUtils {

    private static float sin[];
    private static float cos[];
    public static final float PI = 3.1415926535f;
    private static float radToScale = 4096f / 3.14159265f / 2f;
    private static float step = 256 * 3.14159265f; // PI * 256

    /**
     * disallowing new instances
     */
    private MathUtils() {
    }

    // --------------------------------- TRIGONOMETRY
    // -----------------------------------
    /**
     * from degree to radiant
     */
    public static final float degToRad(float rad) {
        return rad * 0.0174532925194f;
    }

    /**
     * from radiant to degree
     */
    public static final float radToDeg(float deg) {
        return deg * 57.295779514719f;
    }

    /**
     * retuns the sin of angle
     *
     * @param angle
     *            angle
     * @return the sin of angle
     */
    public static final float sin(float angle) {
        if (sin == null) {
            init();
        }
        return sin[(int) ((angle + step) * radToScale) & 0xFFF];
    }

    /**
     * returns the cosin of angle
     *
     * @param angle
     *            angle
     * @return the cosin of angle
     */
    public static final float cos(float angle) {
        if (cos == null) {
            init();
        }
        return cos[(int) ((angle + step) * radToScale) & 0xFFF];
    }

    /**
     * creates the precalculated reference for sin & cosin.
     */
    public static void init() {
        if (sin != null) {
            return;
        }
        System.out.print("Building Math LUT...");
        sin = new float[4096];
        cos = new float[4096];

        for (int i = 0; i < 4096; i++) {
            sin[i] = (float) Math.sin((float) i / radToScale);
            cos[i] = (float) Math.cos((float) i / radToScale);
        }
        System.out.println("Done!");
    }

    // ------------------------------------ RANDOMS NUMBERS ---------------
    /**
     * generates a random number in range of min:max
     */
    public static final float random(float min, float max) {
        return (float) (Math.random() * (max - min) + min);
    }

    // ------------------------------- VECTORS ------------------------------
    /**
     * returns the normal vector of the plane defined by the two vectors
     */
    public static Vector3f getNormal(Vector3f a, Vector3f b) {
        return (Vector3f) cross(a, b).normalise();
    }

    /**
     * returns the normal vector of the plane defined by the two vectors
     */
    public static Vector3f getNormal(Vector3f a, Vector3f b, Vector3f c) {
        return (Vector3f) cross(a, b, c).normalise();
    }

    /**
     * returns a x b
     */
    public static Vector3f cross(Vector3f a, Vector3f b) {
        return new Vector3f(a.y * b.z - b.y * a.z, a.z * b.x - b.z * a.x, a.x * b.y - b.x * a.y);
    }

    /**
     * returnss (b-a) x (c-a)
     */
    public static Vector3f cross(Vector3f a, Vector3f b, Vector3f c) {
        return cross(Vector3f.sub(b, a, null), Vector3f.sub(c, a, null));
    }

    /**
     * returns the angle between 2 vectors
     */
    public static float angle(Vector3f a, Vector3f b) {
        a.normalise();
        b.normalise();
        return (a.x * b.x + a.y * b.y + a.z * b.z);
    }

    /**
     * executes a linear interpolation between 2 floats
     */
    public static float lerp(float a, float b, float percent) {
        return (a * percent) + (b * (1 - percent));
    }

    public static float lerp(float start, float end, float current, long milliTotTime) {
        long elTime = Core.getInstance().getElaspedTimeMilli();
        float percentTimeToInc = (float) elTime / (float) milliTotTime;

        float currentPercent = current - start / current - end;
        float newPercValue = currentPercent + percentTimeToInc;
        if (newPercValue > 1) {
            newPercValue = 1.0f;
        }
        return (1 - newPercValue) * start + (newPercValue * end);
    }
}

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

/**
 * This is an utility for vector specified by an java-array<br>
 * It's an old utility-class of the Game Engine..
 * 
 * @author Daniele Paggi
 * 
 */
public final class VectorUtil {

	private VectorUtil() {
	}

	public static final float lengthSquared(float vec[]) {
		return vec[0] * vec[0] + vec[1] * vec[1] * vec[2] * vec[2];
	}

	public static final float length(float vec[]) {
		return (float) Math.sqrt(vec[0] * vec[0] + vec[1] * vec[1] * vec[2]
				* vec[2]);
	}

	public static float[] add(float vec[], float vec2[]) {
		return new float[] { vec[0] + vec2[0], vec[1] + vec2[1],
				vec[2] + vec2[2] };
	}

	public static float[] sub(float vec[], float vec2[]) {
		return new float[] { vec[0] - vec2[0], vec[1] - vec2[1],
				vec[2] - vec2[2] };
	}

	public static float[] cross(float vec[], float vec2[]) {
		return new float[] { vec[1] * vec2[2] - vec[2] * vec2[1],
				vec[0] * vec2[2] - vec[2] * vec2[0],
				vec[0] * vec2[1] - vec[1] * vec2[0] };
	}

	public static void normalize(float vec[]) {
		// the moltiplication are more fast that the divisions..
		float len = 1 / length(vec);
		vec[0] *= len;
		vec[1] *= len;
		vec[2] *= len;
	}

	public static float dot(float vec[], float vec2[]) {
		return vec[0] * vec2[0] + vec[1] * vec2[1] + vec[2] * vec2[2];
	}

	public static float angle(float vec[], float vec2[]) {
		float dls = dot(vec, vec2)
				/ (float) Math.sqrt(lengthSquared(vec) * lengthSquared(vec2));
		if (dls < -1f)
			dls = -1f;
		else if (dls > 1.0f)
			dls = 1.0f;
		return (float) Math.acos(dls);
	}

}

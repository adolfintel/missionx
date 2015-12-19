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
package org.easyway.geometry2D;

public class VectorSpeed {

	/**
	 * transform from Polar coordinates to cartesian coordinate
	 * 
	 * @param angle
	 *            the angle of polar coordinate
	 * @param distance
	 *            the lenght of polar coordinate
	 * @return an array of float:<br>
	 *         the float[0] is the X.<br>
	 *         the flaot[1] is the Y.
	 */
	public static final double[] getCartesian(double angle, double distance) {
		double cor[] = new double[2];
		cor[0] = Math.cos(angle) * distance;
		cor[1] = Math.sin(angle) * distance;
		return cor;
	}

	/**
	 * transform from cartesian coordinates to polar coordinates.
	 * 
	 * @param x
	 * @param y
	 * @return [0] = angle [1] = lenght
	 */
	public static final double[] getPolar(double x, double y) {
		double pol[] = new double[2];
		// if (x != 0) {
		// pol[0] = Math.atan(y / x);
		//
		// if (x < 0)
		// pol[0] += Math.PI;
		//
		// } else {
		// pol[0] = y > 0 ? Math.PI / 2 : Math.PI * 1.5;
		// }
		pol[1] = Math.sqrt(x * x + y * y);
		if (pol[1] != 0) {
			pol[0] = Math.asin(y / pol[1]);
		} else {
			pol[0] = 0;
		}
		return pol;
	}

	public static final double getAngle(double x, double y) {
		double alpha;
		if (x != 0) {
			alpha = Math.atan(y / x);

			if (x < 0)
				alpha += Math.PI;

		} else {
			alpha = y > 0 ? Math.PI / 2 : Math.PI * 1.5;
		}
		return alpha;
	}
}

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

@Deprecated
public class Retta {

	/** coefficente angolare */
	float m;

	/** intersezione con l'asse Y */
	float q;

	/** x = k */
	float k;

	/** retta in forma y = mx + q */
	public Retta(float m, float q) {
		this.m = m;
		this.q = q;
		this.k = Float.NaN;
	}

	/** retta in forma x = k */
	public Retta(float k) {
		this.k = k;
		this.m = Float.POSITIVE_INFINITY;
		this.q = Float.NaN;
	}

	/**
	 * retta passante per i punti P(x,y) e Q(x1,y1)
	 * 
	 * @param x
	 *            point 1
	 * @param y
	 *            point 1
	 * @param x1
	 *            point 2
	 * @param y1
	 *            point 2
	 */
	public Retta(float x, float y, float x1, float y1) {
		if (x1 - x != 0) {
			m = (y1 - y) / (x1 - x);
			q = y - m * x;
			k = Float.NaN;
		} else {
			m = Float.POSITIVE_INFINITY;
			q = Float.NaN;
			k = x;
		}
	}

	/**
	 * data la X ritorna la Y<br>
	 * 
	 * @param y
	 * @return returns X or Float.POSITIVE_INFINITY if the value can't be found<br>
	 */
	public float getXfromY(float y) {
		if (k != Float.NaN)
			if (m == 0) {
				return Float.POSITIVE_INFINITY;
			} else {
				return (y - q) / m;
			}
		return k;
	}

	/**
	 * data la Y ritorna la X<br>
	 * 
	 * @param x
	 * @return returns the X or Float.POSITIVE_INFINITY if the value can't be found<br>
	 */
	public float getYfromX(float x) {
		if (k != Float.NaN)
			return m * x + q;
		return Float.POSITIVE_INFINITY;
	}

	/**
	 * 
	 * @param r
	 * @return null or Point2D()
	 */

	public Point2D getPoint(Retta r) {
		if (r == null)
			return null;

		float x, y;
		if (k == Float.NaN) {
			if (r.k == Float.NaN) {
				return null;
			}
			x = k;
			y = r.m * x + r.q;
			return new Point2D(x, y);
		}
		if (r.k == Float.NaN) {
			x = k;
			y = r.m * x + r.q;
			return new Point2D(x, y);
		}
		if (r.m - m == 0)
			return null;
		x = (r.q - q) / (r.m - m);
		y = m * x + q;
		return new Point2D(x, y);
	}

	/**
	 * controlla se un punto appartiene alla retta
	 * 
	 * @param point
	 * @return
	 */
	public boolean testPoint(Point2D point) {
		if (point == null)
			return false;
		if (getXfromY(point.y) == point.x)
			return true;
		return false;
	}

	public float[] collision(Retta r) {
		// parallele
		if (m == r.m) {
			if (q == r.q) {
				return new float[] { Float.POSITIVE_INFINITY,
						Float.POSITIVE_INFINITY };
			}
			if (k == r.k) { // rette nella forma: x = k
				return new float[] { Float.POSITIVE_INFINITY,
						Float.POSITIVE_INFINITY };
			}
			return null;
		}

		if (m == Float.POSITIVE_INFINITY) {
			return new float[] { k, r.m * k + r.q };
		}

		if (r.m == Float.POSITIVE_INFINITY) {
			return new float[] { r.k, m * r.k + q };
		}

		float xx = q - r.q / (m - r.m);
		float yy = m * xx + q;
		return new float[] { xx, yy };
		// return null;
	}

}

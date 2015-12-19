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

import java.io.Serializable;

import org.easyway.interfaces.sprites.IPoint2D;
import org.easyway.objects.BaseObject;

public class Point2D extends BaseObject implements IPoint2D, Serializable {
	static final long serialVersionUID = 200;

	float x;

	float y;

	public Point2D(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public float getXOnScreen() {
		return x;
	}

	public float getYOnScreen() {
		return y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

    @Override
	public String toString() {
		return "Point2D [x=" + x + ",y=" + y + "]";
	}

	public boolean equals(Point2D p) {
		if (x == p.getX() && y == p.getY())
			return true;
		return false;
	}
}

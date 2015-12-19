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
package org.easyway.interfaces.sprites;

import org.easyway.collisions.quad.IQuadNodeElement;
import org.easyway.interfaces.base.IBaseObject;

public interface IPlain2D extends IPoint2D, IQuadNodeElement, IBaseObject {
	/**
	 * returns the width of plain
	 */
	public int getWidth();

	/**
	 * returns the height of plain
	 */
	public int getHeight();

	/**
	 * sets the width of plain
	 */
	public void setWidth(int width);

	/**
	 * sets the height of plain
	 */
	public void setHeight(int height);

	/**
	 * sets the width and height of plain
	 */
	public void setSize(int width, int height);
	
	/**
	 * sets the new X coordinate
	 */
	public void setX(float x);
	/**
	 * sets the new Y coordinate
	 */
	public void setY(float y);
	
	/**
	 * sets the new x,y coordinates
	 */
	public void setXY( float x, float y);

        public float getScaleX();

        public float getScaleY();
	
}

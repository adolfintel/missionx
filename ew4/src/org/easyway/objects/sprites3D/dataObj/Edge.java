/* EasyWay Game Engine
 * Copyright (C) 2006 Daniele Paggi.
 *  
 * Written by: 2006 Daniele Paggi<dshnt@hotmail.com>
 *   
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public License
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
package org.easyway.objects.sprites3D.dataObj;

/**
 * this class rappresent a "Segment" in the 3D space
 * @author Daniele Paggi
 *
 */
public class Edge {
	
	/**
	 * starting and ending poing.
	 */
	Vertex a, b;

	/**
	 * creates a new instace of the object
	 */
	private Edge() {
	}

	/**
	 * creates a new instace of the object<br>
	 * make a line starting from v1 and ending in v2.
	 */
	public Edge(Vertex v1, Vertex v2) {
		a = v1;
		b = v2;
	}

	/**
	 * returns the starting point of the edge
	 */
	public Vertex start() {
		return a;
	}

	/**
	 * returns the ending point of the edge
	 * @return
	 */
	public Vertex end() {
		return b;
	}
}

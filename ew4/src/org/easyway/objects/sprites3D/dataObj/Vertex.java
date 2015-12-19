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

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.util.vector.Vector3f;

public class Vertex {

	/** coordinates of vertex */
	public Vector3f pos;

	/** normal vector at vertex */
	public Vector3f normal;

	/** texture x-coordinate */
	public float s = 0;

	/** texture y-coordinate */
	public float t = 0;

	/** index into vertices array */
	public int id;

	/** parents */
	public ArrayList<Triangle> parents = new ArrayList<Triangle>(4); // Neighbor

	/** material of vertex */
	public Material material;
	
	/** future feature */
	//public float red,green,blue;

	// ------------------------------------------------------------------------
	// ------------------------ CONSTRUCTORS -----------------------
	// ------------------------------------------------------------------------
	/** crates a new vertex */
	public Vertex() {
		pos = new Vector3f(0f, 0f, 0f);
		normal = new Vector3f();
	}

	/** creates a new vertex */
	public Vertex(float xpos, float ypos, float zpos) {
		pos = new Vector3f(xpos, ypos, zpos);
		normal = new Vector3f();
	}

	/** creates a new vertex */
	public Vertex(float xpos, float ypos, float zpos, float s, float t) {
		pos = new Vector3f(xpos, ypos, zpos);
		normal = new Vector3f();
		this.s = s;
		this.t = t;
	}

	/** creates a new vertex */
	public Vertex(Vector3f pos) {
		this(pos.x, pos.y, pos.z);
	}

	/** creates a new vertex */
	public Vertex(Vector3f pos, float u, float v) {
		this(pos.x, pos.y, pos.z, u, v);
	}

	/**
	 * make a copy-vertex
	 */
	public Vertex(Vertex vec) {
		this.s = vec.s;
		this.t = vec.t;
		this.pos = new Vector3f(vec.pos);
		this.normal = new Vector3f(vec.normal);
	}

	// ------------------------------------------------------------------------
	// ------------------------ METHODS --------------------------------
	// ------------------------------------------------------------------------
	/**
	 * sets the coordiates of the texture
	 * 
	 * @param s
	 *            horizontal coordinate
	 * @param t
	 *            vertical coordinate
	 */
	public void setST(float s, float t) {
		this.s = s;
		this.t = t;
	}

	// ------------------------ PARENTS ----------------------
	/**
	 * add a neighbor (parent) of the vertex.
	 * 
	 */
	void addParent(Triangle triangle) {
		if (!parents.contains(triangle))
			parents.add(triangle);
	}

	/**
	 * removes all the neighbor (parents) of the vertex.
	 * 
	 */
	void resetParents() {
		parents.clear();
	}

	// ------------------------ NORMALS ----------------------
	/**
	 * generates the normal of the vertex
	 * 
	 */
	public void regenerateNormal() {
		float nx = 0;
		float ny = 0;
		float nz = 0;
		Iterator<Triangle> iterator = parents.iterator();
		if (parents.size() == 0) {
			System.out.println("3DS DAMAGED!");
			nx = 1;
		}
		Triangle tri;
		Vector3f wn;
		while (iterator.hasNext()) {
			tri = iterator.next();
			wn = tri.getWeightedNormal();
			nx += wn.x;
			ny += wn.y;
			nz += wn.z;
		}
		// normal = (Vector3f) new Vector3f(nx, ny, nz).normalise();
		normal.set(nx, ny, nz);
		normal.normalise();
	}

	// -------------------------------------------------
	// -------------------------------------------------

	public String toString() {
		return new String("<vertex  x=" + pos.x + " y=" + pos.y + " z=" + pos.z
				+ " u=" + s + " v=" + t + ">\r\n");
	}

	/** checks if 2 vertex are equals */
	public boolean equals(Vertex v) {
		return ((pos.x == v.pos.x) && (pos.y == v.pos.y) && (pos.z == v.pos.z));
	}

	/** checks if 2 vertex are equals with a tolerance */
	public boolean equals(Vertex v, float epsilon) {
		return Math.abs(Vector3f.sub(pos, v.pos, null).length()) < epsilon;
	}

}
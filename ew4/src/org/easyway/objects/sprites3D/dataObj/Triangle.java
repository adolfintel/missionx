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

import org.easyway.interfaces.base.IPureRender;
import org.easyway.utils.MathUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Triangle implements IPureRender {
	// public Object3D parent; // the object which obtains this triangle

	/**
	 * first vertex
	 */
	public Vertex p1;

	/**
	 * second vertex
	 */
	public Vertex p2;

	/**
	 * third vertex
	 */
	public Vertex p3;

	/**
	 * normal vector
	 */
	public Vector3f normal;

	/**
	 * u,v coordinates for all 3 vertexs<br>
	 * TODO: this code is optimizable not usig arrays?
	 */
	public float uv[][] = new float[3][2];

	/**
	 * material of triangle
	 */
	public Material material = null;

	// --------------------- CONSTRUCTORS ------------------------------
	/**
	 * creates a new triangle from 3 vertex
	 */
	public Triangle(Vertex a, Vertex b, Vertex c) {
		this(a, b, c, null);
	}

	/**
	 * creates a new triangle from 3 vertex and set a material to the triangle
	 * 
	 * @param a
	 *            first vertex
	 * @param b
	 *            second vertex
	 * @param c
	 *            third vertex
	 * @param material
	 *            triangle's material
	 */
	public Triangle(Vertex a, Vertex b, Vertex c, Material material) {
		p1 = a;
		p2 = b;
		p3 = c;
		this.material = material;
	}

	/**
	 * creates a new triangle from another
	 * 
	 * @param triangle
	 *            triangle to copy
	 */
	public Triangle(Triangle triangle) {
		this(new Vertex(triangle.p1), new Vertex(triangle.p2), new Vertex(
				triangle.p3));
		this.uv = triangle.uv;
		this.material = triangle.material;
	}

	// ----------------------------------PUBLIC-------------------------------

	/**
	 * generate the normal vector of triangle
	 */
	public void normalize() {
		normal = MathUtils.getNormal(p1.pos, p2.pos, p3.pos);
	}

	// TODO: increase comments
	/**
	 * returns the weighted normal:<br>
	 * A normal vector that hasn't the lenght egual to 1..
	 */
	public Vector3f getWeightedNormal() {
		return MathUtils.cross(p1.pos, p2.pos, p3.pos);
	}

	/**
	 * returns a new vertex that is the center of the triangle and gets his
	 * probable texture's coordinates.
	 * 
	 * @return a new vertex that is the center of the triangle and gets his
	 *         probabily texture's coordinates.
	 * @see getCenter()
	 */
	public Vertex getMedium() {
		float cx = (p1.pos.x + p2.pos.x + p3.pos.x) / 3;
		float cy = (p1.pos.y + p2.pos.y + p3.pos.y) / 3;
		float cz = (p1.pos.z + p2.pos.z + p3.pos.z) / 3;
		float cu = (p1.s + p2.s + p3.s) / 3;
		float cv = (p1.t + p2.t + p3.t) / 3;
		return new Vertex(cx, cy, cz, cu, cv);
	}

	/**
	 * returns the center of triangle
	 * 
	 * @return the center of triangle
	 * @see getMedium()
	 */
	public Vector3f getCenter() {
		float cx = (p1.pos.x + p2.pos.x + p3.pos.x) / 3;
		float cy = (p1.pos.y + p2.pos.y + p3.pos.y) / 3;
		float cz = (p1.pos.z + p2.pos.z + p3.pos.z) / 3;
		return new Vector3f(cx, cy, cz);
	}

	/**
	 * a degenerate triangle is a triangle that has 2 or 3 equals vertex.
	 * 
	 * @return if the triangle is degenerated or not
	 */
	public boolean isDegenerated() {
		return p1.equals(p2) || p2.equals(p3) || p3.equals(p1);
	}

	@Deprecated
	public void render() {
		if (material != null) {
			if (material.texture != null) {
				// GL11.glEnable(GL11.GL_TEXTURE_2D);
				material.texture.bind();
			} else {
				GL11.glDisable(GL11.GL_TEXTURE_2D);
			}
			GL11.glColor4f(material.red, material.green, material.blue,
					material.transparency);
		}

		GL11.glBegin(GL11.GL_TRIANGLES);
		// GL11.glTexCoord2f(p1.)
		GL11.glVertex3f(p1.pos.x, p1.pos.y, p1.pos.z);
		GL11.glVertex3f(p2.pos.x, p2.pos.y, p2.pos.z);
		GL11.glVertex3f(p3.pos.x, p3.pos.y, p3.pos.z);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);

	}
}
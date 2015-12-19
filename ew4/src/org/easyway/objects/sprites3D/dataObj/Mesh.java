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
import java.util.Enumeration;
import java.util.Vector;

import org.easyway.utils.Utility;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Mesh {

	public ArrayList<Vertex> vertexData = new ArrayList<Vertex>(300);

	public ArrayList<Triangle> triangleData = new ArrayList<Triangle>(100);

	// public Vertex[] vertex;

	// public Triangle[] triangle;

	// public int vertices = 0;

	// public int triangles = 0;

	/**
	 * the name of the mesh
	 */
	public String name;

	// public boolean visible = true;

	public double x, y, z;

	/**
	 * indicates if the object is using relatives coordinates or not.<br>
	 * for init this you should use: deatach()
	 */
	public boolean deatached = false;

	/** transformation matrix */
	public Matrix4f matrix;

	// -------------------------------------------CONSTRUCTORS--------------
	public Mesh() {
	}

	// -------------------------------------------USERS--------------------

	public Vertex vertex(int id) {
		return vertexData.get(id);
	}

	public Triangle triangle(int id) {
		return triangleData.get(id);
	}

	public void addVertex(Vertex newVertex) {
		vertexData.add(newVertex);
		// dirty = true;
	}

	public void addTriangle(Triangle newTriangle) {
		triangleData.add(newTriangle);
		// dirty = true;
	}

	/** aggiunge un triangolo dati 3 vertici pre-esistenti */
	public void addTriangle(int index1, int index2, int index3) {
		addTriangle(vertex(index1), vertex(index2), vertex(index3));
	}

	public void removeVertex(Vertex v) {
		vertexData.remove(v);
	}

	public void removeTriangle(Triangle t) {
		triangleData.remove(t);
	}

	public void removeVertexAt(int index) {
		vertexData.remove(index);
	}

	public void removeTriangleAt(int index) {
		triangleData.remove(index);
	}

	@Deprecated
	public void addVertex(float x, float y, float z) {
		addVertex(new Vertex(x, y, z));
	}

	@Deprecated
	public void addVertex(float x, float y, float z, float u, float v) {
		Vertex vert = new Vertex(x, y, z);
		vert.setST(u, v);
		addVertex(vert);
	}

	public void addTriangle(Vertex a, Vertex b, Vertex c) {
		Triangle t;
		addTriangle(t = new Triangle(a, b, c));
		a.addParent(t);
		b.addParent(t);
		c.addParent(t);
	}

	public void addTriangle(Vertex a, Vertex b, Vertex c, Material mat) {
		addTriangle(new Triangle(a, b, c, mat));
	}

	/**
	 * aggiusta le coordinate dei vertici sulla texture<br>
	 * example:<br>
	 * adjustMap()<br>
	 * rebuild()<br>
	 * CAUTION: Don't use this method more that 1 time!
	 */
	private void rebuildUV() {
		float cw, ch;
		Triangle tr;
		Material mat;
		Vertex vtemp;
		for (int i = 0; i < triangleData.size(); ++i) {
			tr = triangleData.get(i);
			assert tr != null;
			// if (tr == null)
			// continue;
			if ((mat = tr.material) == null)
				continue;
			if (mat.texture == null /* || mat.texture.isRegular */)
				continue;

			ch = mat.texture.getHeight();// getYEnd(); // (float)
			// mat.texture.height /
			// mat.texture.heightHW;
			cw = mat.texture.getWidth();// getXEnd(); // (float)
			// mat.texture.width /
			// mat.texture.widthHW;

			vtemp = tr.p1;
			tr.uv[0][0] = vtemp.s * cw;
			tr.uv[0][1] = vtemp.t * ch;
			vtemp = tr.p2;
			tr.uv[1][0] = vtemp.s * cw;
			tr.uv[1][1] = vtemp.t * ch;
			vtemp = tr.p3;
			tr.uv[2][0] = vtemp.s * cw;
			tr.uv[2][1] = vtemp.t * ch;
		}
		// dirty = true;
		// rebuild();
	}

	/** rebuild the object */
	public void rebuild() {
		rebuildUV();

		for (int i = 0; i < vertexData.size(); ++i) {
			vertexData.get(i).id = i;
		}

		Triangle tri;
		for (int i = triangleData.size() - 1; i >= 0; i--) {
			tri = triangleData.get(i);
			tri.p1.addParent(tri);
			tri.p2.addParent(tri);
			tri.p3.addParent(tri);
		}
		normalize();
	}

	/** Regenerates the vertex normals */
	private void normalize() {
		for (int i = 0; i < triangleData.size(); i++)
			triangleData.get(i).normalize();
		for (int i = 0; i < vertexData.size(); i++)
			vertexData.get(i).regenerateNormal();
	}

	/** returns the min x,y,z */
	public Vector3f min() {
		if (vertexData.size() == 0)
			return new Vector3f(0f, 0f, 0f);
		float minX = vertexData.get(0).pos.x;
		float minY = vertexData.get(0).pos.y;
		float minZ = vertexData.get(0).pos.z;
		for (int i = 1; i < vertexData.size(); i++) {
			if (vertexData.get(i).pos.x < minX)
				minX = vertexData.get(i).pos.x;
			if (vertexData.get(i).pos.y < minY)
				minY = vertexData.get(i).pos.y;
			if (vertexData.get(i).pos.z < minZ)
				minZ = vertexData.get(i).pos.z;
		}
		return new Vector3f(minX, minY, minZ);
	}

	/** returns the max x,y,z */
	public Vector3f max() {
		if (vertexData.size() == 0)
			return new Vector3f(0f, 0f, 0f);
		float maxX = vertexData.get(0).pos.x;
		float maxY = vertexData.get(0).pos.y;
		float maxZ = vertexData.get(0).pos.z;
		for (int i = 1; i < vertexData.size(); i++) {
			if (vertexData.get(i).pos.x > maxX)
				maxX = vertexData.get(i).pos.x;
			if (vertexData.get(i).pos.y > maxY)
				maxY = vertexData.get(i).pos.y;
			if (vertexData.get(i).pos.z > maxZ)
				maxZ = vertexData.get(i).pos.z;
		}
		return new Vector3f(maxX, maxY, maxZ);
	}

	/**
	 * Centers the object in its coordinate system The offset from origin to
	 * object center will be transfered to the matrix, so your object actually
	 * does not move. Usefull if you want prepare objects for self rotation.
	 */
	public void detach(boolean first, float sx, float sy, float sz) {
		Vector3f center = getCenter();
		if (!first) {
			x = center.x - sx;
			y = center.y - sy;
			z = center.z - sz;
		}
		for (int i = 0; i < vertexData.size(); i++) {
			vertexData.get(i).pos.x -= center.x;
			vertexData.get(i).pos.y -= center.y;
			vertexData.get(i).pos.z -= center.z;
		}
		deatached = true;
	}

	/** Returns the center of this object */
	public Vector3f getCenter() {
		Vector3f max = max();
		Vector3f min = min();
		return new Vector3f((max.x + min.x) / 2, (max.y + min.y) / 2,
				(max.z + min.z) / 2);
	}

	/** Returns the x,y,z - Dimension of this object */
	public Vector3f getDimension() {
		Vector3f max = max();
		Vector3f min = min();
		return new Vector3f(max.x - min.x, max.y - min.y, max.z - min.z);
	}

	/** removes the duplicated vertexs */
	public void removeDuplicateVertices() {
		rebuild();
		Vector<Edge> edgesToCollapse = new Vector<Edge>(50);
		int cont = 0;
		Vertex a, b;
		for (int i = 0; i < vertexData.size() - 1; i++) {
			a = vertex(i);
			for (int j = i + 1; j < vertexData.size(); j++) {
				b = vertex(j);
				// if the vertex are equals and have the same material
				// associated we can remove them
				if (a.equals(b, 0.0001f) && a.s == b.s && a.t == b.t
						&& a.material == b.material) {
					++cont;
					edgesToCollapse.addElement(new Edge(vertexData.get(i),
							vertexData.get(j)));
				}
			}
		}
		Utility.error("found <" + cont + "> duplicated vertexs",
				"Object3D.removeDuplicateVertices");
		Enumeration<Edge> enumm = edgesToCollapse.elements();
		while (enumm.hasMoreElements())
			edgeCollapse(enumm.nextElement());

		removeDegeneratedTriangles(); // + rebuild():
		// the rebuild() method is already contained in the
		// removeDegeneratedTriangles()
	}

	/** Collapses the edge [a,b] by replacing b by a */
	private void edgeCollapse(Edge edge) {
		Vertex a = edge.start();
		Vertex b = edge.end();
		assert vertexData.contains(a);
		assert vertexData.contains(b);

		// UGLY CODE!!! I've optimized it.. we should test .. ^_^
		// the Vertex had already the triangles in witch they are contained in
		// the "prents" list..

		// Triangle tri;
		// int ttriangles = triangleData.size();
		// for (int i = 0; i < ttriangles; i++) {
		// tri = triangle(i);
		// if (tri.p1 == b)
		// tri.p1 = a;
		// if (tri.p2 == b)
		// tri.p2 = a;
		// if (tri.p3 == b)
		// tri.p3 = a;
		// }
		Triangle t;
		for (int i = 0; i < b.parents.size(); ++i) {
			t = b.parents.get(i);
			if (t.p1 == b) {
				t.p1 = a;
				a.addParent(t);
			}
			if (t.p2 == b) {
				t.p2 = a;
				a.addParent(t);
			}
			if (t.p3 == b) {
				t.p3 = a;
				a.addParent(t);
			}
		}
		vertexData.remove(b);
	}

	/** removes the triangles that have 2 vertex one on the other */
	public void removeDegeneratedTriangles() {
		// this section can generate bugs if we start from 0
		for (int i = triangleData.size() - 1; i >= 0; --i)
			if (triangle(i).isDegenerated()) {
				removeTriangleAt(i);
			}
		rebuild();
	}

	/**
	 * smooth the mesh
	 * 
	 */
	public void meshSmooth() {
		// TODO: smoothing the object we'll lost the material information..
		rebuild();
		Triangle tri;
		Material mat;
		// float u, v;
		Vertex a, b, c, d, e, f;// , temp;
		Vector3f ab, bc, ca, nab, nbc, nca; // , center;
		float sab, sbc, sca, rab, rbc, rca;
		float uab, vab, ubc, vbc, uca, vca;
		// float sqrt3 = (float) Math.sqrt(3f);
		System.out.println("hereeeeeeeee");
		for (int i = triangleData.size()-1; i >=0; --i) {
			tri = triangle(i);
			mat = tri.material;
			a = tri.p1;
			b = tri.p2;
			c = tri.p3;

			ab = (Vector3f) Vector3f.add(b.pos, a.pos, null).scale(0.5f);
			bc = (Vector3f) Vector3f.add(c.pos, b.pos, null).scale(0.5f);
			ca = (Vector3f) Vector3f.add(a.pos, c.pos, null).scale(0.5f);
			rab = Vector3f.sub(ab, a.pos, null).length();
			rbc = Vector3f.sub(bc, b.pos, null).length();
			rca = Vector3f.sub(ca, c.pos, null).length();

			nab = (Vector3f) Vector3f.add(a.normal, b.normal, null).scale(0.5f);
			nbc = (Vector3f) Vector3f.add(b.normal, c.normal, null).scale(0.5f);
			nca = (Vector3f) Vector3f.add(c.normal, a.normal, null).scale(0.5f);

			uab = 0.5f * (a.s + b.s);
			vab = 0.5f * (a.t + b.t);
			ubc = 0.5f * (b.s + c.s);
			vbc = 0.5f * (b.t + c.t);
			uca = 0.5f * (c.s + a.s);
			vca = 0.5f * (c.t + a.t);

			sab = 1f - nab.length();
			sbc = 1f - nbc.length();
			sca = 1f - nca.length();
			nab.normalise();
			nbc.normalise();
			nca.normalise();

			d = new Vertex(Vector3f.sub(ab, (Vector3f) nab.scale(rab * sab),
					null), uab, vab);
			e = new Vertex(Vector3f.sub(bc, (Vector3f) nbc.scale(rbc * sbc),
					null), ubc, vbc);
			f = new Vertex(Vector3f.sub(ca, (Vector3f) nca.scale(rca * sca),
					null), uca, vca);

			addVertex(d);
			addVertex(e);
			addVertex(f);
			tri.p2 = d;
			tri.p3 = f;
			addTriangle(b, e, d, mat);
			addTriangle(c, f, e, mat);
			addTriangle(d, e, f, mat);
		}

		removeDuplicateVertices();
	}

	public void optimize() {
		removeDuplicateVertices();
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<object id=" + name + ">\r\n");
		for (int i = 0; i < vertexData.size(); i++)
			buffer.append(vertexData.get(i).toString());
		return buffer.toString();
	}

}
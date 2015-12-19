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
package org.easyway.objects.sprites3D;

import java.io.Serializable;
import java.util.ArrayList;

import org.easyway.objects.sprites3D.dataObj.Material;
import org.easyway.objects.sprites3D.dataObj.Mesh;
import org.easyway.objects.texture.Texture;
import org.lwjgl.util.vector.Vector3f;

public class Sprite3DData implements Serializable {

	private static final long serialVersionUID = -130913489497578033L;

	/**
	 * meshes of the object
	 */
	public ArrayList<Mesh> objects;

	/**
	 * the materials that are used in the object
	 */
	public ArrayList<Material> materials;

	/**
	 * the textures that are used in the object
	 */
	public ArrayList<Texture> textures;

	/**
	 * crates a new empty instance of Sprite3DData.<br>
	 * the Sprite3DData should be filled by a 3d object-Loader.
	 */
	public Sprite3DData() {
		objects = new ArrayList<Mesh>(5);
		materials = new ArrayList<Material>(5);
		textures = new ArrayList<Texture>(5);
	}

	/**
	 * returns a mesh
	 */
	public Mesh getMesh(int index) {
		return objects.get(index);
	}

	/**
	 * returns the mesh that have the name specified<br>
	 * if th mesh don't exist it returns null.
	 */
	public Mesh getMesh(String name) {
		Mesh t;
		for (int i = 0; i < objects.size(); i++) {
			if ((t = getMesh(i)).name.equals(name))
				return t;
		}
		return null;
	}

	/**
	 * returns a texture of the object
	 */
	public Texture getTexture(int index) {
		return textures.get(index);
	}

	/**
	 * returns a material
	 */
	public Material getMaterial(int index) {
		return (Material) materials.get(index);
	}

	/**
	 * returns the material that have the name specified<br>
	 * if the material don't exist it returns null.
	 */
	public Material getMaterial(String name) {
		Material t;
		for (int i = 0; i < materials.size(); i++) {
			if ((t = getMaterial(i)).name.compareTo(name) == 0)
				return t;
		}
		return null;

	}

	/**
	 * number of polygons.
	 */
	public int countPoligons() {
		int count = 0;
		for (int k = 0; k < objects.size(); k++)
			count += ((Mesh) objects.get(k)).triangleData.size();
		return count;
	}

	/**
	 * print in the System.out the number of polygons.
	 */
	public void printNumberOfPoligons() {
		System.out.println("number of polygons: " + countPoligons());
	}

	/**
	 * smooth the 3D object <br>
	 * TODO: smoothing the object we'll lost the material information..
	 */
	public void smooth() {

		for (int i = 0; i < objects.size(); i++)
			((Mesh) objects.get(i)).meshSmooth();
	}

	/**
	 * removes double vertex and degenerized triagles.
	 */
	public void optimize() {
		for (int i = 0; i < objects.size(); ++i)
			(objects.get(i)).optimize();
	}

	/** returns the min x,y,z */
	public Vector3f min() {
		float minX, minY, minZ;
		Vector3f v;
		minX = (v = getMesh(0).min()).x;
		minY = v.y;
		minZ = v.z;

		for (int i = 1; i < objects.size(); i++) {
			if (minX > (v = getMesh(i).min()).x)
				minX = v.x;
			if (minY > v.y)
				minY = v.y;
			if (minZ > v.z)
				minZ = v.z;
		}
		return new Vector3f(minX, minY, minZ);
	}

	/** returns the max x,y,z */
	public Vector3f max() {
		float maxX, maxY, maxZ;
		Vector3f v;
		maxX = (v = getMesh(0).max()).x;
		maxY = v.y;
		maxZ = v.z;

		for (int i = 1; i < objects.size(); i++) {
			if (maxX > (v = getMesh(i).max()).x)
				maxX = v.x;
			if (maxY > v.y)
				maxY = v.y;
			if (maxZ > v.z)
				maxZ = v.z;
		}
		return new Vector3f(maxX, maxY, maxZ);
	}

	/**
	 * returns the center of the object.
	 */
	public Vector3f getCenter() {
		Vector3f max = max();
		Vector3f min = min();
		return new Vector3f((max.x + min.x) / 2, (max.y + min.y) / 2,
				(max.z + min.z) / 2);
	}

	/**
	 * sets the x,y,z of all meshes of the object.
	 */
	public void deatach() {

		float sx, sy, sz;
		Vector3f v;
		Mesh mesh;
		sx = (v = (mesh = getMesh(0)).getCenter()).x;
		sy = v.y;
		sz = v.z;
		// put the object in the "center".
		mesh.detach(true, 0, 0, 0);
		mesh.rebuild();
		for (int i = 1; i < objects.size(); i++) {
			// put the object in the relative coordinages.
			(mesh = getMesh(i)).detach(false, sx, sy, sz);
			mesh.rebuild();
		}
	}
}

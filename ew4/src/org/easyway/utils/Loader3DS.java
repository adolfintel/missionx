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
package org.easyway.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.easyway.objects.sprites3D.Sprite3DData;
import org.easyway.objects.sprites3D.dataObj.Material;
import org.easyway.objects.sprites3D.dataObj.Mesh;
import org.easyway.objects.sprites3D.dataObj.Triangle;
import org.easyway.objects.sprites3D.dataObj.Vertex;
import org.easyway.objects.texture.Texture;
import org.easyway.utils.loaderdata.Chunks;
import org.easyway.utils.loaderdata.EffectMaterial;

/**
 * class for loading 3DS files.<br>
 * this is an <b>ALPHA</b> version.
 * 
 * @author Daniele Paggi
 * @version 1
 */
public class Loader3DS {

	/** current readed id */
	private int currentJunkId;

	/** location of next id */
	private int nextJunkOffset;

	/** the incoming scene */
	private Sprite3DData scene;

	/** name current object */
	private String currentObjectName = null;

	/** current object to 'edit' */
	private Mesh currentObject = null;

	/** the stream is ended? */
	private boolean endOfStream = false;

	/** used materials */
	private ArrayList<Material> materialList;

	/** current material */
	private Material currentMaterial = null;

	/** used textures */
	private ArrayList<Texture> textureList;

	/** current texture */
	// private Texture currentTexture = null;
	/** current material */
	private int effectMaterial = EffectMaterial.NULL;

	/** path file */
	private String path;

	// ------------------------ CONSTRUCTORS ----------------------------------

	/**
	 * creates a new instance
	 */
	public Loader3DS() {
	}

	/**
	 * loads from a stream
	 * 
	 * @param path
	 *            3DS model to load
	 * @return the loaded Sprite3DData
	 */
	public Sprite3DData importFromStream(String path) {

		InputStream is;
		{
			String temp = "./../../..";
			if (path.charAt(0) != '/')
				temp = temp + '/';
			is = Utility.class.getResourceAsStream(temp + path);
		}

		this.path = path.substring(0, path.lastIndexOf("/"));

		scene = new Sprite3DData();
		materialList = new ArrayList<Material>(5);
		textureList = new ArrayList<Texture>(5);

		System.out.println(">> Importing scene from 3ds stream ...");
		BufferedInputStream in = new BufferedInputStream(is);

		try {
			readJunkHeader(in);
			if (currentJunkId != Chunks.MAIN) { // 3DS file identifier
				System.out.println("Error: This is not a valid 3ds file.");
				return null;
			}
			while (!endOfStream) {
				readNextJunk(in); // will load into currentObject
			}
		} catch (Throwable ignored) {
			System.out.print("ERRORE" + ignored);
		}

		// currentObject.name = currentObjectName;
		for (int i = 0; i < scene.objects.size(); i++) {
			scene.objects.get(i).rebuild();
		}
		scene.materials = materialList;
		scene.textures = textureList;
		return scene;
	}

	// ----------------------------------- PRIVATE ----------------------------
	private String readString(InputStream in) throws IOException {
		String result = new String();
		byte inByte;
		while ((inByte = (byte) in.read()) != 0)
			result += (char) inByte;
		return result;
	}

	private int readInt(InputStream in) throws IOException {
		return in.read() | (in.read() << 8) | (in.read() << 16)
				| (in.read() << 24);
	}

	private int readShort(InputStream in) throws IOException {
		return (in.read() | (in.read() << 8));
	}

	private float readFloat(InputStream in) throws IOException {
		return Float.intBitsToFloat(readInt(in));
	}

	private void readJunkHeader(InputStream in) throws IOException {
		currentJunkId = readShort(in);
		nextJunkOffset = readInt(in);
		endOfStream = currentJunkId < 0;
	}

	private void readNextJunk(InputStream in) throws IOException {
		readJunkHeader(in);
		switch (currentJunkId) {
		case Chunks.EDITOR:
			break;

		// -------------------- OBJECTS ----------------------
		// finded a new object
		case Chunks.OBJ: {
			currentObjectName = readString(in);
			System.out.println("-----------------------------");
			System.out.println(">> Found new object: " + currentObjectName
					+ getValue());
			break;
		}

			// creates the new mesh
		case Chunks.OBJ_MESH: {
			System.out.println(" |- Making object" + getValue());
			currentObject = new Mesh();
			scene.objects.add(currentObject);
			currentObject.name = currentObjectName;
			break;
		}
			// loads all vertex
		case Chunks.OBJ_MESH_VERTICELIST: {
			System.out.println(" |- loading vertexs" + getValue());
			readVertexList(in);
			break;
		}
			// loads all faces
		case Chunks.OBJ_MESH_FACELIST: {
			System.out.println(" |- loading faces" + getValue());
			readTriangleList(in);
			break;
		}
			// texture coordates
		case Chunks.OBJ_MESH_MAPCOORDLIST: {
			System.out.println(" |- loading u,v coordiates" + getValue());
			readMappingCoordinates(in);
			break;
		}
			// material face
		case Chunks.OBJ_MESH_FACEMAT: {
			System.out.println(" |- materials" + getValue());
			String name = readString(in);
			System.out.println("   " + name);
			Material mat = null;
			for (int i = 0; i < materialList.size(); i++)
				if (((Material) materialList.get(i)).name.compareTo(name) == 0)
					mat = (Material) materialList.get(i);

			int totfaces = readShort(in);
			System.out.println("   n.faccie: " + totfaces);
			Triangle tr;
			int ambcnt = 0;
			for (int i = 0; i < totfaces; ++i) {
				(tr = currentObject.triangleData.get(readShort(in))).material = mat;
				if (tr.p1.material != null && tr.p1.material != mat) {
					++ambcnt;
					currentObject.addVertex(tr.p1 = new Vertex(tr.p1));
				}
				if (tr.p2.material != null && tr.p2.material != mat) {
					++ambcnt;
					currentObject.addVertex(tr.p2 = new Vertex(tr.p2));
					
				}
				if (tr.p3.material != null && tr.p3.material != mat) {
					++ambcnt;
					currentObject.addVertex(tr.p3 = new Vertex(tr.p3));
				}
				tr.p1.material = mat;
				tr.p2.material = mat;
				tr.p3.material = mat;
			}
			if (ambcnt > 0)
				System.out.println("*** cautions: found <" + ambcnt
						+ "> Vertexs whit ambigus material! ");
			// System.out.println(" : " + this.readShort(in));
			break;
		}

		case Chunks.OBJ_MESH_LOCALCOORDS: {
			System.out.println(" |- local coordinates (matrix 4x3)"
					+ getValue());
			if (true) {
				System.out.println("  |- * DISALLOWED --- ");
				skipJunk(in);
				break;
			}

			// currentObject.matrix = new Matrix();//
			// BufferUtils.createFloatBuffer(16);
			// float matrix[][] = new float[4][4];
			//
			// for (int y = 0; y < 4; y++) { // righe
			// for (int x = 0; x < 3; x++) { // colonne
			// float temp = readFloat(in);
			// matrix[y][x] = temp;
			// }
			// matrix[0][3] = 0;
			// matrix[1][3] = 0;
			// matrix[2][3] = 0;
			// matrix[3][3] = 1;
			// }
			// currentObject.matrix.importFromArray(matrix);
			// break;

		}

			// -------------------- MATERIALS ----------------------
		case Chunks.MAT: {
			System.out.println("-----------------------------");
			System.out.println(">> Found new material: " + getValue());
			break;
		}
		case Chunks.MAT_NAME: {
			String temp = readString(in);
			currentMaterial = new Material();
			currentMaterial.name = temp;
			materialList.add(currentMaterial);
			System.out.println(" |- material name: " + temp + getValue());
			break;
		}

			// PARAMETERS
		case Chunks.MAT_AMBIENT: {
			System.out.println(" |- found ambient material " + getValue());
			effectMaterial = EffectMaterial.AMBIENT;
			break;
		}
		case Chunks.MAT_SPECULAR: {
			System.out.println(" |- found specular material " + getValue());
			effectMaterial = EffectMaterial.SPECULAR;
			break;
		}
		case Chunks.MAT_SHININESS: {
			System.out.println(" |- found shininess material " + getValue());
			effectMaterial = EffectMaterial.SHININESS;
			break;
		}
		case Chunks.MAT_TRANSPARENCY: {
			System.out.println(" |- found transparency material "
					+ getValue());
			effectMaterial = EffectMaterial.TRANSPARENCY;
			break;
		}
		case Chunks.MAT_DIFFUSE: {
			System.out.println(" |- found diffuse material " + getValue());
			effectMaterial = EffectMaterial.DIFFUSE;
			break;
		}

			// ----------------------- TEXTURE -----------------------
		case Chunks.MAT_MAP_TEXTURE1: {
			System.out.println(" |- found new texture" + getValue());
			break;
		}
		case Chunks.MAT_MAP_FILENAME: {
			String temp = readString(in);
			System.out.println("  |- file name: " + temp);
			if (!path.endsWith("/"))
				path = path + "/";
			if (!(path.charAt(0) == '/'))
				path = '/' + path;
			currentMaterial.texture = new Texture(path + temp);
			textureList.add(currentMaterial.texture);
			break;
		}
			// ------------------------- DATAS
			// -----------------------------------
		case Chunks.LIN_COLOR_24: { // 12
			System.out.println("  |- **TODO** color RGB_int " + getValue());
			System.out.println("  |   red: " + in.read());
			System.out.println("  |   grn: " + in.read());
			System.out.println("  |   blu: " + in.read());
			break;
		}
		case Chunks.COLOR_24: { // 11
			color(in);
			break;
		}
		case Chunks.SHORT_PERTENTAGE: {
			percent(in);
			break;
		}
		case Chunks.MASTER_SCALE: {
			System.out.println(" |- scaling " + getValue());
			if (currentMaterial != null)
				currentMaterial.scaleFactor = readFloat(in);
			else
				readFloat(in);
			break;
		}
		case Chunks.KEY_FRAME_DATA: { // 0xb000 - Animazione
			System.out.println("\n---\n |- found an animation " + getValue());
			break;
		}
		case Chunks.KEY_FRAME_HDR: {
			System.out.println("  |- HDR (?) " + getValue());
			int i = readShort(in);
			System.out.println("   |- version: " + i);
			String str = readString(in);
			System.out.println("   |- name: " + str);
			i = readInt(in);
			System.out.println("   |- len: " + i);
			// i = readShort(in);
			// System.out.println(" |- len2: "+i);
			break;
		}

		case Chunks.KEY_FRAME_SEG: {
			System.out.println("  |- segment " + getValue());
			int t;
			t = this.readInt(in);
			System.out.println("   |- start: " + t);
			t = readInt(in);
			System.out.println("   |- end: " + t);
			break;
		}
		case Chunks.KEY_FRAME_CURTIME: {
			int t;
			t = this.readInt(in);
			System.out.println("  |- reading current Frame " + t + " "
					+ getValue());
			break;
		}
		case Chunks.OBJECT_NODE_TAG: {
			System.out.println("  |- Found node " + getValue());
			break;
		}
			// case Chunks.M3D_KEY_FRAME_VERSION: {
			// System.out.println(" |- M3D key frame version (?) " +getValue());
			// String str = readString(in);
			// System.out.println(" |- name: "+str);
			// break;
			// }

		default: {
			System.out.println("__current: 0x"
					+ Integer.toHexString(currentJunkId));
			skipJunk(in);
			break;
		}
		}

	}

	private String getValue() {
		return " { " + Integer.toHexString(currentJunkId) + " }";

	}

	private void percent(InputStream in) throws IOException {
		System.out.println("  |- reading percent value " + getValue());
		switch (effectMaterial) {
		case EffectMaterial.NULL:

			System.out.println("   |- ..... val: " + readShort(in));
			break;
		case EffectMaterial.TRANSPARENCY:
			currentMaterial.transparency = 1 - (float) readShort(in) / 255;
			break;
		case EffectMaterial.SHININESS:
			currentMaterial.shinenss = (float) readShort(in) / 255;
			break;
		}
		effectMaterial = EffectMaterial.NULL;
	}

	private void color(InputStream in) throws IOException {
		System.out.println("  |- reading colors " + getValue());
		switch (effectMaterial) {
		case EffectMaterial.DIFFUSE:
			currentMaterial.red = (float) in.read() / 255f;
			currentMaterial.green = (float) in.read() / 255f;
			currentMaterial.blue = (float) in.read() / 255f;
			break;
		case EffectMaterial.AMBIENT:
			readColor(currentMaterial.ambient, in);
			break;
		case EffectMaterial.SPECULAR:
			readColor(currentMaterial.specular, in);
			break;
		}
		effectMaterial = EffectMaterial.NULL;

	}

	private void readColor(FloatBuffer temp, InputStream in) throws IOException {
		temp.put((float) in.read() / 255f);
		temp.put((float) in.read() / 255f);
		temp.put((float) in.read() / 255f);

	}

	private void skipJunk(InputStream in) throws IOException, OutOfMemoryError {
		for (int i = 0; (i < nextJunkOffset - 6) && (!endOfStream); i++) {
			endOfStream = in.read() < 0;
		}
	}

	/**
	 * We must flip the y values with the z values because 3D Max have the Z
	 * axis that pointing up...
	 */
	private void readVertexList(InputStream in) throws IOException {
		float x, y, z, tmpy;
		int vertices = readShort(in); // numero vertici
		System.out.println("  - number: " + vertices);
		for (int i = 0; i < vertices; i++) {
			x = readFloat(in);
			y = readFloat(in);
			z = readFloat(in);
			// switch y z
			tmpy = y;
			y = z;
			z = -tmpy;
			currentObject.addVertex(x, y, z);
		}
	}

	private void readTriangleList(InputStream in) throws IOException {
		int v1, v2, v3;
		int triangles = readShort(in);
		for (int i = 0; i < triangles; i++) {
			v1 = readShort(in);
			v2 = readShort(in);
			v3 = readShort(in);
			readShort(in); // option flag
			// System.out.println(readShort(in));
			currentObject.addTriangle(currentObject.vertex(v1), currentObject
					.vertex(v2), currentObject.vertex(v3));
		}
	}

	private void readMappingCoordinates(InputStream in) throws IOException {
		int vertices = readShort(in);
		for (int i = 0; i < vertices; i++) {
			currentObject.vertex(i).s = readFloat(in);
			currentObject.vertex(i).t = readFloat(in);
			/*******************************************************************
			 * NOT adjust * while (currentObject.vertex(i).u > 1) {
			 * currentObject.vertex(i).u -= 1; } while
			 * (currentObject.vertex(i).u < 0) { currentObject.vertex(i).u += 1; }
			 * currentObject.vertex(i).v = readFloat(in); // adjust while
			 * (currentObject.vertex(i).v > 1) { currentObject.vertex(i).v -= 1; }
			 * while (currentObject.vertex(i).v < 0) { currentObject.vertex(i).v +=
			 * 1; }
			 */

		}
	}

}
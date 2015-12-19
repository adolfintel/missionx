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
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.easyway.interfaces.extended.IDrawing;
import org.easyway.interfaces.extended.ILayerID;
import org.easyway.objects.BaseObject;
import org.easyway.objects.sprites3D.dataObj.Material;
import org.easyway.objects.sprites3D.dataObj.Mesh;
import org.easyway.objects.sprites3D.dataObj.Triangle;
import org.easyway.objects.texture.Texture;
import org.easyway.system.StaticRef;
import org.easyway.system.state.GameState;
import org.easyway.system.state.OpenGLState;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Sprite3D extends BaseObject implements IDrawing, ILayerID,
		Serializable {
	static final long serialVersionUID = 11;

	// -------------------------------------------------------------------------
	/** position in the space */
	public float x, y, z;

	/**
	 * the object's data.
	 */
	public Sprite3DData data;

	// public float scale = 0.005f;
	/**
	 * the layer index.
	 */
	private int layer;

	/**
	 * the translation\rotation matrix
	 */
	protected Matrix4f matrix;

	/** never used?? */
	protected Vector3f direction;

	// -------------------------------------------------------------------------
	/** note: You must "create()" this object */
	public Sprite3D(Sprite3DData data) {
		super();
		setIdLayer(3);
		matrix = new Matrix4f();
		// d_irection = new Vector3f(0, 0, 1);

		// --- ????? E'????
		for (int i = 0; i < data.materials.size(); i++) {
			data.getMaterial(i).diffuse.put(data.getMaterial(i).red);
			data.getMaterial(i).diffuse.put(data.getMaterial(i).green);
			data.getMaterial(i).diffuse.put(data.getMaterial(i).blue);
			data.getMaterial(i).diffuse.put(data.getMaterial(i).transparency);
		}
		// ----

		this.data = data;

	}

	public Sprite3D(Sprite3D sprite) {
		data = sprite.data;
		// mylist = sprite.getList();
		setIdLayer(3);
		matrix = new Matrix4f();
		this.vertexBuffer = sprite.vertexBuffer;
		this.colorBuffer = sprite.colorBuffer;
		this.normalBuffer = sprite.normalBuffer;
		this.triangleBuffer = sprite.triangleBuffer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
		readdToDrawingLists();
	}

	public int getLayer() {
		return layer;
	}

	// -------------------------------------------------------------------------
	@Deprecated
	private void draw() {

		Triangle tr;
		Material lastmaterial = null;
		Texture ctex = null;
		float cx, cy, cz;
		Mesh obj;
		// tutti oggetti
		for (int k = 0; k < data.objects.size(); k++) {

			if ((obj = data.getMesh(k)).deatached) {
				cx = (float) obj.x;
				cy = (float) obj.y;
				cz = (float) obj.z;
			} else
				cx = cy = cz = 0;
			// triangoli in oggetti
			for (int i = 0; i < obj.triangleData.size(); i++) {
				tr = obj.triangle(i);
				if (tr.material != null && lastmaterial != tr.material) {
					// if (true) {
					ctex = tr.material.texture;
					if (ctex != null)
						ctex.bind();
					// -- MATERIALI
					GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR,
							tr.material.specular);
					GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT,
							tr.material.specular);
					GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS,
							tr.material.shinenss);
					GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE,
							tr.material.diffuse);
					// -- COLORE
					GL11.glColor4f(tr.material.red, tr.material.green,
							tr.material.blue, tr.material.transparency);
					// GL11.glColor4f(1,0,0,1);
				}
				GL11.glBegin(GL11.GL_TRIANGLES);
				// -- DATI
				// faccia
				// GL11.glNormal3f(tr.n.x, tr.n.y, tr.n.z);
				// vertici
				GL11.glNormal3f(tr.p1.normal.x, tr.p1.normal.y, tr.p1.normal.z);
				if (ctex != null)
					GL11.glTexCoord2f(tr.uv[0][0], tr.uv[0][1]);

				GL11.glVertex3f(cx + tr.p1.pos.x, cy + tr.p1.pos.y, cz
						+ tr.p1.pos.z);
				GL11.glNormal3f(tr.p2.normal.x, tr.p2.normal.y, tr.p2.normal.z);

				if (ctex != null)
					GL11.glTexCoord2f(tr.uv[1][0], tr.uv[1][1]);
				GL11.glVertex3f(cx + tr.p2.pos.x, cy + tr.p2.pos.y, cz
						+ tr.p2.pos.z);
				GL11.glNormal3f(tr.p3.normal.x, tr.p3.normal.y, tr.p3.normal.z);

				if (ctex != null)
					GL11.glTexCoord2f(tr.uv[2][0], tr.uv[2][1]);
				GL11.glVertex3f(cx + tr.p3.pos.x, cy + tr.p3.pos.y, cz
						+ tr.p3.pos.z);
				GL11.glEnd();
			}
		}

	}

	FloatBuffer vertexBuffer, colorBuffer, normalBuffer, textureBuffer;

	IntBuffer triangleBuffer;

	private Texture texture;

	public void createArray() {
		int step = 0;
		int ntriangoli = 0;
		int size = 0;
		for (int j = 0; j < data.objects.size(); ++j) {
			size += data.objects.get(j).vertexData.size();
			ntriangoli += data.objects.get(j).triangleData.size();
		}
		System.out.println("size " + size);
		System.out.println("ntri " + ntriangoli);
		vertexBuffer = BufferUtils.createFloatBuffer(size * 3);
		normalBuffer = BufferUtils.createFloatBuffer(size * 3);
		colorBuffer = BufferUtils.createFloatBuffer(size * 3);
		triangleBuffer = BufferUtils.createIntBuffer(ntriangoli * 3);
                // -----------------------------------
                // just removed
                // -----------------------------------

		/*if ((texture = data.objects.get(0).vertex(0).material.texture) != null)
			textureBuffer = BufferUtils.createFloatBuffer(size * 2);*/
		Material materialTemp;
		for (int j = 0; j < data.objects.size(); ++j) {
			size = data.objects.get(j).vertexData.size();
			System.out.println(size);
			for (int i = 0; i < size; ++i) {
				vertexBuffer.put(data.objects.get(j).vertex(i).pos.x);
				vertexBuffer.put(data.objects.get(j).vertex(i).pos.y);
				vertexBuffer.put(data.objects.get(j).vertex(i).pos.z);

				if (textureBuffer != null)
					textureBuffer.put(data.objects.get(j).vertex(i).s).put(
							data.objects.get(j).vertex(i).t);

				// data.objects.get(0).vertex[i].neighbor.get(0).material.diffuse.flip();
				// try{
				// cb
				// .put(data.objects.get(j).vertex[i].neighbor.get(0).material.red);
				// cb
				// .put(data.objects.get(j).vertex[i].neighbor.get(0).material.green);
				// cb
				// .put(data.objects.get(j).vertex[i].neighbor.get(0).material.blue);
				// }catch(Exception e) {
				// e.printStackTrace();
				// System.out.println("index: i = "+i);
				if ((materialTemp = data.objects.get(j).vertex(i).material) == null) {
					colorBuffer.put((float) Math.random());
					colorBuffer.put((float) Math.random());
					colorBuffer.put((float) Math.random());
				} else {
					colorBuffer.put(materialTemp.red);
					colorBuffer.put(materialTemp.green);
					colorBuffer.put(materialTemp.blue);
				}
				// }
				normalBuffer.put(data.objects.get(j).vertex(i).normal.x);
				normalBuffer.put(data.objects.get(j).vertex(i).normal.y);
				normalBuffer.put(data.objects.get(j).vertex(i).normal.z);
				// cb.put(data.objects.get(0).triangle[i].material.green);
				// cb.put(data.objects.get(0).triangle[i].material.blue);
			}
			for (int i = 0; i < data.objects.get(j).triangleData.size(); ++i) {
				triangleBuffer
						.put(data.objects.get(j).triangle(i).p1.id + step);
				triangleBuffer
						.put(data.objects.get(j).triangle(i).p2.id + step);
				triangleBuffer
						.put(data.objects.get(j).triangle(i).p3.id + step);
			}

			step += size;
		}
		// mfb.put(s_cubeGeom);
		vertexBuffer.flip();
		normalBuffer.flip();
		// ib.put(s_cubeIndices);
		triangleBuffer.flip();
		colorBuffer.flip();
		if (textureBuffer != null)
			textureBuffer.flip();
	}

	/**
	 * temporary buffer
	 */
	private static FloatBuffer tempBuffer = BufferUtils.createFloatBuffer(16);

	private static Vector3f relativePosition = new Vector3f();

	private static Matrix4f tempMatrix = new Matrix4f();

	public void render() {
		/** MATRIX */
		// GL11.glLoadIdentity();
		// --- creating at any loop a new buffer isn't a good solution ;)
		// FloatBuffer fb = BufferUtils.createFloatBuffer(16);
		tempBuffer.rewind();
		// --- creating a new vector isn't a good solution too ;)
		// Vector3f v = new Vector3f();
		relativePosition.set(x - StaticRef.getCamera().x, y
				- StaticRef.getCamera().y, z - StaticRef.getCamera().z);
		// --- creating a new Matrix isn't a good solution too too ;) xD
		// Matrix4f tempMatrix = new Matrix4f();
		// tempMatrix = Matrix4f.setIdentity(tempMatrix);
		tempMatrix.setIdentity();
		tempMatrix.translate(relativePosition);
		Matrix4f.mul(StaticRef.getCamera().matrix, tempMatrix, tempMatrix);
		Matrix4f.mul(tempMatrix, matrix, tempMatrix);
		tempMatrix.store(tempBuffer);
		tempBuffer.flip();
		GL11.glLoadMatrix(tempBuffer);

		// debugging mode
		// GL11.glBegin(GL11.GL_QUADS);
		// GL11.glVertex3f(-1, -1, 0);
		// GL11.glVertex3f(1, -1, 0);
		// GL11.glVertex3f(1, 1, 0);
		// GL11.glVertex3f(-1, 1, 0);
		// GL11.glEnd();

		/** DRAWING */
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glColor3f(1, 1, 1);
		// --- TODO --- remove the following line
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGLState.enableVertexArray();
		OpenGLState.enableColorArray();
		OpenGLState.enableNormalArray();
		if (textureBuffer != null) {
			OpenGLState.enableTextureArray();
			texture.bind();
			GL11.glTexCoordPointer(2, 0, textureBuffer);
		}
		/** TEMP */
		GL11.glColorPointer(3, 0, colorBuffer);
		GL11.glVertexPointer(3, 0, vertexBuffer);
		GL11.glNormalPointer(0, normalBuffer);

		// data's render
		GL11.glDrawElements(GL11.GL_TRIANGLES, triangleBuffer);
		// --- TODO --- remove the following line
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		OpenGLState.disableVertexArray();
		OpenGLState.disableColorArray();
		OpenGLState.disableNormalArray();
		OpenGLState.disableTextureArray();

	} // -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	public void rotate(float value, Vector3f vector) {
		matrix.rotate(value, vector);
	}

	public void rotateXabsolute(float value) {
		// TODO optimize
		Matrix4f temp = new Matrix4f();
		temp.rotate(value, new Vector3f(1, 0, 0));
		Matrix4f.mul(temp, matrix, matrix);
	}

	public void rotateYabsolute(float value) {
		// TODO optimize
		Matrix4f temp = new Matrix4f();
		temp.rotate(value, new Vector3f(0, 1, 0));
		Matrix4f.mul(temp, matrix, matrix);
	}

	public void rotateZabsolute(float value) {
		// TODO optimize
		Matrix4f temp = new Matrix4f();
		temp.rotate(value, new Vector3f(0, 0, 1));
		Matrix4f.mul(temp, matrix, matrix);
	}

	public void rotateX(float value) {
		rotate(value, new Vector3f(1, 0, 0));
	}

	public void rotateY(float value) {
		rotate(value, new Vector3f(0, 1, 0));
	}

	public void rotateZ(float value) {
		rotate(value, new Vector3f(0, 0, 1));
	}

	public void rotateReset() {
		matrix.setIdentity();
	}

	// ------------------- MOVE ------------------------

	public void moveX(float step) {
		x += step * matrix.m00;
		y += step * matrix.m01;
		z += step * matrix.m02;
	}

	public void moveY(float step) {
		x += step * matrix.m10;
		y += step * matrix.m11;
		z += step * matrix.m12;
	}

	public void moveZ(float step) {
		x += step * matrix.m20;
		y += step * matrix.m21;
		z += step * matrix.m22;
	}

	// -----------------------------------------------------------------
	// ---------------------------- IDLAYER-----------------------------
	// -----------------------------------------------------------------

	int idLayer = -1;

	public int getIdLayer() {
		return idLayer;
	}

	public void setIdLayer(int id) {
		if (idLayer != -1) {
			GameState.getCurrentGEState().getLayers()[idLayer].remove(this);
		}
		if (id < 0) {
			id = 0;
		} else if (id > GameState.getCurrentGEState().getLayers().length) {
			id = GameState.getCurrentGEState().getLayers().length;
		}
		idLayer = id;
		GameState.getCurrentGEState().getLayers()[idLayer].add(this);
	}
}

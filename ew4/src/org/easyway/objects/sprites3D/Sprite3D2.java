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

import org.easyway.interfaces.base.IDestroyable;
import org.easyway.interfaces.extended.IDrawing;
import org.easyway.interfaces.extended.ILayerID;
import org.easyway.objects.BaseObject;
import org.easyway.system.StaticRef;
import org.easyway.system.state.GameState;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Sprite3D2 extends BaseObject implements IDestroyable,
		ILayerID, IDrawing, Serializable {

	private static final long serialVersionUID = -1550268023492780852L;

	// -------------------------------------------------------------------------
	/** posizione nello spazio */
	public float x, y, z;

	private boolean destroyed = false;

	// private int mylist = -1;

	// private static int lastmylist;

	private int layer;

	public Matrix4f matrix;

	/** mai usata */
	public Vector3f direction;

	public Mesh mesh;

	// -------------------------------------------------------------------------
	/** note: You must "create()" this object */
	public Sprite3D2() {
		super();
		setIdLayer(3);
		matrix = new Matrix4f();
	}

	public void setLayer(int layer) {
		this.layer = layer;
		readdToDrawingLists();
	}

	public int getLayer() {
		return layer;
	}

	// -------------------------------------------------------------------------

	public void render() {
		/** CALCOLO MATRICE */
		{
			FloatBuffer fb = BufferUtils.createFloatBuffer(16);
			fb.rewind();
			Vector3f v = new Vector3f(x - StaticRef.getCamera().x, y
					- StaticRef.getCamera().y, z - StaticRef.getCamera().z);
			Matrix4f temp = new Matrix4f();
			temp.translate(v);
			Matrix4f.mul(StaticRef.getCamera().matrix, temp, temp);
			Matrix4f.mul(temp, matrix, temp);
			temp.store(fb);
			fb.flip();
			GL11.glLoadMatrix(fb);
		}
		/** DISEGNO */
		{
			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
//			GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
			if (mesh!=null)
			mesh.render();
			GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
//			GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
		}
	} // -------------------------------------------------------------------------

	/** destroy the Static Image */
	public void destroy() {
		super.destroy();
		if (destroyed)
			return;
		onDestroy();
		destroyed = true;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void onDestroy() {
	}

	// -------------------------------------------------------------------------
	public void rotate(float value, Vector3f vector) {
		matrix.rotate(value, vector);
	}

	public void rotateX(float value) {
		rotate(value, new Vector3f(1, 0, 0));
	}

	public void rotateXabsolute(float value) {
		Matrix4f temp = new Matrix4f();
		temp.rotate(value, new Vector3f(1, 0, 0));
		Matrix4f.mul(temp, matrix, matrix);
	}

	public void rotateYabsolute(float value) {
		Matrix4f temp = new Matrix4f();
		temp.rotate(value, new Vector3f(0, 1, 0));
		Matrix4f.mul(temp, matrix, matrix);
	}

	public void rotateZabsolute(float value) {
		Matrix4f temp = new Matrix4f();
		temp.rotate(value, new Vector3f(0, 0, 1));
		Matrix4f.mul(temp, matrix, matrix);
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

	int idLayer;
	
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

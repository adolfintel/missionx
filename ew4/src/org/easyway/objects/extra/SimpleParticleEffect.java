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
package org.easyway.objects.extra;

import java.io.Serializable;
import java.nio.FloatBuffer;

import org.easyway.interfaces.base.ITexture;
import org.easyway.interfaces.extended.IDrawing;
import org.easyway.interfaces.extended.ILayerID;
import org.easyway.objects.BaseObject;
import org.easyway.system.StaticRef;
import org.easyway.system.state.GameState;
import org.easyway.system.state.OpenGLState;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

/**
 * @author Daniele Paggi
 * @version 3
 */
@Deprecated
public class SimpleParticleEffect extends BaseObject implements IDrawing, ILayerID,
		Serializable {

	static final long serialVersionUID = 26;

	private Sfere sfere[];

	public double x, y;

	private ITexture texture;

	private int layer;

	public float width = 32;

	public float height = 32;

	private int last;

	FloatBuffer data, colors, textures;

	/**
	 * the drawing sheet
	 */
	private int idLayer = -1;

	int i;

	// TODO:
	/**
	 * REMEMBER to CHANGE width and Height;<br>
	 * the default value is 32x32<br>
	 * 
	 * @param x
	 *            coordinate
	 * @param y
	 *            coordinate
	 * @param layer
	 *            drawing sheet
	 * @param n
	 *            number of "particle"
	 * @param text
	 *            image
	 */
	public SimpleParticleEffect(double x, double y, int layer, int n, ITexture text) {
		this.x = x;
		this.y = y;
		type = "$_PARTICLE_ENGINE";
		sfere = new Sfere[n];
		// 2 coords * 4 vertexs * n quads = 8 * n
		data = BufferUtils.createFloatBuffer(8 * n);
		// 4 colors * 4 vertexs * n quads = 16 * n
		colors = BufferUtils.createFloatBuffer(16 * n);
		// 2 coords * 4 vertexs * n quads = 8 * n ..
		// all have the same textures coords then: 8
		// 'cause I'm not able to use only 8 instead of 8*n I'm using 8*n :(
		textures = BufferUtils.createFloatBuffer(8 * n);
		texture = text;
		makeTexturesCoordiates();
		GameState.getCurrentGEState().getLayers()[this.layer = layer].add(this); // 0.3
		for (i = 0; i < sfere.length; i++) {
			sfere[i] = new Sfere();
		}
		last = n;
	}

	/**
	 * make a particle engine from another<br>
	 * the new particle engine will have the same texture of the old one.<br>
	 * this constructor is a bit faster that the standard constructor :)
	 * 
	 * @param x
	 * @param y
	 * @param n
	 * @param particle
	 */
	public SimpleParticleEffect(float x, float y, int n, SimpleParticleEffect particle) {
		this.x = x;
		this.y = y;
		type = "$_PARTICLE_ENGINE";
		sfere = new Sfere[n];
		texture = particle.texture;
		textures = particle.textures;
		// 2 coords * 4 vertexs * n quads = 8 * n
		data = BufferUtils.createFloatBuffer(8 * n);
		// 4 colors * 4 vertexs * n quads = 16 * n
		colors = BufferUtils.createFloatBuffer(16 * n);
		GameState.getCurrentGEState().getLayers()[particle.layer].add(this); // 0.3
		for (i = 0; i < sfere.length; i++) {
			sfere[i] = new Sfere();
		}
		last = n;
	}

	protected void makeTexturesCoordiates() {
		FloatBuffer temp1 = BufferUtils.createFloatBuffer(8);
		FloatBuffer temp2 = BufferUtils.createFloatBuffer(16);
		temp1.put(texture.getXStart()).put(texture.getYStart()).put(
				texture.getXEnd()).put(texture.getYStart()).put(
				texture.getXEnd()).put(texture.getYEnd()).put(
				texture.getXStart()).put(texture.getYEnd());
		temp1.rewind();
		temp2.put(temp1);
		temp1.rewind();
		temp2.put(temp1);
		for (int i = (int)sfere.length/2-1; i>=0; --i) {
			temp2.rewind();
			textures.put(temp2);
		}
		if (sfere.length%2==1) {
			temp1.rewind();
			textures.put(temp1);
		}
	}

	public void setLayer(int l) {
		layer = l;
		readdToDrawingLists();
	}

	public int getLayer() {
		return layer;
	}

	int size;

	public void render() {
		texture.bind();
		// GL13.glClientActiveTexture(texture.texture);
		OpenGLState.enableAlphaTest();
		OpenGLState.enableBlending();
		OpenGLState.enableVertexArray();
		OpenGLState.enableColorArray();
		OpenGLState.enableTextureArray();
		// GL11.glDisable(GL11.GL_TEXTURE_2D);
		// OpenGLState.di();
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
		size = 0;
		// data.rewind();
		data.clear();
		data.rewind();
		colors.clear();
		colors.rewind();
		// textures.clear();
		// textures.rewind();
		for (i = 0; i < last; i++) {
			if (!sfere[i].toRender)
				continue;
			sfere[i].render();
		}
		data.flip();
		colors.flip();
		// textures.flip();
		textures.position(data.position()).flip();
		GL11.glVertexPointer(2, 0, data);
		GL11.glColorPointer(4, 0, colors);
		GL11.glTexCoordPointer(2, 0, textures);

		GL11.glDrawArrays(GL11.GL_QUADS, 0, size / 8);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		if (last <= (sfere.length / 100) * 3) // < 3%?
			destroy();
	}

	public void destroy() {
		if (isDestroyed())
			return;

		sfere = null;
		super.destroy();
	}

	// ------------------------------------------------------------------
	// ------------------------------------------------------------------

	class Sfere implements Serializable {
		static final long serialVersionUID = 27;

		/** red, green, blue */
		float r, g, b;

		float xx, yy;

		/** time to death */
		float life = 1.0f;

		/** speed of deathing */
		float fade;

		/** speed's moviment */
		float xspeed, yspeed;

		// innutile
		boolean toRender;

		public Sfere() {
			// colori
			r = (float) Math.random();
			g = (float) Math.random();
			b = (float) Math.random();
			// fade out
			fade = (float) Math.random() / 25 + 0.01f; // minimo 100 cicli
			toRender = true;
			xx = (float) Math.random() * 32;
			yy = (float) Math.random() * 32;
			xspeed = (float) (Math.random() - 0.5f);
			yspeed = (float) Math.random();
		}

		public void render() {
			if ((life -= fade) <= 0) {
				toRender = false;
				if (i != last - 1) { // se non � l'ultima
					sfere[i] = sfere[last - 1];
				}
				last--;
				return;
			}
			xx += xspeed;// (float)(Math.sin(xspeed)*5);
			yy -= yspeed;

			int nx = (int) (x + xx - StaticRef.getCamera().x);
			int ny = (int) (y + yy - StaticRef.getCamera().y);
			size += 8;

			colors.put(r).put(g).put(b).put(life); // v1
			colors.put(r).put(g).put(b).put(life); // v2
			colors.put(r).put(g).put(b).put(life); // v3
			colors.put(r).put(g).put(b).put(life); // v4
			data.put(nx);
			data.put(ny);
			data.put(nx + width);
			data.put(ny);
			data.put(nx + width);
			data.put(ny + height);
			data.put(nx);
			data.put(ny + height);

		}
	}

	// -----------------------------------------------------------------
	// ---------------------------- IDLAYER-----------------------------
	// -----------------------------------------------------------------

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
			id =GameState.getCurrentGEState().getLayers().length;
		}
		idLayer = id;
		GameState.getCurrentGEState().getLayers()[idLayer].add(this);
	}

}

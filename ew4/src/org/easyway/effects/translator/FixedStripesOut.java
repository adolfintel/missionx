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
package org.easyway.effects.translator;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.easyway.interfaces.extended.IDrawing;
import org.easyway.interfaces.extended.ILayerID;
import org.easyway.objects.BaseObject;
import org.easyway.objects.texture.Texture;
import org.easyway.objects.texture.TextureFBO;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;
import org.easyway.system.state.GameState;
import org.easyway.system.state.OpenGLState;
import org.easyway.utils.ImageUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class FixedStripesOut extends BaseObject implements IDrawing, ILayerID {

	private static final long serialVersionUID = 1578623115107939214L;

	protected int layer;
	
	/**
	 * the drawing sheet
	 */
	private int idLayer = -1;

	protected Texture screen;

	protected TextureFBO dinamicTexture;

	protected Texture linkedTexture;

	protected IntBuffer effect;

	protected int size;

	protected int stepx;

	protected long time;

	public FixedStripesOut(long time, int size) {
		this(time, size, autoAddToLists, 3);
	}

	public FixedStripesOut(long time, int size, boolean autoAddToLists) {
		this(time, size, autoAddToLists, 3);
	}

	public FixedStripesOut(long time, int size, int idLayer) {
		this(time, size, autoAddToLists, idLayer);
	}

	public FixedStripesOut(long time, int size, boolean autoAddToLists,
			int idLayer) {
		super(autoAddToLists);
		GameState.getCurrentGEState().getLayers()[idLayer].add(this);
		screen = ImageUtils.getScreenShot();
		this.time = time * 1000000;
		this.size = size;
		if (size % 2 == 0) {
			stepx = size / 2;
		} else {
			stepx = ((int) (size / 2)) + 1;
		}
		effect = makeEffect();// ImageUtils.makeTexture(size, size);
		linkedTexture = new Texture(screen.getWidth(), screen.getHeight());
		dinamicTexture = new TextureFBO(linkedTexture);
	}

	// ---------------------------
	protected IntBuffer makeEffect() {
		IntBuffer buf = BufferUtils.createIntBuffer(4);
		GL11.glGenTextures(buf);
		ByteBuffer data = BufferUtils.createByteBuffer(size * stepx * 4);
		data.rewind();
		int count = 1, dir = 1;
		for (int y = size - 1, x; y >= 0; --y) {
			for (x = 0; x < stepx; ++x) {
				data.put((byte) 255);
				data.put((byte) 255);
				data.put((byte) 255);
				if (x <= count)
					data.put((byte) 255);
				else
					data.put((byte) 0);
			}
			if (count == stepx - 1)
				dir = -1;
			count += dir;
		}
		data.rewind();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, buf.get(0));
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
				GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
				GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
				GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
				GL11.GL_REPEAT);

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, stepx, size, 0,
				GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
		// Texture.lastTexture = -1;
		// effect.u = effect.v = 1;
		// effect.alphaMask = null;
		// effect.heightHW = effect.height = size;
		// effect.width = effect.widthHW = stepx;
		// effect.isRegular = true;
		// effect.isSolid = false;
		// effect.name = "EFFECT";
		// effect.texture = buf.get(0);
		// effect.textureBuffer = buf;
		return buf;
	}

	// ------------ METHODS ------

	public int getLayer() {
		return layer;
	}

	public void setLayere(int layer) {
		this.layer = layer;
	}

	float dec = 0;

    @Override
	public void render() {
		float step = (float) ((float) Core.getInstance().getElaspedTime() / (float) time);
		//System.out.println(step);
		dec += step * (float) stepx;

		if (dec >= stepx) {
			destroy();
			return;
		}

		OpenGLState.disableBlending();
		OpenGLState.enableAlphaTest();
		// draw

		dinamicTexture.startDrawing();
		{
			// GL11.glAlphaFunc(GL11.GL_NEVER, 1.0f);
			GL11.glLoadIdentity();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, effect.get(0));
			// Texture.lastTexture = -1;
			GL11.glBegin(GL11.GL_QUADS);

			GL11.glTexCoord2f(dec / (float) stepx,
					(float) StaticRef.getCamera().getHeight() / (float) size);
			GL11.glVertex2f(0, 0);
			GL11.glTexCoord2f(dec / (float) stepx,
					(float) StaticRef.getCamera().getHeight() / (float) size);
			GL11.glVertex2f(StaticRef.getCamera().getWidth(), 0);
			GL11.glTexCoord2f(dec / (float) stepx, 0);
			GL11.glVertex2f(StaticRef.getCamera().getWidth(), StaticRef.getCamera()
					.getHeight());
			GL11.glTexCoord2f(dec / (float) stepx, 0);
			GL11.glVertex2f(0, StaticRef.getCamera().getHeight());

			GL11.glEnd();

			GL11.glColorMask(true, true, true, false);
			screen.bind();
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, screen.yEnd);
			GL11.glVertex2f(0, 0);
			GL11.glTexCoord2f(screen.xEnd, screen.yEnd);
			GL11.glVertex2f(StaticRef.getCamera().getWidth(), 0);
			GL11.glTexCoord2f(screen.xEnd, 0);
			GL11.glVertex2f(StaticRef.getCamera().getWidth(), StaticRef.getCamera()
					.getHeight());
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(0, StaticRef.getCamera().getHeight());
			GL11.glEnd();
			GL11.glColorMask(true, true, true, true);
		}
		dinamicTexture.endDrawing();

		linkedTexture.bind();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, linkedTexture.yEnd);
		GL11.glVertex2f(0, 0);
		GL11.glTexCoord2f(linkedTexture.xEnd, linkedTexture.yEnd);
		GL11.glVertex2f(StaticRef.getCamera().getWidth(), 0);
		GL11.glTexCoord2f(linkedTexture.xEnd, 0);
		GL11.glVertex2f(StaticRef.getCamera().getWidth(), StaticRef.getCamera()
				.getHeight());
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(0, StaticRef.getCamera().getHeight());
		GL11.glEnd();

		// screen.bind();
		// GL11.glEnable(GL11.GL_ALPHA_TEST);
		// GL11.glAlphaFunc(GL11.GL_LESS, 1f);

		// GL11.glAlphaFunc(GL11.GL_GREATER, 0f);
		// GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	public void onDestroy() {
		GL11.glDeleteTextures(effect);
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
			id = GameState.getCurrentGEState().getLayers().length;
		}
		idLayer = id;
		GameState.getCurrentGEState().getLayers()[idLayer].add(this);
	}

}

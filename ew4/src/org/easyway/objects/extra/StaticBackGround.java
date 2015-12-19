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

import org.easyway.interfaces.base.ITexture;
import org.easyway.interfaces.extended.IDrawing;
import org.easyway.interfaces.extended.ILayerID;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.BaseObject;
import org.easyway.objects.texture.Texture;
import org.easyway.system.Core;
import org.easyway.system.state.GameState;
import org.lwjgl.opengl.GL11;

public class StaticBackGround extends BaseObject implements IDrawing, ILayerID {

	/**
	 * generated version
	 */
	private static final long serialVersionUID = -79533371340177517L;

	/**
	 * the drawing sheet
	 */
	private int idLayer = -1;

	ITexture tex;

	public StaticBackGround(ITexture image) {
		super(false);
		GameState.getCurrentGEState().getLayers()[0].add(this);
		if (isLoopable) {
			GameState.getCurrentGEState().getLoopList().add((ILoopable) this);
		}
		type = "$_BACKGROUND";
		tex = image;
	}

	public StaticBackGround(String path) {
		super(false);
		GameState.getCurrentGEState().getLayers()[0].add(this);
		if (isLoopable) {
			GameState.getCurrentGEState().getLoopList().add((ILoopable) this);
		}
		tex = new Texture(path);
		type = "$_BACKGROUND";
		// tex = new Texture(path);
	}

	public void render() {
		GL11.glColor3f(1, 1, 1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		tex.bind();
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2f(tex.getXStart(), tex.getYStart());
			GL11.glVertex2f(0, 0);
			GL11.glTexCoord2f(tex.getXEnd(), tex.getYStart());
			GL11.glVertex2f(Core.getInstance().getWidth(), 0);
			GL11.glTexCoord2f(tex.getXEnd(), tex.getYEnd());
			GL11.glVertex2f(Core.getInstance().getWidth(),Core.getInstance()
					.getHeight());
			GL11.glTexCoord2f(tex.getXStart(), tex.getYEnd());
			GL11.glVertex2f(0, Core.getInstance().getHeight());
		}
		GL11.glEnd();

	}

	public int getLayer() {
		return -9999999;
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

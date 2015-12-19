package org.easyway.objects;

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

import org.easyway.interfaces.base.IPureRender;
import org.easyway.objects.texture.Texture;
import org.easyway.objects.texture.TextureFBO;
import org.easyway.system.StaticRef;
import org.easyway.system.state.GameState;
import org.lwjgl.opengl.GL11;

/**
 * @author Daniele Paggi
 * 
 */
public class SplittedCamera extends Camera implements IPureRender {

	/**
	 * self generated
	 */
	private static final long serialVersionUID = 5144515505423705324L;

	/**
	 * the default game engine's camera
	 */
	static Camera gameEngineCamera;

	/**
	 * the texture of the Splitted Camera
	 */
	Texture texture;

	/**
	 * the FB Object for texture-rendering
	 */
	TextureFBO textureFbo;

	/**
	 * the drawing area relative to the game engine's camera
	 */
	Plain2D drawingArea;

	/**
	 * creates a new instance of the SplittedCamera
	 * 
	 * @param width
	 *            the width of the Game's camera
	 * @param height
	 *            the height of the Game's camera
	 * @see #setDrawingArea(int, int, int, int)
	 * @see #setDrawingArea(int, int)
	 * @see #setDrawingArea(Plain2D)
	 */
	public SplittedCamera(int width, int height) {
		super(0, 0, width, height);
		// set2D();
		is2D = true;
 		zoomFactor = 1.0f;
		if (gameEngineCamera == null) {
			gameEngineCamera = StaticRef.getCamera();
		}
		drawingArea = new Plain2D(false, 0, 0, width, height);
		texture = new Texture(width, height,null);
		textureFbo = new TextureFBO(texture);
	}

	/**
	 * you can ovveride this method for a custom drawing<br>
	 * example:<br>
	 * public void customRender() {<br> // draw only on this camera this object<br>
	 * myGuiSprite.render();<br> }<br>
	 * 
	 */
	public void customRender() {

	}

	/**
	 * calling this method will be drawen at screen the Camera<br>
	 * example:<br>
	 * public class Main exnteds Core { <br>
	 *   ...<br>
	 *   boolean isSplitted;<br>
	 *   ...<br>
	 *   SplittedCamera camera1, camera2<br>
	 *   ...<br>
	 *   public void coreRender() {<br>
	 *     if (isSplitted) { <br>
	 *       camera1.render();<br>
	 *       camera2.render();<br>
	 *     } else {<br>
	 *       super.coreRender();<br>
	 *   }<br>
	 *   ...<br>
	 * }<br>
	 * 
	 * 
	 * @see #customRender()
	 */
	public void render() {
//		if (StaticRef.getCamera().is3D()) {
//			set2D(); // force to 2D viewport
//		}
		// select this camera
		StaticRef.setCamera(this);
		center();

		// start drawing
		textureFbo.startDrawing();
		// clean the screen with the back-color
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		// default rendering
		for (int i = 0; i < GameState.getCurrentGEState().getLayers().length; ++i) {
			GameState.getCurrentGEState().getLayers()[i].render();
		}
		customRender(); // custom rendering
		textureFbo.endDrawing();
		// end drawing
		StaticRef.setCamera(gameEngineCamera); // reset to the default game
												// engine's camera
		texture.bind(); // select the texture to render
		GL11.glColor3f(1, 1, 1); // white
		GL11.glLoadIdentity(); // reset matrix to identity
		GL11.glBegin(GL11.GL_QUADS); // draw a quad
		{
			GL11.glTexCoord2f(texture.getXStart(), texture.getYEnd());
			GL11.glVertex2f(drawingArea.getX(), drawingArea.getY());
			GL11.glTexCoord2f(texture.getXEnd(), texture.getYEnd());
			GL11.glVertex2f(drawingArea.getX() + drawingArea.getWidth(),
				drawingArea.getY());
			GL11.glTexCoord2f(texture.getXEnd(), texture.getYStart());
			GL11.glVertex2f(drawingArea.getX() + drawingArea.getWidth(),
				drawingArea.getY() + drawingArea.getHeight());
			GL11.glTexCoord2f(texture.getXStart(), texture.getYStart());
			GL11.glVertex2f(drawingArea.getX(), drawingArea.getY()
					+ drawingArea.getHeight());
		}
		GL11.glEnd();

	}

	/**
	 * sets the dimension of the ViewPort on the default Game Endine's camera
	 * 
	 */
	public void setDrawingArea(int width, int height) {
		drawingArea.setSize(width, height);
	}

	/**
	 * sets the dimension and the position of the ViewPort on the default Game
	 * Endine's camera
	 * 
	 * @param x
	 *            the x position
	 * @param y
	 *            the y position
	 * @param width
	 *            the width of the ViewPort
	 * @param height
	 *            the height of the ViewPort
	 */
	public void setDrawingArea(int x, int y, int width, int height) {
		drawingArea.setXY(x, y);
		drawingArea.setSize(width, height);
	}

	public Plain2D getDrawingArea() {
		return drawingArea;
	}

	public void setDrawingArea(Plain2D drawingArea) {
		this.drawingArea = drawingArea;
	}

//	public boolean is2D() {
//		return true; // force to 2D viewport
//	}

}

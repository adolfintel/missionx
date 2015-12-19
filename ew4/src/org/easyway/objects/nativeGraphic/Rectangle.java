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
/**
 * 
 */
package org.easyway.objects.nativeGraphic;

import org.easyway.interfaces.extended.IDrawing;
import org.easyway.interfaces.extended.ILayerID;
import org.easyway.objects.Plain2D;
import org.easyway.shader.Shader;
import org.easyway.system.StaticRef;
import org.easyway.system.state.GameState;
import org.easyway.system.state.OpenGLState;
import org.lwjgl.opengl.GL11;

/**
 * 
 * This class draws a colorized rectangle to the screen.<br>
 * A rectangle is composed by a point (X,Y) and two dimensions (Width, Height)<br>
 * The Rectangle is composed from 4 vertexs.<br>
 * 
 * @author Daniele Paggi
 * 
 */
public class Rectangle extends Plain2D implements IDrawing, ILayerID {

    private static final long serialVersionUID = -8421532616527351392L;
    /**
     * specify the color of the rectangle<br>
     * <br>
     * <b>red[index], green[index], blue[index] and alpha[index]</b>:<br>
     * sets the color to the 'index' vertex of the rectangle.<br>
     * <br>
     * the vertexs are:<br>
     * 0: top left<br>
     * 1: top right<br>
     * 2: bottom right<br>
     * 3: bottom left<br>
     * <br>
     * a little ascii image to explain better:<br>
     * (0)----------(1)<br>
     * ...----------...<br>
     * ...----------...<br>
     * (3)----------(2)<br>
     *
     */
    public float red[], green[], blue[], alpha[];
    /**
     * indicates if the coordinates are relative to the screen or to the game's
     * world coordinates
     */
    protected boolean fixedOnScreen = true;
    /**
     * the drawing sheet
     */
    private int idLayer = -1;
    /** Depth of object */
    private int layer;
    public boolean fillIt = true;

    { // initialization
        red = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
        green = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
        blue = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
        alpha = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
    }

    /**
     * creates a new instance of the rectangle
     *
     * @param x
     *            x coordinate
     * @param y
     *            y coordinate
     * @param width
     *            width of the rectangle
     * @param height
     *            height of the rectangle
     */
    public Rectangle(float x, float y, int width, int height) {
        super(x, y, (int) width, (int) height);
        if (autoAddToLists) {
            setIdLayer(GameState.getCurrentGEState().getLayers().length - 1);
        }
    }

    /**
     * sets an uniform color
     *
     * @param red
     *            red component
     * @param green
     *            green component
     * @param blue
     *            blue component
     *
     */
    public void setColor(float red, float green, float blue) {
        for (int i = 0; i < 4; ++i) {
            this.red[i] = red;
            this.green[i] = green;
            this.blue[i] = blue;
        }
    }

    /**
     * sets an uniform color
     *
     * @param red
     *            red component
     * @param green
     *            green component
     * @param blue
     *            blue component
     * @param alpha
     *            alpha component (1: solid, 0: transparent)
     */
    public void setColor(float red, float green, float blue, float alpha) {
        for (int i = 0; i < 4; ++i) {
            this.red[i] = red;
            this.green[i] = green;
            this.blue[i] = blue;
            this.alpha[i] = alpha;
        }
    }

    /**
     * sets the specified color to the specified vertex-id<br>
     * the vertexs are:<br>
     * 0: top left<br>
     * 1: top right<br>
     * 2: bottom right<br>
     * 3: bottom left<br>
     * <br>
     * a little ascii image to explain better:<br>
     * (0)----------(1)<br>
     * ...----------...<br>
     * ...----------...<br>
     * (3)----------(2)<br>
     *
     * @param vertexid
     *            the id of the vertex (0,1,2,3)
     * @param red
     *            red component
     * @param green
     *            green component
     * @param blue
     *            blue component
     * @param alpha
     *            alpha component (1: solid, 0: transparent)
     */
    public void setColor(int vertexid, float red, float green, float blue,
            float alpha) {

        if (vertexid < 0) {
            vertexid = 0;
        }
        if (vertexid > 3) {
            vertexid = 3;
        }

        this.red[vertexid] = red;
        this.green[vertexid] = green;
        this.blue[vertexid] = blue;
        this.alpha[vertexid] = alpha;
    }

    @Override
    public void render() {
        OpenGLState.enableBlending();
        OpenGLState.enableAlphaTest();
        Shader.unBind();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        if (fillIt) {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        } else {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        }
        GL11.glBegin(GL11.GL_QUADS);
        {
            if (fixedOnScreen) {
                GL11.glColor4f(red[0], green[0], blue[0], alpha[0]);
                GL11.glVertex2f(getX(), getY());
                GL11.glColor4f(red[1], green[1], blue[1], alpha[1]);
                GL11.glVertex2f(getX() + getWidth(), getY());
                GL11.glColor4f(red[2], green[2], blue[2], alpha[2]);
                GL11.glVertex2f(getX() + getWidth(), getY() + getHeight());
                GL11.glColor4f(red[3], green[3], blue[3], alpha[3]);
                GL11.glVertex2f(getX(), getY() + getHeight());
            } else {
                float cx = StaticRef.getCamera().getX();
                float cy = StaticRef.getCamera().getY();
                GL11.glColor4f(red[0], green[0], blue[0], alpha[0]);
                GL11.glVertex2f(getX() - cx, getY() - cy);
                GL11.glColor4f(red[1], green[1], blue[1], alpha[1]);
                GL11.glVertex2f(getX() + getWidth() - cx, getY() - cy);
                GL11.glColor4f(red[2], green[2], blue[2], alpha[2]);
                GL11.glVertex2f(getX() + getWidth() - cx, getY() + getHeight() - cy);
                GL11.glColor4f(red[3], green[3], blue[3], alpha[3]);
                GL11.glVertex2f(getX() - cx, getY() + getHeight() - cy);
            }
        }
        GL11.glEnd();
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public void setFixedOnScreen(boolean fixedOnScreen) {
        this.fixedOnScreen = fixedOnScreen;
    }

    /**
     * returns if the object is fixed or not to screen<br>
     * a fixed object have the x,y coordinates relatives to the monitor: not to
     * the world's coordinates
     */
    public boolean isFixedOnScreen() {
        return fixedOnScreen;
    }

    /**
     * change the layer
     *
     * @param layer
     *            the new layer
     */
    public void setLayer(int layer) {
        this.layer = layer;
        readdToDrawingLists();
    }

    @Override
    public int getLayer() {
        return layer;
    }

    @Override
    public int getIdLayer() {
        return idLayer;
    }

    @Override
    public void setIdLayer(int id) {
        if (idLayer != -1) {
            GameState.getCurrentGEState().getLayers()[idLayer].remove(this);
        }
        if (id < 0) {
            id = 0;
        } else if (id > GameState.getCurrentGEState().getLayers().length) {
            id = GameState.getCurrentGEState().getLayers().length - 1;
        }
        idLayer = id;
        GameState.getCurrentGEState().getLayers()[idLayer].add(this);
    }
}

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
import org.easyway.objects.BaseObject;
import org.easyway.shader.Shader;
import org.easyway.system.StaticRef;
import org.easyway.system.state.GameState;
import org.easyway.system.state.OpenGLState;
import org.lwjgl.opengl.GL11;

/**
 * This class draws a colorized line to screen
 * 
 * @author Daniele Paggi
 * 
 */
public class Line extends BaseObject implements IDrawing, ILayerID {

    private static final long serialVersionUID = 1L;
    /**
     * indicates if the coordinates are relative to the screen or to the game's
     * world coordinates
     */
    protected boolean fixedOnScreen = true;
    /**
     * coordiate x
     */
    protected float xstart, xend;
    /**
     * coordinate y
     */
    protected float ystart, yend;
    /**
     * start colors
     */
    protected float reds, greens, blues, alphas;
    /**
     * end colors
     */
    protected float rede, greene, bluee, alphae;
    /**
     * the drawing sheet
     */
    private int idLayer = -1;
    /** Depth of object */
    private int layer;

    /**
     * creates a new instance of the line
     *
     * @param xstart
     *            x start point
     * @param ystart
     *            y start point
     * @param xend
     *            x end point
     * @param yend
     *            y end point
     * @param red
     *            red color component
     * @param green
     *            green color component
     * @param blue
     *            blue color component
     */
    public Line(float xstart, float ystart, float xend, float yend, int red,
            int green, int blue) {
        this.xstart = xstart;
        this.ystart = ystart;
        this.xend = xend;
        this.yend = yend;
        setColor(red, green, blue, 1.0f);
        setIdLayer(GameState.getCurrentGEState().getLayers().length - 1);
        // setWhite(true,true);
    }

    /**
     * creates a new White Line
     *
     * @param xstart
     *            x start point
     * @param ystart
     *            y start point
     * @param xend
     *            x end point
     * @param yend
     *            y end point
     */
    public Line(float xstart, float ystart, float xend, float yend) {
        this.xstart = xstart;
        this.ystart = ystart;
        this.xend = xend;
        this.yend = yend;
        setWhite(true, true);
        setIdLayer(GameState.getCurrentGEState().getLayers().length - 1);
    }

    /**
     * change the start point
     *
     * @param x
     *            new x coordinate
     * @param y
     *            new y coordinate
     */
    public void setStartPoint(float x, float y) {
        xstart = x;
        ystart = y;
    }

    /**
     * change the end point
     *
     * @param x
     *            new x coordinate
     * @param y
     *            new y coordinate
     */
    public void setEndPoint(float x, float y) {
        xend = x;
        yend = y;
    }

    /**
     * change the start and the end point
     *
     * @param xs
     *            new x start point
     * @param ys
     *            new y start point
     * @param xe
     *            new x end point
     * @param ye
     *            new y end point
     */
    public void setPoint(float xs, float ys, float xe, float ye) {
        xstart = xs;
        ystart = ys;
        xend = xe;
        yend = ye;
    }

    /**
     * set to white the line<br>
     * for a full white line use: setWhite(true, true)
     *
     * @param start
     *            indicates if the start point is to set to white
     * @param end
     *            indicates if the end point is to set to white
     */
    public void setWhite(boolean start, boolean end) {
        if (start) {
            reds = greens = blues = alphas = 1.0f;
        }
        if (end) {
            rede = greene = bluee = alphae = 1.0f;
        }
    }

    /**
     * sets an uniform color to the line
     *
     * @param red
     *            red component
     * @param green
     *            green component
     * @param blue
     *            blue component
     */
    public void setColor(float red, float green, float blue) {
        setColor(red, green, blue, 1.0f);
    }

    /**
     * sets an uniform color to the line
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
        reds = rede = red;
        greens = greene = green;
        blues = bluee = blue;
        alphas = alphae = alpha;
    }

    /**
     * change the color used in the start point
     *
     * @param red
     *            red component
     * @param green
     *            green component
     * @param blue
     *            blue component
     *
     */
    public void setStartColor(float red, float green, float blue) {
        reds = red;
        greens = green;
        blues = blue;
    }

    /**
     * change the color used in the start point
     *
     * @param red
     *            red component
     * @param green
     *            green component
     * @param blue
     *            blue component
     * @param alpha
     *            alpha component (1: solid, 0: transparent)
     *
     */
    public void setStartColor(float red, float green, float blue, float alpha) {
        reds = red;
        greens = green;
        blues = blue;
        alphas = alpha;
    }

    /**
     * change the color used in the end point
     *
     * @param red
     *            red component
     * @param green
     *            green component
     * @param blue
     *            blue component
     *
     */
    public void setEndColor(float red, float green, float blue) {
        rede = red;
        greene = green;
        bluee = blue;
    }

    /**
     * change the color used in the end point
     *
     * @param red
     *            red component
     * @param green
     *            green component
     * @param blue
     *            blue component
     * @param alpha
     *            alpha component (1: solid, 0: transparent)
     *
     */
    public void setEndColor(float red, float green, float blue, float alpha) {
        rede = red;
        greene = green;
        bluee = blue;
        alphae = alpha;
    }

    /**
     * sets an uniform alpha (1: solid, 0: transparent)
     *
     * @param alpha
     */
    public void setAlpha(float alpha) {
        alphas = alphae = alpha;
    }

    /**
     * sets if the coordinates (X,Y) are relatives to the monitor or to the
     * game's coordinate system
     *
     * @param fixedOnScreen
     *            true: the coordinate are relative to the monitor<br>
     *            false: the coordinate are relative to the Game's coordinate
     *            system
     */
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

    @Override
    public void render() {
        Shader.unBind();
        OpenGLState.enableBlending();
        OpenGLState.enableAlphaTest();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBegin(GL11.GL_LINES);
        {
            if (fixedOnScreen) {
                GL11.glColor4f(reds, greens, blues, alphas);
                GL11.glVertex2f(xstart, ystart);
                GL11.glColor4f(rede, greene, bluee, alphae);
                GL11.glVertex2f(xend, yend);
            } else {
                float cx = StaticRef.getCamera().getX();
                float cy = StaticRef.getCamera().getY();
                GL11.glColor4f(reds, greens, blues, alphas);
                GL11.glVertex2f(xstart - cx, ystart - cy);
                GL11.glColor4f(rede, greene, bluee, alphae);
                GL11.glVertex2f(xend - cx, yend - cy);
            }
        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    /**
     * change the layer
     *
     * @param layer
     *            new layer
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

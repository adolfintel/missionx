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

import org.easyway.interfaces.extended.IDrawing;
import org.easyway.interfaces.extended.ILayerID;
import org.easyway.objects.BaseObject;
import org.easyway.objects.texture.Texture;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;
import org.easyway.system.state.GameState;
import org.easyway.system.state.OpenGLState;
import org.easyway.utils.ImageUtils;
import org.lwjgl.opengl.GL11;

public class FadeOut extends BaseObject implements IDrawing, ILayerID {

    /**
     * autogenerated serial version of class
     */
    private static final long serialVersionUID = -2160247990013805705L;
    /**
     * the layer of objcet
     */
    private int layer;
    /**
     * the drawing sheet
     */
    private int idLayer = -1;
    /**
     * the time, in nanos, how much the
     */
    protected long time;
    /**
     * the previous image
     */
    protected Texture texture;
    /**
     * the alpha channel used to "fade-out" the image
     */
    protected float alpha;

    /**
     * creates a new instance of FadeOut
     *
     * @param time
     *            indicates in how muck ms the fade out should be done
     */
    public FadeOut(int time) {
        this(time, true, 6);
    }

    /**
     * creates a new instance of FadeOut
     *
     * @param time
     *            indicates in how muck ms the fade out should be done
     * @param idLayer
     *            the drawing sheet.
     */
    public FadeOut(int time, int idLayer) {
        this(time, true, idLayer);
    }

    /**
     * creates a new instance of FadeOut
     *
     * @param time
     *            indicates in how muck ms the fade out should be done
     * @param autoAddToLists
     *            indicates if the objects auto add itself to the game engine's
     *            lists.
     */
    public FadeOut(int time, boolean autoAddToLists) {
        this(time, autoAddToLists, 6);
    }

    /**
     * creates a new instance of FadeOut
     *
     * @param time
     *            indicates in how muck ms the fade out should be done
     * @param autoADdToLists
     *            indicates if the objects auto add itself to the game engine's
     *            lists.
     * @param idLayer
     *            the drawing sheet.
     */
    public FadeOut(int time, boolean autoADdToLists, int idLayer) {
        super(autoAddToLists);
        GameState.getCurrentGEState().getLayers()[idLayer].add(this);
        alpha = 1.0f;
        if (time < 100) {
            time = 100;
        }
        // nano seconds to milli seconds
        this.time = time * 1000000;

        texture = ImageUtils.getScreenShot();
    }

    @Override
    public void render() {
        alpha = alpha - (float) ((float) Core.getInstance().getElaspedTime() / (float) time);
        if (alpha <= 0) {
            destroy();
            return;
        }
        texture.bind();

        // GL11.glLoadIdentity();
        // we can speed up these following 3 lines?
        OpenGLState.enableBlending();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glColor4f(1, 1, 1, alpha);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, texture.yEnd);
        GL11.glVertex2f(0, 0);
        GL11.glTexCoord2f(texture.xEnd, texture.yEnd);
        GL11.glVertex2f(StaticRef.getCamera().getWidth(), 0);
        GL11.glTexCoord2f(texture.xEnd, 0);
        GL11.glVertex2f(StaticRef.getCamera().getWidth(), StaticRef.getCamera().getHeight());
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(0, StaticRef.getCamera().getHeight());
        GL11.glEnd();

    }

    @Override
    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
        readdToDrawingLists();
    }

    @Override
    public void onDestroy() {
        texture.destroy();
    }

    // -----------------------------------------------------------------
    // ---------------------------- IDLAYER-----------------------------
    // -----------------------------------------------------------------
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
            id = GameState.getCurrentGEState().getLayers().length;
        }
        idLayer = id;
        GameState.getCurrentGEState().getLayers()[idLayer].add(this);
    }
}
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
package org.easyway.objects;

import java.awt.Color;

import java.io.Serializable;
import java.util.ArrayList;
import org.easyway.gui.base.GuiContainer;
import org.easyway.interfaces.sprites.IPlain2D;
import org.easyway.lists.GameList;
import org.easyway.objects.texture.TextureFBO;
import org.easyway.system.Core;
import org.easyway.system.state.GameState;
import org.easyway.system.state.OpenGLState;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * The Camera of Game<br>
 * A Camera shows a partition of "world" on screen<br>
 */
public class Camera implements IPlain2D, Serializable {

    private static final long serialVersionUID = 1847978706275643411L;

    public static Camera getCurrentCamera() {
        return GameState.getCurrentGEState().getCamera();
    }
    /**
     * Auto-Center the camera on the "centered" Sprite. <br>
     */
    public IPlain2D center;
    /**
     * "centred" Sprite Position on the Camera. <br>
     * Posizione dello sprite "centrato" sulla camera.
     */
    private double xCenter;
    /**
     * "centred" Sprite Position on the Camera. <br>
     */
    private double yCenter;
    /**
     * attrack the camera to the Linked - Sprite <br>
     */
    public boolean attrack = false;
    /**
     * cartesian position
     */
    public float x, y, z;
    /**
     * clear colors
     */
    protected float red = 100 / 255f, green = 149 / 255f, blue = 237 / 255f, alpha = 0f;
    /**
     * the value 1.0f indicates that the zoom is at original size<br>
     * a zoom of 2.0f will place the camera more close to the "drawing-sheet"<br>
     * a zoom of 0.5f will place the camera more distance to the "drawing-sheet"<br>
     * <b>caution:</b><br>
     * if you're using a Grid-Manager you shouldn't uses values less that 0.5f!
     */
    protected float zoomFactor = 1.0f;
    /**
     * this is the matrix that the camera uses to remember his rotation in the
     * 3D space
     */
    public Matrix4f matrix = new Matrix4f();
    /**
     * vector position: it's NOT constanty update!<br>
     * you generally should uses the 'x','y' and 'z' fields instead of
     * 'position'.
     */
    private Vector3f position = new Vector3f();
    /**
     * sizes of show screen
     */
    private int width, height;
    /**
     * indicates if the camera is in 2D mode or in 3D mode
     */
    protected boolean is2D = false;
    /**
     * indicates if the object will be collected in the 'objectsOnScreen' list
     * or not
     */
    protected boolean collectingObjectsOnScreen = true;
    /**
     * a collection of object that are at screen
     */
    private GameList<IPlain2D> objectsOnScreen = new GameList<IPlain2D>();

    /**
     * Creates a new Camera <br>
     *
     * @param x
     *            x coordinate
     * @param y
     *            y coordinate
     * @param w
     *            Width of Camera
     * @param h
     *            Height of Camera
     */
    public Camera(int x, int y, int w, int h) {
        this.x = (float) x;
        this.y = (float) y;
        width = w;
        height = h;
        //if (mainFather == null) {

        /*} else {
        mainFather.setSize(w, h);
        mainFather.setIdLayer(GuiContainer.DEF_IDLAYER);
        }*/
        // mainFather.father = null;

    }

    private Camera() {
    }

    public GameList<IPlain2D> getObjectsOnScreen() {
        return objectsOnScreen;
    }

    public boolean isCollectingObjectsOnScreen() {
        return collectingObjectsOnScreen;
    }

    public void setCollectingObjectsOnScreen(boolean shouldCollect) {
        collectingObjectsOnScreen = shouldCollect;
    }

    public void clearCollectedObjectsOnScreen() {
        objectsOnScreen.removeAll();
    }

    public void addObjectsOnScreen(IPlain2D object) {
        if (!collectingObjectsOnScreen) {
            return;
        }
        if (object == null) {
            new Exception().printStackTrace();
        }
        this.objectsOnScreen.add(object);
    }

    public void setObjectsOnScreen(GameList<IPlain2D> objectsOnScreen) {
        this.objectsOnScreen = objectsOnScreen;
    }

    // --------------------------------------------------------------------------------
    /** initializes OpenGL */
    public void initGL(int width, int height) {
        // tracking material
        // GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        // GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);
        GL11.glEnable(GL11.GL_TEXTURE_2D); // Enable Texture Mapping
        // inverte colori
        // GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE,
        // GL11.GL_BLEND );
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE,
                GL11.GL_MODULATE);

        GL11.glShadeModel(GL11.GL_SMOOTH);

        GL11.glClearColor(0f, 0f, 0f, 0f); // Black Background
        GL11.glClearDepth(100.0); // Depth Buffer Setup
        OpenGLState.enableZtest(); // GL11.glEnable(GL11.GL_DEPTH_TEST); //
        // Enables Depth Testing
        GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Testing To Do

        // ------------------- proiezione --------------------
        GL11.glMatrixMode(GL11.GL_PROJECTION); // Select The Projection Matrix
        GL11.glLoadIdentity(); // Reset The Projection Matrix

        GL11.glFrustum(-2.0, 2.0, -1.5, 1.5, 1.5, 3000/* 10000 */);
        // -------------------- visuale ----------------------
        GL11.glMatrixMode(GL11.GL_MODELVIEW); // Select The Modelview Matrix
        GL11.glLoadIdentity();
        GL11.glViewport(0, 0, width, height);
        /** ----------------- trasparenze -------------------- */
        // GL11.glEnable(GL11.GL_BLEND);
        // old
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        // new
        // GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA,
        // GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        // GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0f);

        // Really Nice Perspective Calculations
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
        set2D(width, height);
    }

    // --------------------------------------------------
    public void setDrawingArea(int width, int height) {
        System.out.println("DRAWING AREA W:" + width + " H:" + height);
        if (is2D) {
            GL11.glViewport(0, 0, width, height);
            // this.width = width;
            // this.height = height;
        }
    }

    /**
     * moves the camera:<br>
     * x += incx;<br>
     * y += incy;<br>
     *
     * @param incx
     *            the x movement
     * @param incy
     *            the y movement
     */
    public void move(double incx, double incy) {
        x += incx;
        y += incy;
    }

    /**
     * moves the camera:<br>
     * x += incx;<br>
     * y += incy;<br>
     * z += incz;
     *
     * @param incx
     *            the x movement
     * @param incy
     *            the y movement *
     * @param incz
     *            the z movement
     * @see #setx(float)
     * @see #sety(float)
     * @see #setz(float)
     */
    public void move(float incx, float incy, float incz) {
        x += incx;
        y += incy;
        z += incz;
    }

    /**
     * sets a new position<br>
     * x = newx;
     */
    public void setx(float newx) {
        x = newx;
    }

    /**
     * sets a new position<br>
     * y = newy;
     */
    public void sety(float newy) {
        y = newy;
    }

    /**
     * sets a new position<br>
     * z = newz;
     */
    public void setz(float newz) {
        z = newz;
    }

    /**
     * returns the x position
     *
     * @return x position
     */
    @Override
    public float getX() {
        return x;
    }

    /**
     * returns the y position
     *
     * @return y position
     */
    @Override
    public float getY() {
        return y;
    }

    /**
     * returns the z position
     *
     * @return z position
     */
    public float getZ() {
        return z;
    }

    @Override
    public float getScaleX() {
        return 1f;
    }

    @Override
    public float getScaleY() {
        return 1f;
    }

    // --------------------------------------------------
    /**
     * returns the actual width of camera<br>
     * the width is influenced by the zoomFactor<br>
     * <br>
     *
     * @return width * zoomFactor
     */
    @Override
    public int getWidth() {
        return (int) (width * zoomFactor);
    }

    /**
     * returns the actual height of camera<br>
     * the height is influenced by the zoomFactor<br>
     * <br>
     *
     * @return height * zoomFactor
     */
    @Override
    public int getHeight() {
        return (int) (height * zoomFactor);
    }

    // --------------------------------------------------
    /**
     * place the 'spr' in center of the screen and then attrack it to camera.<br>
     * if the spr will be moved the camera will follow it.<br>
     *
     * @param spr
     *            sprite to center on camera
     */
    public void centerOn(IPlain2D spr) {
        centerOn(spr, width / 2.0, height / 2.0, true);
    }

    /**
     * returns if the camera is centered on a sprite
     * @return
     */
    public boolean isCentered() {
        return center != null;
    }

    /**
     * place the 'spr' in center of the screen.<br>
     * if 'attrack' is setted to 'true' the camera will follow the 'spr'.<br>
     *
     * @param spr
     *            sprite to center on camera
     * @param aggancia
     *            select if attrack the camera to the Linked-Sprite
     */
    public void centerOn(IPlain2D spr, boolean attrack) {
        centerOn(spr, width / 2.0, height / 2.0, attrack);
    }

    /**
     * place the 'spr' in center (in the x\y specified coordinates) of the
     * screen and then attrack it to camera.<br>
     * if the spr will be moved the camera will follow it.<br>
     *
     *
     * @param spr
     *            sprite to center on camera
     * @param xCenter
     *            x position of "centration"
     * @param yCenter
     *            y position of "centration"
     */
    public void centerOn(IPlain2D spr, double xCenter, double yCenter) {
        centerOn(spr, xCenter, yCenter, true);
    }

    /**
     * place the 'spr' in center (in the x\y specified coordinates) of the
     * screen.<br>
     * if 'attrack' is setted to 'true' the camera will follow the 'spr'.<br>
     *
     * @param xCenter
     *            x position of "centration"
     * @param yCenter
     *            y position of "centration"
     * @param attrack
     *            select if attrack the camera to the Linked-Sprite
     * @param spr
     *            sprite to center on camera
     */
    public void centerOn(IPlain2D spr, double xCenter, double yCenter,
            boolean attrack) {
        center = spr;
        this.attrack = attrack;
        this.xCenter = xCenter;
        this.yCenter = yCenter;
    }

    /**
     * Center che camera on the attracked IPlain2D spefied<br>
     * <b>generally it's selfcalled by the GameEngine</b>
     *
     */
    public void center() {
        // System.out.println("CENTER : "+center+" : "+attrack);
        if (center != null && attrack) {
            setx((float) (center.getX() + (center.getWidth() / 2) - xCenter + (width * (1 - zoomFactor)) / 2));
            sety((float) (center.getY() + (center.getHeight() / 2) - yCenter + (height * (1 - zoomFactor)) / 2));
        }
    }

    // --------------------------------------------------
    /**
     * set the zoom factor<br>
     * the normal zoomfactor is 1.0f<br>
     *
     * a zoom of 0.5f will place the camera more close to the "drawing-sheet"<br>
     * a zoom of 2.0f will place the camera more distance to the "drawing-sheet"<br>
     *
     */
    public void setZoom2D(float factor) {
        zoomFactor = factor;
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity(); // clear the perspective matrix
        if (TextureFBO.isDrawingOnTextureFBO()) {
            GL11.glOrtho(0, width * factor, 0, height * factor, -32, 32);
        } else {
            GL11.glOrtho(0, width * factor, height * factor, 0, -32, 32);
        }
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    /**
     * increase\decrease the zoom factor of 'factorinc' value<br/>
     * It's like do: setZoom2D( getZoom2D() + factoric );<br/>
     * the normal zoomfactor is 1.0f<br>
     *
     * a zoom of 0.5f will place the camera more close to the "drawing-sheet"<br>
     * a zoom of 2.0f will place the camera more distance to the "drawing-sheet"<br>
     *
     */
    public void incZoom2D(float factorinc) {
        setZoom2D(getZoom2D() + factorinc);
    }

    /**
     * returns the 2D zoom<br>
     * the normal zoomfactor is 1.0f<br>
     *
     * a zoom of 0.5f will place the camera more close to the "drawing-sheet"<br>
     * a zoom of 2.0f will place the camera more distance to the "drawing-sheet"<br>
     *
     * @return
     */
    public float getZoom2D() {
        return zoomFactor;
    }

    /**
     * returns the factor of difference of size between the Camera and the core
     * @return
     */
    public float getWidthFactor() {
        int w = Core.getInstance().getWidth();
        if (width == w) {
            return 1;
        }
        return (float) width / (float) w;
    }

    /**
     * returns the factor of difference of size between the Camera and the core
     * @return
     */
    public float getHeightFactor() {
        int h = Core.getInstance().getHeight();
        if (height == h) {
            return 1;
        }
        return (float) height / (float) h;
    }

    public void set2D() {
        set2D(width, height);
    }

    /**
     * initializes the camera to drawing 2D scenes
     */
    public void set2D(int width, int height) {
        if (is2D) {
            return;
        }
        is2D = true;
        // prepare to render in 2D
        // GL11.glDisable(GL11.GL_LIGHTING);
        // scene
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix(); // preserve perspective view
        GL11.glLoadIdentity(); // clear the perspective matrix
        if (TextureFBO.isDrawingOnTextureFBO()) {
            GL11.glOrtho(0, width, 0, height, -1000, 1000);
        } else {
            GL11.glOrtho(0, width, height, 0, -1000, 1000);
        }
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix(); // Preserve the Modelview Matrix
        GL11.glLoadIdentity(); // clear the Modelview Matrix
        GL11.glViewport(0, 0, width, height);
        OpenGLState.disableZtest(); // so 2D stuff stays on top of 3D
    }

    /**
     * initializes the camera to drawing 3D scenes
     */
    public void set3D() {
        if (!is2D) {
            return;
        }
        is2D = false;
        // restore the original positions and views
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        // GL11.glEnable(GL11.GL_LIGHTING);
        OpenGLState.enableZtest();// turn Depth Testing back on
    }

    /**
     * returns if the camera is settet to drawing a 2D scene or not
     *
     * @return is drawing a 2D scene?
     */
    public boolean is2D() {
        return is2D;
    }

    /**
     * returns if the camera is settet to drawing a 3D scene or not
     *
     * @return is drawing a 3D scene?
     */
    public boolean is3D() {
        return !is2D;
    }

    // --------------------------------------------------
    /**
     * sets the color of background of screen
     */
    public void setBackgroundColor(Color color) {
        setClearColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 0f);
    }

    /**
     * sets the color of background of screen
     *
     * @param red
     *            red component
     * @param green
     *            green component
     * @param blue
     *
     */
    public void setBackgroundColor(int red, int green, int blue) {
        setClearColor((float) red / 255.0f, (float) green / 255.0f, (float) blue / 255.0f, 0.0f);
    }

    /**
     * sets the color of background of screen
     *
     * @param red
     *            red component
     * @param green
     *            green component
     * @param blue
     *            blue component
     * @param alpha
     *            alpha component (usually 0.0f)
     */
    public void setClearColor(float red, float green, float blue, float alpha) {
        GL11.glClearColor(this.red = red, this.green = green, this.blue = blue,
                this.alpha = alpha);
    }

    // -------------------------------------------------------------------------
    /**
     * rotates the camera of vale radiant on the spefied vector
     *
     * @param value
     *            radiant value
     * @param vector
     *            vector of rotation
     */
    public void rotate(float value, Vector3f vector) {
        matrix.rotate(value, vector);
    }

    /**
     * rotates the camera of value radiant on the X axis
     *
     * @param value
     *            radiant value
     */
    public void rotateX(float value) {
        rotate(value, new Vector3f(1, 0, 0));
    }

    /**
     * rotates the camera of value radiant on the Y axis
     *
     * @param value
     *            radiant value
     */
    public void rotateY(float value) {
        rotate(value, new Vector3f(0, 1, 0));
    }

    /**
     * rotates the camera of value radiant on the Z axis
     *
     * @param value
     *            radiant value
     */
    public void rotateZ(float value) {
        rotate(value, new Vector3f(0, 0, 1));
    }

    /**
     * removes all rotations from camera: sets the identity matrix<br>
     */
    public void rotateReset() {
        matrix.setIdentity();
    }

    /**
     * returns the position of camera (x,y,z)
     *
     * @return position of camera
     */
    public Vector3f getPosition() {
        position.setX(x);
        position.setY(y);
        position.setZ(z);
        return position;
    }

    public Color getBackgroundColor() {
        return new Color(red, green, blue, alpha);
    }

    // -------------------- MOVE -----------------------------------------------
    /** move the camera relative to rotation */
    public void moveX(float step) {
        x += step * matrix.m00;
        y += step * matrix.m01;
        z += step * matrix.m02;
    }

    /** move the camera with the rotation */
    public void moveY(float step) {
        x += step * matrix.m10;
        y += step * matrix.m11;
        z += step * matrix.m12;
    }

    /** move the camera with the rotation */
    public void moveZ(float step) {
        x += step * matrix.m20;
        y += step * matrix.m21;
        z += step * matrix.m22;
    }

    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        s.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        setClearColor(red, green, blue, alpha);
        //width = StaticRef.core.getWidth();
        //height = StaticRef.core.getHeight();
        //rebind();
    }

    public void rebind() {
        //System.out.println("zoomFactor: "+zoomFactor);
        setZoom2D(zoomFactor);
        setClearColor(red, green, blue, alpha);
    }

    /**
     * returns the main father of all GuiComponent
     * @return
     */
    public static GuiContainer getMainFather() {
        return GameState.getCurrentGEState().getMainGuiFather();
    }

    public float getRightBound() {
        return x + width;
    }

    public float getLeftBound() {
        return x;
    }

    public float getBottomBound() {
        return y + height;
    }

    public float getTopBound() {
        return y;
    }

    @Override
    public void setWidth(int width) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setHeight(int height) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setSize(int width, int height) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    @Override
    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public float getXOnScreen() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getYOnScreen() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList getQuadEntries() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList getUsedInQuadNodes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isQuadTreeUsable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList getEntries() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDestroyed() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getType() {
        return "$_CAMERA";
    }
}

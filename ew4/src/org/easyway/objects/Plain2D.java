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

import java.io.Serializable;

import java.util.ArrayList;
import org.easyway.collisions.IQuadTreeUsable;
import org.easyway.collisions.ManagedGroupCollision;
import org.easyway.collisions.MgcList;
import org.easyway.collisions.quad.QuadEntry;
import org.easyway.collisions.quad.QuadNode;
import org.easyway.interfaces.sprites.IPlain2D;
import org.easyway.collisions.quad.QuadTree;
import org.easyway.utils.Matrix3fUtil;
import org.easyway.interfaces.sprites.IRotation;
import org.easyway.interfaces.sprites.ISpriteColl;
import org.easyway.system.StaticRef;
import org.easyway.utils.MathUtils;
import org.lwjgl.util.vector.Matrix3f;

/**
 * A 2D plain that is usable with the Quad-Engine.<br>
 * The quad-Engine is an engine that speed-up your game.<br>
 * 
 * @see org.easyway.quad.QaudManager
 * 
 * @author Paggi Daniele
 * 
 */
public class Plain2D extends BaseObject implements IPlain2D, IRotation, Serializable {

    private static final long serialVersionUID = -4705260369634061760L;
    /** sizes of plain */
    private int width, height;
    /** rotation of the object */
    private float rotation;
    /** coordinates of object in the game world */
    private float x, y;
    //protected Vector3f position = new Vector3f(0,0,1);
    // protected Matrix3f transformation = new Matrix3f();

    /**
     * cretes a new instace of Plain2D
     */
    public Plain2D() {
        super(true);
    }

    /**
     * creates a new instance of Plain2D
     *
     * @param x
     *            x coordiante
     * @param y
     *            y coordinate
     * @param width
     *            width of plain
     * @param height
     *            height of plain
     * @param idLayer
     *            drawing-sheet id
     */
    public Plain2D(float x, float y, int width, int height) {
        this(true, x, y, width, height);
    }

    /**
     * creates a new instance of Plain2D
     *
     * @param autoAdd
     *            indicates if the Plain2D will be auto-added to the game
     *            engine's lists
     * @param x
     *            x coordiante
     * @param y
     *            y coordinate
     * @param width
     *            width of plain
     * @param height
     *            height of plain
     * @param idLayer
     *            drawing-sheet id
     */
    public Plain2D(boolean autoAdd, float x, float y, int width, int height) {
        super(autoAdd);
        setXY(x, y);
        /*this.x = x;
        this.y = y;*/
        if (width >= 0 && height >= 0) {
            setSize(width, height);
        }
    }

    /**
     * clones the plain
     * @param plain2D the original object to be cloned
     */
    public Plain2D(Plain2D obj) {
        super(obj);
        setXY(obj.getX(), obj.getY());
        setSize(obj.getWidth(), obj.getHeight());
        setRotation(obj.getRotation());
    }

    @Override
    public void activate() {
        super.activate();
        if (testQuadTreeUsable()) {
            QuadTree.getDefaultInstance().add((IQuadTreeUsable) this);
        }
    }

    @Override
    protected void disactivate() {
        removeQuadsUpdate();
        super.disactivate();
    }

    /**
     * changes the sizes of the plain
     *
     * @param width
     *            new width
     * @param height
     *            new height
     */
    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        addQuadsUpdate();
    }

    /**
     * sets the dimension of sprite
     *
     * @param width
     *            width of sprite
     * @param height
     *            height of sprite
     * @see #getWidth()
     * @see #getHeight()
     */
    public void setSize(float width, float height) {
        this.setSize((int) width, (int) height);
    }

    @Override
    public void setWidth(int width) {
        setSize(width, height);
    }

    @Override
    public void setHeight(int height) {
        setSize(width, height);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    /**
     * returns the x coordinate
     *
     */
    @Override
    public float getX() {
        return x;
    }

    /**
     * returns the y coordinate
     *
     */
    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getXOnScreen() {
        return getX() - Camera.getCurrentCamera().getX();
    }

    @Override
    public float getYOnScreen() {
        return getY() - Camera.getCurrentCamera().getY();
    }

    /**
     * sets a new x position
     *
     */
    @Override
    public void setX(float x) {
        setXY(x, getY());
    }

    /**
     * sets a new y position *
     *
     */
    @Override
    public void setY(float y) {
        setXY(getX(), y);
    }

    /**
     * sets a new x,y position
     *
     * @param x
     *            new x position
     * @param y
     *            new y position
     */
    @Override
    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
        addQuadsUpdate();
    }

    @Override
    public float getScaleX() {
        return width;
    }

    @Override
    public float getScaleY() {
        return height;
    }

    /**
     * increments it's coordinates of incx, incy x += incx;<br>
     * y += incy;<br>
     *
     * @param incx
     * @param incy
     */
    public void move(float incx, float incy) {
        setXY(getX() + incx, getY() + incy);
    }

    /**
     * increments it's x coordinate of incx<br>
     * x += incx;
     */
    public void incX(float incx) {
        move(incx, 0);
    }

    /**
     * increments it's y coordiante of incy<br>
     * y += incy;
     */
    public void incY(float incy) {
        move(0, incy);
    }

    @Override
    public float getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(float newRotation) {
        rotation = newRotation;
        // transformation = Matrix3fUtil.rotate(MathUtils.degToRad(rotation), transformation);
    }

    /**
     * removes the plain from the Quad-Engine<br>
     */
    protected void removeQuadsUpdate() {
        if (testQuadTreeUsable()) {
            QuadTree.getDefaultInstance().remove((IQuadTreeUsable) this);
        }
    }

    /**
     * adds the plain from the Quad-Engine
     *
     */
    protected void addQuadsUpdate() {
        if (testQuadTreeUsable()) {
            IQuadTreeUsable mgci = (IQuadTreeUsable) this;
            QuadTree.getDefaultInstance().add(mgci);
            MgcList list = mgci.getMgcs();
            for (ManagedGroupCollision mgc : list) {
                mgc.update((ISpriteColl) this);
            }
        }
    }

    @Override
    public Plain2D clone() {
        return new Plain2D(this);
    }
    ArrayList<QuadNode> quadNodes;

    @Override
    public ArrayList<QuadNode> getUsedInQuadNodes() {
        return quadNodes == null ? quadNodes = new ArrayList<QuadNode>(4) : quadNodes;
    }
    ArrayList<QuadEntry> quadEntries;

    @Override
    public ArrayList<QuadEntry> getQuadEntries() {
        return quadEntries == null ? quadEntries = new ArrayList<QuadEntry>(4) : quadEntries;
    }

    protected final boolean testQuadTreeUsable() {
        return isQuadTreeUsable && isQuadTreeUsable();
    }

    @Override
    public boolean isQuadTreeUsable() {
        return true;
    }
}

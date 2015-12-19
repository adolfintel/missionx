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
package org.easyway.objects.sprites2D;

import java.io.Serializable;
import java.util.ArrayList;

import org.easyway.collisions.CollisionUtils;
import org.easyway.collisions.ICollisionMethod;
import org.easyway.collisions.methods.HardWarePixelMethod;
import org.easyway.collisions.IHardwareCollisionable;
import org.easyway.collisions.MgcList;
import org.easyway.collisions.methods.SoftwareCollisionMethod;
import org.easyway.interfaces.base.ITexture;
import org.easyway.interfaces.sprites.IMaskerable;
import org.easyway.interfaces.sprites.ISpriteColl;
import org.easyway.objects.animo.Animo;
import org.easyway.system.Core;
import org.easyway.tiles.TileMapLayer;
import org.easyway.tiles.TileSprite;

/**
 * A simple that can be used to tests the collisions.
 * 
 * @Author Daniele Paggi, Do$$e
 */
public class SpriteColl extends Sprite implements ISpriteColl, IMaskerable, IPolyMaskerable, Serializable {

    /**
     * generated version
     */
    private static final long serialVersionUID = 3582682613742361484L;
    private ArrayList<ISpriteColl> collisionList = new ArrayList<ISpriteColl>(128);
    /**
     * indicates if the object is added or not to the auto-collisionLists.
     */
    protected boolean collAdded = false;
    /** the collision mask */
    private Mask mask;
    /**the polygon collision data*/
    private PolygonMask polyMask;
    /** the collision method used by the sprite */
    private ICollisionMethod collisionMethod = Core.getGLIntVersion() >= 15 ?  HardWarePixelMethod.getDefaultInstance() : SoftwareCollisionMethod.getDefaultInstance();

    // -----------------------------------------------------------------
    // ---------------------CONSTRUCTORS--------------------------------
    // -----------------------------------------------------------------

    public SpriteColl(SpriteColl obj) {
        super(obj);
        this.mask = obj.mask;
        this.polyMask = obj.polyMask;
        this.collisionMethod = obj.collisionMethod;
    }

    public SpriteColl(SimpleSprite spr) {
        super(spr);
    }

    public SpriteColl() {
    }

    public SpriteColl(boolean toAdd) {
        super(toAdd);
    }

    public SpriteColl(float x, float y) {
        super(x, y);
    }

    public SpriteColl(boolean toAdd, float x, float y) {
        super(toAdd, x, y);
    }

    public SpriteColl(boolean toAdd, ITexture image) {
        super(toAdd, image);
    }

    public SpriteColl(float x, float y, ITexture image) {
        super(x, y, image);
    }

    public SpriteColl(float x, float y, ITexture image, int layer) {
        super(x, y, image, layer);
    }

    public SpriteColl(boolean toAdd, float x, float y, ITexture image, int layer) {
        super(toAdd, x, y, image, layer);
    }

    public SpriteColl(boolean toAdd, float x, float y, ITexture image) {
        super(toAdd, x, y, image);
    }

    public SpriteColl(float x, float y, Animo animo) {
        super(x, y, animo);
    }

    public SpriteColl(boolean toAdd, float x, float y, Animo animo) {
        super(toAdd, x, y, animo);
    }

    public SpriteColl(ITexture image) {
        super(image);
    }

    public SpriteColl(int layer) {
        super(layer);
    }

    public SpriteColl(int layer, int idLayer) {
        super(layer, idLayer);
    }

    public SpriteColl(boolean toAdd, int layer) {
        super(toAdd, layer);
    }

    public SpriteColl(boolean toAdd, int layer, int idLayer) {
        super(toAdd, layer, idLayer);
    }

    public SpriteColl(boolean autoAdd, float x, float y, ITexture img, Animo animo, int layer, int idLayer) {
        super(autoAdd, x, y, img, animo, layer, idLayer);
    }

    // -----------------------------------------------------------------
    // --------------------- OTHER -------------------------------------
    // -----------------------------------------------------------------
    @Override
    public Mask getMask() {
        if (mask != null) {
            return mask;
        }
        if (image != null) {
            return image.getMask();
        }
        return null;
    }

    /**
     * returns the collision mask
     *
     * @param mask
     *            the collision mask
     */
    public void setMask(Mask mask) {
        this.mask = mask;
    }

    @Override
    public PolygonMask getPolyMask() {
        if (polyMask != null) {
            return polyMask;
        }
        if (image != null) {
            return image.getPolyMask();
        }
        return null;
    }

    /**
     * returns the collision mask
     *
     * @param mask
     *            the collision mask
     */
    public void setPolygonMask(PolygonMask mask) {
        this.polyMask = mask;
    }

    @Override
    public ArrayList<ISpriteColl> getCollisionList() {
        return collisionList;
    }

    @Override
    public void onCollision() {
    }

    @Override
    public boolean isAddedToCollisionList() {
        return collAdded;
    }

    @Override
    public void setAddedToCollisionList(boolean value) {
        collAdded = value;
    }

    @Override
    public SpriteColl clone() {
        return new SpriteColl(this);
    }

    @Override
    public ICollisionMethod getCollisionMethod() {
        return collisionMethod;
    }

    public void setCollisionMethod(ICollisionMethod collMethod) {
        this.collisionMethod = collMethod;
    }

    @Override
    public Object getCollisionData() {
        switch (getCollisionMethod().getCollisionType()) {
            case PIXEL_SOFTWARE:
                return getMask();
            case VECTORIAL:
                return polyMask;
            default:
                return null;
        }
    }

    @Override
    public Class getCollisionDataType() {
        switch (getCollisionMethod().getCollisionType()) {
            case PIXEL_SOFTWARE:
                return Mask.class;
            case VECTORIAL:
                return PolygonMask.class;
            default:
                return null;
        }
    }

    /**
     * auto move the sprite in relation with a collision of a tile
     * @param stepx amount to move on x
     * @param stepy amount to move on y
     * @param tm the TiledMap
     * @param tileName the tile name
     */
    public void autoAlign(float stepx, float stepy, TileMapLayer tm, String tileName) {
        ArrayList<ISpriteColl> coll = tm.testCollision(this);
        boolean found = false;
        do {
            coll = tm.testCollision(this);
            TileSprite tSprite;
            found = false;
            if (coll != null) {
                for (ISpriteColl spr : coll) {
                    tSprite = (TileSprite) spr;
                    if (tSprite.getTileType().equals(tileName)) {
                        found = true;
                        break;
                    }
                }
            }
            move(stepx, stepy);
        } while (found);

    }
    MgcList collisionTesterList;

    public MgcList getMgcs() {
        return collisionTesterList == null ? collisionTesterList = new MgcList() : collisionTesterList;
    }

    public boolean testCollision(ISpriteColl spr) {
        return Core.getGLIntVersion()>=15?CollisionUtils.trueHardWareHit(this, (IHardwareCollisionable) spr):CollisionUtils.trueHit(this, spr);
    }
}

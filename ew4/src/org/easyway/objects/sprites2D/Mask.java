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
import java.nio.ByteBuffer;

import org.easyway.interfaces.base.ITexture;

/**
 * this class records in an array of boolean if a pixel is collisionable or not.<br>
 * The Mask is used to test the precise pixel collision.<br>
 * 
 * @author Daniele Paggi
 */
public class Mask implements Serializable {

    private static final long serialVersionUID = -5679205580926696806L;

    // we use public modifier to speed-up the access
    /** the width of collision mask */
    public int width;

    // we use public modifier to speed-up the access
    /** the height of collision mask */
    public int height;

    // note: we can use a ByteBuffer or same of similar? O.o''
    /** the collision mask data */
    public boolean mask[][]; // ByteBuffer data;

    // we use public modifier to speed-up the access
    /** indicates if the mask is fully sets to 'true' */
    public boolean full;

    protected Mask() {
    }

    /**
     * Creates a new instance of Mask.<br>
     * The Mask will be maked fully<br>
     * 
     * @see #full()
     * 
     * @param width
     *            width of mask
     * @param height
     *            height of mask
     */
    public Mask(int width, int height) {
        this.width = width;
        this.height = height;
        mask = new boolean[width][height];
        full();
    }

    /**
     * Creates a new instance of Mask from a texture
     * 
     * @param texture
     *            the source texture
     */
    public Mask(ITexture texture) {
        // int max = texture.width * texture.height;
        // data = ByteBuffer.allocateDirect(max);
        width = texture.getWidth();
        height = texture.getHeight();
        int t = texture.getWidthHW();
        int xs = (int) (texture.getXStart() * t);
        int xe = (int) (texture.getXEnd() * t);
        int ys = (int) (texture.getYStart() * (t = texture.getHeightHW()));
        int ye = (int) (texture.getYEnd() * t);
        //System.out.println("xs:" + xs + " xe: " + xe);
        //System.out.println("ys:" + ys + " ye: " + ye);
        //width = Math.abs(xe - xs)+1;
        //height = Math.abs(ye - ys)+1;
        texture.setMask(this);
        // mask = new boolean[texture.getWidth()][texture.getHeight()];
        mask = new boolean[width][height];
        ByteBuffer temp = texture.getData();
        temp.rewind();
        int alpha;
        for (int y = 0; y < texture.getHeightHW(); y++) {
            for (int x = 0; x < texture.getWidthHW(); x++) {
                temp.get();
                temp.get();
                temp.get();
                alpha = temp.get();
                if (x >= xs && x < xe && y >= ys && y < ye) {
                    //if (x < texture.getWidth()) {
                    if (alpha == 0) {
                        full = mask[x - xs][y - ys] = false;
                    } else {
                        mask[x - xs][y - ys] = true;
                    //}
                    }
                }
                if (y >= ye) {
                    break;
                }
            }
        // TEST - CODE
        //for (int y = 0; y < texture.getHeight(); y++) {
        //for (int x = 0; x < texture.getWidth(); x++) {
        //System.out.print(mask[x][y] ? "X" : ".");
        //}
        //System.out.println();
        //}
        }
    }

    public Mask(Mask obj) {
        // super(obj);
        width = obj.width;
        height = obj.height;
        full = obj.full;
        mask = obj.mask.clone(); // this will works?

    }

    /** creates a full-rectangular mask */
    public void full() {
        for (int y = 0, x; y < height; ++y) {
            for (x = 0; x < width; ++x) {
                mask[x][y] = true;
            }
        }
        full = true;
    }

    /**
     * changes the x,y value of the mask
     * 
     * @param x
     *            coordinate
     * @param y
     *            coordinate
     * @param val
     *            new value
     */
    public void set(int x, int y, boolean val) {
        // note:
        // we have removed the check (x>0 && y>0 && x<width && y<height)
        // to speed-up the code
        if (!(mask[x][y] = val) && full) {
            full = false;
        }
    }

    // ------------------------------------- SERIALIZATION -----------
    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        s.writeInt(width);
        s.writeInt(height);
        s.writeBoolean(full);
        if (mask != null) {
            s.writeBoolean(true);
            s.writeObject(mask);
        } else {
            s.writeBoolean(false);
        }
    }

    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        width = s.readInt();
        height = s.readInt();
        full = s.readBoolean();
        if (s.readBoolean()) {
            mask = (boolean[][]) s.readObject();
        }
    }

    @Override
    public Object clone() {
        return new Mask(this);
    }
}

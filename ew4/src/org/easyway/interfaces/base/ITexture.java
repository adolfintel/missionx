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
package org.easyway.interfaces.base;

import java.nio.ByteBuffer;

import org.easyway.interfaces.sprites.IMaskerable;
import org.easyway.objects.sprites2D.IPolyMaskerable;
import org.easyway.objects.sprites2D.Mask;
import org.easyway.objects.sprites2D.PolygonMask;

public interface ITexture extends IDestroyable, IMaskerable, IPolyMaskerable {

    /**
     * bind the current texture in the VRAM
     */
    public void bind();

    /**
     * bind the current texture object in the specified texture unit
     * @param unit the texture unit in witch the current TextureObject will be binded
     */
    public void bind(int unit);

    /**
     * returns the width of image
     *
     * @return the width of image
     */
    public int getWidth();

    /**
     * returns the height of image
     *
     * @return the height of image
     */
    public int getHeight();

    /**
     * return the width Hardware of image
     *
     * @return
     */
    public int getWidthHW();

    /**
     * return the height hardware of image
     *
     * @return
     */
    public int getHeightHW();

    /**
     * returns the start X-coordinate
     *
     * @return the start X-coordinate
     */
    public float getXStart();

    /**
     * returns the end X-coordinate
     *
     * @return the end X-coordinate
     */
    public float getXEnd();

    /**
     * returns the start Y-coordinate
     *
     * @return the start Y-coordinate
     */
    public float getYStart();

    /**
     * returns the end Y-coordinate
     *
     * @return the end Y-coordinate
     */
    public float getYEnd();

    /**
     * sets the region of the image
     * @param x xstart position
     * @param y ystart position
     * @param width width of the region
     * @param height height of the region
     */
    public void setRegion(int x, int y, int width, int height);

    /**
     * flips the image vertically
     */
    public void flipY();
    /**
     * flips the image horizzontally
     */

    public void flipX();


    /**
     * returns the ID of image in the Vram
     *
     * @return the ID of image in the Vram
     */
    public int getID();

    /**
     * returns the texture's pixel in a ByteBuffer
     *
     * EXAMPLE:<br>
     * ByteBuffer bb = getData();<br>
     * byte r, g, b;<br>
     * bb.rewind(); //<-- IMPORTANT!!<br>
     * try {<br>
     * while (true) {<br>
     * bb.mark();<br>
     * r = bb.get();<br>
     * g = bb.get();<br>
     * b = bb.get();<br>
     * bb.reset();<br>
     * bb.put((byte)(r+red));<br>
     * bb.put((byte)(g+green));<br>
     * bb.put((byte)(b+blue));<br>
     * bb.get(); // alpha<br> }<br> } catch (Exception e) {<br> }<br>
     * setData(bb);<br>
     *
     * @retun texture's pixel
     */
    public ByteBuffer getData();

    /**
     * sets the texture's pixel from a ByteBuffer
     *
     * EXAMPLE:<br>
     * ByteBuffer bb = getData();<br>
     * byte r, g, b;<br>
     * bb.rewind(); //<-- IMPORTANT!!<br>
     * try {<br>
     * while (true) {<br>
     * bb.mark();<br>
     * r = bb.get();<br>
     * g = bb.get();<br>
     * b = bb.get();<br>
     * bb.reset();<br>
     * bb.put((byte)(r+red));<br>
     * bb.put((byte)(g+green));<br>
     * bb.put((byte)(b+blue));<br>
     * bb.get(); // alpha<br> }<br> } catch (Exception e) {<br> }<br>
     * setData(bb);<br>
     *
     * @param data
     *            texture's pixel data
     */
    public void setData(ByteBuffer data);

    // public ITexture getAlphaMask();
    // public void setAlphaMask(ITexture alphaMask);
    /**
     * Pixel collision mask of texture
     * @param mask
     */
    public void setMask(Mask mask);

    /**
     * sets the vectorial mask of collisions
     *
     * @param mask
     *            the vectorial mask of collisions to set
     */
    public void setPolyMask(PolygonMask polyMask);

    /**
     * sets the specified alpha for each pixel that it's equal to the red, green blue value specified
     * @param red color used in the test
     * @param green color used in the test
     * @param blue color used in the test
     * @param alpha the alpha color that will be setted to the pixel that pass the test
     */
    public void setAlphaForeach(int red, int green, int blue, int alpha);

    /**
     * sets transparent each pixel that it's equal to the red, green blue value specified
     * @param red color used in the test
     * @param green color used in the test
     * @param blue color used in the test
     */
    public void makeTransp(int red, int green, int blue);

    /**
     * indicates if the texture is solid or not.<br/>
     * a non solid texture is a texture that containe an alpha channel
     * @return if the texture is solid or not.
     */
    public boolean isSolid();
}

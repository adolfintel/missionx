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
 * package containing different utility.
 */
package org.easyway.utils;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.easyway.interfaces.base.ITexture;
import org.easyway.objects.texture.Texture;
import org.easyway.objects.texture.TextureID;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;
import org.lwjgl.opengl.GL11;

/**
 * class to simplify images management.
 * 
 * 
 * @author Daniele Paggi
 * @version 1
 * @javadoc Linda Ferrari
 */
public class ImageUtils {

    public static boolean USE_MIPMAP = true;

    private ImageUtils() {
    }

    // ----------------------------------------------------------------------
    // ----------------------------LOAD TEXTURE------------------------------
    // ----------------------------------------------------------------------

    public static ByteBuffer makeTransp(ByteBuffer data, int red, int green, int blue, int widthHW, int heightHW) {
        return makeTransp(data, red, green, blue, 0, widthHW, heightHW);
    }

    public static ByteBuffer makeTransp(ByteBuffer data, int red, int green, int blue, int alpha, int widthHW, int heightHW) {
        data.rewind();
        byte r, g, b;
        for (int y = 0; y < heightHW; ++y) {

            for (int x = 0; x < widthHW; ++x) {
                //data.mark();
                r = data.get(); // red
                g = data.get(); // green
                b = data.get(); // blue
                if (r == (byte) red && g == (byte) green && b == (byte) blue) { // test
                    //data.reset();
                    //data.putInt(0); // speed up
                    data.put((byte) alpha);
                    //data.put((byte) 0).put((byte) 0).put((byte) 0).put((byte) 0); // transparent
                } else {
                    data.get(); // ignore alpha
                }
            }
        }
        data.rewind();
        return data;
    }

    @Deprecated
    public static void setSmoothImageState(boolean value) {
        if (value) {
            if (TextureID.USE_MIPMAP) {
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
                        GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            } else {
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
                        GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            }
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
                    GL11.GL_LINEAR);
        } else {

            if (TextureID.USE_MIPMAP) {
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
                        GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_NEAREST);
            } else {
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
                        GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            }
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
                    GL11.GL_NEAREST);
        }
    }

    /**
     * removes mistakes which are present on some ruined gif or png .<br>
     * or problems you may have during the application of the blender-effect on
     * images with transparents parts.
     *
     * @param texture
     *            source texture
     * @param limit
     *            a value [in range of 0:1] that indicates the depure
     */
    public static void depureTexture(Texture texture, float limit) {
        ByteBuffer data = texture.getData();
        data.rewind();
        // for (int i=0; i<256;i++)
        // System.out.println("i"+i+" "+(byte)i);
        byte alpha;
        int ialpha;
        int ilimit = (int) (limit * 255);
        long tot = texture.getWidthHW() * texture.getHeightHW();
        for (int i = 0; i < tot; ++i) {
            data.mark();
            data.get();
            data.get();
            data.get();
            alpha = data.get();
            if (alpha < 0) {
                ialpha = 256 + alpha;
            } else {
                ialpha = (int) alpha;
            }
            if (ialpha < ilimit) { // trasparente?
                data.reset();
                data.put((byte) 0);
                data.put((byte) 0);
                data.put((byte) 0);
                data.put((byte) 0);
            }
        }
        texture.setData(data);
    }

    /**
     * Get the closest greater power of 2 to the fold number<br/>
     * This version check if the current computer's hardware support Not Power Of 2
     * texture: in this case this method will retourn the fold istead of the closest greater
     * power of 2 value.
     *
     * @param fold
     *            The target number
     * @return The power of 2
     */
    public static int getNextPowerOfTwoHW(int fold) {
        if (Core.supportNPTTexture()) {
            return fold;
        }
        int pow = 2;
        while (pow < fold) {
            pow += pow;
        }
        return pow;
    }

    /**
     * Get the closest greater power of 2 to the fold number
     *
     * @param fold
     *            The target number
     * @return The power of 2
     */
    public static int getNextPowerOfTwo(int fold) {
        int pow = 2;
        while (pow < fold) {
            pow += pow;
        }
        return pow;
    }


    public static void getScrenShot(ITexture image, boolean withAlpha) {
        image.bind();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
                GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
                GL11.GL_LINEAR);
        GL11.glCopyTexImage2D(GL11.GL_TEXTURE_2D, 0, withAlpha?GL11.GL_RGBA:GL11.GL_RGB, 0, 0,
                image.getWidthHW(), image.getHeightHW(), 0);
    }

    public static Texture getScreenShot() {
        Texture texture = new Texture(Core.getInstance().getWidth(),Core.getInstance().getHeight());
        texture.bind();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
                GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
                GL11.GL_LINEAR);
        GL11.glCopyTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, 0, 0,
                texture.getWidthHW(), texture.getHeightHW(), 0);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        texture.flipY();
        return texture;
    }

    /**
     * saves a texture on a png file
     *
     * @param texture
     *            the texture to save
     * @param path
     *            the file path
     */
    public static void savePngImage(Texture texture, String path) {
        saveImage(texture, path, "png");
    }
/**
     * saves a texture on a jpg file
     *
     * @param texture
     *            the texture to save
     * @param path
     *            the file path
     */
    public static void saveJpgImage(Texture texture, String path) {
        saveImage(texture, path, "jpg");
    }

    /**
     * saves a texture on a bmp file
     *
     * @param texture
     *            the texture to save
     * @param path
     *            the file path
     */
    public static void saveBmpImage(Texture texture, String path) {
        saveImage(texture, path, "bmp");
    }
    
    /**
     * saves a texture on a image file<br>
     * example: ImageUtils.saveImage( myTexture, "C:\image.png", "png" );
     *
     * @param texture
     *            the texture to save
     * @param path
     *            the file path
     * @param format
     *            the type of image, for example: "bmp"
     */
    public static void saveImage(Texture texture, String path, String format) {
        BufferedImage image = new BufferedImage(texture.getWidth(), texture.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = image.getRaster();

        ByteBuffer bb = texture.getData();
        bb.rewind();
        for (int y = 0; y < texture.getHeightHW(); ++y) {
            if (y >= texture.getHeight()) {
                break;
            }
            for (int x = 0; x < texture.getWidthHW(); ++x) {
                if (x >= texture.getWidth()) {
                    bb.get(); // r
                    bb.get(); // g
                    bb.get(); // b
                    bb.get(); // a
                    continue;
                }
                raster.setPixel(x, texture.getHeight() - 1 - y, new int[]{
                            bb.get(), bb.get(), bb.get()});
                bb.get(); // a
            }
        }
        try {
            ImageIO.write(image, "png", new File(path));
        } catch (IOException e) {
            Utility.error("can't save the image on file " + path,
                    "ImageUtils.saveImage", e);
        }
    }

    public static void freeSpace() {
        for (ITexture t : StaticRef.textures) {
            t.setData(null); // free memory
        }
    }
}

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
 */package org.easyway.objects.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;

import java.util.ArrayList;
import java.util.HashMap;
import org.easyway.interfaces.base.IDestroyable;
import org.easyway.interfaces.base.ITexture;
import org.easyway.objects.sprites2D.Mask;
import org.easyway.objects.sprites2D.PolygonMask;
import org.easyway.system.StaticRef;
import org.easyway.system.state.OpenGLState;
import org.easyway.utils.Utility;
import org.easyway.utils.ImageUtils;
import org.lwjgl.opengl.GL13;

/**
 * With this class you can use images in the game.<br>
 * <br>
 * example:<br>
 * ...<br>
 * Texture img = new Texture( "path" );<br>
 * new Sprite( img );<br>
 * ...<br>
 */
// @UEditor("Manage Textures")//
public class Texture implements IDestroyable, ITexture, Serializable {

    private static HashMap<String, Texture> textures = new HashMap<String, Texture>();
    /**
     * generated version
     */
    private static final long serialVersionUID = 7472363451408935314L;
    /**
     * indicates if all the texture will auto create the masks on load<br/>
     * The auto creation works only with the getTexture() methods
     */
    public static boolean autoCreateMask = false;
    /** indicates if the texture is destroyed or not */
    private boolean destroyed = false;
    /**
     * size of texture
     */
    protected int width, height;
    /**
     * internal texture coordinates<br>
     * it's used to get the max border of texture...
     */
    protected float xStart = 0, yStart = 0;
    /**
     * internal texture coordinates<br>
     * it's used to get the max border of texture...
     */
    public float xEnd = 1, yEnd = 1;
    /** the mask used in software collisions test */
    protected Mask mask;
    /** the mask used in vectorial collisions test */
    protected PolygonMask polyMask;
    /**
     * the class to manage the VRAM data
     */
    protected TextureID dataid;
    /**
     * the texture name
     */
    protected String name;
    /**
     * indicates if the texture is solid or not.
     * a not solid texture is a texture that contain an alpha channel
     */
    protected boolean solid;

    // ------------------------------------------------------
    public Texture(TextureID data, String name) {
        dataid = data;
        dataid.referenceCount++;
        solid = dataid.solid;
        width = data.width;
        height = data.height;
        xEnd = (float) width / (float) data.widthHW;
        yEnd = (float) height / (float) data.heightHW;
        this.name = name;
        if (name != null) {
            textures.put(name, this);
            StaticRef.textures.add(this);
        }
    }

    public Texture(BufferedImage bImage, String name) {
        this(new TextureID(new ImageData(bImage)), name);
    }

    /**
     * LOADS and crete a texture from a file<br>
     * 
     * @param file
     *            relative path
     */
    public Texture(String file) {
        this(new TextureID(file), file);
        setUseAlphaChannel(true);
    }

    /**
     * LOADS and crete a texture from a file<br>
     * 
     * @param file
     *            relative path
     * @param useAlphaChannel indicates if the image should use or not the alpha channel
     */
    public Texture(String file, boolean useAlphaChannel) {
        this(new TextureID(file), file);
        setUseAlphaChannel(useAlphaChannel);
    }

    /**
     * create a new empty texture.
     * 
     * @param width
     *            size of texture
     * @param height
     *            size of texture
     */
    public Texture(int width, int height, String name) {
        this(new TextureID(width, height), name);
    }

    /**
     * create a new empty texture.
     *
     * @param width
     *            size of texture
     * @param height
     *            size of texture
     */
    public Texture(int width, int height) {
        this(new TextureID(width, height), null);
    }

    /**
     * loads and create a texture from a file and cretes as trasparent the
     * section that have the color equal to the RGB valued spefied
     * 
     * @param file
     *            relative path
     * @param red
     *            red value to compare
     * @param green
     *            green value to compare
     * @param blue
     *            blue value to compare
     */
    public Texture(String file, int red, int green, int blue) {
        this(new TextureID(file, red, green, blue), file);
    }

    /**
     * creates a copy of an existent texture<br>
     * 
     * @param t
     *            original texture
     */
    public Texture(Texture t) {
        this(t.dataid, t.name + "(copy)");
        this.width = t.width;
        this.height = t.height;
        this.name = t.name;
        this.xStart = t.xStart;
        this.yStart = t.yStart;
        this.xEnd = t.xEnd;
        this.yEnd = t.yEnd;
        //this.dataid = t.dataid;
        this.solid = t.solid;
    }

    /**
     * creates an emptiy texture and adds it to the game engine's texture list
     * 
     */
    public Texture() {
    }

    // ------------------------------------------------------
    /**
     * Blinds the image
     */
    @Override
    public void bind() {
        bind(GL13.GL_TEXTURE0);
    }

    public void bind(int unit) {
        if (isDestroyed() || !isValid()) {
            return;
        }
        GL13.glActiveTexture(unit);
        dataid.bind();
        OpenGLState.enableAlphaTest();
    }

    // ------------------------------------------------------
    // ------------------------------------------------------
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
    // bisogna tener conto di u,v
    @Override
    public ByteBuffer getData() {
        return dataid.getData();
    }

    /**
     * sets the texture's pixel from a ByteBuffer
     * 
     * EXAMPLE:<br>
     * ByteBuffer bb = getData();<br/>
     * byte r, g, b;<br>
     * bb.rewind(); //<-- IMPORTANT!!<br/>
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
    @Override
    public void setData(ByteBuffer data) {
        dataid.setData(data);
    }

    public TextureID getTextureId() {
        return dataid;
    }

    // ------------------------------------------------------
    // ------------------------------------------------------
    // ------------------------------------------------------
    /**
     * indicates if the texture has a mask of collisions or not
     */
    public boolean isCollisionable() {
        if (mask != null) {
            return true;
        }
        return false;
    }

    /**
     * returns the mask of collisions
     * 
     * @return mask of collisions
     */
    @Override
    public Mask getMask() {
        return mask;
    }

    /**
     * returns the vectorial mask of collisions
     *
     * @return vectorial mask of collisions
     */
    @Override
    public PolygonMask getPolyMask() {
        return polyMask;
    }

    /**
     * sets the mask of collisions
     * 
     * @param mask
     *            the mask of collisions to set
     */
    @Override
    public void setMask(Mask mask) {
        this.mask = mask;
    }

    /**
     * sets the vectorial mask of collisions
     *
     * @param mask
     *            the vectorial mask of collisions to set
     */
    @Override
    public void setPolyMask(PolygonMask mask) {
        this.polyMask = mask;
    }

    /**
     * creates the mask of collisions<br>
     * 
     */
    public void createMask() {
        new Mask(this);
    }

    // ------------------------------------------------------
    /**
     * destroys the image and release all resources
     */
    @Override
    public void destroy() {
        if (destroyed) {
            return;
        }
        System.out.println("image destroyed");
        if (dataid != null) {
            if (--dataid.referenceCount == 0) {
                dataid.destroy();
            }
        }
        StaticRef.textures.remove(this);
        destroyed = true;
    }

    // public void finalize() {
    // destroyed = true;
    // if (OpenGLState.lastTextureID == getID()) {
    // OpenGLState.lastTextureID = -1;
    // }
    // }
    /**
     * returns if the texture is destroyed or not
     * 
     */
    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    // ------------------------------------------------------
    public Texture[] split(int xOffset, int yOffset, int row, int coloumn, int width, int height, int spaceX, int spaceY) {
        Texture[] temp = new Texture[row * coloumn];
        for (int y = 0; y < row; ++y) {
            for (int x = 0; x < coloumn; ++x) {
                temp[x + y * coloumn] = new Texture(getTextureId(), name + "_" + row + "_" + coloumn);
                temp[x + y * coloumn].setRegion(xOffset + x * width + spaceX * x, yOffset + y * height + spaceY * y, width, height);
            }
        }
        return temp;
    }

    public Texture[][] split2D(int xstep[], int ystep[]) {
        if (xstep == null || ystep == null) {
            return null;
        }
        Texture texts[][] = new Texture[xstep.length][ystep.length];
        Texture ttext;

        float oldx, oldy, newW, newH;

        oldx = oldy = newH = 0;
        for (int y = 0; y < ystep.length; ++y) {
            oldy += newH;
            newH = (float) ystep[y] / (float) getHeightHW();
            oldx = 0;
            for (int x = 0; x < xstep.length; ++x) {
                newW = (float) xstep[x] / (float) getWidthHW();

                ttext = texts[x][y] = new Texture(this);

                ttext.width = xstep[x];
                ttext.height = ystep[y];
                ttext.xStart = oldx;
                ttext.xEnd = (oldx += newW);
                ttext.yStart = oldy;
                ttext.yEnd = oldy + newH;
            }
        }
        return texts;
    }

    @Override
    public int getWidthHW() {
        return dataid.widthHW;
    }

    @Override
    public int getHeightHW() {
        return dataid.heightHW;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getID() {
        return dataid.id;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public float getXEnd() {
        return xEnd;
    }

    @Override
    public float getXStart() {
        return xStart;
    }

    @Override
    public float getYEnd() {
        return yEnd;
    }

    @Override
    public float getYStart() {
        return yStart;
    }

    @Override
    public void setRegion(int x, int y, int width, int height) {
        if (x < 0 || x > getWidthHW()) {
            Utility.error("input error value: x", new Exception("error input value: x "+x));
            return;
        }
        if (y < 0 || y > getHeightHW()) {
            Utility.error("input error value: y", new Exception("error input value: y "+y));
            return;
        }
        if (width <= 0) {
            Utility.error("input error value: width", new Exception("error input value: width "+width));
            return;
        }
        if (height <= 0) {
            Utility.error("input error value: height", new Exception("error input value: height "+height));
            return;
        }
        if (width + x > getWidthHW()) {
            width = getWidthHW() - x;
        }
        if (height > getHeightHW()) {
            height = getHeightHW() - y;
        }

        xStart = (float) x / (float) getWidthHW();
        yStart = (float) y / (float) getHeightHW();
        xEnd = (float) (x + width) / (float) getWidthHW();
        yEnd = (float) (y + height) / (float) getHeightHW();
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean isSolid() {
        return solid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            return;
        }
        if (name.equals(this.name)) {
            //this.name = name;
            if (!textures.containsKey(name)) {
                textures.put(name, this);
            }
            return;
        }

        if (textures.containsKey(name)) {
            throw new TextureNameAlreadyInUseException(name);
        }
        if (textures.containsKey(this.name)) {
            textures.remove(this.name);
        }
        this.name = name;
        textures.put(name, this);
    }

    /**
     * indicates if the texture contains the alpha channel or not
     * @param value if true, the image will use the alpha channel
     */
    public void setUseAlphaChannel(boolean value) {
        dataid.solid = solid = !value;
    }

    /**
     * indicates if the image will use the alpha channel or note
     * @return if the image will use the alpha channel or note
     */
    public boolean getUseAlphaChannel() {
        return !solid;
    }

    @Override
    public void setAlphaForeach(int red, int green, int blue, int alpha) {
        if (getTextureId().getImageData() != null) {
            getTextureId().getImageData().makeTransp((byte) red, (byte) green, (byte) blue, (byte) alpha);
        } else {
            setData(ImageUtils.makeTransp(getData(), red, green, blue, alpha, getWidthHW(), getHeightHW()));
        }
        AlphaColorIndex alphaColorIndex = new AlphaColorIndex(red, green, blue, alpha);
        if (!dataid.alphaList.contains(alphaColorIndex)) {
            dataid.alphaList.add(alphaColorIndex);
        }

    }

    @Override
    public void makeTransp(int red, int green, int blue) {
        setAlphaForeach(red, green, blue, 0);
    }

    public void setCustomizedTexture() {
        dataid.pathFileName = null;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean isValid() {
        return dataid != null;
    }

    /**
     * gets a texture from it's name; If the texture isn't already loaded this method
     * will load it.
     * @param name the name of texture
     * @return returns the texture from the given name
     */
    public static Texture getTexture(String name) {
        if (textures.containsKey(name)) {
            return textures.get(name);
        }
        try {
            Texture t = new Texture(name);
            if (autoCreateMask /*&& t.getWidthHW()<=800 && t.getHeightHW() <= 800*/) {
                t.createMask();
            }
            textures.put(name, t);
            return t;
        } catch (TextureNotFoundException exp) {
            return null;
        }
    }

    public static void clearTextures() {
        textures.clear();
    }

    private void readVersion3(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {

        if (textures.containsKey(name)) {
            System.out.println("ERROR: Texture's name already loaded");
        } else {
            textures.put(name, this);
        }

        // fields:
        // there is the pathFileName field?
        boolean fromFile = in.readBoolean();
        if (fromFile) {
            // use file
            String path = (String) in.readObject();
            boolean useAlpha = !in.readBoolean();

            xStart = in.readFloat();
            xEnd = in.readFloat();
            yStart = in.readFloat();
            yEnd = in.readFloat();

            System.out.println("path: " + path);
            //Texture t = Texture.getTexture(path);
            dataid = new TextureID(path); // <----
            dataid.referenceCount++;

            if (useAlpha) {
                ArrayList<AlphaColorIndex> alphaColors = (ArrayList<AlphaColorIndex>) in.readObject();

                AlphaColorIndex alphaColor;
                for (int i = 0; i < alphaColors.size(); ++i) {
                    alphaColor = alphaColors.get(i);
                    makeTransp(alphaColor.red, alphaColor.green, alphaColor.blue);
                }
            }
            //new RuntimeException().printStackTrace();

        } else {
            System.out.println("Loading runtime customized texture");
            dataid = (TextureID) in.readObject();
        }

        //boolean useMask = in.readBoolean();
        if (in.readBoolean()) {
            mask = (Mask) in.readObject();
        }
    }

    private void readObject(java.io.ObjectInputStream in)
            throws java.io.IOException, ClassNotFoundException {

        try {
            // read the version (at this time it's only 1)
            int version = in.readInt();
            name = (String) in.readObject();
            if (version < 3) {
                Texture t = Texture.getTexture(name);
                width = t.width;
                height = t.height;
                mask = t.mask;
                dataid = t.dataid;
                xStart = t.xStart;
                xEnd = t.xEnd;
                yStart = t.yStart;
                yEnd = t.yEnd;
                if (textures.containsKey(name)) {
                    System.out.println("WARNING: Texture's name already loaded: " + name);
                    if (getTexture(name).equals(this)) {
                        System.out.println("-- ERROR: Redondant texture: " + name);
                        dataid = null; // not valid texture :)
                        return;
                    } else {
                        name = name + "_loaded";
                        textures.put(name, this);
                    }

                } else {
                    textures.put(name, this);
                }
                StaticRef.textures.add(this);
                return;
            } else if (version == 3) {
                readVersion3(in);
                StaticRef.textures.add(this);
                return;
            } else {
                xStart = in.readFloat();
                xEnd = in.readFloat();
                yStart = in.readFloat();
                yEnd = in.readFloat();
                width = in.readInt();
                height = in.readInt();
                solid = in.readBoolean();
                if (in.readBoolean()) {
                    mask = (Mask) in.readObject();
                }

                dataid = (TextureID) in.readObject();
                if (textures.containsKey(name)) {
                    System.out.println("WARNING: Texture's name already loaded: " + name);
                    if (getTexture(name).equals(this)) {
                        System.out.println("-- ERROR: Redondant texture: " + name);
                        dataid = null; // not valid texture :)
                        return;
                    } else {
                        name = name + "_loaded";
                        textures.put(name, this);
                    }

                } else {
                    textures.put(name, this);
                }
                StaticRef.textures.add(this);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // in.close();
            e.printStackTrace();
        }
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws java.io.IOException {

        try {
            // the version-id
            out.writeInt(4);
            out.writeObject(name);

            out.writeFloat(xStart);
            out.writeFloat(xEnd);
            out.writeFloat(yStart);
            out.writeFloat(yEnd);
            out.writeInt(width);
            out.writeInt(height);
            out.writeBoolean(solid);

            if (mask != null) {
                out.writeBoolean(true);
                out.writeObject(mask);
            } else {
                out.writeBoolean(false);
            }
            out.writeObject(dataid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean equals(Texture other) {
        return other.xStart == xStart &&
                other.xEnd == xEnd &&
                other.yStart == yStart &&
                other.yEnd == yEnd &&
                other.width == width &&
                other.height == height &&
                //other.mask == mask &&
                other.solid == solid &&
                (dataid != null && other.dataid != null ? //
                (other.dataid.pathFileName != null && dataid.pathFileName != null ? //
                other.dataid.pathFileName.equals(dataid.pathFileName)
                : true)
                : true);
    }

    @Override
    protected void finalize() throws Throwable {
        if (!isDestroyed()) {
            destroy();
        }
        super.finalize();
    }

    public void flipY() {
        float swap = yEnd;
        yEnd = yStart;
        yStart = swap;
    }

    public void flipX() {
        float swap = xEnd;
        xEnd = xStart;
        xStart = swap;
    }
}

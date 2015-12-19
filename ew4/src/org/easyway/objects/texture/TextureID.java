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
package org.easyway.objects.texture;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.easyway.interfaces.base.IDestroyable;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;
import org.easyway.system.state.OpenGLState;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

public class TextureID implements IDestroyable, Serializable {

    private static final long serialVersionUID = 4409253583065563738L;
    transient protected ImageData data;
    protected int width;
    protected int height;
    protected int widthHW;
    protected int heightHW;
    //protected int format;
    protected boolean solid;
    transient protected IntBuffer idBuffer;
    transient protected int id = -1;
    public static boolean USE_MIPMAP = false;
    public static boolean FREE_MEMORY = true;
    int referenceCount = 0;
    /**
     * list of the transparent Component of the image
     */
    ArrayList<AlphaColorIndex> alphaList = new ArrayList<AlphaColorIndex>();
    /**
     * the filename from where I've loaded the TextureID
     */
    protected String pathFileName;

    protected TextureID() {
    }

    public TextureID(int width, int height) {
        data = new ImageData(width, height);
        createTexture();
    }

    public TextureID(ImageData image) {
        data = image;
        createTexture();
    }

    public TextureID(String path, int red, int green, int blue) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        int index;
        // path = path.replaceAll("\\", "/");
        while ((index = path.indexOf("\\")) != -1) {
            path = path.substring(0, index) + '/' + path.substring(index + 1);
        }

        (data = new ImageData(path)).makeTransp((byte) red, (byte) green,
                (byte) blue);
        alphaList.add(new AlphaColorIndex(red, green, blue, 0));
        pathFileName = path;
        createTexture();
    }

    public ImageData getImageData() {
        return data;
    }

    public void setImageData(ImageData data) {
        this.data = data;
    }

    public TextureID(String path) {
        data = new ImageData(path);
        pathFileName = path;
        createTexture();
    }

    private void createTexture() {
        width = data.width;
        height = data.height;
        widthHW = data.widthHW;
        heightHW = data.heightHW;
        //format = data.format;
        solid = data.solid;
        generateHwId();
    }

    private void generateHwId() {
        if (Thread.currentThread() != StaticRef.getRenderThread()) {
            if (Core.getInstance() != null) {
                return;
            }
        }
        // generate the ID
        idBuffer = BufferUtils.createIntBuffer(4);
        //System.out.println("idBuffer: " + idBuffer);
        GL11.glGenTextures(idBuffer);
        id = idBuffer.get(0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, OpenGLState.lastTextureID = id);
        // Filtering
        if (USE_MIPMAP) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
                    GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        } else {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
                    GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        }
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
                GL11.GL_LINEAR);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
                GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
                GL12.GL_CLAMP_TO_EDGE);
        // Generate The Texture - NEW
        if (USE_MIPMAP) {
            generateMipmap(data.data);
        } else {
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, widthHW, heightHW,
                    0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.data);
        }
        if (FREE_MEMORY) {
            data.data = null;
            data = null;
        }
    }

    /**
     * generates the mipmap of the texture
     */
    private void generateMipmap(ByteBuffer data) {
        data.rewind();
        if (Core.getGLMajorVersion() >= 3) {
            // GPU MipMap generation
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, widthHW, heightHW,
                    0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        } else if (Core.getGLMajorVersion() >= 2) {
            // GPU MipMap generation
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, widthHW, heightHW,
                    0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
        } else {
            // Should be Opengl version 1.1 (or less)
            // CPU MipMap generation
            GLU.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D, GL11.GL_RGBA, widthHW,
                    heightHW, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
        }
    }

    //@Deprecated
    public TextureID(TextureID source) {
        // TODO
        ByteBuffer buffer = source.getData();
        data = new ImageData(source, buffer);
        createTexture();
    }

    // ------------------------------------------------------------
    // ---------------------------USERS----------------------------
    // ------------------------------------------------------------
    /**
     * binds the current texture
     */
    public void bind() {
        if (id == -1) {
            generateHwId();
        }
        if (id != OpenGLState.lastTextureID) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
            OpenGLState.lastTextureID = id;
        }
    }

    /**
     * free memory space
     *
     */
    public void freeMemory() {
        // removed for speedup GC
        // if (data != null)
        // data.data = null;
        data = null;
    }

// ------------------------------------------------------------
// ---------------------------DATA-----------------------------
// ------------------------------------------------------------
    /**
     * if the data is null will be free the memory from the RAM but not from the
     * VRAM
     */
    public void setData(ByteBuffer bdata) {
        if (bdata == null) {
            freeMemory();
            return;
        }
        bind();
        bdata.rewind();
        //if (format == ImageType.RGBA) {
        GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, widthHW,
                heightHW, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bdata);
        if (data != null) {
            data.data = bdata;
        }

        if (USE_MIPMAP) {
            generateMipmap(bdata);
        }

        /*} else {
        assert format == ImageType.RGB; // RGB
        GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, widthHW,
        heightHW, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, bdata);

        if (data != null) {
        data.data = bdata;
        }

        if (USE_MIPMAP) {
        //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        //GLU.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D, GL11.GL_RGB, widthHW,
        //heightHW, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, bdata);
        }

        }*/
    }

    public ByteBuffer getData() {
        if (data != null && data.data != null) {
            return data.data;
        }
        bind();
        /*if (format == ImageType.RGB) {
        ByteBuffer bb = ByteBuffer.allocateDirect(heightHW * widthHW * 3);
        GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB,
        GL11.GL_UNSIGNED_BYTE, bb);
        return bb;
        } else { // RGBA
        assert format == ImageType.RGBA;*/
        ByteBuffer bb = ByteBuffer.allocateDirect(heightHW * widthHW * 4);
        GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA,
                GL11.GL_UNSIGNED_BYTE, bb);
        return bb;
        //}

    }

    // ------------------------------------------------------------
    // --------------------------DESTROY---------------------------
    // ------------------------------------------------------------
    public void destroy() {
        if (id == -1) {
            return;
        }

        idBuffer.rewind();
        GL11.glDeleteTextures(idBuffer);
        // idBuffer = null;
        id = -1;
    }

    @Override
    public void finalize() {
        if (id == -1) {
            return;
        }

        idBuffer.rewind();
        GL11.glDeleteTextures(idBuffer);
        id = -1;
        // removed for speed up the GC
        // idBuffer = null;

    }

    public boolean isDestroyed() {
        return id == -1;
    }

    public boolean isSolid() {
        return solid;
    }

    public String getPathFileName() {
        return pathFileName;
    }
// ------------------------------------- SERIALIZATION -----------

    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        boolean fromFile = pathFileName == null;
        if (!fromFile) {
            if (data == null) {
                data = new ImageData(this, getData());
            }
            s.writeBoolean(false);
            s.writeObject(data);
            s.defaultWriteObject();
        } else {
            s.writeBoolean(true);
            s.defaultWriteObject();
        }
    }

    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        boolean fromFile = s.readBoolean();
        if (!fromFile) {
            data = (ImageData) s.readObject();
            s.defaultReadObject();
        } else {
            data = new ImageData(pathFileName);
            s.defaultReadObject();
        }

        createTexture();

    }
}

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
package org.easyway.tiles;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.easyway.interfaces.base.ITexture;
import org.easyway.objects.BaseObject;
import org.easyway.objects.animo.Animo;
import org.easyway.objects.animo.AnimoData;
import org.easyway.objects.texture.Texture;
import org.easyway.system.state.OpenGLState;
import org.easyway.utils.Utility;
import org.lwjgl.opengl.GL11;

/**
 * this class rappresents the tile of the TileManager.<br>
 */
public class Tile extends BaseObject {

    /**
     * NOT IMPLEMENTED YET
     */
    private static final int NoMirror = 0x000;
    /**
     * NOT IMPLEMENTED YET
     */
    private static final int MirrorH = 0x001;
    /**
     * NOT IMPLEMENTED YET
     */
    private static final int MirrorV = 0x010;
    /**
     * NOT IMPLEMENTED YET
     */
    private static final int MirrorHV = MirrorH | MirrorV;
    private static final long serialVersionUID = -8893099952450072728L;
    /**
     * the tile image
     */
    public ITexture image;
    /**
     * animation
     */
    public Animo animo;
    /**
     * color component
     */
    public float red, green, blue, alpha;
    /**
     * mirror value<br>
     * NOT IMPLEMENTED YET
     */
    private int mirror;
    //volatile protected int id;

    /**
     * creates a new instance of Tile
     *
     * @param image
     *            the image of tile
     */
    public Tile(ITexture image) {
        super(false);
        this.image = image;
        red = 1.0f;
        green = 1.0f;
        blue = 1.0f;
        alpha = 1.0f;
        type = "$_TILE"; // 0.1.9
    }

    /**
     * creates a new instance of Tile
     *
     * @param image
     *            the image of tile type the name of the tile
     */
    public Tile(ITexture image, String type) {
        super(false);
        this.image = image;
        red = 1.0f;
        green = 1.0f;
        blue = 1.0f;
        alpha = 1.0f;
        this.type = type;
        this.name = type;
    }

    /**
     * draws the tile on screen
     *
     * @param x
     *            position of tile
     * @param y
     *            position of tile
     * @param width
     *            width of tile
     * @param height
     *            height of tile
     */
    public void render(float x, float y, float width, float height) {
        if (animo != null) {
            image = animo.get();
        }
        if (image == null) {
            return;
        }

        // if (alpha == 1.0f && GL11.glIsEnabled(GL11.GL_BLEND) &&
        // image.isSolid) {
        // GL11.glDisable(GL11.GL_BLEND);
        // } else if (!GL11.glIsEnabled(GL11.GL_BLEND) && !image.isSolid) {
        // GL11.glEnable(GL11.GL_BLEND);
        // }

        if (alpha != 1) {
            OpenGLState.enableBlending();
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        } else {
            OpenGLState.disableBlending();
        }

        GL11.glColor4f(red, green, blue, alpha);
        image.bind();
        // if (!GL11.glIsEnabled(GL11.GL_ALPHA_TEST)) {
        // System.out.println("CAZZO");
        // }
        // GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(image.getXStart(), image.getYStart());
        GL11.glVertex2f(x, y);

        GL11.glTexCoord2f(image.getXEnd(), image.getYStart());
        GL11.glVertex2f(x + width, y);

        GL11.glTexCoord2f(image.getXEnd(), image.getYEnd());
        GL11.glVertex2f(x + width, y + height);

        GL11.glTexCoord2f(image.getXStart(), image.getYEnd());
        GL11.glVertex2f(x, y + height);
        GL11.glEnd();
    }

    /**
     * NOT IMPLEMENTED YET
     */
    protected int getMirror() {
        return mirror;
    }

    /**
     * NOT IMPLEMENTED YET
     */
    protected boolean isMirroredH() {
        return (mirror & MirrorH) > 0;
    }

    /**
     * NOT IMPLEMENTED YET
     */
    protected boolean isMirroredV() {
        return (mirror & MirrorV) > 0;
    }

    /**
     * NOT IMPLEMENTED YET
     */
    protected boolean isMirrorerHV() {
        return (mirror & MirrorHV) > 0;
    }

    /**
     * NOT IMPLEMENTED YET
     */
    protected void setMirror(boolean H, boolean V) {
        mirror = NoMirror;
        if (H) {
            mirror |= MirrorH;
        }
        if (V) {
            mirror |= MirrorV;
        }
    }

    /**
     * returns the current image of the Tile
     *
     * @return
     */
    public ITexture getImage() {
        return image;
    }

    /**
     * changes the image of the tile<br>
     * note: the Animo has the priority
     *
     * @param image
     *            the new image
     */
    public void setImage(ITexture image) {
        this.image = image;
    }

    /**
     * returns the current animo associated to the Tile
     *
     * @return
     */
    public Animo getAnimo() {
        return animo;
    }

    /**
     * changes the animo<br>
     * If you want remove the animation you should set the animo to "null"
     *
     * @param animo
     *            the new animation or null
     */
    public void setAnimo(Animo animo) {
        this.animo = animo;
    }

    private void readObject(java.io.ObjectInputStream in)
            throws java.io.IOException, ClassNotFoundException {
        //readFromFile(in);
        in.defaultReadObject();
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws java.io.IOException, ClassNotFoundException {
        //writeOnFile(out);
        out.defaultWriteObject();
    }

    public static Tile readFromFile(ObjectInputStream in) {

        // ObjectInputStream in = Utility.getLocalFile(file);
        if (in == null) {
            return null;
        }

        try {
            Animo animo = null;
            Texture image = null;
            // read the version (at this time it's only 1)
            int version = in.readInt();

            boolean useAnimo = in.readBoolean();
            if (useAnimo) {
                animo = (Animo) in.readObject();
                //animo = new Animo(AnimoData.readFromFile(in));
                image = (Texture) animo.get(0);

                //animo.start();
            } else {
                image = (Texture) in.readObject();
                if (!image.isValid()) {
                    image = Texture.getTexture(image.getName());
                }
                // image = Texture.readFromFile(in);
            }
            // color fields
            float r = in.readFloat();
            float g = in.readFloat();
            float b = in.readFloat();
            // name
            String name = (String) in.readObject();

            Tile tile = new Tile(image, name);
            // set fields
            tile.setAnimo(animo);
            // tile.animo = animo;
            tile.red = r;
            tile.green = g;
            tile.blue = b;
            // in.close();
            return tile;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // in.close();
            e.printStackTrace();
        }
        return null;
    }

    public void writeOnFile(ObjectOutputStream out) {
        if (image == null && animo == null) {
            Utility.error("tile with a null image; What I should Write??",
                    new Exception("tile with null image and animo"));
            return;
        }

        try {
            // FileOutputStream fout = new FileOutputStream(filename);
            // ObjectOutputStream out = new ObjectOutputStream(fout);

            // the version-id
            out.writeInt(1);

            out.writeBoolean(animo != null);
            if (animo != null) {
                out.writeObject(animo);
                //animo.getData().writeOnFile(out);
            } else {
                out.writeObject(image);
                //((Texture) image).writeOnFile(out);
            }

            // color-fields
            out.writeFloat(red);
            out.writeFloat(green);
            out.writeFloat(blue);
            // name
            out.writeObject(getType().toString());

            // out.close();
            // fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        int index = getClass().getName().lastIndexOf(".") + 1;
        return "[ @" + getClass().getName().substring(index) + " Image: " + image + " Type: " + type + " Name: " + name + "]";

    }
}

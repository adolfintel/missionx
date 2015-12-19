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

import org.easyway.collisions.IHardwareCollisionable;
import org.easyway.interfaces.base.ITexture;
import org.easyway.interfaces.extended.IDrawing;
import org.easyway.interfaces.extended.ILayerID;
import org.easyway.objects.Camera;
import org.easyway.objects.Plain2D;
import org.easyway.objects.StateObject;
import org.easyway.objects.animo.Animo;
import org.easyway.objects.texture.Texture;
import org.easyway.shader.Shader;
import org.easyway.system.StaticRef;
import org.easyway.system.state.GameState;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

/**
 * A SimpleSprite is an Object that can moved and drawen.<br>
 * It can have a Texture applied on or an Animation.<br>
 * It can't have any collisions: for this you must use SimpleSpriteColl class or
 * SpriteClass.<br>
 * For a complex managment of Sprite I guess you to use the Sprite class.<br>
 * 
 * @see SimpleSpriteColl
 * @see Sprite
 * @see SpriteColl
 * @author Daniele Paggi
 * 
 */
public class SimpleSprite extends Plain2D implements IDrawing, ILayerID, IHardwareCollisionable,
        Serializable {

    // -----------------------------------------------------------------
    // --------------------VARIABLES------------------------------------
    // -----------------------------------------------------------------
    private static final long serialVersionUID = 833416315621548814L;
    public static int DEFAULT_ID_LAYER = 3;
    /** animation of sprite */
    public Animo animo;
    /** current image of sprite */
    transient public ITexture image;
    /** Depth of sprite */
    private int layer;
    /**
     * the drawing sheet
     */
    private int idLayer = -1;
    /** indicates if draw or not the sprite on screen */
    public boolean visible = true;
    /**
     * indicates if the sprite is fixed or not <br>
     * it's usually set to true to create the HUD
     */
    public boolean fixedOnScreen = false;
    /**
     * indicates the current state of the Object
     */
    protected StateObject state;
    /** the used shader in the rendering process */
    private Shader shader = Shader.getDefaultShader();

    // -----------------------------------------------------------------
    // ---------------------CONSTRUCTORS--------------------------------
    // -----------------------------------------------------------------
    public SimpleSprite(SimpleSprite spr) {
        super(spr);
        this.animo = spr.animo;
        // setImage(spr.image);
        this.image = spr.image;
        /*if (!quad && autoAddToLists) {
        setIdLayer(spr.idLayer);
        if (spr.layer != 0)
        setLayer(spr.layer);
        }*/
        this.visible = spr.visible;
        setFixedOnScreen(spr.fixedOnScreen);
    }

    public SimpleSprite() {
        this(true, 0, 0, null, null, 0, DEFAULT_ID_LAYER);
    }

    public SimpleSprite(boolean toAdd) {
        this(toAdd, 0, 0, null, null, 0, DEFAULT_ID_LAYER);
    }

    public SimpleSprite(float x, float y) {
        this(true, x, y, null, null, 0, DEFAULT_ID_LAYER);
    }

    public SimpleSprite(boolean toAdd, float x, float y) {
        this(toAdd, x, y, null, null, 0, DEFAULT_ID_LAYER);
    }

    public SimpleSprite(boolean toAdd, ITexture image) {
        this(toAdd, 0, 0, image, null, 0, DEFAULT_ID_LAYER);
    }

    public SimpleSprite(float x, float y, ITexture image) {
        this(true, x, y, image, null, 0, DEFAULT_ID_LAYER);
    }

    public SimpleSprite(float x, float y, ITexture image, int layer) {
        this(true, x, y, image, null, layer, DEFAULT_ID_LAYER);
    }

    public SimpleSprite(boolean toAdd, float x, float y, ITexture image, int layer) {
        this(toAdd, x, y, image, null, layer, DEFAULT_ID_LAYER);
    }

    public SimpleSprite(boolean toAdd, float x, float y, ITexture image) {
        this(toAdd, x, y, image, null, 0, DEFAULT_ID_LAYER);
    }

    public SimpleSprite(float x, float y, Animo animo) {
        this(true, x, y, null, animo, 0, DEFAULT_ID_LAYER);
    }

    public SimpleSprite(boolean toAdd, float x, float y, Animo animo) {
        this(toAdd, x, y, null, animo, 0, DEFAULT_ID_LAYER);
    }

    public SimpleSprite(ITexture image) {
        this(true, 0, 0, image, null, 0, DEFAULT_ID_LAYER);
    }

    public SimpleSprite(int layer) {
        this(true, 0, 0, null, null, layer, DEFAULT_ID_LAYER);
    }

    public SimpleSprite(int layer, int idLayer) {
        this(true, 0, 0, null, null, layer, idLayer);
    }

    public SimpleSprite(boolean toAdd, int layer) {
        this(toAdd, 0, 0, null, null, layer, DEFAULT_ID_LAYER);
    }

    public SimpleSprite(boolean toAdd, int layer, int idLayer) {
        this(toAdd, 0, 0, null, null, layer, idLayer);
    }

    /**
     * create a new instance of sprite
     *
     * @param autoAdd
     *            if it's 'true' the object will be aut-added to the game
     *            engine's lists.
     * @param x
     *            x cartesian coordinate
     * @param y
     *            y cartesian coordiante
     * @param img
     *            the image of sprite
     * @param animo
     *              the animation of sprite
     * @param layer
     *            the depth of sprite
     * @param idLayer
     *            the id of Drawing-layer (drawing sheet)
     */
    public SimpleSprite(boolean autoAdd, float x, float y, ITexture img, Animo animo,
            int layer, int idLayer) {
        super(autoAdd, x, y, 0, 0);
        if (autoActivateTest() && autoAdd) {
            setIdLayer(idLayer);
        }
        setLayer(layer);
        setImage(img);
        setAnimo(animo);
        type = "$_SIMPLE-SPRITE";
    }

    // -----------------------------------------------------------------
    // ---------------------DRAWING-------------------------------------
    // -----------------------------------------------------------------
    @Override
    public void render() {
        if (animo != null) {
            setImage(animo.get());// .getCurrentImage();
        }
        if (getImage() == null) {
            return;
        }
        if (!visible) {
            return;
        }
        if (getWidth() <= 0 || getHeight() <= 0) {
            return;
        }
        Camera camera = StaticRef.getCamera();


        GL11.glColor4f(1, 1, 1, 1);

        if (fixedOnScreen) {
            float oldZoom = camera.getZoom2D();
            camera.setZoom2D(1.0f);
            drawIt();
            camera.setZoom2D(oldZoom);
        } else {
            drawIt();
        }

    }

    protected void drawIt() {

        float nx = getXOnScreen();
        float ny = getYOnScreen();
        final Camera camera = Camera.getCurrentCamera();

        if (nx < camera.getWidth() && ny < camera.getHeight() && (nx + getWidth()) > 0 && (ny + getHeight()) > 0) {
            image.bind();
            if (shader != null) {
                shader.bind();
                shader.update(this);
            }
            drawPlain(nx, ny);
            camera.addObjectsOnScreen(this);
        }
    }

    /** draw a dinamic plain */
    protected void drawPlain(float x, float y) {
        GL11.glBegin(GL11.GL_QUADS); // Draw A Quad
        GL11.glTexCoord2f(image.getXStart(), image.getYStart());
        GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(image.getXEnd(), image.getYStart());
        GL11.glVertex2f(x + getWidth(), y);
        GL11.glTexCoord2f(image.getXEnd(), image.getYEnd());
        GL11.glVertex2f(x + getWidth(), y + getHeight());
        GL11.glTexCoord2f(image.getXStart(), image.getYEnd());
        GL11.glVertex2f(x, y + getHeight());
        GL11.glEnd();
    }

    // -----------------------------------------------------------------
    // ----------------------USER METHODS-------------------------------
    // -----------------------------------------------------------------
    /** sets the depth */
    public void setLayer(int layer) {
        if (this.layer != layer) {
            this.layer = layer;
            readdToDrawingLists();
        }
    }

    @Override
    public int getLayer() {
        return layer;
    }

    public void setState(StateObject state) {
        this.state = state;
        if (state != null) {
            state.setObject(this);
        }
    }

    public StateObject getState() {
        return state;
    }

    // -----------------------------------------------------------------
    // ----------------------- DESTROY -------------------------------
    // -----------------------------------------------------------------
    @Override
    public void destroy() {
        if (isDestroyed()) {
            return;
        }
        super.destroy();
        visible = false;
        image = null;
        animo = null;
    }

    // public boolean isDestroyed() {
    // return destroyed;
    // }
    // public void onDestroy() {
    // }
    // -----------------------------------------------------------------
    // ---------------------------- PLAIN ------------------------------
    // -----------------------------------------------------------------
    @Override
    public float getScaleX() {
        if (image != null) {
            return (float) this.getWidth() / (float) image.getWidth();
        }
        return 0f;
    }

    @Override
    public float getScaleY() {
        if (image != null) {
            return (float) this.getHeight() / (float) image.getHeight();
        }
        return 0f;
    }

    public void setScaleX(float scalex) {
        if (image != null && scalex >= 0) {
            this.setWidth((int) (image.getWidth() * scalex));
        }
    }

    public void setScaleY(float scaley) {
        if (image != null && scaley >= 0) {
            this.setHeight((int) (image.getHeight() * scaley));
        }
    }

    /**
     * returns the x coordinate relative to WORLD
     *
     */
    @Override
    public float getX() {
        if (fixedOnScreen) {
            return super.getX() + StaticRef.getCamera().getX();
        } else {
            return super.getX();
        }
    }

    /**
     * returns the y coordinate relative to WORLD
     *
     */
    @Override
    public float getY() {
        if (fixedOnScreen) {
            return super.getY() + StaticRef.getCamera().getY();
        } else {
            return super.getY();
        }
    }

    /**
     * returns the x coordinate relative to SCREEN
     *
     */
    @Override
    public float getXOnScreen() {
        if (fixedOnScreen) {
            return super.getX();
        } else {
            return (super.getX() - StaticRef.getCamera().getX());
        }
    }

    /**
     * returns the y coordinate relative to SCREEN
     *
     */
    @Override
    public float getYOnScreen() {
        if (fixedOnScreen) {
            return super.getY();
        } else {
            return (super.getY() - StaticRef.getCamera().getY());
        }
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
        } else if (id >= GameState.getCurrentGEState().getLayers().length) {
            id = GameState.getCurrentGEState().getLayers().length - 1;
        }
        idLayer = id;
        GameState.getCurrentGEState().getLayers()[idLayer].add(this);
    }

    /**
     * gets the animo associated to the object
     *
     * @return the animo
     */
    public Animo getAnimo() {
        return animo;
    }

    /**
     * sets the animo associated to the object
     *
     * @param animo
     *            the animo to assign
     */
    // TODO: @Method("set Animo") //
    public void setAnimo(Animo animo) {
        this.animo = animo;
        if (animo != null && animo.get() != null) {
            setImage(animo.get());
        }
    }

    /**
     * sets if the object is fixed or not to screen<br>
     * a fixed object have the x,y coordinates relatives to the monitor: not to
     * the world's coordinates
     *
     * @param fixedOnScreen
     */
    public void setFixedOnScreen(boolean fixedOnScreen) {
        this.fixedOnScreen = fixedOnScreen;
    }

    /**
     * returns if the object is fixed or not to screen<br>
     * a fixed object have the x,y coordinates relatives to the monitor: not to
     * the world's coordinates
     */
    public boolean isFixedOnScreen() {
        return fixedOnScreen;
    }

    /**
     * returns the current image of the object
     *
     * @return
     */
    public ITexture getImage() {
        return image;
    }

    /**
     * changes the current image of the object
     *
     * @param image
     */
    public void setImage(ITexture image) {
        if (image != null) {
            if (this.image == null || getWidth() == 0 || getHeight() == 0) {
                setSize(image.getWidth(), image.getHeight());
            }
        }
        this.image = image;
        addQuadsUpdate();
    }

    public void setImage(String imagePath) {
        setImage(Texture.getTexture(imagePath));
    }

    protected void loopState() {
        if (state != null) {
            state.loop();
        }
    }

    protected void finalLoopState() {
        if (state != null) {
            state.finalLoop();
        }
    }

    protected void onCollisionState() {
        if (state != null) {
            state.onCollision();
        }
    }

    @Override
    public SimpleSprite clone() {
        return new SimpleSprite(this);
    }

    @Override
    public String getType() {
        return super.getType();
    }

    @Override
    public String toString() {
        return "[ @Sprite Type: " + getType() + " Name: " + getName() + " Image: " + getImage() + " ]";
    }

// ------------------------------------- SERIALIZATION -----------
    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        s.writeObject(image);
        s.defaultWriteObject();

    }

    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        image = (ITexture) s.readObject();
        if (image instanceof Texture) {
            Texture texture = (Texture) image;
            if (!texture.isValid()) {
                image = Texture.getTexture(texture.getName());
            }
        }
        s.defaultReadObject();

    }

    public void renderAt(float nx, float ny) {
        renderAt(nx, ny, 0, GL13.GL_TEXTURE0);
    }

    @Override
    public void renderAt(float nx, float ny, float nz, int unit) {
        //Camera camera = StaticRef.getCamera();

        //if (nx < camera.getWidth() && ny < camera.getHeight() && (nx + getWidth()) > 0 && (ny + getHeight()) > 0) {
        image.bind(unit);
        if (shader != null) {
            shader.bind();
            shader.update(this);
        }
        GL11.glTranslatef(0, 0, nz);
        drawPlain(nx, ny);
        //}
    }
}

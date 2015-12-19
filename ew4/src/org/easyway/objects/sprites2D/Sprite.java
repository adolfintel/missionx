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
import org.easyway.interfaces.base.ITexture;
import org.easyway.interfaces.sprites.IRotation;
import org.easyway.objects.Camera;
import org.easyway.objects.animo.Animo;
import org.easyway.shader.Shader;
import org.easyway.shader.ShaderEffect;
import org.easyway.system.state.OpenGLState;
import org.lwjgl.opengl.GL11;

/**
 * A Sprite is an Object that can moved and drown.<br>
 * It can have a Texture2 applied on or an Animation.<br>
 * It can't have any collisions: for this you must use SimpleSpriteColl class or
 * SpriteClass.<br>
 * It can be saturated, or some other effects.<br>
 * 
 * @see SpriteColl
 * @see SimpleSprite
 * @see SimpleSpriteColl
 * @author Daniele Paggi
 * 
 */
public class Sprite extends SimpleSprite implements Serializable, IRotation {

    private static final long serialVersionUID = -5617254063364952192L;
    // -----------------------------------------------------------------
    // --------------------VARIABLES------------------------------------
    // -----------------------------------------------------------------
    /**
     * dynamic image <br>
     * if not enabled, the image is fastest draws on screen <br>
     * but you can't resize the image.<br>
     * {From the last game engine changes I haven't tested this function yet}
     */
    // private boolean isStatic = false; // ?
    /** static object to draw on screen */
    // private StaticObject staticObj = null; // I think that will not more use
    // TODO: comment
    /** animo of sprite */
    // public Animo animo;
    /** current image of sprite */
    // protected Texture2 image;
    /** indicates if at change image the dimensions are updated */
    public boolean autoReSizeDimensions = false;
    /** colors used to create the saturation */
    protected float colorRed = 1, colorGreen = 1, colorBlue = 1, colorAlpha = 1;
    /**
     * indicates if the sprite will use the alpha channel or not
     */
    protected boolean useAlphaChannel;
    /** coordinates of object in the game world */
    // public float z;
    /** coordiates of drawing relative of image */
    //protected int cx = 0, cy = 0;
    /** indicates if the image will be smooth or pixelated */
    protected boolean smoothImage = true;
    /** the used shader in the rendering process */
    //private Shader shader = Shader.getDefaultShader();
    /** the used shader effect in the rendering process */
    private ShaderEffect effect = ShaderEffect.getDefaultShaderEffect();

    public Sprite(boolean autoAdd, float x, float y, ITexture img, Animo animo, int layer, int idLayer) {
        super(autoAdd, x, y, img, animo, layer, idLayer);
    }

    public Sprite(boolean toAdd, int layer, int idLayer) {
        super(toAdd, layer, idLayer);
    }

    public Sprite(boolean toAdd, int layer) {
        super(toAdd, layer);
    }

    public Sprite(int layer, int idLayer) {
        super(layer, idLayer);
    }

    public Sprite(int layer) {
        super(layer);
    }

    public Sprite(ITexture image) {
        super(image);
    }

    public Sprite(boolean toAdd, float x, float y, Animo animo) {
        super(toAdd, x, y, animo);
    }

    public Sprite(float x, float y, Animo animo) {
        super(x, y, animo);
    }

    public Sprite(boolean toAdd, float x, float y, ITexture image) {
        super(toAdd, x, y, image);
    }

    public Sprite(boolean toAdd, float x, float y, ITexture image, int layer) {
        super(toAdd, x, y, image, layer);
    }

    public Sprite(float x, float y, ITexture image, int layer) {
        super(x, y, image, layer);
    }

    public Sprite(float x, float y, ITexture image) {
        super(x, y, image);
    }

    public Sprite(boolean toAdd, ITexture image) {
        super(toAdd, image);
    }

    public Sprite(boolean toAdd, float x, float y) {
        super(toAdd, x, y);
    }

    public Sprite(float x, float y) {
        super(x, y);
    }

    public Sprite(boolean toAdd) {
        super(toAdd);
    }

    public Sprite() {
    }

    public Sprite(SimpleSprite spr) {
        super(spr);
    }

    // -----------------------------------------------------------------
    // ---------------------CONSTRUCTORS--------------------------------
    // -----------------------------------------------------------------
    public Sprite(Sprite obj) {
        super(obj);
        this.effect = obj.effect;
        this.smoothImage = obj.smoothImage;
        this.useAlphaChannel = obj.useAlphaChannel;
        //anglez = obj.anglez;
        autoReSizeDimensions = obj.autoReSizeDimensions;
        setRGBA(obj.colorRed, obj.colorGreen, obj.colorBlue, obj.colorAlpha);
    }

    // -----------------------------------------------------------------
    // ---------------------DRAWING-------------------------------------
    // -----------------------------------------------------------------
    public void configureShader(Shader shader) {
    }

    @Override
    public void render() {
        if (animo != null) {
            setImage(animo.get());// .getCurrentImage());
        }
        if (image == null) {
            return;
        }

        // not visible
        if (!visible) {
            return;
        }
        // not visible
        if (getWidth() <= 0 || getHeight() <= 0) {
            return;
        }
        // not visible
        if (colorAlpha <= 0) {
            return;
        }

        final Camera camera = Camera.getCurrentCamera();


        if (fixedOnScreen) {
            final float oldZoom = camera.getZoom2D();
            camera.setZoom2D(1.0f);
            drawIt();
            camera.setZoom2D(oldZoom);
        } else {
            drawIt();
        }

    }

    protected void customDraw() {
        if (effect != null) {
            for (Shader shader : effect) {
                if (shader != null) {
                    shader.bind();
                    shader.update(this);
                }
                draw();
            }
        } else {
            draw();
        }
    }

    final protected void draw() {
        GL11.glLoadIdentity();
        GL11.glTranslatef(getXOnScreen() + getWidth() / 2, getYOnScreen() + getHeight() / 2, 0);
        GL11.glRotatef(getRotation(), 0, 0, 1);
        drawPlainCenter();
        GL11.glLoadIdentity();
    }

    @Override
    final protected void drawIt() {
        float nx = getXOnScreen();
        float ny = getYOnScreen();
        Camera camera = Camera.getCurrentCamera();
        //if (((nx < camera.getWidth() || ny < camera.getHeight()) && !((nx + width) < 0 || (ny + height) < 0))) {
        if (nx < camera.getWidth() && ny < camera.getHeight() && (nx + getWidth()) > 0 && (ny + getHeight()) > 0) {

            if (colorAlpha != 1 || useAlphaChannel) {
                OpenGLState.enableBlending();
                OpenGLState.useTranspBlendingMode();
            } else {
                OpenGLState.disableBlending();
            }
            if (!smoothImage) {
                OpenGLState.setSmoothImageState(false);
            }
            GL11.glColor4f(colorRed, colorGreen, colorBlue, colorAlpha);
            image.bind();
            customDraw();
            if (!smoothImage) {
                OpenGLState.setSmoothImageState(true);
            }
            Camera.getCurrentCamera().addObjectsOnScreen(this);
        }
    }

    protected void drawPlainCenter() {
        final float w = getWidth() / 2;
        final float h = getHeight() / 2;
        GL11.glBegin(GL11.GL_QUADS); // Draw A Quad
        GL11.glNormal3f(0f, 0f, 1f);
        GL11.glTexCoord2f(image.getXStart(), image.getYStart());
        GL11.glVertex2f(-w, -h);
        GL11.glTexCoord2f(image.getXStart(), image.getYEnd());
        GL11.glVertex2f(-w, +h);
        GL11.glTexCoord2f(image.getXEnd(), image.getYEnd());
        GL11.glVertex2f(+w, +h);
        GL11.glTexCoord2f(image.getXEnd(), image.getYStart());
        GL11.glVertex2f(+w, -h);
        GL11.glEnd();
    }

    @Override
    public void renderAt(float nx, float ny, float nz, int unit) {
        //Camera camera = StaticRef.getCamera();
        //if (nx < camera.getWidth() && ny < camera.getHeight() && (nx + getWidth()) > 0 && (ny + getHeight()) > 0) {
            /*if (!smoothImage) {
        ImageUtils.setSmoothImageState(false);
        }*/
        image.bind(unit);

        if (effect != null) {
            for (Shader shader : effect) {
                if (shader != null) {
                    shader.bind();
                    shader.update(this);
                }

                GL11.glLoadIdentity();
                GL11.glTranslatef(nx + getWidth() / 2, ny + getHeight() / 2, nz);
                GL11.glRotatef(getRotation(), 0, 0, 1);
                drawPlainCenter();
                //GL11.glLoadIdentity();
                }
        } else {

            GL11.glLoadIdentity();
            GL11.glTranslatef(nx + getWidth() / 2, ny + getHeight() / 2, nz);
            GL11.glRotatef(getRotation(), 0, 0, 1);
            drawPlainCenter();
            GL11.glLoadIdentity();
        }
        /*if (!smoothImage) {
        ImageUtils.setSmoothImageState(true);
        }*/
        //}
    }

    // -----------------------------------------------------------------
    // ----------------------USER METHODS-------------------------------
    // -----------------------------------------------------------------
    public ShaderEffect getEffect() {
        return effect;
    }

    public void setEffect(ShaderEffect effect) {
        this.effect = effect;
    }

    /**
     * sets the image of sprite
     *
     * @param image
     *            the image to sets
     * @see #getImage()
     */
    @Override
    public void setImage(ITexture newImage) {
        if (newImage != null) {
            if (this.autoReSizeDimensions) {
                /*int w = newImage.getWidth();
                int h = newImage.getHeight();
                if (this.image != null) {
                if (width != w || height != h) {
                cx += (w - width) / 2;
                cy += (h - height) / 2;
                }
                width = w;
                height = h;
                }*/
                setSize(newImage.getWidth(), newImage.getHeight());
            } else if (this.image == null || getWidth() == 0 || getHeight() == 0) {
                setSize(newImage.getWidth(), newImage.getHeight());
            }
            useAlphaChannel = !newImage.isSolid();
        }
        // System.out.println(useAlphaChannel);
        this.image = newImage;
    }

    /**
     * returns the current image of sprite.
     *
     * @see #setImage( ITexture )
     * @return current image
     */
    @Override
    public ITexture getImage() {
        return image;
    }

    public boolean isSmoothImage() {
        return smoothImage;
    }

    public void setSmoothImage(boolean smoothImage) {
        this.smoothImage = smoothImage;
    }

    /**
     * sets the saturation colors.<br>
     * the values must be in the range of 0:1<br>
     * Caution: there is not any check that the value that you have inserted is
     * corrected<br>
     *
     * @param red
     *            red component
     * @param green
     *            green component
     * @param blue
     *            blue component
     * @param alpha
     *            alpha component
     * @see #isUsingAlpha()
     * @see #useAlpha(boolean)
     */
    public void setRGBA(float red, float green,
            float blue, float alpha) {
        colorRed = range01(red);
        colorGreen = range01(green);
        colorBlue = range01(blue);
        colorAlpha = range01(alpha);
    }

    protected float range01(float value) {
        return value > 1.0f ? 1.0f : value < 0.0f ? 0.0f : value;
    }

    public float getColorAlpha() {
        return colorAlpha;
    }

    public void setColorAlpha(float colorAlpha) {
        this.colorAlpha = range01(colorAlpha);
    }

    public float getColorBlue() {
        return colorBlue;
    }

    public void setColorBlue(float colorBlue) {
        this.colorBlue = range01(colorBlue);
    }

    public float getColorGreen() {
        return colorGreen;
    }

    public void setColorGreen(float colorGreen) {
        this.colorGreen = range01(colorGreen);
    }

    public float getColorRed() {
        return colorRed;
    }

    public void setColorRed(float colorRed) {
        this.colorRed = range01(colorRed);
    }

    /**
     * sets if use the alpha channel or not<br>
     * if you set to use the alphachannel, if the colorAlpha field is equal to
     * 1.0f it will be changed as 1.1f<br>
     * if you set to don't use the alphachannel, if the colorAlpha field is
     * different from 1.0f it will be changed as 1.0f<br>
     * I advise you to look the sources to understand this..
     *
     * @param value
     *            indicates if use the alpha channel or not
     * @see #isUsingAlpha()
     * @see #setRGBA(float, float, float, float)
     */
    public void setUseAlpha(boolean value) {
        useAlphaChannel = value;
    }

    /**
     * returns if the sprite is using the alpha channel or not
     *
     * @return if the sprite is using the alpha channel or not
     * @see #useAlpha(boolean)
     * @see #setRGBA(float, float, float, float)
     */
    public boolean isUseAlpha() {
        return useAlphaChannel;
    }

    public boolean isAutoReSizeDimensions() {
        return autoReSizeDimensions;
    }

    public void setAutoReSizeDimensions(boolean autoReSizeDimensions) {
        this.autoReSizeDimensions = autoReSizeDimensions;
    }

    // -----------------------------------------------------------------
    // ---------------------------- PLAIN ------------------------------
    // -----------------------------------------------------------------
    @Override
    public Sprite clone() {
        return new Sprite(this);
    }
}

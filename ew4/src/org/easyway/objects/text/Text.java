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
package org.easyway.objects.text;

import java.util.ArrayList;
import static org.easyway.objects.text.EWFont.COLOR_CHAR;

import java.awt.Color;

import org.easyway.interfaces.extended.IDrawing;
import org.easyway.interfaces.extended.ILayerID;
import org.easyway.interfaces.sprites.IFont;
import org.easyway.interfaces.sprites.IPlain2D;
import org.easyway.objects.BaseObject;
import org.easyway.objects.Camera;
import org.easyway.shader.Shader;
import org.easyway.system.StaticRef;
import org.easyway.system.state.GameState;
import org.lwjgl.opengl.GL11;

public class Text extends BaseObject implements IDrawing, ILayerID, IPlain2D {

    private static final long serialVersionUID = 3166077429305328097L;
    private int layer;
    /**
     * the drawing sheet
     */
    private int idLayer = -1;
    protected String text;
    protected int width;
    protected int numberOfLines = 1;
    protected int height;
    protected int size;
    protected int x;
    protected int y;
    protected IFont font;
    protected int alignH;
    protected int alignV;
    protected Shader shader;
    /**
     * indicates if the coordinates are relative to the screen or to the
     * monitor.<br>
     *
     * if true the coordinates are relative to the monitor else the coordinates
     * are relative to the screen
     */
    public boolean fixedOnScreen = true;
    /**
     * indicates if the zoomFactor of the Camera will have affect on the Text or
     * not<br>
     */
    public boolean fixedDimension = true;

    // ----------------------------------------------
    public Text(int x, int y, String text) {
        this(x, y, text, null);
    }

    public Text(int x, int y, String text, IFont font) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.font = (font == null) ? IFont.defaultFont : font;
        alignH = TextAlign.LEFT;
        alignV = TextAlign.TOP;
        // the following line calculates the width but the
        // numberOfLines too
        width = calculateMaxString();
        // System.out.println("Width: "+width);
        height = (size = this.font.getSize()) * numberOfLines;
        type = "$_TEXT";
        if (autoAddToLists) {
            setIdLayer(GameState.getCurrentGEState().getLayers().length - 1);
        }
    }

    @Override
    public void render() {
        if (text == null || font == null) // invisibile
        {
            return;
        }
        /**
         * note: the Camera reference should be not changed in the Rendering
         * time.. I hope: I don't want speed down the code writing a check
         */
        final Camera camera = Camera.getCurrentCamera();
        boolean mustchange = false;
        if (camera.is3D()) {
            mustchange = true;
            camera.set2D();
        }
        float oldZoom = camera.getZoom2D();
        if (fixedDimension) {
            camera.setZoom2D(1.0f);
        }

        // OpenGLState.disableBlending();
        GL11.glColor4f(1, 1, 1, 1);
        font.setSize(size);
        int nx = (int) x, ny = (int) y;
        if (!fixedOnScreen) {
            nx = (int) (x - StaticRef.getCamera().getX());
            ny = (int) (y - StaticRef.getCamera().getY());
        }
        // align:
        if (alignH == TextAlign.MIDDLE) {
            nx -= getWidth() / 2;
        } else if (alignH == TextAlign.RIGHT) {
            nx -= getWidth();
        }
        if (alignV == TextAlign.BOTTOM) {
            ny -= getHeight();
        } else if (alignV == TextAlign.MIDDLE) {
            ny -= getHeight() / 2;
        }
        if (shader != null) {
            shader.bind();
            shader.update(this);
            configureShader(shader);
        }
        font.writeString(text, text.length(), nx, ny);
        if (mustchange) {
            camera.set3D();
        }
        if (fixedDimension) {
            camera.setZoom2D(oldZoom);
        }
        assert camera == StaticRef.getCamera();
        StaticRef.getCamera().addObjectsOnScreen(this);
    }

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader<?> shader) {
        this.shader = shader;
    }

    /** to override if you want to configure the shader */
    protected void configureShader(Shader<?> shader) {}

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
        readdToDrawingLists();
    }

    public int getAlignH() {
        return alignH;
    }

    public void setAlignH(int alignH) {
        this.alignH = alignH;
    }

    public int getAlignV() {
        return alignV;
    }

    public void setAlignV(int alignV) {
        this.alignV = alignV;
    }

    public IFont getFont() {
        return font;
    }

    public void setFont(IFont font) {
        this.font = font;
        width = calculateMaxString();
        if (!font.canChangeSize()) {
            size = font.getSize();
            height = size * numberOfLines;
        }
    }

    /**
     * returns the Height of the text in pixels
     *
     * @see #getWidth()
     * @see #getLength()
     */
    public int getHeight() {
        return height;
    }

    /**
     * returns the message of the text
     */
    public String getText() {
        return text;
    }

    /**
     * append a string to the Text's message<br/>
     */
    public void append(String text) {
        this.text += text;
        if (font == null) {
            return;
        }
        width = calculateMaxString();
        height = numberOfLines * size;
    }

    /**
     * append a string that will colorize the next text<br/>
     * @param color
     */
    public void append(Color color) {
        createColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * change the text's message
     */
    public void setText(String text) {
        this.text = text;
        if (font == null) {
            return;
        }
        width = calculateMaxString();
        height = numberOfLines * size;
    }

    // -----------------------------------------------------------------
    // ---------------------------- PLAIN ------------------------------
    // -----------------------------------------------------------------
    /** not implemented */
    public void setHeight(int height) {
        return;
    }

    /** not implemented */
    public void setSize(int width, int height) {
        return;
    }

    /** not implemented */
    public void setWidth(int width) {
        return;
    }

    /**
     * returns the x coordinate relative to WORLD
     *
     */
    public float getX() {
        if (fixedOnScreen) {
            return x + StaticRef.getCamera().getX();
        } else {
            return x;
        }
    }

    /**
     * returns the y coordinate relative to WORLD
     *
     */
    public float getY() {
        if (fixedOnScreen) {
            return y + StaticRef.getCamera().getY();
        } else {
            return y;
        }
    }

    /**
     * returns the x coordinate relative to SCREEN
     *
     */
    public float getXOnScreen() {
        if (fixedOnScreen) {
            return x;
        } else {
            return x - StaticRef.getCamera().getX();
        }
    }

    /**
     * returns the y coordinate relative to SCREEN
     *
     */
    public float getYOnScreen() {
        if (fixedOnScreen) {
            return y;
        } else {
            return y - StaticRef.getCamera().getY();
        }
    }

    /**
     * return the length of the text<br>
     * it's the same of getWidth()
     *
     * @see #getWidth()
     * @see #getHeight()
     */
    public int getLength() {
        return width;
    }

    /**
     * returns the length in pixel of the text<br>
     * it's the same of getLength
     *
     * @see #getLength()
     * @see #getHeight()
     */
    public int getWidth() {
        return width;
    }

    public void setX(float x) {
        this.x = (int) x;
    }

    public void setXY(float x, float y) {
        this.x = (int) x;
        this.y = (int) y;
    }

    public void setY(float y) {
        this.y = (int) y;
    }

    private String[] split(char split) {
        int l = text.length() - 1; // I don't want check the last char
        char tchar;
        // 0: no colorstate
        // 1,2,3: first, second, third color's char
        int colorState = 0;
        for (int i = 0; i < l; ++i) {
            tchar = text.charAt(i);
            if (tchar == COLOR_CHAR) {
            }
        }

        return null;
    }

    private int calculateMaxString() {
        if (text == null || font == null || text.equals("")) {
            return 0;
        }
        String texts[] = text.split("\n");
        numberOfLines = texts.length;
        int max = 0;
        int temp = 0;
        for (String mtext : texts) {
            if (max < (temp = font.getLenght(mtext))) {
                max = temp;
            }
        }
        return max;
    }

    @SuppressWarnings("unused")
    //
    private int calculateNumberOfLines() {
        if (text == null) {
            return 0;
        }
        String texts[] = text.split("\n");
        return (numberOfLines = texts.length);
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if (font.canChangeSize()) {
            this.size = size;
        }
    }

    /**
     * creates a new color
     *
     * @param red
     *            red component (0-255)
     * @param green
     *            green component (0-255)
     * @param blue
     *            blue component (0-255)
     * @return returns a special string that can be used for change the color of
     *         the text<br>
     *         "COLOR_CHAR(char)red(char)green(char)blue"
     */
    public static String createColor(int red, int green, int blue) {
        if (red == COLOR_CHAR || red == '\n') {
            --red;
        }
        if (green == COLOR_CHAR || green == '\n') {
            --green;
        }
        if (blue == COLOR_CHAR || blue == '\n') {
            --blue;
        }
        return "" + COLOR_CHAR + (char) red + (char) green + (char) blue;
    }

    /**
     * creates a gradient color text
     *
     * @param inputText
     *            the text to color
     * @param startColor
     *            the start color of the gradient
     * @param endColor
     *            the end color of the gradient
     * @return the gradient color text
     */
    public static String createGradientText(String inputText, Color startColor,
            Color endColor) {
        if (inputText == null || inputText.length() == 0) {
            return "";
        }
        String temp = "";
        int inLength = inputText.length();
        float rstep = (endColor.getRed() - startColor.getRed()) / inLength;
        float gstep = (endColor.getGreen() - startColor.getGreen()) / inLength;
        float bstep = (endColor.getBlue() - startColor.getBlue()) / inLength;
        float cred = startColor.getRed(), cgreen = startColor.getGreen(), cblue = startColor.getBlue();
        --inLength;
        for (int i = 0; i < inLength; i++) {
            temp += Text.createColor((char) cred, (char) cgreen, (char) cblue);
            temp += inputText.charAt(i);
            cred += rstep;
            cgreen += gstep;
            cblue += bstep;
        }
        temp += inputText.charAt(inLength);
        return temp;
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
        } else if (id > GameState.getCurrentGEState().getLayers().length) {
            id = GameState.getCurrentGEState().getLayers().length;
        }
        idLayer = id;
        GameState.getCurrentGEState().getLayers()[idLayer].add(this);
    }

    /**
     * returns if the size changes in relation of the zoomfactor of the game
     * engine's camera
     */
    public boolean isFixedOnScreen() {
        return fixedOnScreen;
    }

    /**
     * indicates if the coordinates are relative to the screen or to the
     * monitor.<br>
     *
     * @param fixedOnScren
     *            if true the coordinates are relative to the monitor else the
     *            coordinates are relative to the screen
     */
    public void setFixedOnScreen(boolean fixedOnScreen) {
        this.fixedOnScreen = fixedOnScreen;
    }

    /**
     * returns if the coordinates are relative to the screen or to the monitor
     */
    public boolean isFixedDimension() {
        return fixedDimension;
    }

    /**
     * indicates if the text change the size in relation of the zoomFactor of
     * the Game engine's camera
     *
     * @param fixedDimension
     *            true: don't change dimension, else change it
     */
    public void setFixedDimension(boolean fixedDimension) {
        this.fixedDimension = fixedDimension;
    }

    public ArrayList getQuadEntries() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ArrayList getUsedInQuadNodes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isQuadTreeUsable() {
        return false;
    }

    @Override
    public float getScaleX() {
        return getWidth();
    }

    @Override
    public float getScaleY() {
        return getHeight();
    }
}

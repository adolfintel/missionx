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

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.easyway.interfaces.base.IDestroyable;
import org.easyway.interfaces.sprites.IFont;
import org.easyway.objects.texture.Texture;
import org.easyway.system.Core;
import org.easyway.system.state.OpenGLState;
import org.easyway.utils.Utility;
import org.lwjgl.opengl.GL11;

public class EWFont implements IFont, IDestroyable, Serializable {

    private static final long serialVersionUID = 21;
    private static final Color OPAQUE_WHITE = new Color(0xFFFFFFFF, true);
    private static final Color TRANSPARENT_BLACK = new Color(0x00000000, true);
    private Texture texture;
    private int base; // Base Display List For The Font Set
    private int size = 18;
    transient private IntBuffer textureBuffer;
    private FontMetrics fontMatric;
    private int internalFontSize;
    private float scalar; // size / internalFontSize;
    private boolean destroyed = false;
    public static char COLOR_CHAR = (char) 256;

    // --------------------- CONSTRUCTORS -----------------------
    /**
     * loads a new font<br>
     *
     * @param name
     *            if localFont is true:<br>
     *            the name is the relative path where is located a truetype font
     *            file<br>
     *            else if localFont is false the name indicates the font's name
     *            that the GameEngine will load from the OS's Fonts
     * @param localFont
     *            if true the font will be loaded from a file, else the font
     *            will be loaded from the fonts of the Operative System
     *
     */
    public EWFont(String name, boolean localFont) throws FontFormatException,
            IOException {
        this(name, localFont ? Font.createFont(Font.TRUETYPE_FONT,
                getInputStream(name)).deriveFont(24f) : null);
    }

    public EWFont(String fontName) {
        this(fontName, null);
    }

    /**
     * creates EWFont from a trueType font file
     *
     * @param is
     *            trueType font file
     * @throws FontFormatException
     *             could not read font
     * @throws IOException
     *             is wrong
     */
    public EWFont(InputStream is) throws FontFormatException, IOException {
        this("My font", Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(24f));
    }

    public EWFont(String fontName, Font font) {
        if (fontName == null) {
            fontName = "Arial";
        }
        BufferedImage fontImage; // image for creating the bitmap
        int bitmapSize = 512; // set the size for the
        // bitmap
        // texture
        boolean sizeFound = false;
        boolean directionSet = false;
        int delta = 0;
        int fontSize = 24;

        // ----- size of image --------
        // font = new Font(fontName, Font.PLAIN, fontSize); // Font Name
        if (font == null) {
            while (!sizeFound) {
                font = new Font(fontName, Font.PLAIN, fontSize); // Font Name

                fontImage = new BufferedImage(bitmapSize, bitmapSize,
                        BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D g = (Graphics2D) fontImage.getGraphics();
                g.setFont(font);
                FontMetrics fm = g.getFontMetrics();
                int width = getMaxWidth(fm);// fm.charWidth('W');
                int height = fm.getHeight();
                // 0.3.1
                // g.setFont(font);
                // fm = g.getFontMetrics();
                int lineWidth = (width > height) ? width * 16 : height * 16;
                if (!directionSet) {
                    if (lineWidth > bitmapSize) {
                        delta = -2;
                    } else {
                        delta = 2;
                    }
                    directionSet = true;
                }
                if (delta > 0) {
                    if (lineWidth < bitmapSize) {
                        fontSize += delta;
                    } else {
                        sizeFound = true;
                        fontSize -= delta;
                    }
                } else if (delta < 0) {
                    if (lineWidth > bitmapSize) {
                        fontSize += delta;
                    } else {
                        sizeFound = true;
                        fontSize -= delta;
                    }
                }
            }
            /*
             * Now that a font size has been determined, create the final image,
             * set the font and draw the standard/extended ASCII character set
             * for that font.
             */
            font = new Font(fontName, Font.PLAIN, fontSize); // Font Name
        }

        // use BufferedImage.TYPE_4BYTE_ABGR to allow alpha blending
        fontImage = new BufferedImage(bitmapSize, bitmapSize,
                BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = (Graphics2D) fontImage.getGraphics();
        g.setFont(font);
        g.setColor(OPAQUE_WHITE);
        g.setBackground(TRANSPARENT_BLACK);
        FontMetrics fm = g.getFontMetrics();
        fontMatric = fm;
        internalFontSize = fontSize;
        for (int i = 0; i < 256; i++) {
            int x = i % 16;
            int y = i / 16;
            // TODO: DA RIVEDERE che fa CAGARE
            char ch[] = {(char) i};
            String temp = new String(ch);
            g.drawString(temp, (x * 32) + 1, (y * 32) + fm.getAscent());
        }

        // Flip Image
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -fontImage.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx,
                AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        fontImage = op.filter(fontImage, null);

        // Put Image In Memory
        ByteBuffer scratch = ByteBuffer.allocateDirect(4 * fontImage.getWidth() * fontImage.getHeight());

        byte data[] = (byte[]) fontImage.getRaster().getDataElements(0, 0,
                fontImage.getWidth(), fontImage.getHeight(), null);
        scratch.clear();
        scratch.put(data);
        scratch.rewind();

        texture = new Texture(fontImage.getWidth(), fontImage.getHeight());
        texture.setData(scratch);

        // Generate The Texture - NEW
		 /*GLU.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D, GL11.GL_RGBA,
        texture.widthHW, texture.heightHW, GL11.GL_RGBA,
        GL11.GL_UNSIGNED_BYTE, scratch);*/

        scalar = (float) size / (float) internalFontSize;
    }

    private int getMaxWidth(FontMetrics fm) {
        int widths[] = fm.getWidths();
        int max = -1;
        for (int i = 0; i < widths.length; ++i) {
            if (widths[i] > max) {
                max = widths[i];
            }
        }
        return max;
    }

    // --------------------- WRITERS ROUTINES -----------------------
    /**
     * draws a char on screen<br>
     * At coordinates 0,0
     */
    public void drawChar(int mchar, byte red, byte green, byte blue) {
        float xSize = fontMatric.charWidth(mchar);// getLenght((char)mchar);
        final float uSize = xSize / 512.0f;

        float textureDelta = 0.0625f; // 1.0f / 16.0f;
        float u = ((float) (mchar % 16)) * textureDelta; // / 16;
        float v = 1.f - (((float) ((int) (mchar / 16/* textureDelta */))) * textureDelta);

        //GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
        //GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);
        OpenGLState.enableBlending();
        OpenGLState.useTranspBlendingMode();
        //GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ZERO);
        texture.bind();
        GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) 0);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(u, v);
        GL11.glVertex3f(0, 0, 0);
        GL11.glTexCoord2f((u + uSize), v);
        GL11.glVertex3f(xSize = (xSize * scalar), 0, 0);
        GL11.glTexCoord2f((u + uSize), v - textureDelta);
        GL11.glVertex3f(xSize, size, 0);
        GL11.glTexCoord2f(u, v - textureDelta);
        GL11.glVertex3f(0, size, 0);
        GL11.glEnd();

        texture.bind();
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4ub(red, green, blue, (byte) 255);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(u, v);
        GL11.glVertex3f(0, 0, 0);
        GL11.glTexCoord2f((u + uSize), v);
        GL11.glVertex3f(xSize = (xSize * scalar), 0, 0);
        GL11.glTexCoord2f((u + uSize), v - textureDelta);
        GL11.glVertex3f(xSize, size, 0);
        GL11.glTexCoord2f(u, v - textureDelta);
        GL11.glVertex3f(0, size, 0);
        GL11.glEnd();
        OpenGLState.useTranspBlendingMode();
    }

    public void writeFPS() {
        int fps = Core.getInstance().getFps();
        String t;
        if (fps > 60) {
            t = Text.createColor(0, 255, 0);
        } else if (fps < 50) {
            t = Text.createColor(255, 0, 0);
        } else {
            t = Text.createColor(255, 255, 0);
        }
        OpenGLState.disableBlending();
        GL11.glColor4f(1, 1, 1, 1);
        writeString(t = ("FPS: " + t + fps), t.length(), 0, 0);
    }

    @Override
    public void writeString(String msg, int lenght, int x, int y) { // Custom
        if (msg == null) {
            return;
        }
        lenght = Math.min(msg.length(), lenght);
        int line = 0;
        char currChar;
        OpenGLState.disableBlending();
        GL11.glLoadIdentity();
        GL11.glTranslatef(x, y, 0f);
        texture.bind();
        byte red = (byte) 255, green = (byte) 255, blue = (byte) 255;
        for (int i = 0; i < lenght; ++i) {
            if (msg.charAt(i) == COLOR_CHAR && i != msg.length() - 1) {
                if (msg.charAt(i + 1) == COLOR_CHAR) {
                    ++i;
                    if (i >= msg.length()) {
                        break;
                    }
                } else { // msg.charAt(i + 1) != '%'
                    try {
                        ++i;
                        red = (byte) msg.charAt(i);
                        ++i;
                        green = (byte) msg.charAt(i);
                        ++i;
                        blue = (byte) msg.charAt(i);

                        ++i;
                        if (i >= msg.length()) {
                            break;
                        }
                    } catch (Exception e) {
                        Utility.error("incorrect use of the " + COLOR_CHAR + " character",
                                "EWFont.writeStringRect", e);
                        continue;
                    }
                }
            }
            if ((currChar = msg.charAt(i)) == '\n') {
                GL11.glLoadIdentity();
                line += size;
                GL11.glTranslatef(x, y + line, 0);
            } else {
                drawChar(currChar, red, green, blue);
                GL11.glTranslatef((int) (scalar * fontMatric.charWidth(currChar)), 0, 0);
            }
        } // end for
        GL11.glLoadIdentity();

    }

    public void writeStringRect(String msg, int lenght, int x, int y, int width) {
        if (msg == null) {
            return;
        }
        lenght = Math.min(msg.length(), lenght);
        int position = 0;
        int line = 0;
        OpenGLState.disableBlending();
        GL11.glLoadIdentity();
        GL11.glTranslatef(x, y, 0f);
        texture.bind();
        byte red = (byte) 255, green = (byte) 255, blue = (byte) 255;
        for (int i = 0; i < lenght; ++i) {
            if (msg.charAt(i) == COLOR_CHAR && i != msg.length() - 1 && msg.charAt(i + 1) != COLOR_CHAR) {
                try {
                    ++i;
                    red = (byte) msg.charAt(i);
                    ++i;
                    green = (byte) msg.charAt(i);
                    ++i;
                    blue = (byte) msg.charAt(i);
                    ++i;
                } catch (Exception e) {
                    Utility.error("incorrect use of " + COLOR_CHAR,
                            "EWFont.writeStringRect", e);
                    continue;
                }
            }
            if (msg.charAt(i) == '\n') {
                position = width;
            }

            // GL11.glCallList(base + msg.charAt(i));
            drawChar(msg.charAt(i), red, green, blue);
            position += scalar * fontMatric.stringWidth("" + msg.charAt(i));

            if (position >= width) {
                GL11.glLoadIdentity();
                line += size;
                GL11.glTranslatef(x, y + line, 0);
                position = 0;
                if (i + 1 < msg.length() && msg.charAt(i + 1) == ' ') {
                    ++i;
                }
            } else {
                GL11.glTranslatef((int) (scalar * fontMatric.charWidth(msg.charAt(i))), 0, 0);
            }
        }
        GL11.glLoadIdentity();

    }

    private static InputStream getInputStream(String path) {
        InputStream is = null;
        {
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            int index;
            // path = path.replaceAll("\\", "/");
            while ((index = path.indexOf("\\")) != -1) {
                path = path.substring(0, index) + '/' + path.substring(index + 1);
            }
            try {
                is = Thread.currentThread().getContextClassLoader().getResource(path).openStream();
            } catch (Exception e) {
                Utility.error("font " + path + " was not found!", e);
            }
        }
        return is;

    }

    // --------------------- USER -----------------------
    public void setSize(int size) {
        this.size = size;
        scalar = (float) size / (float) internalFontSize;
    }

    public int getSize() {
        return size;
    }

    public int getLenght(char character) {
        return (int) (scalar * fontMatric.charWidth(character));
    }

    public int getLenght(String string) {
        if (string == null || string.equals("")) {
            return 0;
        }
        String strings[] = string.split("" + COLOR_CHAR);
        if (strings.length == 1) { // no split
            return (int) (scalar * fontMatric.stringWidth(string));
        }

        int incWidth = (int) (scalar * fontMatric.stringWidth(strings[0]));
        for (int i = 1; i < strings.length; ++i) {
            if (strings[i].length() == 0) {
                continue;
            }
            if (strings[i].charAt(0) == COLOR_CHAR) {
                incWidth += (int) (scalar * fontMatric.stringWidth(strings[i]));
                continue;
            }
            strings[i] = strings[i].substring(3);
            incWidth += (int) (scalar * fontMatric.stringWidth(strings[i]));

        }
        return incWidth;// (int) (scalar * fontMatric.stringWidth(string));
    }

    public void moveDrawingPoint(int x, int y) {
        GL11.glTranslatef(x, y, 0);
    }

    public void changeColor(float red, float green, float blue) {
        GL11.glColor3f(red, green, blue);
    }

    // --------------------- DESTROY -----------------------
    public void finalize() {
        destroy();
    }

    public void destroy() {
        if (destroyed) {
            return;
        }
        GL11.glDeleteLists(base, 256);
        if (textureBuffer != null) {
            GL11.glDeleteTextures(textureBuffer);
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public boolean canChangeSize() {
        return true;
    }
}

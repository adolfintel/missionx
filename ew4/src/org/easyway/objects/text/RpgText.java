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

import org.easyway.interfaces.extended.IDrawing;
import org.easyway.interfaces.extended.ILayerID;
import org.easyway.interfaces.sprites.IFont;
import org.easyway.objects.Camera;
import org.easyway.system.StaticRef;
import org.easyway.system.state.GameState;
import org.easyway.utils.TimerJGM;
import org.lwjgl.opengl.GL11;

public class RpgText extends Text implements IDrawing, ILayerID {

    /**
     * generated serial version
     */
    private static final long serialVersionUID = -3093427023194101624L;
    /**
     * maximum length in pixel of the Text
     */
    protected int width;
    /**
     * typing speed
     */
    protected int speed = 80;
    /**
     * caret position
     */
    private int max = 1;
    private TimerEffect timer;
    /**
     * indicates if the zoomFactor of the Camera will have affect on the Text or
     * not<br>
     */
    public boolean fixedDimension = true;

    public RpgText(IFont font, int x, int y, String text) {
        this(font, x, y, Integer.MAX_VALUE, text, font == null ? 32 : font.getSize(), true, GameState.getCurrentGEState().getLayers().length - 1);
    }

    public RpgText(int x, int y, String text) {
        this(null, x, y, Integer.MAX_VALUE, text, 32, true,
                GameState.getCurrentGEState().getLayers().length - 1);
    }

    public RpgText(IFont font, int x, int y, String text, int size) {
        this(font, x, y, Integer.MAX_VALUE, text, size, true,
                GameState.getCurrentGEState().getLayers().length - 1);
    }

    public RpgText(int x, int y, String text, int size) {
        this(null, x, y, Integer.MAX_VALUE, text, size, true,
                GameState.getCurrentGEState().getLayers().length - 1);
    }

    public RpgText(IFont font, int x, int y, int width, String text, int size) {
        this(font, x, y, width, text, size, true, GameState.getCurrentGEState().getLayers().length - 1);
    }

    public RpgText(IFont font, int x, int y, int width, String text, int size,
            boolean autoAdd, int idLayer) {
        super(x, y, text, font);
        setIdLayer(idLayer);
        this.text = text;
        this.width = width;
        if (font == null) {
            this.font = EWFont.defaultFont;
        } else {
            this.font = font;
        }
        this.x = x;
        this.y = y;
        this.size = size;

    }

    // -------------------------------------------------------
    // ------------------RENDER-------------------------------
    // -------------------------------------------------------
    public void render() {
        if (text == null || font == null) // invisibile
        {
            return;
        }
        Camera camera = StaticRef.getCamera();
        final float oldZoom = camera.getZoom2D();
        boolean mustchange = false;
        if (camera.is3D()) {
            mustchange = true;
            camera.set2D();
        }
        if (fixedDimension) {
            camera.setZoom2D(1.0f);
        }
        int nx = (int) getXOnScreen(), ny = (int) getYOnScreen();
        if (!fixedOnScreen) {
            nx = (int) getX();
            ny = (int) getY();
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

        GL11.glColor4f(1, 1, 1, 1);
        font.setSize(size);
        // we should synch the 'text' (? sure)
        // TODO: Test
        synchronized (text) {
            if (shader != null) {
                shader.bind();
                shader.update(this);
                configureShader(shader);
            }
            font.writeStringRect(text, max, nx, ny, width);
        }
        if (mustchange) {
            camera.set3D();
        }
        if (fixedDimension) {
            camera.setZoom2D(oldZoom);
        }
    }

    // -------------------------------------------------------
    // ------------------TEXT---------------------------------
    // -------------------------------------------------------
    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (timer != null) {
            max = 1;
            timer.stop();
        }
        this.text = text;
    }

    // -------------------------------------------------------
    // ------------------FONT SIZE----------------------------
    // -------------------------------------------------------
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /**
     * starts the effect with the default\last used speed
     */
    public void startEffect() {
        timer = new TimerEffect();
    }

    /**
     * starts the effect with the selected speed
     *
     * @param speed
     *            the speed is in ms (1000 = 1 second)
     */
    public void startEffect(int speed) {
        this.speed = speed;
        timer = new TimerEffect();
    }

    public int getWidth() {
        return Math.min(super.getWidth(), width);
    }

    /**
     * sets maximum length in pixel of the Text
     */
    public void setWidth(int width) {
        this.width = width;
    }

    class TimerEffect extends TimerJGM {

        public TimerEffect() {
            super(speed);
            start();
        }

        public void onTick() {
            // we should synch the 'text'
            synchronized (text) {
                ++max;
                if (max >= text.length()) {
                    max = text.length();
                    stop();
                } else if (text.charAt(max) == EWFont.COLOR_CHAR && max + 1 != text.length() && text.charAt(max + 1) != EWFont.COLOR_CHAR) {
                    max += 4;
                }
            }
        }
    }

    public void destroy() {
        if (isDestroyed()) {
            return;
        }
        if (timer != null) {
            timer.stop();
        }
        super.destroy();
    }

    public void finalize() {
        if (timer != null) {
            timer.stop();
        }
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isEffectTerminated() {
        return max >= text.length();
    }

    public void setCaretPosition(int position) {
        max = Math.max(1, Math.min(text.length(), position));
    }

    public void skip() {
        max = text.length();
    }

    public int getCaretPosition() {
        return max;
    }
}

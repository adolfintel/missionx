/* EasyWay Game Engine
 * Copyright (C) 2007 Daniele Paggi.
 *  
 * Written by: 2007 Daniele Paggi<dshnt@hotmail.com>
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
package org.easyway.gui.base;

import org.easyway.interfaces.base.ITexture;
import org.easyway.interfaces.sprites.IClickable;
import org.easyway.objects.sprites2D.Mask;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.text.Text;
import org.easyway.objects.texture.Texture;

public abstract class ButtonSprite extends Sprite implements IClickable {

    protected static ITexture upImageDef;
    protected ITexture upImage;
    protected ITexture selectedImage;
    protected ITexture downImage;

    static {
        if (upImageDef == null) {
            upImageDef = Texture.getTexture("org/easyway/gui/images/buttonUp.png");
        }
    }
    protected Text text;

    public ButtonSprite(int x, int y, String text) {
        super(x, y, upImageDef);
        upImage = upImageDef;
        this.text = new Text(x + 10, y + 10, text);
        int h = this.text.getHeight();
        int w = this.text.getWidth();
        setSize(w + 20, h + 20);
        fixedOnScreen = true;
    }

    @Override
    public void onClick(int x, int y) {
    }

    @Override
    public void onOver(int nx, int ny) {
    }

    @Override
    public void onDown(int x, int y) {
        if (downImage != null) {
            setRGBA(1, 1, 1, 1);
            setImage(downImage);
        } else {
            setRGBA(0, 1, 1, 1);
        }
    }

    @Override
    public void onDrag(int incx, int incy) {
    }

    @Override
    public void onEnter() {
        if (selectedImage != null) {
            setRGBA(1, 1, 1, 1);
            setImage(selectedImage);
        } else {
            setRGBA(1, 1, 0, 1);
        }
    }

    @Override
    public void onExit() {
        // if (upImageDef != null && getImage() != upImageDef) {
        setImage(upImage);
        // }
        setRGBA(1, 1, 1, 1);
    }

    @Override
    public void onRelease(int x, int y) {
        // if (upImageDef != null && getImage() != upImageDef) {
        setImage(upImage);
        // }
        setRGBA(1, 1, 1, 1);
        command();
    }

    public void setUpImage(ITexture image) {
        setImage(upImage = image);
    }

    @Override
    public Mask getMask() {
        return null;
    }

    public abstract void command();

    @Override
    public void incX(float incx) {
        move(incx,0);
    }

    @Override
    public void incY(float incy) {
        move(0,incy);
    }

    @Override
    public void move(float incx, float incy) {
        super.move(incx, incy);
        if (text != null) {
            text.setXY((int) (text.getX() + incx),(int) (text.getY() + incy));
        }
    }

    @Override
    public void setX(float x) {
        setXY(x, getY());
    }

    @Override
    public void setXY(float x, float y) {
        super.setXY(x, y);
        if (text != null) {
            text.setXY((int) x + 10,(int)y+10);
        }
    }

    @Override
    public void setY(float y) {
        setXY(getX(),y);
    }

    @Override
    public void setImage(ITexture image) {
        this.image = image;
    }

    @Override
    public void destroy() {
        super.destroy();
        text.destroy();
    }
}

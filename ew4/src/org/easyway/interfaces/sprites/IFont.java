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
package org.easyway.interfaces.sprites;

import org.easyway.objects.text.EWFont;

public interface IFont {

    /**
     * draw a string on screen
     * @param text
     * @param lenght
     * @param x
     * @param y
     */
    public void writeString(String text, int lenght, int x, int y);

    /**
     * draw a charcater on screen
     * @param character
     */
    public void drawChar(int character, byte red, byte gren, byte blue);

    /**
     * draw a string in a rectangular space
     * @param text
     * @param lenght
     * @param x
     * @param y
     * @param width
     */
    @Deprecated
    public void writeStringRect(String text, int lenght, int x, int y, int width);

    /**
     * set the size of Font
     * @param size
     */
    public void setSize(int size);

    public int getSize();

    public boolean canChangeSize();

    public int getLenght(String text);

    public int getLenght(char character);
    public static final IFont defaultFont = new EWFont((String) null);
}

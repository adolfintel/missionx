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

public class StaticFullyMask extends Mask {

    private static final long serialVersionUID = 7571083115774914092L;

    /**
     * creates a new instance.
     * @param width the width of Collision Mask
     * @param height the height of collision mask
     */
    public StaticFullyMask(int width, int height) {
        super();
        full = true;
        this.width = width;
        this.height = height;
    }

    /**
     * in the StaticFullyMask this method doesn't have effect.
     */
    @Override
    public void set(int x, int y, boolean val) {
    }
}

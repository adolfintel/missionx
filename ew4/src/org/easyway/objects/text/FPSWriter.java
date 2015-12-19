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

import org.easyway.system.Core;

public class FPSWriter extends Text {

    static final long serialVersionUID = 23;
    static final String redColor = Text.createColor(255, 0, 0);
    static final String greenColor = Text.createColor(0, 255, 0);
    static final String yellowColor = Text.createColor(255,255,0);

    public FPSWriter(EWFont font, int x, int y) {
        super(x, y, "", font);
    }

    public FPSWriter(EWFont font, int x, int y, int size) {
        super(x, y, "", font);
        setSize(size);
    }

    @Override
    public void render() {
        int fps = Core.getInstance().getFps();
        String color = yellowColor;
        if (fps > 60) {
            color = greenColor;
        } else if (fps < 40) {
            color = redColor;
        }
        setText("FPS: " + color + fps);
        super.render();
    }
}

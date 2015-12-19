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
package org.easyway.debug;

import org.easyway.input.Mouse;
import org.easyway.interfaces.base.IDestroyable;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.lists.BaseList;
import org.easyway.objects.BaseObject;
import org.easyway.objects.text.Text;

public class ObjectPicker extends BaseObject implements ILoopable, IDestroyable {

    private static final long serialVersionUID = 7148489467747032206L;
    Text text;

    public ObjectPicker() {
        text = new Text(0, 20, "ob. picker: ON");
        System.out.println("CREATO");
    }

    public void loop() {
        if (Mouse.isLeftDown()) {
            BaseList list = Mouse.getOver();
            if (list != null) {
                new DebugManager(list);
            }
            destroy();
        }
    }

    protected void onDestroy() {
        text.destroy();
    }
}

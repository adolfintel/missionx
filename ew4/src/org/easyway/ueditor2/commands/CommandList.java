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
package org.easyway.ueditor2.commands;

import java.util.ArrayList;

import java.util.Vector;
import org.easyway.ueditor2.commands.intrefaces.ICommand;

public class CommandList {

    static Vector<ICommand> objlistToCreate = new Vector<ICommand>(10);
    static ArrayList<ICommand> copyObjlistToCreate = new ArrayList<ICommand>(10);
    static ArrayList<ICommand> objlist = new ArrayList<ICommand>(10);
    static ArrayList<ICommand> destroyList = new ArrayList<ICommand>(10);

    static public void render() {

        // loop all
        for (ICommand obj : objlist) {
            if (obj.isDestroyed()) {
                destroyList.add(obj);
                continue;
            }
            obj.render();
            if (obj.isDestroyed()) {
                destroyList.add(obj);
                continue;
            }
        }

        // destroy all
        for (int i = destroyList.size() - 1; i >= 0; --i) {
            objlist.remove(destroyList.get(i));
            destroyList.remove(i);
        }
    }

    static public void loop() {
        synchronized (objlistToCreate) {

            if (objlistToCreate.size() > 0) {
                for (ICommand obj : objlistToCreate) {
                    copyObjlistToCreate.add(obj);
                    objlist.add(obj);
                }
                objlistToCreate.clear();
            }

            for (ICommand obj : copyObjlistToCreate) {
                obj.creation();
            }
            copyObjlistToCreate.clear();
            // loop all
            for (ICommand obj : objlist) {
                if (obj.isDestroyed()) {
                    destroyList.add(obj);
                    continue;
                }
                obj.loop();
                if (obj.isDestroyed()) {
                    destroyList.add(obj);
                    continue;
                }
            }
        }

        // destroy all
        for (int i = destroyList.size() - 1; i >= 0; --i) {
            objlist.remove(destroyList.get(i));
            destroyList.remove(i);
        }

    }
}

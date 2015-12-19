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
package org.easyway.collisions;

import java.io.Serializable;
import java.util.ArrayList;

import org.easyway.interfaces.base.IPureLoopable;
import org.easyway.interfaces.sprites.ICollisionTester;
import org.easyway.interfaces.sprites.ISpriteColl;
import org.easyway.tiles.TileMapLayer;
import org.easyway.utils.Utility;

public class CoreCollision implements IPureLoopable, Serializable {

    static final long serialVersionUID = 1000;
    public ArrayList<TileMapLayer> tileGroups;
    public ArrayList<ICollisionTester> groups;
    public ArrayList<ICollisionTester> autoGroups;

    public CoreCollision() {
        groups = new ArrayList<ICollisionTester>(10);
        tileGroups = new ArrayList<TileMapLayer>(10);
        autoGroups = new ArrayList<ICollisionTester>(10);
    }

    public void loop() {
        TileMapLayer tm;
        ICollisionTester temp;
        for (int i = tileGroups.size() - 1; i >= 0; i--) {
            tm = tileGroups.get(i);
            if (tm == null || (temp = tm.getTestingCollisionList()) == null) {
                Utility.error("Found a null TileGroup!",
                        "CoreCollision.loop");
                new Exception();
                continue;
            }
            // gain the objects that are inside the TileMapLayer area:
            // fills the 'collisions' field of TileMapLayer
            tm.getTestingCollisionList().loop();

            tm.onCollision();
        }

        for (int i = groups.size() - 1; i >= 0; i--) {
            temp = groups.get(i);
            if (temp == null) {
                Utility.error("Found a null ICollisionTester!",
                        "CoreCollision.loop");
                new Exception().printStackTrace();
                continue;
            }
            temp.loop();
        }
    }

    public ICollisionTester search(String name) {
        ICollisionTester temp;
        String ttemp = name;
        for (int i = autoGroups.size() - 1; i >= 0; --i) {
            temp = autoGroups.get(i);
            if (temp.getType() == null) {
                continue;
            }
            if (temp.getType().equals(ttemp)) {
                return temp;
            }
        }
        TileMapLayer tm = searchTileMap(name);
        return tm == null ? null : tm.getTestingCollisionList();
    }

    public TileMapLayer searchTileMap(String name) {
        TileMapLayer tmap;
        String ttemp = name;
        for (int i = tileGroups.size() - 1; i >= 0; --i) {
            tmap = tileGroups.get(i);
            if (tmap.getType() == null) {
                continue;
            }
            if (tmap.getType().equals(ttemp)) {
                return tmap;
            }
        }
        return null;
    }

    public void loadIncremental(CoreCollision core) {
        boolean found;
        for (TileMapLayer tm : core.tileGroups) {
            found = false;
            for (TileMapLayer tm2 : tileGroups) {
                if (tm.getType().equals(tm2)) {
                    found = true;
                    for(ISpriteColl spr : tm.getTestingCollisionList()) {
                        tm2.add(spr);
                    }
                }
            }
            if (!found) {
                tileGroups.add(tm);
            }
        }
        //tileGroups.addAll(core.tileGroups);
        groups.addAll(core.groups);
        autoGroups.addAll(core.autoGroups);
    }
}

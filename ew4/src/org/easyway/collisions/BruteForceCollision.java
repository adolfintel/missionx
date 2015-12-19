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

import org.easyway.interfaces.sprites.ICollisionTester;
import org.easyway.interfaces.sprites.ISpriteColl;
import org.easyway.lists.GameList;
import org.easyway.system.state.GameState;
import org.easyway.utils.Utility;

public class BruteForceCollision implements ICollisionTester, Serializable {

    static final long serialVersionUID = 1001;
    public GameList<ISpriteColl> list = new GameList<ISpriteColl>();
    public String type;
    //public boolean isPrecise = true;
    protected boolean isAdded;
    protected boolean autoGroupped;

    public BruteForceCollision(boolean isToAdd, String name) {
        if (isAdded = isToAdd) {
            GameState.getCurrentGEState().getCoreCollision().groups.add(this);
        }
        type = name;
    }

    public BruteForceCollision() {
        this(true, null);
    }

    public BruteForceCollision(String name) {
        this(true, name);
    }

    public BruteForceCollision(boolean isToAdd) {
        this(isToAdd, null);
    }

    @Override
    public void loop() {
        ISpriteColl sourceObj;
        ISpriteColl destinationObj;
        ICollisionMethod collMethod;

        for (int i = list.size() - 1; i > 0; --i) {
            sourceObj = list.get(i);
            collMethod = sourceObj.getCollisionMethod();
            for (int j = i - 1; j >= 0; --j) {                
                destinationObj = list.get(j);
                if (collMethod.isCompatible(destinationObj.getCollisionMethod())) {
                    collMethod.executeCollision(sourceObj, destinationObj);
                } else if (destinationObj.getCollisionMethod().isCompatible(collMethod)) {
                    destinationObj.getCollisionMethod().executeCollision(sourceObj, destinationObj);
                }
            }
        }
    }

    public void add(ISpriteColl spr) {
        list.add(spr);
    }

    public void remove(ISpriteColl spr) {
        if (spr == null) {
            return;
        }
        list.remove(spr);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) { // 0.1.9
        this.type = type;
    }

    public void destroy() {
        if (isAdded) {
            GameState.getCurrentGEState().getCoreCollision().groups.remove(this);
        }
        if (autoGroupped) {
            GameState.getCurrentGEState().getCoreCollision().autoGroups.remove(this);
        }

    }

    // --------------------------
    public static BruteForceCollision createGroup(String name) {
        return createGroup(name, true);
    }

    public static BruteForceCollision createGroup(String name, boolean isToAdd) {
        BruteForceCollision temp = new BruteForceCollision(isToAdd, name);
        temp.autoGroupped = true;
        GameState.getCurrentGEState().getCoreCollision().autoGroups.add(temp);
        return temp;
    }

    public static BruteForceCollision getGroup(String name) {
        ICollisionTester temp;
        if ((temp = GameState.getCurrentGEState().getCoreCollision().search(name)) != null) {
            try {
                return (BruteForceCollision) temp;
            } catch (Exception e) {
                Utility.error("Error on casting to BruteForceCollision",
                        "BruteForceCollision.getGroup", e);
                return null;
            }
        }
        Utility.error("type not found: " + name+"; Auto-Creating it!", "BruteForceCollision.getGroup");
        return createGroup(name);
    }
}

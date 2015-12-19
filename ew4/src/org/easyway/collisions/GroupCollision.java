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
import org.easyway.system.StaticRef;
import org.easyway.system.state.GameState;
import org.easyway.utils.Utility;

public class GroupCollision implements ICollisionTester, Serializable {

    static final long serialVersionUID = 1003;
    public String type;
    protected GameList<ISpriteColl> source = new GameList<ISpriteColl>();
    protected GameList<ISpriteColl> destination = new GameList<ISpriteColl>();
    //public boolean isPrecise = true;
    protected boolean isAdded;
    protected boolean autoGroupped;
    ManagedGroupCollision mgcFather = null;
    public int getSourceSize(){return source.size();}
    public int getDestinationSize(){return destination.size();}
    public GroupCollision(boolean isToAdd, String name) {
        if (isAdded = isToAdd) {
            GameState.getCurrentGEState().getCoreCollision().groups.add(this);
        }
        source.setType("$_S_GROUPCOLLISION");
        destination.setType("$_D_GROUPCOLLISION");
        type = name;
    }

    public GroupCollision() {
        this(true, null);

    }

    public GroupCollision(String name) {
        this(true, name);
    }

    public GroupCollision(boolean isToAdd) {
        this(isToAdd, null);
    }

    public void addToSource(ISpriteColl obj) {
        source.add(obj);
    }

    public void addToDestination(ISpriteColl obj) {
        destination.add(obj);
    }

    public void removeFromSource(ISpriteColl obj) {
        source.remove(obj);
    }

    public void removeFromDestination(ISpriteColl obj) {
        destination.remove(obj);
    }

    public void loop() {
//        System.out.println("GC-NAME: "+getType()+" SRC: "+source.size()+" DST: "+destination.size());
        ISpriteColl sourceObj;
        ISpriteColl destinationObj;
        ICollisionMethod collMethod;
        for (source.startScan(); source.next();) {

            sourceObj = source.getCurrent();
            collMethod = sourceObj.getCollisionMethod();
            for (destination.startScan(); destination.next();) {

                destinationObj = destination.getCurrent();
                if (collMethod.isCompatible(destinationObj.getCollisionMethod())) {
                    collMethod.executeCollision(sourceObj, destinationObj);
                } else if (destinationObj.getCollisionMethod().isCompatible(collMethod)) {
                    destinationObj.getCollisionMethod().executeCollision(sourceObj, destinationObj);
                }

            }
        }
    }

    public String getType() {
        return type;
    }

    //0.1.9
    public void setType(String type) {
        this.type = type;
    }

    public ManagedGroupCollision getMgcFather() {
        return mgcFather;
    }

    public void setMgcFather(ManagedGroupCollision mgcFather) {
        if ((this.mgcFather = mgcFather) != null) {
            mgcFather.groupsList.add(this);
        }
    }

    public void destroy() {
        if (isAdded) {
            GameState.getCurrentGEState().getCoreCollision().groups.remove(this);
        }
        if (autoGroupped) {
            GameState.getCurrentGEState().getCoreCollision().autoGroups.remove(this);
        }
        if (mgcFather != null) {
            mgcFather.groupsList.remove(this);
            mgcFather = null;
        }
        source.removeAll();
        destination.removeAll();
        source = null;
        destination = null;
    }

    /* @Override
    public boolean equals(Object obj) {
    if (obj instanceof GroupCollision) {
    GroupCollision gc = (GroupCollision) obj;
    return gc.getType().equals(getType());
    }
    return super.equals(obj);
    }*/
    // --------------------------
    public static GroupCollision createGroup(String name) {
        return createGroup(name, true);
    }

    public static GroupCollision createGroup(String name, boolean isToAdd) {
        GroupCollision temp = new GroupCollision(true, name);
        temp.autoGroupped = true;
        if (GameState.getCurrentGEState().getCoreCollision().autoGroups.contains(temp)) {
            throw new RuntimeException("Alrady Existing GroupCollision");
        }
        GameState.getCurrentGEState().getCoreCollision().autoGroups.add(temp);
        return temp;
    }

    public static GroupCollision getGroup(String name) {
        ICollisionTester temp;
        if ((temp = GameState.getCurrentGEState().getCoreCollision().search(name)) != null) {
            try {
                return (GroupCollision) temp;
            } catch (Exception e) {
                Utility.error("Error on casting to GroupCollision",
                        "GroupCollision.getGroup", e);
                return null;
            }
        }
        Utility.error("type not found: " + name + "; Auto-Creating it!", "GroupCollision.getGroup");
        return createGroup(name);
    }
}

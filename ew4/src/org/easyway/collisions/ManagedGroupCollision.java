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

import java.util.ArrayList;
import org.easyway.interfaces.sprites.ISpriteColl;
import org.easyway.collisions.quad.QuadNode;
import org.easyway.collisions.quad.QuadTree;
import org.easyway.lists.Entry;
import org.easyway.objects.BaseObject;

/**
 *
 * @author Daniele Paggi
 */
public class ManagedGroupCollision extends BaseObject implements IManagedCollisionableTester {

    protected static ArrayList<ManagedGroupCollision> managedGCList = new ArrayList<ManagedGroupCollision>(10);
    static int lastId = -1;
    /**
     * the name of the MGC
     */
    ArrayList<GroupCollision> groupsList;
    int id;

    public ManagedGroupCollision(String name) {
        this.name = name;
        this.type = "$_MANAGEDGROUPCOLLISION";
        if (managedGCList.contains(this)) {
            throw new RuntimeException("ManagedGroupCollision " + name + " already exists");
        }
        managedGCList.add(this);
        groupsList = new ArrayList<GroupCollision>(64);
        id = ++lastId;
        initGroups();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ManagedGroupCollision other = (ManagedGroupCollision) obj;
        return other.name.equals(name);
    }

    /*public ManagedGroupCollision getManagedGC(String name) {
        for (ManagedGroupCollision mgc : managedGCList) {
            if (mgc.name.equals(name)) {
                return mgc;
            }
        }
        Utility.error("ManagedGroupCollision with name: " + name + " don't exists: " +
                "Autogenerating it!", "ManagedGroupCollision.getManagedGC(String)");
        return new ManagedGroupCollision(name);
    }*/

    public void addToSource(ISpriteColl obj) {
        ArrayList<QuadNode> nodes = obj.getUsedInQuadNodes();
        ArrayList<GroupCollision> gcs;
        GroupCollision gc;
        for (QuadNode node : nodes) {
            gcs = node.getGroupCollisions();
            gc = gcs.get(id);
            if (!groupsList.contains(gc)) {
                throw new RuntimeException("BUG: "+gc+" id: "+id+" gcs:"+gcs);
            }
            gc.addToSource(obj);
        }
        obj.getMgcs().add(this, true);
    }

    public void addToDestination(ISpriteColl obj) {
        ArrayList<QuadNode> nodes = obj.getUsedInQuadNodes();
        ArrayList<GroupCollision> gcs;
        GroupCollision gc;
        for (QuadNode node : nodes) {
            gcs = node.getGroupCollisions();
            gc = gcs.get(id);
            if (!groupsList.contains(gc)) {
                throw new RuntimeException("BUG: "+gc+" id: "+id+" gcs:"+gcs);
            }
            gc.addToDestination(obj);
        }
        obj.getMgcs().add(this, false);
    }

    public void remove(IQuadTreeUsable obj) {
        removeFromGC(obj);
        obj.getMgcs().remove(this);
    }

    protected void removeFromGC(IQuadTreeUsable obj) {
        ArrayList<Entry> list = obj.getEntries();
        String entryListType;
        //System.out.println("__________________--");
        for (int i = list.size() - 1; i >= 0; --i) {
            Entry entry = list.get(i);
            //System.out.println(entry.getList().getType());
            if ((entryListType = entry.getList().getType()).equals("$_S_GROUPCOLLISION")) {
                entry.remove();
                //System.out.println("_______REMOVED");
                // it's a source group
            } else if (entryListType.equals("$_D_GROUPCOLLISION")) {
                entry.remove();
                //System.out.println("_______REMOVED");
                // it's a destination group
            }
        }

    }

    public void update(ISpriteColl obj) {
        removeFromGC(obj);
        boolean isSource = obj.getMgcs().getEntry(this).isSource;
        //boolean isSource = Math.random() > 0.5;
        //System.out.println("issource: "+isSource);
        ArrayList<QuadNode> nodes = obj.getUsedInQuadNodes();
        for (QuadNode node : nodes) {
            ArrayList<GroupCollision> gcs = node.getGroupCollisions();
            if (isSource) {
                gcs.get(id).addToSource(obj);
            } else {
                gcs.get(id).addToDestination(obj);
            }
        }
    }

    protected void initGroups() {
        ArrayList<QuadNode> leafList = QuadTree.getDefaultInstance().getLeafs();
        GroupCollision gc;
        for (QuadNode node : leafList) {
            groupsList.add(gc = new GroupCollision(true, name));
            gc.mgcFather = this;
            node.addGroupCollision(gc);
        }
    }

    public static ManagedGroupCollision get(String name) {
        if (!QuadTree.isUsingQuadTree()) {
            throw new RuntimeException("Can't use ManagedGroupCollision with the QuadTree disabled");
        }
        for (ManagedGroupCollision mgc : managedGCList) {
            if (mgc.name.equals(name)) {
                return mgc;
            }
        }
        return new ManagedGroupCollision(name);
    }
}

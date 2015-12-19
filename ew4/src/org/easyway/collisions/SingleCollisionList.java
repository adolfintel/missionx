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

import org.easyway.interfaces.sprites.ICollisionTester;
import org.easyway.interfaces.sprites.ISpriteColl;
import org.easyway.lists.GameList;

/** una lista per manipolare\testare le collisioni tra oggetti */
public class SingleCollisionList extends GameList<ISpriteColl> implements ICollisionTester {

    static final long serialVersionUID = 1002;
    protected ISpriteColl sprite;
    protected ICollisionMethod collMethod;

    //public boolean isPrecise = true;
    /** Costruttore */
    public SingleCollisionList(ISpriteColl spr) {
        this(true, spr);
    }

    public SingleCollisionList(boolean toAdd, ISpriteColl spr) {
        super(toAdd);
        sprite = spr;
        collMethod = spr.getCollisionMethod();
    }

    /** test the "private collisions" */
    public void loop() {
        /*ISpriteColl spr;
        for (int i = size() - 1; i >= 0; i--) {
            spr = get(i);
            if (spr == null) {
                continue;
            }*/
        for (ISpriteColl spr : this ) {
            //destinationObj = destination.getCurrent();
            if (collMethod.isCompatible(spr.getCollisionMethod())) {
                collMethod.executeCollision(sprite, spr);
            } else if (spr.getCollisionMethod().isCompatible(collMethod)) {
                spr.getCollisionMethod().executeCollision(sprite, spr);
            }
        }
    }

    @Override
    public String getType() {
        return type;
    }
}

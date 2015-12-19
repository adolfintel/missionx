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

import java.util.ArrayList;

import org.easyway.interfaces.base.IBaseObject;

public interface ICollisionable extends IBaseObject {
	/** 
	 * this method is auto-called than a collision is occurred 
	 *
	 */
	public void onCollision();
	/**
	 * returns a list filled whith all object that have done a collision with this object.
	 * @return a list of object that have done a collision. 
	 */
	public ArrayList<ISpriteColl> getCollisionList();
	
	/**
	 * indicates if the object is already added to the Auto-CollisionList
	 */
	public boolean isAddedToCollisionList();
	/**
         * sets if the object is already added to the auto-collisionlist
         * @param value
         */
	public void setAddedToCollisionList(boolean value);

        public Object getCollisionData();

        public Class getCollisionDataType();
}

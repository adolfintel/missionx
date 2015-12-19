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
package org.easyway.interfaces.base;

import org.easyway.lists.Entry;

public interface IList<T extends IBaseObject, E extends Entry<T,E>> extends IBaseObject<E> {
	/** add an object to the list */
	public void add(T obj);

	/** add an object to the list */
	public void add(T obj, int index);

	/** remove an object to the list */
	public void remove(T obj);

	/** remove an object to the list */
	public void remove(int index);

	/** return the object of index position */
	public T get(int index);

	/** returns the numeber of objects that compose the list */
	public int size();

	/**
	 * returns the index of the object
	 * 
	 * @param obj
	 *            the object to search
	 * @return the index of obj
	 */
	public int indexOf(T obj);

	/**
	 * automated stanning:<br>
	 * <br>
	 * baseList.startScan();<br>
	 * while(baseList.next() ) {<br>
	 * IBaseObject spr = baseList.getCurrent();<br>
	 * ...<br> }<br>
	 * <br>
	 */
	public void startScan();

	/**
	 * automated stanning:<br>
	 * <br>
	 * baseList.startScan();<br>
	 * while(baseList.next() ) {<br>
	 * IBaseObject spr = baseList.getCurrent();<br>
	 * ...<br> }<br>
	 * <br>
	 */
	public void startScan(int index);

	/**
	 * automated stanning:<br>
	 * <br>
	 * baseList.startScan();<br>
	 * while(baseList.next() ) {<br>
	 * IBaseObject spr = baseList.getCurrent();<br>
	 * ...<br> }<br>
	 * <br>
	 */
	public boolean next();

	/**
	 * automated stanning:<br>
	 * <br>
	 * baseList.startScan();<br>
	 * while(baseList.next() ) {<br>
	 * IBaseObject spr = baseList.getCurrent();<br>
	 * ...<br> }<br>
	 * <br>
	 */
	public T getCurrent();
	
	/** returns if the list contains or not the object 
	 * if it's contained the returned value will be different from -1<br>
	 * if it's not contained the returned value will be -1.
	 * @param obj the obj to check
	 * @return -1 if not contained.
	 */
	public boolean contains( T obj);
}

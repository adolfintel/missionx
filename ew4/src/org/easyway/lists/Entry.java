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
package org.easyway.lists;

import java.io.Serializable;
import java.util.ArrayList;
import org.easyway.interfaces.base.IBaseObject;

/**
 * The Cell of the linkedList
 * 
 * @author Daniele Paggi
 * 
 */
public class Entry<T extends IBaseObject, E extends Entry<T,E>> implements Serializable {

    public Entry(E next, E prev, T value, BaseList list) {
        this.next = next;
        this.prev = prev;
        this.value = value;
        this.list = list;
    }
    /**
     * the next Entry in the linked list
     */
    public E next;
    /**
     * the previous Entry in the linked list
     */
    public E prev;
    /**
     * the value of this entry
     */
    public T value;
    /**
     * the list in witch the Entry is used
     */
    public BaseList list;

    /**
     * indicates if the Entry is the first of the list
     * @return true if the Entry is the first of the list
     */
    boolean isFirst() {
        return prev == null;
    }

    /**
     * indicates if the Entry is the last of the list
     * @return true if Entry is the last of the list
     */
    boolean isLast() {
        return next == null;
    }

    /**
     * returns the list that contains the Entry
     * @return the list that contains the Entry
     */
    public BaseList getList() {
        return list;
    }

    /**
     * sets the list in witch is located the Entry
     * @param list the list in witch the Entry is used
     */
    public void setList(BaseList list) {
        this.list = list;
    }

    /**
     * returns the next Entry
     * @return the next Entry
     */
    public E getNext() {
        return next;
    }

    /**
     * changes the next entry
     * @param next the next entry</br>use null for set this entry as the last entry
     */
    public void setNext(E next) {
        this.next = next;
    }

    /**
     * returns the prev entry
     * @return the prev Entry
     */
    public E getPrev() {
        return prev;
    }

    /**
     * changes the prev entry
     * @param prev the prev entry</br>use null for set this entry as the first entry
     */
    public void setPrev(E prev) {
        this.prev = prev;
    }

    /**
     * returns the object contained in the entry
     * @return the object contained in the entry
     */
    public T getValue() {
        return value;
    }

    /**
     * changes the contained object
     * @param value the new object that is contained in the entry
     */
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * removes the entry from the LinkedList
     */
    public void remove() {
        if (list != null) {
            list.updateNextScanOnRemove(this);
            if (prev != null) {
                prev.next = next;
            } else {
                // it's the first!
                list.first.next = next;
            }
            if (next != null) {
                next.prev = prev;
            } else {
                // it's the last!
                list.last.prev = prev;
            }
            --list.count;

            prev = next = null;
            list = null;
        }  else {
            throw new RuntimeException();
        }
        if (value != null) {
            if (value.isDestroyed()) {
                throw new RuntimeException();
            }
            //value.getEntries();
            getEntries(value).remove(this);
            value = null;
        } else {
            throw new RuntimeException();
        }
    }

    protected ArrayList<Entry> getEntries(T value) {
        return value.getEntries();
    }
}

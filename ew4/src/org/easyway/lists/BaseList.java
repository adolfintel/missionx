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
import java.util.Iterator;
import java.util.Stack;
import org.easyway.interfaces.base.IBaseObject;
import org.easyway.interfaces.base.IList;
import org.easyway.objects.BaseObject;
import org.easyway.system.StaticRef;
import org.easyway.utils.Utility;

public class BaseList<T extends IBaseObject,E extends Entry<T,E>>
        extends BaseObject<T,E> implements IList<T,E>, Serializable, Iterable<T> {

    protected static final int SIZE_STACK = 20;
    private static final long serialVersionUID = 2076531540807338042L;
    /**
     * the first entry of the linked list
     */
    E first;
    /**
     * the last entry of the linked list
     */
    E last;
    /**
     * number of entry in the linked list
     */
    int count;
    /**
     * the current entry used for iterating the list
     */
    E indexScan;
    /**
     * the next entry used for scanning the list
     */
    E nextScan;
    /**
     * the scanning stack
     */
    Stack<E> stack, nextStack;

    /**
     * creates a new instace
     */
    public BaseList() {
        first = last = createEntry(null, null, null, this);
        indexScan = null;
        type = "$_BASELINKEDLIST";
        count = 0;
    }

    /**
     * creates a new instace
     * @param toAdd indicates if the LinkedList should be added to the GameEngine lists
     */
    public BaseList(boolean toAdd) {
        super(toAdd);
        first = last = createEntry(null, null, null, this);
        indexScan = null;
        type = "$_BASELINKEDLIST";
        count = 0;
    }

    /**
     * adds a new entry at the end of position
     * @param obj the object to add in the list
     */
    public void add(T obj) { // O( 1 )
        if (count == 0) {
            addAsFirst(obj);
        } else {
            addAtEnd(obj);
        }
    }

    /**
     * adds the object obj in the first position.<br/>
     * this method is called ONLY if the list is emty.
     * @param obj the object to add in the list
     */
    protected void addAsFirst(T obj) {
        assert count == 0;
        E entry = createEntry(null, null, obj, this);
        last.prev = entry;
        first.next = entry;
        ++count;
        getEntries(obj).add(entry);
    }

    protected E createEntry(E next, E prev, T value, BaseList list) {
        if (StaticRef.DEBUG && contains(value)) {
            throw new RuntimeException("Object already contained! Check your code!");
        }
        return (E)new Entry<T,E>(next, prev, value, list);
    }

    /**
     * adds the object obj in the last position of the list
     * @param obj the object to add in the list
     */
    public void addAtEnd(T obj) {
        // add the object at end of list
        E mlast = getLast();
        getEntries(obj).add(last.prev = mlast.next = createEntry(null, mlast, obj, this));
        ++count;
        updateNextScanOnAdd(mlast.next);
    }

    /**
     * adds the object obj in the first position of the list
     * @param obj the object to add in the list
     */
    public void addAtBegin(T obj) {
        if (count == 0) {
            addAsFirst(obj);
            return;
        }
        E mfirst = getFirst();
        getEntries(obj).add(first.next = mfirst.prev = createEntry(mfirst, null, obj, this));
        ++count;
        updateNextScanOnAdd(mfirst.prev);
    }

    public void addAfter(T objToIns, T current) {
        if (objToIns == null || current == null) {
            throw new NullPointerException("objToIns (" + objToIns + ") or current (" + current + ") is null");
        }
        E entry = getEntry(current);
        if (entry == null) {
            throw new RuntimeException("object: " + current + " is not contained");
        }
        if (entry.isLast()) {
            addAtEnd(objToIns);
            return;
        }
        E added;
        entry.next = (entry.next.prev = added = createEntry(entry.next, entry, objToIns, this));
        updateNextScanOnAdd(added);
        getEntries(objToIns).add(added);
        //objToIns.getEntries().add(added);
        ++count;
    }

    public void addAfter(T objToIns, E entry) {
        if (objToIns == null) {
            throw new NullPointerException("objToIns is null");
        }
        if (entry == null) {
            throw new RuntimeException("entry is not contained");
        }
        if (entry.list != this) {
            throw new RuntimeException("wrong entry: it's used in another linkedList");
        }
        if (entry.isLast()) {
            addAtEnd(objToIns);
            return;
        }
        E added;
        entry.next = (entry.next.prev = added = createEntry(entry.next, entry, objToIns, this));
        updateNextScanOnAdd(added);
        getEntries(objToIns).add(added);
        //objToIns.getEntries().add(added);
        ++count;
    }

    public void addBefore(T objToIns, T current) {
        if (objToIns == null || current == null) {
            throw new NullPointerException("objToIns (" + objToIns + ") or current (" + current + ") is null");
        }
        E entry = getEntry(current);
        if (entry == null) {
            throw new RuntimeException("object: " + current + " is not contained");
        }
        E added;
        entry.prev = (entry.prev.next = (added = createEntry(entry, entry.prev, objToIns, this)));
        // entry.next = (entry.next.prev = added = new Entry(entry.next, entry, objToIns, this));
        updateNextScanOnAdd(added);
        getEntries(objToIns).add(added);
        //objToIns.getEntries().add(added);
        ++count;
    }

    public void addBefore(T objToIns, E entry) {
        if (objToIns == null) {
            throw new NullPointerException("objToIns is null");
        }
        if (entry == null) {
            throw new RuntimeException("entry is not contained");
        }
        if (entry.list != this) {
            throw new RuntimeException("wrong entry: it's used in another linkedList");
        }
        E added;
        entry.prev = (entry.prev.next = (added = createEntry(entry, entry.prev, objToIns, this)));
        // entry.next = (entry.next.prev = added = new Entry(entry.next, entry, objToIns, this));
        updateNextScanOnAdd(added);
        getEntries(objToIns).add(added);
        //objToIns.getEntries().add(added);
        ++count;
    }

    public void addElementsFrom(BaseList<T,E> list) {
        for (T obj : list) {
            add(obj);
        }
    }

    public void add(T obj, int index) { // O( n/2 )
        if (count == 0) {
            addAsFirst(obj);
            return;
        }
        if (index == 0) {
            addAtBegin(obj);
        } else if (index >= count) {
            addAtEnd(obj);
        } else {
            if (index >= count / 2f) {
                E added;
                int i = count - 1;
                E current = getLast();
                while (i != index) {
                    assert current.prev != null;
                    current = current.prev;
                    --i;
                }
                current.prev = (current.prev.next = (added = createEntry(
                        current, current.prev, obj, this)));
                updateNextScanOnAdd(added);
                ++count;
                getEntries(obj).add(added);
            } else {
                E added;
                // assert index<count;
                int i = 1; // note: I'll start counting from 1 and not from
                // 0
                E current = getFirst();
                while (i != index) {
                    current = current.next;
                    ++i;
                }
                current.next = (current.next.prev = (added = createEntry(
                        current, current.next, obj, this)));
                updateNextScanOnAdd(added);
                ++count;
                getEntries(obj).add(added);
            }
        }

    }

    public E getEntry(T obj) {
        if (count == 0) {
            return null;
        }
        ArrayList<E> list = getEntries(obj);
        E e;

        for (int i=list.size()-1; i>=0; --i) {
            if ((e = list.get(i)).getList() == this) {
                return e;
            }
        }
        return null;
    }

    protected ArrayList<E> getEntries(T obj) {
        return obj.getEntries();
    }

    /**
     * returns 1 if the object is contained<br/>
     * if the object is not contained returns -1
     * @param obj the object to search
     * @return 1 if the object is contained<br/> -1 if the object is not contained
     */
    public boolean contains(T obj) { // O( n )
        if (count == 0) {
            return false;
        }

        for (E e : getEntries(obj)) {
            if (e.getList() == this) {
                return true;
            }
        }
        return false;
    }

    public T get(int index) { // O( n/2 )
        if (count == 0) {
            return null;
        }
        if (index < 0 || index >= count) {
            Utility.error("BaseLinkedList.get(int)", new Exception("index out of range"));
        }

        if (index >= count / 2) {
            int i = count - 1;
            E current = getLast();
            while (i != index) {
                assert current.prev != null;
                current = current.prev;
                --i;
            }
            return current.getValue();
        } else {
            int i = 0;
            E current = getFirst();
            while (i != index) {
                assert current.next != null;
                current = current.next;
                ++i;
            }
            return current.getValue();
        }
    }

    public int indexOf(T obj) { // O( n )
        if (count == 0) {
            return -1;
        }
        int index = 0;

        E current = getFirst();
        do {
            if (current.getValue().equals(obj)) {
                return index;
            }
            ++index;
            current = current.next;
        } while (current != null);
        return -1; // not found
    }

    public void remove(T obj) {
        if (count == 0) {
            return;
        }

        for (E e : getEntries(obj)) {
            if (e.getList() == this) {
                e.remove();
                return;
            }
        }
    }

    public void remove(int index) { // O( n/2 )
        if (count == 0) {
            return;
        }
        if (index < 0 || index >= count) {
            Utility.error("BaseLinkedList.remove(int)", new Exception("index out of range"));
        }

        if (index >= count / 2) {
            int i = count - 1;
            E current = getLast();
            while (i != index) {
                assert current.prev != null;
                current = current.prev;
                --i;
            }
            current.remove();
            // already the Entry decrease the count field..
        } else {
            int i = 0;
            E current = getFirst();
            while (i != index) {
                assert current.next != null;
                current = current.next;
                ++i;
            }
            current.remove();
            // already the Entry decrease the count field..
        }
        // not found
    }

    protected void addToEntry(T obj, E entry) {
        getEntries(obj).add(entry);
        //obj.getEntries().add(entry);
    }

    /**
     * update the reference that point to the next entry in the linked list when
     * ad object is added to the list
     * @param current
     */
    protected void updateNextScanOnAdd(E current) {
        if (nextScan == current.getPrev()) { // it was a bug?
            nextScan = current;
        }
        if (nextStack != null) {
            for (Entry<T,?> e : nextStack) {
                if (e == current.getPrev()) { // it was a bug?
                    e = current;
                }
            }
        }
    }

    /**
     * updates the reference that point to the next entry in the linked list
     * when an object is removed fromm the list
     * @param current
     */
    protected void updateNextScanOnRemove(E current) {
        if (nextScan == current) {
            nextScan = current.prev;
        }
        if (nextStack != null) {
            for (Entry<T,?> e : nextStack) {
                if (e == current) {
                    e = current.prev;
                }
            }
        }
    }

    public int size() {
        /*if (StaticRef.DEBUG) {
            int c = 0;
            Entry entry = getFirst();
            while (entry != null) {
                ++c;
                entry = entry.next;
            }
            if (c != count) {
                throw new RuntimeException();
            }
        }*/
        return count;
    }

    /**
     * removes all the objects from the list
     */
    public void removeAll() {
        breakScan(); // stops any iteration
        while (getFirst() != null) {
            getFirst().remove();
        }
    }

    /**
     * removes and destry all the object from the list<br/>
     * Note: this will call the destroy() method toeach object; not the kill() method
     */
    public void destroyAll() {
        breakScan(); // stops any iteration
        while (getFirst() != null) {
            getFirst().getValue().destroy();
        }
    }

    public void startScan() {
        if (indexScan != null) {
            pushIndexScan();
        }
        indexScan = last;
        nextScan = indexScan.getPrev();
    }

    public void startScan(int index) {
        if (count == 0) {
            return;
        }
        if (index < 0 || index >= count) {
            Utility.error("BaseLinkedList.startScan(int)", new Exception("index out of range"));
        }

        if (indexScan != null) {
            pushIndexScan();
        }

        if (index >= count / 2) {
            int i = count - 1;
            E current = getLast();
            while (i != index) {
                assert current.prev != null;
                current = current.prev;
                --i;
            }
            indexScan = current;
            nextScan = indexScan;
            // already the Entry decrease the count field..
        } else {
            int i = 0;
            E current = getFirst();
            while (i != index) {
                assert current.next != null;
                current = current.next;
                ++i;
            }
            indexScan = current;
            nextScan = indexScan;
            // already the Entry decrease the count field..
        }
    }

    /**
     * stops the scanning of the list
     */
    public void breakScan() {
        if (stack != null) {
            stack.empty();
            nextStack.empty();
        }
        indexScan = null;
        nextScan = null;
    }

    /**
     * push a scanning pointer
     */
    protected void pushIndexScan() {
        if (stack == null) {
            stack = new Stack<E>();
            new RuntimeException();
            nextStack = new Stack<E>();
        }
        stack.push(indexScan);
        nextStack.push(nextScan);
    }

    /**
     * pops a scanning pointer
     */
    protected void popIndexScan() {
        if (stack != null) {
            indexScan = stack.pop();
            nextScan = nextStack.pop();
        }
    }

    public T getCurrent() {
        if (indexScan != null) {
            return indexScan.getValue();
        }
        return null;
    }

    public boolean next() {
        if (indexScan != null /*&& nextScan != null*/) {
            indexScan = nextScan;
            if (indexScan != null) {
                nextScan = indexScan.getPrev();
            }
            return indexScan != null;
        }
        return false;
    }

    /**
     * returns the first element of the list
     * @return the first element of the list
     */
    public E getFirst() {
        return first.next;
    }

    /**
     * returns the last element of the list 
     * @return the last element of the list
     */
    public E getLast() {
        return last.prev;
    }

    public Iterator<T> iterator() {
        return new BaseListIterator<T>(this);
    }

    @Override
    protected void finalize() throws Throwable {
        removeAll();
        super.finalize();
    }


}

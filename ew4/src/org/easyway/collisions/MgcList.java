/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.collisions;

import java.util.ArrayList;
import org.easyway.lists.BaseList;

/**
 *
 * @author RemalKoil
 */
public class MgcList extends BaseList<ManagedGroupCollision, MgcEntry> {

    boolean whereToAdd;

    public MgcList(boolean toAdd) {
        super(toAdd);
    }

    public MgcList() {
    }

    public void add( ManagedGroupCollision mgc, boolean toSource ) {
        whereToAdd = toSource;
        add(mgc);
    }

    @Override
    protected MgcEntry createEntry(MgcEntry next, MgcEntry prev, ManagedGroupCollision value, BaseList list) {
        return new MgcEntry(whereToAdd, next, prev, value, list);
    }


    @Override
    public boolean contains(ManagedGroupCollision obj) { // O( n )
        return indexOf(obj) >= 0;
    }

    @Override
    public MgcEntry getEntry(ManagedGroupCollision obj) {
        MgcEntry entry = getFirst();
        while (entry.getValue() != obj) {
            entry = entry.getNext();
        }
        return entry;
    }



}

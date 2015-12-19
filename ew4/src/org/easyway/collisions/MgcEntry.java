/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.collisions;

import java.util.ArrayList;
import org.easyway.lists.BaseList;
import org.easyway.lists.Entry;

/**
 *
 * @author Daniele Paggi
 */
public class MgcEntry extends Entry<ManagedGroupCollision, MgcEntry> {

    boolean isSource;

    public MgcEntry(boolean isSource, MgcEntry next, MgcEntry prev, ManagedGroupCollision value, BaseList list) {
        super(next, prev, value, list);
        this.isSource = isSource;
    }

    public boolean isSource() {
        return isSource;
    }

}

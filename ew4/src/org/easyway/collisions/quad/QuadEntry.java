/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.collisions.quad;

import java.util.ArrayList;
import org.easyway.interfaces.sprites.IPlain2D;
import org.easyway.lists.BaseList;
import org.easyway.lists.Entry;

/**
 *
 * @author Daniele Paggi
 */
public class QuadEntry<T extends IPlain2D> extends Entry<T, QuadEntry<T>> {

    public QuadEntry(QuadEntry<T> next, QuadEntry<T> prev, T value, BaseList list) {
        super(next, prev, value, list);
    }

    @Override
    protected ArrayList<Entry> getEntries(T value) {
        return value.getQuadEntries();
    }


    
}

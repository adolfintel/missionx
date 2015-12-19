/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.collisions.quad;

import java.util.ArrayList;
import org.easyway.collisions.IQuadTreeUsable;
import org.easyway.lists.BaseList;

/**
 *
 * @author Daniele Paggi
 */
public class QuadNodeChildList<T extends IQuadTreeUsable> extends BaseList<T,QuadEntry<T>> {

    public QuadNodeChildList() {
        super();
        setType("$_QUADLIST");
    }

    public QuadNodeChildList(boolean toAdd) {
        super(toAdd);
    }

    @Override
    protected ArrayList<QuadEntry<T>> getEntries(T obj) {
        return obj.getQuadEntries();
    }

    @Override
    protected QuadEntry<T> createEntry(QuadEntry<T> next, QuadEntry<T> prev, T value, BaseList list) {
        return new QuadEntry(next, prev, value, list);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easyway.lists;

import java.util.Iterator;
import org.easyway.interfaces.base.IBaseObject;

/**
 *
 * @author Daniele Paggi
 */
public class BaseListIterator<T extends IBaseObject> implements Iterator {

    BaseList list;

    public BaseListIterator(BaseList list) {
        this.list = list;
        list.startScan();
    }

    public boolean hasNext() {
        return list.next();
    }

    public IBaseObject next() {
        return list.getCurrent();
    }

    public void remove() {

    }
}

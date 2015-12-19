/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easyway.lists;

import org.easyway.interfaces.base.IBaseObject;

/**
 *
 * @author Daniele Paggi
 */
public class GameEntry<T extends IBaseObject> extends Entry<T,GameEntry<T>> {
    public GameEntry(GameEntry<T> next, GameEntry<T> prev, T value, BaseList list) {
        super(next, prev, value, list);
    }
}

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
public class GameList<T extends IBaseObject>
        extends BaseList<T, GameEntry<T>> {

    public GameList(boolean toAdd) {
        super(toAdd);
    }

    public GameList() {
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easyway.input;

import org.easyway.interfaces.base.IBaseObject;
import org.easyway.lists.BaseList;
import org.easyway.lists.GameEntry;

/**
 *
 * @author Daniele Paggi
 */
public class MouseEntry extends GameEntry {
    
    IBaseObject obj;

    public MouseEntry(GameEntry next, GameEntry prev, IBaseObject value, MouseAssociation ass, BaseList list) {
        super(next, prev, ass, list);
        this.obj = value;
    }

    @Override
    public void remove() {
        super.remove();
        obj.getEntries().remove(this);
    }
    
}

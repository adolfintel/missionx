/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easyway.lists;

import org.easyway.interfaces.extended.ILoopable;

/**
 *
 * @author Daniele Paggi
 */
public class LoopEntry extends Entry<ILoopable<LoopEntry>, LoopEntry>{

    public LoopEntry(LoopEntry next, LoopEntry prev, ILoopable<LoopEntry> value, BaseList list) {
        super(next, prev, value, list);
    }

}

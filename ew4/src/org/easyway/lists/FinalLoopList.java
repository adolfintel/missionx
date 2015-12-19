/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easyway.lists;

import java.io.Serializable;
import org.easyway.interfaces.extended.IFinalLoopable;

/**
 *
 * @author Daniele Paggi
 */
public class FinalLoopList <T extends IFinalLoopable<E>, E extends Entry<T,E>>
        extends BaseList<T,E>
        implements IFinalLoopable<E>, Serializable {

    public FinalLoopList(boolean toadd) {
        super(toadd);
    }

    public FinalLoopList() {
    }


    public void finalLoop() {
        for(IFinalLoopable lobj : this ) {
            lobj.finalLoop();
        }
    }

}

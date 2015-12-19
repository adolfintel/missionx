/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easyway.lists;

import java.io.Serializable;
import org.easyway.interfaces.extended.IRender;

/**
 *
 * @author Daniele Paggi
 */
public class DrawingList <T extends IRender<E>, E extends Entry<T,E>>
        extends BaseList<T,E>
        implements IRender<E>, Serializable{

    public DrawingList() {
    }

    public void render() {
        for( IRender obj : this ) {
            obj.render();
        }
    }

}

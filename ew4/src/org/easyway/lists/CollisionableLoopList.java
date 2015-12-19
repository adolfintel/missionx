/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.lists;

import java.io.Serializable;
import java.util.ArrayList;
import org.easyway.interfaces.base.IPureLoopable;
import org.easyway.interfaces.sprites.ICollisionable;

/**
 *
 * @author Daniele Paggi
 */
public class CollisionableLoopList extends ArrayList<ICollisionable> implements
        IPureLoopable, Serializable {

    public CollisionableLoopList() {
    }

    @Override
    public boolean add(ICollisionable e) {
        return super.add(e);
    }

    public void loop() {
        //for (ICollisionable spr : this) {
        ICollisionable spr;
        for (int i=0; i<this.size(); ++i) {
            spr = get(i);
            spr.onCollision();
            spr.setAddedToCollisionList(false);
            spr.getCollisionList().clear();
        }
        clear();
    }
    
}

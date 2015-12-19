/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.objects.rules;

import org.easyway.interfaces.base.IBaseObject;
import org.easyway.interfaces.base.IRule;
import org.easyway.lists.GameList;
import org.easyway.objects.BaseObject;

/**
 *
 * @author Daniele Paggi
 */
public abstract class GameRule<T extends IBaseObject> extends BaseObject implements IRule {
    
    GameList<T> objects;
    private int order;

    public GameRule() {
        objects = new GameList<T>(false);
        setType("$_GAMERULE");
    }

    public void addObject(T object) {
        objects.add(object);
    }

    public void removeObject(T object){
        objects.remove(object);
    }

    public void clear() {
        objects.removeAll();
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public abstract void loop();
}

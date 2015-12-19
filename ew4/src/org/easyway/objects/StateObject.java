/* EasyWay Game Engine
 * Copyright (C) 2006 Daniele Paggi.
 *  
 * Written by: 2006 Daniele Paggi<dshnt@hotmail.com>
 *   
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 * 
 * You should have received a copy of the GNU Library General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.easyway.objects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniele Paggi
 */
public abstract class StateObject<OBJ> implements Serializable {

    public static final HashMap<String, StateObject> allStates = new HashMap<String, StateObject>();

    public static final StateObject getStateObject(String name) {
        if (name != null && allStates.containsKey(name)) {
            return allStates.get(name);
        }
        return null;
    }

    public static final boolean addStateObject(String name, StateObject stateObject) {
        if (name == null || allStates.containsKey(name) || stateObject == null) {
            return false;
        }
        stateObject.name = name;
        allStates.put(name, stateObject);
        return true;
    }

    public static final boolean addStateObject(String name, Class<? extends StateObject> clazz) {
        if (name == null || allStates.containsKey(name) || clazz == null) {
            return false;
        }
        StateObject stateObj = null;
        try {
            stateObj = clazz.newInstance();
            stateObj.name = name;
        } catch (InstantiationException ex) {
            Logger.getLogger(StateObject.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IllegalAccessException ex) {
            Logger.getLogger(StateObject.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        allStates.put(name, stateObj);
        return true;
    }
    /**
     * the referencied object
     */
    protected OBJ This;
    protected String name;

    public StateObject() {
        This = null;
    }

    public StateObject(OBJ object) {
        setObject(object);
    }

    /**
     * Create a new instances of a StateObject and automatically add it to the HashMap of StateObjects<br/>
     * @param name the UNIQUE name of the stateObject
     */
    public StateObject(String name, OBJ object) {
        if (!addStateObject(name, this)) {
            org.easyway.utils.Utility.error("StateObject already created with name: " + name, "StateObject(String)", new RuntimeException("StateObject already created with name: " + name));
        }
        setObject(object);
    }

    /**
     * change the referencied object
     * @param object the object to point to
     */
    public void setObject(OBJ object) {
        this.This = object;
        init();
    }

    /**
     * initialize the state
     */
    public abstract void init();

    /**
     * the loop state code
     */
    public abstract void loop();

    /**
     * the onCollision state code
     */
    public void onCollision() {
    }

    /**
     * the finalLoop state code
     */
    public void finalLoop() {
    }
}

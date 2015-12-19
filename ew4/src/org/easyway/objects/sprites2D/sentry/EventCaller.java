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
package org.easyway.objects.sprites2D.sentry;

import org.easyway.objects.*;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.easyway.interfaces.extended.IDrawing;
import org.easyway.system.state.GameState;
import org.easyway.utils.Utility;

/**
 *
 * @author Daniele Paggi
 */
public class EventCaller extends BaseObject implements IDrawing {

    /**
     * the number of signal input
     */
    protected int inputNumber;
    /**
     * the number of recived signal input
     */
    protected int recivedInputNumber;
    /**
     * target object
     */
    protected Object target;
    /**
     * method that will be called on the target object
     */
    transient protected Method targetMethod;
    /**
     * name of the method
     */
    String methodName;
    /**
     * default parameter for the call method 
     */
    protected static Object[] defaultParams = {};
    /**
     * records what are the objects that have already called the signal method
     */
    Set<Object> set = new HashSet<Object>();

    public EventCaller(Object target, String methodName, int inputNumber) {
        this.target = target;
        this.inputNumber = inputNumber;
        this.methodName = methodName;
        getMethod();
        GameState.getCurrentGEState().getLayers()[GameState.getCurrentGEState().getLayers().length - 1].add(this);
    }

    private void getMethod() {
        Class clazz = target.getClass();
        boolean retry;
        do {
            retry = false;
            try {
                targetMethod = clazz.getDeclaredMethod(methodName, new Class[]{});
            } catch (NoSuchMethodException ex) {
                clazz = clazz.getSuperclass();
                if (clazz == Object.class) {
                    Utility.error("Method: " + methodName + " in " + target + " not found", ex);
                } else {
                    retry = true;
                }
            }
        } while (retry);
    }

    public void render() {
        recivedInputNumber = 0;
        set.clear();
    }

    public void callEvent() {
        if (targetMethod != null && target != null) {
            try {
                targetMethod.invoke(target, defaultParams);
            } catch (Exception ex) {
                Utility.error("error calling the event", ex);
            }
        }
    }

    /**
     *
     * send a signal message to the EventCaller onlt if
     * is the first time that the 'object id' is used as parameter in this game's loop<br/>
     * After "inputNumber" signal message will be automatically called the callEvent method
     * @param id
     */
    public void signal(Object id) {
        if (!set.contains(id)) {
            set.add(id);
            signal();
        }
    }

    /**
     * send a signal message to the EventCaller<br/>
     * After "inputNumber" signal message will be automatically called the callEvent method
     */
    public void signal() {
        ++recivedInputNumber;

        // Using recivedInputNumber == inputNumber and not:
        // recivedInputNumber >= inputNumber
        // we'll gain that the callEvent will be called only 1 time for each 
        // loop data game
        if (recivedInputNumber == inputNumber) {
            callEvent();
        }
    }

    public int getRecivedInputNumber() {
        return recivedInputNumber;
    }

    public int getInputNumber() {
        return inputNumber;
    }

    public void setInputNumber(int inputNumber) {
        this.inputNumber = inputNumber;
    }

    public int getLayer() {
        return 0;
    }

    public void destroy() {
        if (!isDestroyed()) {
            return;
        }
        target = null;        // GC speed up
        super.destroy();
    }

    private void readObject(java.io.ObjectInputStream in)
            throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        getMethod();
    }
}

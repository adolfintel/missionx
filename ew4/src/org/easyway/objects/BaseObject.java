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
import java.util.ArrayList;

import org.easyway.collisions.IQuadTreeUsable;
import org.easyway.collisions.quad.QuadTree;
import org.easyway.input.Mouse;
import org.easyway.interfaces.base.IBaseObject;
import org.easyway.interfaces.base.IDestroyable;
import org.easyway.interfaces.base.IType;
import org.easyway.interfaces.extended.IDrawing;
import org.easyway.interfaces.extended.IFinalLoopable;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.interfaces.extended.IRender;
import org.easyway.interfaces.sprites.IClickable;
import org.easyway.lists.BaseList;
import org.easyway.lists.DrawingLayaredList;
import org.easyway.lists.Entry;
import org.easyway.system.StaticRef;
import org.easyway.system.state.GameState;

/**
 * Generally all the classes in the Game engine are extended by this class.<br>
 * a BaseObject object has the capacity to:<br> - record his history: where
 * it's used and referenced<br> - auto-adds it self to Game Engine apparates
 * (for example render lists)<br> - auto-removes it self from Game Engine and
 * other references when it's necessary (for example on destroy)
 *
 * @author Daniele Paggi
 * @version 3
 */
public abstract class BaseObject<T extends IBaseObject, E extends Entry<T, E>> implements
        IType,
        IDestroyable,
        Serializable,
        IBaseObject<E> {

    private static final long serialVersionUID = 1854678327275417660L;
    /** indicates if the object is destroyed or not */
    private boolean destroyed = false;
    /** a list of entry-lists that references this object */
    protected ArrayList<E> usedInLinkedList;
    /** type of object */
    protected String type = "$";
    /** name of the object */
    protected String name = null;
    /**
     * if setted to true when this object is created it'll auto-added to game
     * engine apparates
     */
    public static boolean autoAddToLists = true;
    /**
     * indicates if the object is to kill or destroy<br>
     * If the object is to kill will not executed the onDestroy method.
     */
    public static boolean KILL = false;
    /**
     * indicates if the class has the method:<br>
     * public void loop()
     *
     * @see ILoopable
     */
    protected boolean isLoopable;
    /**
     * indicates if the class has the method:<br>
     * public void finalLoop()
     *
     * @see IFinalLoopable
     */
    protected boolean isFinalLoopable;
    /**
     * indicates if the class has the method:<br>
     * public void render()
     *
     * @see IRender
     */
    protected boolean isDrawable;
    /**
     * indicates if the class impelemnts the IClickable interface;
     *
     * @see IClickable;
     */
    protected boolean isClickable;
    /**
     * indicates if the object implements IManagedCollisionable interface
     */
    protected boolean isQuadTreeUsable;

    /** crates a new instance of object */
    public BaseObject() {
        this(autoAddToLists);
    }

    /**
     * crates a new instance of object
     *
     * @param autoAddToLists
     *            indicates if it'll auto-added to game engine apparates
     * @param idLayer
     *            indicates in witch layer (drawing-sheet) this object will be
     *            added to.
     */
    public BaseObject(boolean autoAddToLists) {
        usedInLinkedList = new ArrayList<E>(10);
        autoScanInterfaces();
        autoActivate(autoAddToLists);
    }

    /**
     * clone the object
     * @param obj the orinigal object
     */
    public BaseObject(BaseObject<T, E> obj) {
        destroyed = obj.destroyed;
        setType(obj.type);
        setName(obj.name);
        usedInLinkedList = new ArrayList<E>(10);
        autoScanInterfaces();
        autoActivate(true);
        for (E entry : obj.usedInLinkedList) {
            entry.getList().add(this);
        }
    }

    /**
     * automatically scans the interfaces and obtain information about the object
     */
    protected void autoScanInterfaces() {
        isDrawable = this instanceof IRender;
        isLoopable = this instanceof ILoopable;
        isFinalLoopable = this instanceof IFinalLoopable;
        isClickable = this instanceof IClickable;
        isQuadTreeUsable = this instanceof IQuadTreeUsable && QuadTree.isUsingQuadTree();
    }

    /**
     * checks if the game engine should automatically activate the object
     * @return true if the game engine should automatically activate the object.
     */
    protected boolean autoActivateTest() {
        return StaticRef.initialized && autoAddToLists;
    }

    /**
     * auto adds the BaseObject to the game engine's lists
     *
     */
    protected void autoActivate(boolean toAdd) {
        if (autoActivateTest() && toAdd) {
            activate();
        }
    }

    /**
     * activate the object
     */
    protected void activate() {
        if (isClickable) {
            Mouse.add((IClickable) this);
        }
        if (isLoopable) {
            GameState.getCurrentGEState().getLoopList().add((ILoopable) this);
        }
        if (isFinalLoopable) {
            GameState.getCurrentGEState().getFinalLoopList().add((IFinalLoopable) this);
        }
    }

    /**
     * disactivate the object:<br/>
     * remove the object from the game engine lists
     */
    protected void disactivate() {
        ArrayList<BaseList> lists = new ArrayList<BaseList>();
        for (int i = usedInLinkedList.size() - 1; i >= 0; i--) {
            Entry e = usedInLinkedList.get(i);
            lists.add(e.list);
            usedInLinkedList.get(i).remove();
        }

        if (StaticRef.DEBUG) {
            for (BaseList list : lists) {
                if (list.contains(this)) {
                    throw new RuntimeException();
                }
            }
        }
    }

    /**
     * removes and re-add itself from all drawing list that contain it<br>
     * It's used generally when the layer (not IdLayer) change
     */
    protected void readdToDrawingLists() {
        if (!isDrawable) {
            return;
        }

        DrawingLayaredList tempL;
        for (int i = usedInLinkedList.size() - 1; i >= 0; --i) {
            if (usedInLinkedList.get(i).list instanceof DrawingLayaredList) {
                tempL = (DrawingLayaredList) usedInLinkedList.get(i).list;
                tempL.remove((IDrawing) this);
                tempL.add((IDrawing) this);
            }
        }
    }

    /**
     * returns if the object is destroyed or not
     *
     * @return the object is destroyed?
     */
    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * destroys the object<br>
     * <b>Note:</b> when You extends a class from this remember to call
     * "super.destroy()"!<br>
     * for example:<br>
     * <br>
     * <font color=GREEN>public class</font> MyClass <font color=GREEN>extends</font>
     * BaseObject <font color=GREEN>{</font><br>
     * ...<br>
     * <font color=GREEN>public void</font> destroy() <font color=GREEN>{</font><br>
     * if (isDestroyed()) <br>
     * return;<br>
     * super.detroy();<br>
     * ...<br>
     * <font color=GREEN>}<br> }<br>
     * </font>
     *
     * @see #kill()
     */
    @Override
    public void destroy() {
        if (destroyed) {
            return;
        }
        if (!KILL) // 0.1.6
        {
            onDestroy();
        }
        disactivate();
        destroyed = true;
    }

    @Override
    protected void finalize() throws Throwable {
        if (!isDestroyed()) {
            kill();
        }
        super.finalize();
    }

    /**
     * kill the object.<br>
     * It's similar to the destroy method but this will not call the onDestroy
     * method.<br>
     *
     * @see #destroy()
     */
    public final void kill() {
        boolean backupKILL = KILL;
        KILL = true;
        destroy();
        KILL = backupKILL;
    }

    /**
     * this method is called when the object is destroyed<br>
     * example:<br>
     * <font color=GREEN>public class</font> MyClass <font color=GREEN>extends</font>
     * BaseObject <font color=GREEN>{</font><br>
     * ...<br>
     * <font color=GREEN>public void</font> onDestroy() <font color=GREEN>{</font><br>
     * scores += 1; <font color=GREEN>//increases the score<br> }<br> }</font><br>
     *
     */
    protected void onDestroy() {
    }

    @Override
    public String getType() {
        return type;
    }

    /**
     * changes the type object
     * @param type the type of object
     */
    public void setType(String type) {
        this.type = type;
    }

    /** returns the object's name
     * @return the object's name
     */
    public String getName() {
        return name;
    }

    /**
     * sets a new name to the object
     * @param name the new name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ArrayList<E> getEntries() {
        return usedInLinkedList;
    }

    @Override
    public String toString() {
        int index = getClass().getName().lastIndexOf(".") + 1;
        return "[ @" + getClass().getName().substring(index) + " Type: " + type + " Name: " + name + "]";
    }
}

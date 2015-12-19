/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.ueditor2.system;

import java.util.Vector;
import java.util.concurrent.LinkedBlockingDeque;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.interfaces.sprites.IPlain2D;
import org.easyway.lists.BaseList;
import org.easyway.objects.BaseObject;
import org.easyway.objects.sprites2D.SimpleSprite;
import org.easyway.system.state.GameState;

/**
 *
 * @author Daniele
 */
public class ObjectList {

    Vector<Object> list = new Vector<Object>();

    public ObjectList() {
        clearList();
        scanLayers();
        scanLoopList();
    }

    public void completeScan() {
        clearList();
        scanLayers();
        scanLoopList();
    }

    public void scanLoopList() {
        for (ILoopable loopable : GameState.getCurrentGEState().getLoopList()) {
            if (!list.contains(loopable)) {
                list.add(loopable);
            }
        }
    }

    public void clearList() {
        list.clear();
    }

    public void scanLayers() {
        LinkedBlockingDeque<BaseList> others = new LinkedBlockingDeque<BaseList>();

        for (int i = 0; i < GameState.getCurrentGEState().getLayers().length; ++i) {
            others.push(GameState.getCurrentGEState().getLayers()[i]);
        }

        while (others.size() > 0) {
            BaseList blist = others.pop();
            for (Object obj : blist) {
                if (obj instanceof BaseList) {
                    others.push((BaseList) obj);
                }
                if (!list.contains(obj)) {
                    list.add(obj);
                }
            }
        }
    }

    public Vector<Object> getObjects() {
        return (Vector<Object>) list.clone();
    }

    public Vector<BaseObject> getBaseObjects() {
        return getObjects(BaseObject.class);
    }

    public Vector<IPlain2D> getIPlain2D() {
        return getObjects(IPlain2D.class);
    }

    public Vector<SimpleSprite> getSprite() {
        return getObjects(SimpleSprite.class);
    }

    public Vector<BaseObject> getObjects(String name) {
        Vector<BaseObject> outList = new Vector<BaseObject>(list.size());
        for (Object obj : list) {
            if (obj.toString().toUpperCase().contains(name.toUpperCase())) {
                outList.add((BaseObject) obj);
            }
        }
        return outList;
    }

    public Vector getObjects(Class<?> clazz) {
        Vector outList = new Vector(list.size());
        for (Object obj : list) {
            try {
                clazz.cast(obj); // instance of
                outList.add(obj);
            } catch (ClassCastException exp) {
            }
        }
        return outList;
    }
}

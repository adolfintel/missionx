/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.lists;

import java.io.Serializable;
import java.util.ArrayList;
import org.easyway.interfaces.extended.IDrawing;
import org.easyway.interfaces.extended.IRender;
import org.easyway.system.StaticRef;

/**
 *
 * @author Daniele Paggi
 */
public class DrawingLayaredList
        extends LayerList<IDrawing<DrawingEntry>,DrawingEntry>
        implements IRender<DrawingEntry>, Serializable, Iterable<IDrawing<DrawingEntry>> {

    public DrawingLayaredList() {
    }

    public void render() {
        for (IDrawing obj : this) {
            obj.render();
        }
    }

    @Override
    protected ArrayList<DrawingEntry> getEntries(IDrawing<DrawingEntry> obj) {
        return super.getEntries(obj);
    }


    @Override
    protected DrawingEntry createEntry(DrawingEntry next, DrawingEntry prev, IDrawing<DrawingEntry> value, BaseList list) {
        if (StaticRef.DEBUG && contains(value)) {
            throw new RuntimeException("Object already contained! Check your code!");
        }
        return new DrawingEntry(next, prev, value, list);
    }



}

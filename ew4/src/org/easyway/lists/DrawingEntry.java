/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.lists;

import org.easyway.interfaces.extended.IDrawing;

/**
 *
 * @author Daniele Paggi
 */
public class DrawingEntry extends Entry<IDrawing<DrawingEntry>, DrawingEntry> {

    public DrawingEntry(DrawingEntry next, DrawingEntry prev, IDrawing value, BaseList list) {
        super(next, prev, value, list);
    }
}

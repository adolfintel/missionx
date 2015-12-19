/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.lists;

import java.io.Serializable;
import org.easyway.interfaces.extended.ILayer;

/**
 *
 * @author Daniele Paggi
 */
public class Layer extends DrawingLayaredList implements ILayer<DrawingEntry>, Serializable {

    int layer;

    public Layer() {
        setLayer(0);
    }

    public Layer(int layer) {
        setLayer(layer);
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
        readdToDrawingLists();
    }
}

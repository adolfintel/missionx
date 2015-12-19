/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.lists;

import java.io.Serializable;

import org.easyway.interfaces.extended.ILayer;
import org.easyway.utils.Utility;

/**
 *
 * @author Daniele Paggi
 */
public class LayerList<T extends ILayer<E>, E extends Entry<T,E>> 
        extends BaseList<T,E>
        implements Serializable {

    int minLayer;
    int maxLayer;

    public LayerList() {
        super(false);
        setType("$_LAYERLINKEDLIST");
    }

    @Override
    public void add(T obj) {
        generateMaxMinLayers();
        // ILayer lobj = (ILayer) obj;
        int objLayer = obj.getLayer();
        if (minLayer == maxLayer) {
            if (size() == 0) {
                minLayer = maxLayer = objLayer;
            }
            // assert objlay == minLayer;
            if (objLayer == minLayer) {
                super.add(obj);
                return;
            }
        }
        if (objLayer <= minLayer) {
            minLayer = objLayer;
            super.add(obj);
            return;
        }
        if (objLayer >= maxLayer) {
            maxLayer = objLayer;
            // super.add(obj, 0);
            super.addAtBegin(obj);
            return;
        }
        
        // for ( int i=size; i>=0; --i)
        for (E currentEntry = getLast(); currentEntry != null;
                currentEntry = currentEntry.getPrev()) {

            if (currentEntry.getValue().getLayer() >= objLayer) {
                addAfter(obj, currentEntry);
                return;
            }

        }
        
        // this section of code should be never reached!
        int size = size();
        new Exception().printStackTrace();
        Utility.error("bug in the LayerList! info: mind:" + minLayer + " max:" + maxLayer + " current:" + objLayer, "LayerList.add( BaseObject )");
        System.out.println(" |~ SIZE: " + size);
        for (int i = 0; i < size; ++i) {
            System.out.println(" |~ Layer: " + get(i).getLayer());
        }
        System.out.println("min " + minLayer + " max " + maxLayer);
        assert false : "bug in the layerList";
        System.exit(0);
    }

    /** finds the max and min layer used */
    private void generateMaxMinLayers() {
        if (size() >= 1) {
            maxLayer = getFirst().getValue().getLayer();
            minLayer = getLast().getValue().getLayer();
        } else {
            maxLayer = minLayer = 0;
        }
    }


    @Override
    public void remove(T obj) {
        super.remove(obj);
        generateMaxMinLayers();
    }

    @Override
    public void remove(int index) {
        super.remove(index);
        generateMaxMinLayers();
    }

    @Override
    public void destroyAll() {
        super.destroyAll();
        maxLayer = minLayer = 0;
    }

    @Override
    public void removeAll() {
        super.removeAll();
        maxLayer = minLayer = 0;
    }

}

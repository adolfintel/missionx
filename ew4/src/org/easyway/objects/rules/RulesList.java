/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.objects.rules;

import org.easyway.interfaces.base.IPureLoopable;
import org.easyway.lists.BaseList;
import org.easyway.utils.Utility;

/**
 *
 * @author Daniele Paggi
 */
public class RulesList extends BaseList<GameRule, RulesEntry> implements IPureLoopable {

    int minOrder;
    int maxOrder;

    public RulesList() {
        super(false);
        setType("$_RULESLIST");
    }

    @Override
    public void add(GameRule obj) {
        generateMinMaxOrders();
        // ILayer lobj = (ILayer) obj;
        int objOrder = obj.getOrder();
        if (minOrder == maxOrder) {
            if (size() == 0) {
                minOrder = maxOrder = objOrder;
            }
            // assert objlay == minLayer;
            if (objOrder == minOrder) {
                super.add(obj);
                return;
            }
        }
        if (objOrder <= minOrder) {
            minOrder = objOrder;
            super.add(obj);
            return;
        }
        if (objOrder >= maxOrder) {
            maxOrder = objOrder;
            // super.add(obj, 0);
            super.addAtBegin(obj);
            return;
        }
        

        // for ( int i=size; i>=0; --i)
        for (RulesEntry currentEntry = getLast(); currentEntry != null;
                currentEntry = currentEntry.getPrev()) {

            if (currentEntry.getValue().getOrder() >= objOrder) {
                addAfter(obj, currentEntry);
                return;
            }

        }

        // this section of code should be never reached!
        int size = size();
        new Exception().printStackTrace();
        Utility.error("bug in the RulesList! info: mind:" + minOrder + " max:" + maxOrder + " current:" + objOrder, "RulesList.add( GameRule )");
        System.out.println(" |~ SIZE: " + size);
        for (int i = 0; i < size; ++i) {
            System.out.println(" |~ Order: " + get(i).getOrder());
        }
        System.out.println("min " + minOrder + " max " + maxOrder);
        assert false : "bug in the layerList";
        System.exit(0);
    }

    /** finds the max and min orders used */
    private void generateMinMaxOrders() {
        if (size() >= 1) {
            maxOrder = getFirst().getValue().getOrder();
            minOrder = getLast().getValue().getOrder();
        } else {
            maxOrder = minOrder = 0;
        }
    }


    @Override
    public void remove(GameRule obj) {
        super.remove(obj);
        generateMinMaxOrders();
    }

    @Override
    public void remove(int index) {
        super.remove(index);
        generateMinMaxOrders();
    }

    @Override
    public void destroyAll() {
        super.destroyAll();
        maxOrder = minOrder = 0;
    }

    @Override
    public void removeAll() {
        super.removeAll();
        maxOrder = minOrder = 0;
    }

    public void loop() {
        for (GameRule rule : this) {
            rule.loop();
        }
    }
}

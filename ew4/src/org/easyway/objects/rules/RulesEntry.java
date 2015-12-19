/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easyway.objects.rules;

import org.easyway.lists.BaseList;
import org.easyway.lists.Entry;

/**
 *
 * @author RemalKoil
 */
public class RulesEntry extends Entry<GameRule,RulesEntry> {

    public RulesEntry(RulesEntry next, RulesEntry prev, GameRule value, BaseList list) {
        super(next, prev, value, list);
    }

}

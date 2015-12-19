/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easyway.xmlParser.dialogSystem;

import org.easyway.xmlParser.Action;
import org.easyway.xmlParser.GlobalVars;

/**
 *
 * @author Administrator
 */
public class SetGlobalVarAction implements Action {

    String name;
    double value;

    public SetGlobalVarAction(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public void onAction(Dialog dialog, DialogOption invokingOption) {
        GlobalVars.setVariable(name, value);
    }
}

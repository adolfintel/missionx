/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easyway.xmlParser.tags.actionGenerators;

import org.easyway.xmlParser.Action;
import org.easyway.xmlParser.GlobalVars;
import org.easyway.xmlParser.ParserHelper;
import org.easyway.xmlParser.dialogSystem.SetGlobalVarAction;
import org.easyway.xmlParser.tags.ActionGenerator;
import org.jdom.Attribute;
import org.jdom.Element;

/**
 *
 * @author Administrator
 */
public class SetGlobalVariableActionGenerator extends ActionGenerator{

    // <editor-fold defaultstate="collapsed" desc="Singleton-Pattern">
    private static SetGlobalVariableActionGenerator thisInstance;

    public static SetGlobalVariableActionGenerator getDefault() {
        if (thisInstance == null) {
            thisInstance = new SetGlobalVariableActionGenerator();
        }
        return thisInstance;
    }
    // </editor-fold>

    public SetGlobalVariableActionGenerator() {
        super("setvar");
    }


    @Override
    public Action generateAction(ParserHelper helper, Element node) {
        Attribute var = node.getAttribute("var");
        if (var==null) {
            throw new RuntimeException(" the tag 'action' with 'id=setvar' must have got the attribute 'var'");
        }
        String varName = var.getValue();
        String varValue = node.getValue();
        return new SetGlobalVarAction(varName, GlobalVars.parseText(varValue));
    }
}

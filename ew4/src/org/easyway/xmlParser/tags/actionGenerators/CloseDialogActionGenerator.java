/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.xmlParser.tags.actionGenerators;

import org.easyway.xmlParser.Action;
import org.easyway.xmlParser.ParserHelper;
import org.easyway.xmlParser.dialogSystem.CloseDialogAction;
import org.easyway.xmlParser.tags.ActionGenerator;
import org.jdom.Element;

/**
 *
 * @author Daniele Paggi
 */
public class CloseDialogActionGenerator extends ActionGenerator {
    
    // <editor-fold defaultstate="collapsed" desc="Singleton-Pattern">
    private static CloseDialogActionGenerator thisInstance;

    public static CloseDialogActionGenerator getDefault() {
        if (thisInstance == null) {
            thisInstance = new CloseDialogActionGenerator();
        }
        return thisInstance;
    }
    // </editor-fold>
    
    public CloseDialogActionGenerator() {
        super("end");
    }

    @Override
    public Action generateAction(ParserHelper helper, Element node) {
        return new CloseDialogAction();
    }
}

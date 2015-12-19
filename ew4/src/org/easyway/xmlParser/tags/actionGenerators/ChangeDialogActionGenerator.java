/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.xmlParser.tags.actionGenerators;

import org.easyway.xmlParser.Action;
import org.easyway.xmlParser.ParserHelper;
import org.easyway.xmlParser.dialogSystem.ChangeDialogAction;
import org.easyway.xmlParser.tags.ActionGenerator;
import org.jdom.Element;

/**
 *
 * @author Daniele Paggi
 */
public class ChangeDialogActionGenerator extends ActionGenerator {
    // <editor-fold defaultstate="collapsed" desc="Singleton-Pattern">
    private static ChangeDialogActionGenerator thisInstance;

    public static ChangeDialogActionGenerator getDefault() {
        if (thisInstance == null) {
            thisInstance = new ChangeDialogActionGenerator();
        }
        return thisInstance;
    }
    // </editor-fold>
    public ChangeDialogActionGenerator() {
        super("dialog");
    }

    @Override
    public Action generateAction(ParserHelper helper, Element node) {
        String text = node.getValue();
        return new ChangeDialogAction(text);
    }
}

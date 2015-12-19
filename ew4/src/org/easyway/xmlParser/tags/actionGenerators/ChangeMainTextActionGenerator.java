/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.xmlParser.tags.actionGenerators;

import org.easyway.xmlParser.Action;
import org.easyway.xmlParser.ParserHelper;
import org.easyway.xmlParser.dialogSystem.ChangeMainTextAction;
import org.easyway.xmlParser.tags.ActionGenerator;
import org.easyway.xmlParser.tags.TextTag;
import org.jdom.Element;

/**
 *
 * @author Daniele Paggi
 */
public class ChangeMainTextActionGenerator extends ActionGenerator {
    
    // <editor-fold defaultstate="collapsed" desc="Singleton-Pattern">
    private static ChangeMainTextActionGenerator thisInstance;

    public static ChangeMainTextActionGenerator getDefault() {
        if (thisInstance == null) {
            thisInstance = new ChangeMainTextActionGenerator();
        }
        return thisInstance;
    }
    // </editor-fold>
    
    public ChangeMainTextActionGenerator() {
        super("text");
    }

    @Override
    public Action generateAction(ParserHelper helper, Element node) {
        String text = TextTag.formatText(node.getValue());
        return new ChangeMainTextAction(text);
    }
}

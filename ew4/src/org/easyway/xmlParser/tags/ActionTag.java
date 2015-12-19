/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.xmlParser.tags;

import java.util.HashMap;
import org.easyway.xmlParser.Action;
import org.easyway.xmlParser.TagParser;
import org.easyway.xmlParser.ParserHelper;
import org.easyway.xmlParser.dialogSystem.SetGlobalVarAction;
import org.easyway.xmlParser.tags.actionGenerators.ActionListActionGenerator;
import org.easyway.xmlParser.tags.actionGenerators.ChangeDialogActionGenerator;
import org.easyway.xmlParser.tags.actionGenerators.ChangeMainTextActionGenerator;
import org.easyway.xmlParser.tags.actionGenerators.CloseDialogActionGenerator;
import org.easyway.xmlParser.tags.actionGenerators.SetGlobalVariableActionGenerator;
import org.easyway.xmlParser.tags.reciverTags.ReciveActionTag;
import org.jdom.Attribute;
import org.jdom.Element;

/**
 *
 * @author Daniele Paggi
 */
public class ActionTag extends TagParser<ReciveActionTag> {

    // <editor-fold defaultstate="collapsed" desc="Singleton-Pattern">
    static {
        getDefault();
    }
    private static ActionTag thisInstance;

    public static ActionTag getDefault() {
        if (thisInstance == null) {
            thisInstance = new ActionTag();
            ChangeMainTextActionGenerator.getDefault();
            CloseDialogActionGenerator.getDefault();
            ChangeDialogActionGenerator.getDefault();
            ActionListActionGenerator.getDefault();
            SetGlobalVariableActionGenerator.getDefault();
        }
        return thisInstance;
    }
    // </editor-fold>
    HashMap<String, ActionGenerator> actionGenerator;

    protected ActionTag() {
        super("action");
        actionGenerator = new HashMap<String, ActionGenerator>();
    }

    public void parse(ParserHelper helper, Element node, ReciveActionTag father) {
        // test if the father is null
        testNullFather(this, father);

        Attribute idAttribute = getAttribute(node, "id");

        ActionGenerator generator = actionGenerator.get(idAttribute.getValue());
        if (generator == null) {
            throw new RuntimeException(" the tag" + getName() + " have got a not recognized id value: " + idAttribute);
        }
        Action action = generator.generateAction(helper, node);
        father.reciveActionTag(action);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.xmlParser.tags.actionGenerators;

import java.util.ArrayList;
import java.util.List;
import org.easyway.xmlParser.Action;
import org.easyway.xmlParser.ParserHelper;
import org.easyway.xmlParser.TagParser;
import org.easyway.xmlParser.dialogSystem.Dialog;
import org.easyway.xmlParser.dialogSystem.DialogOption;
import org.easyway.xmlParser.tags.ActionGenerator;
import org.easyway.xmlParser.tags.ActionTag;
import org.easyway.xmlParser.tags.reciverTags.ReciveActionTag;
import org.jdom.Element;

/**
 *
 * @author Administrator
 */
public class ActionListActionGenerator extends ActionGenerator implements ReciveActionTag {

    // <editor-fold defaultstate="collapsed" desc="Singleton-Pattern">
    private static ActionListActionGenerator thisInstance;

    public static ActionListActionGenerator getDefault() {
        if (thisInstance == null) {
            thisInstance = new ActionListActionGenerator();
        }
        return thisInstance;
    }
    // </editor-fold>
    final List<Action> listActions = new ArrayList<Action>(10);

    public ActionListActionGenerator() {
        super("list");
    }

    @Override
    public Action generateAction(ParserHelper helper, Element node) {
        List<Element> list = node.getChildren();
        for (Element e : list) {
            TagParser tagParser = TagParser.getTagParser(e);
            if (tagParser instanceof ActionTag) {
                ActionTag.getDefault().parse(helper, e, this);
            }
        }

        return new ActionList(listActions);
    }

    class ActionList implements Action {

        List<Action> actionList;

        public ActionList(List<Action> list) {
            actionList = new ArrayList<Action>(list.size());
            for (Action act : list) {
                actionList.add(act);
            }
        }

        public void onAction(Dialog dialog, DialogOption invokingOption) {
            for (Action act : actionList) {
                act.onAction(dialog, invokingOption);
            }
        }

    }

    public void reciveActionTag(Action action) {
        synchronized (listActions) {
            listActions.add(action);
        }
    }

    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void parse(ParserHelper helper, Element node, Object father) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.xmlParser.tags;

import org.easyway.xmlParser.Action;
import org.easyway.xmlParser.TagParser;
import org.easyway.xmlParser.dialogSystem.DialogOption;
import org.easyway.xmlParser.ParserHelper;
import org.easyway.xmlParser.tags.reciverTags.ReciveActionTag;
import org.easyway.xmlParser.tags.reciverTags.ReciveOptionTag;
import org.easyway.xmlParser.tags.reciverTags.ReciveTextTag;
import org.jdom.Element;

/**
 *
 * @author Daniele Paggi
 */
public class OptionTag extends TagParser<ReciveOptionTag> implements ReciveTextTag<ReciveOptionTag>, ReciveActionTag<ReciveOptionTag> {

    // <editor-fold defaultstate="collapsed" desc="Singleton-Pattern">
    static {
        getDefault();
    }
    private static OptionTag thisInstance;

    public static OptionTag getDefault() {
        if (thisInstance == null) {
            thisInstance = new OptionTag();
        }
        return thisInstance;
    }
    // </editor-fold>
    protected DialogOption option;

    protected OptionTag() {
        super("option");
    }

    @Override
    public void parse(ParserHelper helper, Element node, ReciveOptionTag father) {
        // test if the father is null
        testNullFather(this, father);

        option = new DialogOption();
        defaultParser(helper, node);
        father.reciveOptionTag(option);
    }

    public void reciveTextTag(String text) {
        option.setText(text);
    }

    public void reciveActionTag(Action action) {
        option.setAction(action);
    }
}

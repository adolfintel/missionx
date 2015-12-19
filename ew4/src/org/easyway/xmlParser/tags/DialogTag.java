/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.xmlParser.tags;

import org.easyway.xmlParser.TagParser;
import org.easyway.xmlParser.dialogSystem.Dialog;
import org.easyway.xmlParser.dialogSystem.DialogOption;
import org.easyway.xmlParser.ParserHelper;
import org.easyway.xmlParser.tags.reciverTags.ReciveOptionTag;
import org.easyway.xmlParser.tags.reciverTags.ReciveTextTag;
import org.jdom.Element;

/**
 *
 * @author Daniele Paggi
 */
public class DialogTag extends TagParser implements ReciveTextTag, ReciveOptionTag {

    // <editor-fold defaultstate="collapsed" desc="Singleton-Pattern">
    private static DialogTag thisInstance;

    public static DialogTag getDefault() {
        if (thisInstance == null) {
            thisInstance = new DialogTag();
        }
        return thisInstance;
    }
    // </editor-fold>
    private Dialog dialog;

    protected DialogTag() {
        super("dialog");
    }

    public void parse(ParserHelper helper, Element node, Object father) {
        dialog = new Dialog();
        helper.getData().addObject(dialog);
        defaultParser(helper, node);
    }

    public void reciveTextTag(String text) {
        dialog.setText(text);
    }

    public void reciveOptionTag(DialogOption option) {
        dialog.addOption(option);
    }
}

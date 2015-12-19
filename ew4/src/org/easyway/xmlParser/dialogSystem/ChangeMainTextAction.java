/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.xmlParser.dialogSystem;

import org.easyway.xmlParser.Action;

/**
 *
 * @author Daniele Paggi
 */
public class ChangeMainTextAction implements Action {

    String text;

    public ChangeMainTextAction(String text) {
        this.text = text;
    }

    public void onAction(Dialog dialog, DialogOption invokingOption) {
        dialog.setText(text);
    }
}

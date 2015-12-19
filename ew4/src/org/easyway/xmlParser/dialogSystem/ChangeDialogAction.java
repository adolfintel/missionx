/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.xmlParser.dialogSystem;

import org.easyway.xmlParser.Action;
import org.easyway.xmlParser.ParserHelper;

/**
 *
 * @author Daniele Paggi
 */
public class ChangeDialogAction implements Action {

    String path;

    public ChangeDialogAction(String path) {
        this.path = path;
    }

    public void onAction(Dialog dialog, DialogOption invokingOption) {
        Dialog newDialog = new ParserHelper().parse(path).getObject(Dialog.class);
        dialog.setText(newDialog.getText());
        dialog.resetOptions();
        for (DialogOption opt : newDialog.getOptions()) {
            dialog.addOption(opt);
        }
    }
}

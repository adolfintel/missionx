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
public class CloseDialogAction implements Action {

    public void onAction(Dialog dialog, DialogOption invokingOption) {
        dialog.endDialog();
    }

}

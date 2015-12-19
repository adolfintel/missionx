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
public class DialogOption {

    private String text;
    private Dialog dialog;
    private Action action;
    
    public DialogOption() {
    }
    
    protected void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }
    
    public void setAction(Action action) {
        this.action = action;
    }
    
    public Action getAction() {
        return action;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText( String text ) {
        this.text = text;
        if (dialog != null) {
            dialog.changedOption(this);
        }
    }
    
    public void executeAction() {
        if (action == null) {
            throw new RuntimeException("null action");
        }
        
        action.onAction(dialog, this);
    }
    
}

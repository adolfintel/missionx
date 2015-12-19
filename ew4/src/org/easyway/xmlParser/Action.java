/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easyway.xmlParser;

import org.easyway.xmlParser.dialogSystem.Dialog;
import org.easyway.xmlParser.dialogSystem.DialogOption;

/**
 *
 * @author Daniele Paggi
 */
public interface Action {
    
    public void onAction( Dialog dialog, DialogOption invokingOption );
    
}

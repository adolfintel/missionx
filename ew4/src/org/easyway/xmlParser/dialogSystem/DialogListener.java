/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easyway.xmlParser.dialogSystem;

import org.easyway.xmlParser.dialogSystem.Talkerable;
import org.easyway.xmlParser.dialogSystem.DialogOption;

/**
 *
 * @author Daniele Paggi
 */
public interface DialogListener {

    public void onEndDialog();
    public void onChangeMainText(String newText);
    public void onChangeTalker(Talkerable newTalker);
    public void onAddOption(DialogOption newOption);
    public void onResetOption();
    public void onChangeOption(DialogOption optionChanged);
}

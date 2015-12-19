/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.xmlParser.tags.reciverTags;

import org.easyway.xmlParser.tags.TagParserInterface;
import org.easyway.xmlParser.dialogSystem.DialogOption;

/**
 *
 * @author Daniele Paggi
 */
public interface ReciveOptionTag extends TagParserInterface {

    public void reciveOptionTag(DialogOption option);
}

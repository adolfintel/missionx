/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.xmlParser;

import org.easyway.xmlParser.dialogSystem.Dialog;
import org.easyway.xmlParser.ParserHelper;

/**
 *
 * @author Daniele Paggi
 */
public interface Conditionable {

    public boolean testCondition( ParserHelper helper );
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easyway.xmlParser;

import org.easyway.xmlParser.TagParser;

/**
 *
 * @author Daniele Paggi
 */
public class WrongTagUseException extends RuntimeException {
    
    public WrongTagUseException(TagParser tag) {
        super("wrong use of tag: "+tag.getName());
    }

}

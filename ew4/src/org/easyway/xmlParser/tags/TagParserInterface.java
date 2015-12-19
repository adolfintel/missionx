/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easyway.xmlParser.tags;

import org.easyway.xmlParser.ParserHelper;
import org.jdom.Element;

/**
 *
 * @author Daniele Paggi
 */
public interface TagParserInterface<T> {

    public String getName();
    
    public void parse(ParserHelper helper, Element node, T father);
    
}

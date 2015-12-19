/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.xmlParser.tags;

import java.beans.Beans;
import java.io.IOException;
import org.easyway.xmlParser.ParserHelper;
import org.easyway.xmlParser.TagParser;
import org.jdom.Element;

/**
 *
 * @author Daniele Paggi
 */
public class ObjectTag extends TagParser {

    Object object;

    public ObjectTag() {
        super("object");
    }

    public void parse(ParserHelper helper, Element node, Object father) {
        String className = node.getAttributeValue("class");
        if (className == null) {
            throw new RuntimeException("object tag must have a class attribute");
        }
        try {
            object = Beans.instantiate(null, className);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("object tag have got a not valid class attribute value: " + className);
        }
    }
}

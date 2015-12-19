/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.xmlParser.tags;

import org.easyway.xmlParser.Action;
import org.easyway.xmlParser.ParserHelper;
import org.jdom.Element;

/**
 *
 * @author Daniele Paggi
 */
public abstract class ActionGenerator {

    private final String name;

    public ActionGenerator(String name) {
        ActionTag.getDefault().actionGenerator.put(name, this);
        this.name = name;
    }

    public final String getActionId() {
        return name;
    }

    public abstract Action generateAction(ParserHelper helper, Element node);
}

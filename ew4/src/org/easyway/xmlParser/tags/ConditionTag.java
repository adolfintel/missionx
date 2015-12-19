/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.xmlParser.tags;

import java.util.List;
import java.util.regex.Pattern;
import org.easyway.xmlParser.GlobalVars;
import org.easyway.xmlParser.ParserHelper;
import org.easyway.xmlParser.TagParser;
import org.easyway.xmlParser.WrongTagUseException;
import org.jdom.Attribute;
import org.jdom.Element;

/**
 *
 * @author Administrator
 */
public class ConditionTag extends TagParser {

    // <editor-fold defaultstate="collapsed" desc="Singleton-Pattern">
    static {
        getDefault();
    }
    private static ConditionTag thisInstance;

    public static ConditionTag getDefault() {
        if (thisInstance == null) {
            thisInstance = new ConditionTag();
        }
        return thisInstance;
    }
    // </editor-fold>

    public ConditionTag() {
        super("condition");
    }

    public void parse(ParserHelper helper, Element node, Object father) {
        // test if the father is null
        if (father == null) {
            throw new WrongTagUseException(this);
        }

        Attribute testAttr = getAttribute(node, "test");
        if (testAttr == null) {
            throw new RuntimeException("condition Tag must have got a 'test' attribute");
        }
        String testValue = testAttr.getValue();
        Pattern pattern = Pattern.compile("[ \t]+");
        String arrayString[] = pattern.split(testValue);
        if (arrayString.length != 3) {
            throw new RuntimeException("condition Tag: the test attribute isn't correct");
        }

        boolean result = false;
        String var1 = arrayString[0];
        String cmp = arrayString[1];
        String var2 = arrayString[2];

        double v1 = GlobalVars.parseText(var1);

        double v2 = GlobalVars.parseText(var2);

        if (cmp.equals("=") || cmp.equals("==")) {
            result = (v1 == v2);
        }
        if (cmp.equals(">")) {
            result = (v1 > v2);
        }
        if (cmp.equals("<")) {
            result = (v1 < v2);
        }
        if (cmp.equals("<=")) {
            result = (v1 <= v2);
        }
        if (cmp.equals(">=")) {
            result = (v1 >= v2);
        }
        if (cmp.equals("!=") || cmp.equals("<>")) {
            result = (v1 != v2);
        }

        if (result == true) {
            List<Element> childs = node.getChildren();

            TagParser parser;
            for (Element el : childs) {
                parser = getTagParser(el);
                if (parser == null) {
                    throw new RuntimeException("can't find a tag parser for the tag: " + el.getName());
                }
                parser.parse(helper, el, father);
            }
        }

    }
}

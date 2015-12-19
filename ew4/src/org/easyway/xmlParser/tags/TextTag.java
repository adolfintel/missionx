/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.xmlParser.tags;

import java.util.regex.Pattern;
import org.easyway.xmlParser.TagParser;
import org.easyway.xmlParser.ParserHelper;
import org.easyway.xmlParser.tags.reciverTags.ReciveTextTag;
import org.jdom.Element;

/**
 *
 * @author Daniele Paggi
 */
public class TextTag extends TagParser<ReciveTextTag> {

    // <editor-fold defaultstate="collapsed" desc="Singleton-Pattern">
    static {
        getDefault();
    }
    private static TextTag thisInstance;

    public static TextTag getDefault() {
        if (thisInstance == null) {
            thisInstance = new TextTag();
        }
        return thisInstance;
    }
    // </editor-fold>
    public TextTag() {
        super("text");
    }

    static String removeSpaces(String value) {
        Pattern pattern = Pattern.compile("[ \t]+");
        String arrayString[] = pattern.split(value);

        value = "";
        final int size = arrayString.length - 1;
        for (int i = 0; i < size; ++i) {
            value += arrayString[i] + " ";
        }
        value += arrayString[size];
        return value;
    }

    static String removeCR(String value) {
        Pattern pattern = Pattern.compile("\n[ \t]*");
        String arrayString[] = pattern.split(value);

        value = "";
        final int size = arrayString.length;
        for (int i = 0; i < size; ++i) {
            value += arrayString[i];
        }
        return value;
    }

    static String parseCR(String value) {
        Pattern pattern = Pattern.compile("\\\\n");
        String arrayString[] = pattern.split(value);

        value = "";
        final int size = arrayString.length;
        for (int i = 0; i < size; ++i) {
            value += arrayString[i] + '\n';
        }
        return value;
    }

    public static String formatText(String value) {
        return parseCR(removeCR(removeSpaces(value)));
    }

    public void parse(ParserHelper helper, Element node, ReciveTextTag father) {
        // test if the father is null
        testNullFather(this, father);
        String value = node.getValue();

        //value = removeSpaces(value);
        //value = removeCR(value);
        //value = parseCR(value);
        value = formatText(value);
        
        father.reciveTextTag(value);
    }
}

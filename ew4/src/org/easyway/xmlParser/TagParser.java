/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.xmlParser;

import java.util.HashMap;
import java.util.List;
import org.easyway.xmlParser.ParserHelper;
import org.easyway.xmlParser.WrongTagUseException;
import org.jdom.Attribute;
import org.jdom.Element;
import org.easyway.xmlParser.tags.*;

/**
 *
 * @author Daniele Paggi
 */
public abstract class TagParser<T extends TagParserInterface> implements TagParserInterface<T> {

    private static HashMap<String, TagParser> tags = new HashMap<String, TagParser>();
    private final String name;

    protected TagParser(String tagName) {
        name = tagName;
        addTagParser(this);
    }

    public final void defaultParser(ParserHelper helper, Element node) {
        List<Element> childs = node.getChildren();

        TagParser parser;
        for (Element el : childs) {
            parser = getTagParser(el);
            if (parser == null) {
                throw new RuntimeException("can't find a tag parser for the tag: " + el.getName());
            }
            parser.parse(helper, el, this);
        }
    }

    public final String getName() {
        return name;
    }

    protected final Attribute getAttribute(Element node, String attributeName) {
        Attribute attribute = node.getAttribute(attributeName);
        if (attribute == null) {
            throw new RuntimeException(" the tag " + getName() + " must have got an attribue '" + attributeName + "'");
        }
        return attribute;
    }

    public static final void testNullFather(TagParser tag, TagParserInterface father) {
        if (father == null) {
            throw new WrongTagUseException(tag);
        }
    }

    public static final <T> void tagImplemenst(Class<T> imp, TagParserInterface tagToTest) {
        if (!imp.isInstance(tagToTest)) {
            throw new RuntimeException(" the tag: " + tagToTest.getName() + " must implement the interface " + imp.getCanonicalName());
        }
    }

    public static TagParser getTagParser(Element node) {
        if (node == null) {
            throw new NullPointerException();
        }
        return tags.get(node.getName());
    }

    public static void addTagParser(TagParser tagparser) {
        tags.put(tagparser.getName(), tagparser);
    }
}

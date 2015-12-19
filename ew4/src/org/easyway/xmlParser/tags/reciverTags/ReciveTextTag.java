/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.xmlParser.tags.reciverTags;

import org.easyway.xmlParser.tags.TagParserInterface;

/**
 *
 * @author Daniele Paggi
 */
public interface ReciveTextTag<T> extends TagParserInterface<T> {

    public void reciveTextTag(String text);
}

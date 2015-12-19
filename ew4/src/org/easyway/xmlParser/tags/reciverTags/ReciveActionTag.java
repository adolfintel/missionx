/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easyway.xmlParser.tags.reciverTags;

import org.easyway.xmlParser.tags.TagParserInterface;
import org.easyway.xmlParser.Action;

/**
 *
 * @author Daniele Paggi
 */
public interface ReciveActionTag<T> extends TagParserInterface<T> {
    public void reciveActionTag(Action action);
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.xmlParser;

import java.io.IOException;
import java.io.InputStream;
import org.easyway.utils.Utility;
import org.easyway.xmlParser.tags.ActionTag;
import org.easyway.xmlParser.tags.ConditionTag;
import org.easyway.xmlParser.tags.DialogTag;
import org.easyway.xmlParser.tags.OptionTag;
import org.easyway.xmlParser.tags.TextTag;
import org.easyway.xmlParser.tags.actionGenerators.CloseDialogActionGenerator;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author Daniele Paggi
 */
public class ParserHelper {

    protected MultiDataCollection multiData;

    public ParserHelper() {
        DialogTag.getDefault();
        TextTag.getDefault();
        OptionTag.getDefault();
        ActionTag.getDefault();
        ConditionTag.getDefault();
    }

    public MultiDataCollection getData() {
        return multiData;
    }

    public MultiDataCollection parse(String path) {
        //InputStream inputFile = Utility.getLocalFile(path);
        InputStream inputFile = null;
        {
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            int index;
            // path = path.replaceAll("\\", "/");
            while ((index = path.indexOf("\\")) != -1) {
                path = path.substring(0, index) + '/' + path.substring(index + 1);
            }
            try {
                inputFile = Thread.currentThread().getContextClassLoader().getResource(path).openStream();
            } catch (Exception e) {
                Utility.error("the file " + path + " was not found!", e);
            }
        }
        
        multiData = new MultiDataCollection();
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document;
        try {
            document = saxBuilder.build(inputFile);
            Element root = document.getRootElement();

            TagParser tag = TagParser.getTagParser(root);
            if (tag == null) {
                throw new RuntimeException("can't find a TagParser for the tag: "+root.getName());
            }
            tag.parse(this, root, null);

        } catch (JDOMException ex) {
            Utility.error("DialogHelper(String)", ex);
        } catch (IOException ex) {
            Utility.error("DialogHelper(String)", ex);
        }
        return multiData;
    }
}

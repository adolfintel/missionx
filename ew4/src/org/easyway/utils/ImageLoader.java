/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.easyway.objects.texture.Texture;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author Daniele Paggi
 */
public class ImageLoader {
    private static String SEPARATOR = ":";

    public static void loadImage(String filename) {
        Texture original = Texture.getTexture(filename);
        InputStream is = getIS(filename + ".xml");


        SAXBuilder saxBuilder = new SAXBuilder();
        Document document;
        try {
            document = saxBuilder.build(is);
            document.getRootElement();
            Element root = document.getRootElement();

            List<Element> childs = root.getChildren("part");
            for (Element element : childs) {
                String name = element.getAttributeValue("name");

                Element startPoint = element.getChild("p");
                int x = Integer.parseInt(startPoint.getChildText("x"));
                int y = Integer.parseInt(startPoint.getChildText("y"));

                Element size = element.getChild("size");
                int w = Integer.parseInt(size.getChildText("width"));
                int h = Integer.parseInt(size.getChildText("height"));

                Texture outTexture = new Texture(original.getTextureId(), filename + SEPARATOR + name);
                outTexture.setRegion(x, y, w, h);

            }


        } catch (JDOMException ex) {
            Utility.error("ImageLoader.loadImagae(String)", ex);
        } catch (IOException ex) {
            Utility.error("ImageLoader.loadImagae(String)", ex);
        }
    }

    private static InputStream getIS(String path) {

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
        return inputFile;
    }
}

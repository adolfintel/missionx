/* EasyWay Game Engine
 * Copyright (C) 2010 Daniele Paggi.
 *
 * Written by: 2006 Daniele Paggi<dshnt@hotmail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.easyway.objects.sprites2D;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.easyway.geometry2D.Ray2D;
import org.easyway.geometry2D.Segment;
import org.easyway.utils.Utility;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author Daniele Paggi
 */
public class PolygonMask {

    ArrayList<Segment> segments = new ArrayList<Segment>(10);

    public PolygonMask() {
    }

    public PolygonMask(String path) {
        readFromXmlFile(path);
    }

    public PolygonMask(String path, float width, float height) {
        readFromXmlFile(path, width, height);
    }

    public void clear() {
        segments.clear();
    }

    public void addSegment(Segment seg) {
        segments.add(seg);
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }

    public void readFromXmlFile(String localPath) {
        readFromXmlFile(localPath, 1f, 1f);
    }

    public void readFromXmlFile(String localPath, float width, float height) {
        if (width == 0.0f) {
            width = 1.0f;
        }
        if (height == 0.0f) {
            height = 1.0f;
        }
        InputStream inputFile = null;
        {
            if (localPath.startsWith("/")) {
                localPath = localPath.substring(1);
            }
            int index;
            // path = path.replaceAll("\\", "/");
            while ((index = localPath.indexOf("\\")) != -1) {
                localPath = localPath.substring(0, index) + '/' + localPath.substring(index + 1);
            }
            try {
                inputFile = Thread.currentThread().getContextClassLoader().getResource(localPath).openStream();
            } catch (Exception e) {
                Utility.error("the file " + localPath + " was not found!", e);
            }
        }

        SAXBuilder saxBuilder = new SAXBuilder();
        Document document;
        try {
            document = saxBuilder.build(inputFile);
            Element root = document.getRootElement();
            List<Element> segList = root.getChildren("segment");
            for (Element el : segList) {
                Element p = el.getChild("p");
                float px = Float.parseFloat(p.getAttributeValue("x"));
                float py = Float.parseFloat(p.getAttributeValue("y"));

                Element q = el.getChild("q");
                float qx = Float.parseFloat(q.getAttributeValue("x"));
                float qy = Float.parseFloat(q.getAttributeValue("y"));
                Segment segment = new Segment(px * width, py * height, qx * width, qy * height);
                addSegment(segment);
            }
        } catch (JDOMException ex) {
            Utility.error("PolygonMask.readFromXmlFile(String,float,float)", ex);
        } catch (IOException ex) {
            Utility.error("PolygonMask.readFromXmlFile(String,float,float)", ex);
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.vectorialEditor;

import java.awt.Image;
import java.util.ArrayList;
import org.easyway.geometry2D.Segment;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author RemalKoil
 */
public class Data {

    public static Image image;
    public static ArrayList<Vector3f> points = new ArrayList<Vector3f>();
    public static ArrayList<Segment> segments = new ArrayList<Segment>();
    public static Vector3f selectedPoint;

    static void resetPoints() {
        points.clear();
        segments.clear();
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        Vector3f a, b, c, d;
        points.add(a = new Vector3f(0, 0, 0));
        points.add(b = new Vector3f(w, 0, 0));
        points.add(c = new Vector3f(w, h, 0));
        points.add(d = new Vector3f(0, h, 0));
        segments.add(new Segment(a, Vector3f.sub(b, a, new Vector3f())));
        segments.add(new Segment(b, Vector3f.sub(c, b, new Vector3f())));
        segments.add(new Segment(c, Vector3f.sub(d, c, new Vector3f())));
        segments.add(new Segment(d, Vector3f.sub(a, d, new Vector3f())));
    }

}

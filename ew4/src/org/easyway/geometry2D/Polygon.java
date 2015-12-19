/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.geometry2D;

import java.util.ArrayList;
import java.util.Collection;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author RemalKoil
 */
public class Polygon {

    Matrix3f transformation = new Matrix3f();

    ArrayList<Segment> segments = new ArrayList<Segment>();

    public void addSegment(Segment segment) {
        segments.add(segment);
    }

    public void removeSegment(Segment segment) {
        segments.remove(segment);
    }

    public void removeSegment(int index) {
        segments.remove(index);
    }

    public boolean isClosed() {
        ArrayList<Vector3f> vectors = new ArrayList<Vector3f>(segments.size() * 2);
        int[] counts = new int[segments.size() * 2];
        Segment s;
        for (int i = 0; i < segments.size() - 1; ++i) {
            s = segments.get(i);
            Vector3f p1 = new Vector3f();
            Vector3f.sub(s.p0, s.dir, p1);
            if (!vectors.contains(s.p0)) {
                if (vectors.size() >= counts.length) {
                    return false;
                }
                vectors.add(s.p0);
            }
            if (++counts[vectors.indexOf(s.p0)] > 2) {
                return false;
            }
            if (!vectors.contains(p1)) {
                if (vectors.size() >= counts.length) {
                    return false;
                }
                vectors.add(p1);
            }
            if (++counts[vectors.indexOf(p1)] > 2) {
                return false;
            }
        }
        return true;
    }

    public boolean isDegen() {
        Segment s, v;
        for (int i = 0; i < segments.size() - 1; ++i) {
            s = segments.get(i);
            for (int j = i + 1; i < segments.size(); ++j) {
                v = segments.get(j);
                if (s.intersect(v) != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public Vector3f intersect(Segment segment) {
        Vector3f out;
        for (Segment s : segments) {

            if ((out = segment.intersect(s)) != null) {
                return out;
            }
        }
        return null;
    }

    public Collection<Vector3f> getIntersections(Segment segment) {
        ArrayList<Vector3f> outList = new ArrayList<Vector3f>();
        Vector3f out;
        for (Segment s : segments) {
            if ((out = segment.intersect(s)) != null) {
                outList.add(out);
            }
        }
        return outList;
    }

    public boolean collision(Polygon poly) {
        Matrix3f tran = new Matrix3f();
        tran.load(poly.transformation);
        Matrix3f invTr = new Matrix3f();
        Matrix3f.invert(transformation, invTr);
        Matrix3f.mul(tran, invTr, tran);

        Vector3f p = new Vector3f();
        Vector3f dir = new Vector3f();
        for (Segment s : segments) {
            for (Segment v : poly.segments) {
                Matrix3f.transform(tran, v.p0, p);
                Matrix3f.transform(tran, v.dir, dir);
                if (s.intersect(new Segment(p, dir)) != null) {
                    return true;
                }
            }
        }
        return false;
    }
}

/* EasyWay Game Engine
 * Copyright (C) 2009 Daniele Paggi.
 *
 * Written by: 2009 Daniele Paggi<dshnt@hotmail.com>
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
package org.easyway.geometry2D;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Daniele Paggi
 */
public class Segment extends Ray2D {

    private static final long serialVersionUID = -1655891334451272269L;

    public Segment(Vector3f p0, Vector3f dir) {
        super(p0, dir);
    }

    public Segment(float x, float y, float x1, float y1) {
        super(x, y, x1, y1);
    }
    /**
     *
     * @param segment
     * @return the intersection point or a Vector2f(NaN,Float.)
     * if exists more that one intersection point
     */
    public Vector3f intersect(Segment segment) {
        float t = super.intersect(segment);
        if (t >= 0 && t <= 1) {
            Vector3f p = getPointAt(t);
            t = segment.intersect((Ray2D) this);
            if (t >= 0 && t <= 1) {
                return p;
            }
        }
        if (t == Float.NaN) {
            return new Vector3f(Float.NaN, Float.NaN, 1);
        }
        assert t == Float.POSITIVE_INFINITY || t == Float.NEGATIVE_INFINITY;
        return null;
    }
}

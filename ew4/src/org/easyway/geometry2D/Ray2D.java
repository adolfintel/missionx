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

import java.io.Serializable;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Daniele Paggi
 */
public class Ray2D implements Serializable {

    private static final long serialVersionUID = 6195422030092823305L;
    Vector3f p0;
    Vector3f dir;

    public Ray2D(float x, float y, float x1, float y1) {
        p0 = new Vector3f(x, y, 1);
        dir = new Vector3f(x1, y1, 0);
        Vector3f.sub(dir, p0, dir);
    }

    public Ray2D(Vector3f p0, Vector3f dir) {
        this.p0 = new Vector3f(p0);
        this.dir = new Vector3f(dir);
    }

    public Vector3f getPointAt(float t) {
        Vector3f out = new Vector3f(dir);
        return Vector3f.add(p0, (Vector3f) out.scale(t), out);
    }

    public Vector3f collision(Ray2D ray) {
        float t = intersect(ray);
        if (t != Float.NaN) {
            return getPointAt(t);
        }
        return null;

    }

    public Vector3f getDir() {
        return dir;
    }

    public void setDir(Vector3f dir) {
        this.dir = dir;
    }

    public Vector3f getP0() {
        return p0;
    }

    public void setP0(Vector3f p0) {
        this.p0 = p0;
    }

    public float intersect(Ray2D ray) {
        try {
            return -(p0.x * ray.dir.y - ray.p0.x * ray.dir.y - ray.dir.x * p0.y + ray.dir.x * ray.p0.y) / (dir.x * ray.dir.y - ray.dir.x * dir.y);
        } catch (ArithmeticException e) {
            return Float.NaN;
        }
    }

    @Override
    public String toString() {
        return "Px: " + p0.getX() + " Py: " + p0.getY() + " dirX: " + dir.getX() + " dirY: " + dir.getY();
    }
}

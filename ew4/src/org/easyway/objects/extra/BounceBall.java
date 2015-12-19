package org.easyway.objects.extra;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collection;
import org.easyway.collisions.CollisionUtils;
import org.easyway.interfaces.sprites.IPoint2D;
import org.easyway.interfaces.sprites.ISpriteColl;
import org.easyway.objects.texture.Java2DTexture;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author RemalKoil
 */
public class BounceBall {

    Vector2f points[];
    float radius;

    public BounceBall(float radius) {
        this(360, radius);
    }

    public BounceBall(int precision, float radius) {
        points = new Vector2f[precision];
        this.radius = radius;
        double step = (2d * Math.PI) / (double) precision;
        double currentAngle = 0;
        for (int i = 0; i < precision; ++i) {
            points[i] = new Vector2f((float) Math.cos(currentAngle) * radius, (float) Math.sin(currentAngle) * radius);
            /*points[i].setX((((int) points[i].getX() * 100) / 100));
            points[i].setY((((int) points[i].getY() * 100) / 100));*/
            currentAngle += step;
        }
    }

    public int getPrecision() {
        return points.length;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius() {
        for (Vector2f v : points) {
            v.normalise().scale(radius);
        }
    }

    public Vector2f getDirection(ISpriteColl spr, int tx, int ty) {
        tx += radius;
        ty += radius;
        Vector2f dir = new Vector2f(0, 0);
        System.out.println();
        for (Vector2f v : points) {

            if (CollisionUtils.testCollision(spr, (int) v.getX() + tx, (int) v.getY() + ty)) {
                System.out.println("pre-dir: " + dir);
                Vector2f.add(dir, v, dir);
                System.out.println("V: " + v);
                System.out.println(" after-dir: " + dir);

            }
        }
        if (dir.length() != 0) {
            dir.normalise();
            System.out.println("dir: " + dir);
        }
        return dir;
    }

    public Vector2f getFinalDirection(Collection<ISpriteColl> coll, IPoint2D p) {
        return getFinalDirection(coll, (int) p.getXOnScreen(), (int) p.getYOnScreen());

    }

    public Vector2f getFinalDirection(Collection<ISpriteColl> coll, int tx, int ty) {
        Vector2f dir = new Vector2f(0, 0);
        Vector2f temp;
        for (ISpriteColl spr : coll) {
            temp = getDirection(spr, tx, ty);
            if (temp.length() != 0) {
                temp.normalise();
            }
            Vector2f.add(dir, temp, dir);
            if (dir.length() != 0) {
                dir.normalise();
            }
        }
        if (dir.length() != 0) {
            dir.normalise();
        }
        return dir;
    }

    public void show(int tx, int ty) {
        Java2DTexture.getDefault().clear();
        Graphics2D g2d = Java2DTexture.getDefault().getGraphics();

        for (Vector2f v : points) {
            g2d.setColor(Color.RED);
            g2d.drawOval((int) v.getX() + tx, (int) v.getY() + ty, 2, 2);
        }
        Java2DTexture.getDefault().update();
    }
}

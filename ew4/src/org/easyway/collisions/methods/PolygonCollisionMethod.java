/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.collisions.methods;

import java.io.Serializable;
import org.easyway.collisions.CollisionUtils;
import org.easyway.collisions.FeedBackCollision;
import org.easyway.collisions.ICollisionMethod;
import org.easyway.geometry2D.LineDrawer;
import org.easyway.geometry2D.Segment;
import org.easyway.interfaces.sprites.ISpriteColl;
import org.easyway.objects.sprites2D.PolygonMask;
import org.easyway.utils.MathUtils;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author RemalKoil
 */
public class PolygonCollisionMethod implements ICollisionMethod<ISpriteColl>, Serializable {

    @Override
    public boolean isCompatible(ICollisionMethod mth) {
        return mth.getCollisionType() == CollisionMethods.VECTORIAL;
    }

    @Override
    /*
     * TODO:<br/>
     * CAN SPEED UP
     */
    public FeedBackCollision executeCollision(ISpriteColl src, ISpriteColl dst) {
        // TODO: TO SPEED UP
        PolygonMask mask1 = (PolygonMask) src.getCollisionData();
        PolygonMask mask2 = (PolygonMask) dst.getCollisionData();
        if (!CollisionUtils.circleHit(src, dst)) {
            return null;
        }
        Segment segmentTransformed, segmentTransformed2;
        final float alpha = MathUtils.degToRad( src.getRotation() );
        final float cosA = (float) Math.cos(alpha);
        final float sinA = (float) Math.sin(alpha);
        final float scaleX = src.getScaleX();
        final float scaleY = src.getScaleY();

        final float scaleXcosA = scaleX * cosA;
        final float scaleXsinA = scaleX * sinA;
        final float scaleYsinA = scaleY * sinA;
        final float scaleYcosA = scaleY * cosA;
        final float tx = src.getX() + src.getWidth()/2f;
        final float ty = src.getY() + src.getHeight()/2f;

        final float beta = MathUtils.degToRad( dst.getRotation() );
        final float cosB = (float) Math.cos(beta);
        final float sinB = (float) Math.sin(beta);
        final float scaleX2 = dst.getScaleX();
        final float scaleY2 = dst.getScaleY();

        final float scaleX2cosB = scaleX2 * cosB;
        final float scaleX2sinB = scaleX2 * sinB;
        final float scaleY2sinB = scaleY2 * sinB;
        final float scaleY2cosB = scaleY2 * cosB;
        final float tx2 = dst.getX()  + dst.getWidth()/2f;
        final float ty2 = dst.getY()  + dst.getHeight()/2f;

        for (final Segment segment : mask1.getSegments()) {
            segmentTransformed = new Segment(segment.getP0(), segment.getDir());
            float x = segmentTransformed.getP0().x;
            float y = segmentTransformed.getP0().y;
            segmentTransformed.getP0().x = x * scaleXcosA - y * scaleYsinA + tx; // roto scale translate
            segmentTransformed.getP0().y = x * scaleXsinA + y * scaleYcosA + ty;
            x = segmentTransformed.getDir().x;
            y = segmentTransformed.getDir().y;
            segmentTransformed.getDir().x = x * scaleXcosA - y * scaleYsinA; // roto scale
            segmentTransformed.getDir().y = x * scaleXsinA + y * scaleYcosA;
            for (Segment s : mask2.getSegments()) {
                segmentTransformed2 = new Segment(s.getP0(), s.getDir());
                x = segmentTransformed2.getP0().x;
                y = segmentTransformed2.getP0().y;
                segmentTransformed2.getP0().x = x * scaleX2cosB - y * scaleY2sinB + tx2; // roto scale translate
                segmentTransformed2.getP0().y = x * scaleX2sinB + y * scaleY2cosB + ty2;
                x = segmentTransformed2.getDir().x;
                y = segmentTransformed2.getDir().y;
                segmentTransformed2.getDir().x = x * scaleX2cosB - y * scaleY2sinB; // roto scale
                segmentTransformed2.getDir().y = x * scaleX2sinB + y * scaleY2cosB;
                if (segmentTransformed.intersect(segmentTransformed2) != null) {
                    CollisionUtils.hitted(src, dst);
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public FeedBackCollision checkCollision(ISpriteColl src, ISpriteColl dst) {
        return null;
    }

    @Override
    public CollisionMethods getCollisionType() {
        return CollisionMethods.VECTORIAL;
    }
}

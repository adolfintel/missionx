/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.collisions.methods;

import org.easyway.collisions.ICollisionMethod;
import java.io.Serializable;
import org.easyway.collisions.CollisionUtils;
import org.easyway.collisions.FeedBackCollision;
import org.easyway.interfaces.sprites.ISpriteColl;

/**
 *
 * @author RemalKoil
 */
public class SoftwareCollisionMethod implements ICollisionMethod<ISpriteColl>, Serializable {

    private static SoftwareCollisionMethod thisInstance;
    private static final long serialVersionUID = 3649248184632591301L;

    public static SoftwareCollisionMethod getDefaultInstance() {
        return thisInstance == null ? thisInstance = new SoftwareCollisionMethod() : thisInstance;
    }

    @Override
    public boolean isCompatible(ICollisionMethod mth) {
        return mth == this || mth instanceof SoftwareCollisionMethod;
    }

    @Override
    public FeedBackCollision executeCollision(ISpriteColl src, ISpriteColl dst) {
        if (CollisionUtils.rectangleHit(src, dst)) {
            if (CollisionUtils.trueHit(src, dst)) {
                CollisionUtils.hitted(src, dst);
            }
        }
        return null;
    }

    @Override
    public FeedBackCollision checkCollision(ISpriteColl src, ISpriteColl dst) {
        if (CollisionUtils.rectangleHit(src, dst)) {
            if (CollisionUtils.trueHit(src, dst)) {
                return new FeedBackCollision(true);
            }
        }
        return new FeedBackCollision(false);
    }

    @Override
    public CollisionMethods getCollisionType() {
        return CollisionMethods.PIXEL_SOFTWARE;
    }
}

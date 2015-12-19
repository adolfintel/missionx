/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.collisions.methods;

import org.easyway.collisions.*;
import java.io.Serializable;
import org.easyway.collisions.CollisionUtils;
import org.easyway.collisions.FeedBackCollision;
import org.easyway.interfaces.sprites.ISpriteColl;

/**
 *
 * @author RemalKoil
 */
public class CircleCollisionMethod implements ICollisionMethod<ISpriteColl>, Serializable {

    private static CircleCollisionMethod thisInstance;

    public static CircleCollisionMethod getDefaultInstance() {
        return thisInstance == null ? thisInstance = new CircleCollisionMethod() : thisInstance;
    }

    @Override
    public boolean isCompatible(ICollisionMethod mth) {
        return mth == this || mth instanceof CircleCollisionMethod;
    }

    @Override
    public FeedBackCollision executeCollision(ISpriteColl src, ISpriteColl dst) {
        if (CollisionUtils.circleHit(src, dst)) {
            CollisionUtils.hitted(src, dst);
        }
        return null;
    }

    @Override
    public FeedBackCollision checkCollision(ISpriteColl src, ISpriteColl dst) {
        return new FeedBackCollision(true);
    }

    @Override
    public CollisionMethods getCollisionType() {
        return CollisionMethods.CIRCLE;
    }
}

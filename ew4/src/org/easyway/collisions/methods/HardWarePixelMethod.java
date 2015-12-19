/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.collisions.methods;

import org.easyway.collisions.*;
import java.io.Serializable;
import org.easyway.interfaces.sprites.ISpriteColl;

/**
 *
 * @author Administrator
 */
public class HardWarePixelMethod implements ICollisionMethod<ISpriteColl>, Serializable {

    private static HardWarePixelMethod thisInstance;
    private static final long serialVersionUID = 3649248184632591301L;

    public static HardWarePixelMethod getDefaultInstance() {
        return thisInstance == null ? thisInstance = new HardWarePixelMethod() : thisInstance;
    }

    @Override
    public boolean isCompatible(ICollisionMethod mth) {
        return true;
    }

    @Override
    public FeedBackCollision executeCollision(ISpriteColl sourceObj, ISpriteColl destinationObj) {
        if (sourceObj.getRotation() == 0 && destinationObj.getRotation() == 0) {
            if (CollisionUtils.rectangleHit(sourceObj, destinationObj)) {

                if (CollisionUtils.trueHardWareHit(sourceObj, destinationObj)) {
                    CollisionUtils.hitted(sourceObj, destinationObj);
                }
            }
        } else if (CollisionUtils.circleHit(sourceObj, destinationObj)) {

            if (CollisionUtils.trueHardWareHit(sourceObj, destinationObj)) {
                CollisionUtils.hitted(sourceObj, destinationObj);
            }
        }
        return null;
    }

    @Override
    public FeedBackCollision checkCollision(ISpriteColl sourceObj, ISpriteColl destinationObj) {
        if (sourceObj.getRotation() == 0 && destinationObj.getRotation() == 0) {
            if (CollisionUtils.rectangleHit(sourceObj,
                    destinationObj)) {
                /*return new FeedBackCollision(CollisionUtils.trueHit(sourceObj,
                destinationObj));*/
                /*IPlain2D plain = CollisionUtils.getRectangleHit(sourceObj, destinationObj);
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                GL11.glScissor((int) plain.getX(), (int) plain.getY(), plain.getWidth(), plain.getHeight());*/

                FeedBackCollision feed = new FeedBackCollision(CollisionUtils.trueHardWareHit(sourceObj, destinationObj));
                //GL11.glDisable(GL11.GL_SCISSOR_TEST);
                return feed;
            }
        } else if (CollisionUtils.circleHit(sourceObj,
                destinationObj)) {
            /*IPlain2D plain = CollisionUtils.getRectangleHit(CollisionUtils.getCircleRectangle(sourceObj),
            CollisionUtils.getCircleRectangle(destinationObj));
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GL11.glScissor((int) plain.getX(), (int) plain.getY(), plain.getWidth(), plain.getHeight());*/

            FeedBackCollision feed = new FeedBackCollision(CollisionUtils.trueHardWareHit(sourceObj, destinationObj));
            //GL11.glDisable(GL11.GL_SCISSOR_TEST);
            return feed;
        }
        return new FeedBackCollision(false);
    }

    public CollisionMethods getCollisionType() {
        return CollisionMethods.PIXEL_HARDWARE;
    }
}

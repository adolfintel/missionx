/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easyway.collisions;

import org.easyway.collisions.methods.*;
import org.easyway.collisions.*;
import java.io.Serializable;


/**
 *
 * @author Administrator
 */
public interface ICollisionMethod<T> extends Serializable {

    public boolean isCompatible(ICollisionMethod mth);

    public FeedBackCollision executeCollision(T src, T dst);

    public FeedBackCollision checkCollision(T src, T dst);

    public CollisionMethods getCollisionType();


}

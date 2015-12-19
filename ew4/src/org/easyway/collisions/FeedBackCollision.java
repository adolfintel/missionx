/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.collisions;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class FeedBackCollision {

    boolean collided;

    public FeedBackCollision(boolean collided) {
        this.collided = collided;
    }

    public boolean isCollided() {
        return collided;
    }

    public ArrayList<Object> getData() {
        return null;
    }
}

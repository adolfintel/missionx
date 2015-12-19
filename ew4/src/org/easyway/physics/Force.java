/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.physics;

import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author Administrator
 */
public class Force {

    Vector2f direction;
    double force;

    public Force(double force, Vector2f dir) {
        this.force = force;
        this.direction = dir;
    }
}

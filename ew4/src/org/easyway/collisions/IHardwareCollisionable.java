/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easyway.collisions;


/**
 *
 * @author RemalKoil
 */
public interface IHardwareCollisionable {
    public void renderAt( float x, float y, float z, int unit );
    public float getX();
    public float getY();
}

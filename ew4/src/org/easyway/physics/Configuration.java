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
public class Configuration {

    /**
     * how much pixels is 1 meter<br/>
     * Default value: 1 meter = 1 pixel
     */
    double meterValue = 1.0;
    /**
     * default gravity in meters
     */
    double defaultGravity = 9.8;
    /**
     * default gravity direction
     */
    Vector2f defaultGravityDirection = new Vector2f(0, 1);

    public double meterToPixel(double m) {
        return m * meterValue;
    }

    public double pixelToMeter(double pixs) {
        return pixs / meterValue;
    }

    /**
     * how much pixels is 1 meter<br/>
     * Default value: 1 meter = 1 pixel
     */
    public void setMeterToPixelScale(double meterValue) {
        this.meterValue = meterValue;
    }

    public double getDefaultGravity() {
        return defaultGravity;
    }

    public void setDefaultGravity(double gravity) {
        this.defaultGravity = gravity;
    }

    public Vector2f getDefaultGravityDirection() {
        return defaultGravityDirection;
    }

    public void setDefaultGravityDirection(Vector2f gravityDirection) {
        this.defaultGravityDirection = gravityDirection;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.objects.texture;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
class AlphaColorIndex implements Serializable {

    byte red;
    byte green;
    byte blue;
    byte alpha;

    AlphaColorIndex(int red, int green, int blue, int alpha) {
        this.red = (byte) red;
        this.green = (byte) green;
        this.blue = (byte) blue;
        this.alpha = (byte) alpha;
    }
}

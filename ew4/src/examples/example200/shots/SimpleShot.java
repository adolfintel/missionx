/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example200.shots;

import org.easyway.objects.texture.Texture;

/**
 *
 * @author Daniele Paggi
 */
public class SimpleShot extends BaseShot {

    public SimpleShot(float x, float y) {
        super(x, y, Texture.getTexture("images/shipGame/shot.png"));
        incY(-getHeight() / 2);
        setDirection(0);
        setSpeed(15);
    }
}

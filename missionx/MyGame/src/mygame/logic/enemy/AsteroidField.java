/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.logic.enemy;

import org.easyway.objects.BaseObject;
import org.easyway.objects.texture.Texture;

/**
 *
 * @author prog5ia1
 */
public class AsteroidField extends BaseObject {
    public AsteroidField(int xa, int ya, int xb, int yb, int n) {
        for (int i = 0; i < n; i++) {
            float r = (float) Math.random();
            float x = (float) (Math.random() * (xb - xa) + xa);
            float y = (float) (Math.random() * (yb - ya) + ya);
            if (r < 0.33f) {
                new Asteroid(x, y, 10,Texture.getTexture("images/asteroids/1.png"),Texture.getTexture("images/asteroids/1_hit.png"));
            } else if (r < 0.67f) {
                new Asteroid(x, y, 15,Texture.getTexture("images/asteroids/2.png"),Texture.getTexture("images/asteroids/2_hit.png"));
            } else {
                new Asteroid(x, y, 20,Texture.getTexture("images/asteroids/3.png"),Texture.getTexture("images/asteroids/3_hit.png"));
            }
        }
    }
}

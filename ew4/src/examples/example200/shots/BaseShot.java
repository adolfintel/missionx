/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example200.shots;

import examples.example200.*;
import org.easyway.collisions.GroupCollision;
import org.easyway.interfaces.sprites.ISpriteColl;
import org.easyway.objects.texture.Texture;

/**
 *
 * @author Daniele Paggi
 */
public class BaseShot extends MovableObject {

    float damagePower = 1;

    public BaseShot(float x, float y, Texture img) {
        super(x, y, img);
        /*if (GroupCollision.getGroup("Shot-Hittable")==null) {
        GroupCollision.createGroup("Shot-Hittable");
        }*/
        GroupCollision.getGroup("Shot-Hittable").addToSource(this);
    }

    // ------------------------------
    //  SET - GET
    // ------------------------------
    public float getDamagePower() {
        return damagePower;
    }

    public void setDamagePower(float damage) {
        this.damagePower = Math.max(damage, 1f);
    }

    // ------------------------------
    //  CORE METHODS
    // ------------------------------
    @Override
    public void loop() {
        super.loop(); // move the object
    }

    @Override
    public void onCollision() {
        for (ISpriteColl spr : getCollisionList()) {
            if (spr instanceof Hittable) {
                Hittable target = (Hittable) spr;
                target.hit(getDamagePower());
                destroy();
                break;
            }
        }
    }
}

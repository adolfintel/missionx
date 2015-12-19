/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example200.enemy;

import examples.example200.*;
import org.easyway.collisions.GroupCollision;
import org.easyway.interfaces.base.ITexture;
import org.easyway.interfaces.sprites.ISpriteColl;

/**
 *
 * @author Daniele Paggi
 */
public class BaseEnemy extends HittableObject {

    float damagePower = 10;

    public BaseEnemy(float x, float y, ITexture img) {
        super(x, y, img);

        /*if (GroupCollision.getGroup("Shot-Hittable") == null) {
            GroupCollision.createGroup("Shot-Hittable");
        }*/
        GroupCollision.getGroup("Shot-Hittable").addToDestination(this);

        /*if (GroupCollision.getGroup("Player-Enemy") == null) {
            GroupCollision.createGroup("Player-Enemy");
        }*/
        GroupCollision.getGroup("Player-Enemy").addToDestination(this);
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
            }
        }
    }

    // ------------------------------
    //  SET - GET
    // ------------------------------
    public float getDamagePower() {
        return damagePower;
    }

    public void setDamagePower(float damage) {
        this.damagePower = Math.max(damage,1f);
    }
}

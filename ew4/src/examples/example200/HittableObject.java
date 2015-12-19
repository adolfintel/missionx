/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package examples.example200;

import examples.example200.art.DestroyEffect;
import org.easyway.interfaces.base.ITexture;

/**
 *
 * @author Daniele Paggi
 */
public class HittableObject extends MovableObject implements Hittable {

    float healt = 1;
    float maxHealt = 1;

    public HittableObject(float x, float y, ITexture img) {
        super(x, y, img);
    }    

    // ------------------------------
    //  SET - GET
    // ------------------------------
    public float getHealt() {
        return healt;
    }

    public void setHealt(float newHealt) {
        this.healt = newHealt;
        if (newHealt <= 0) {
            die();
            healt = 0;
        } else if (newHealt > maxHealt) {
            healt = maxHealt;
        }
    }

    public float getMaxHealt() {
        return maxHealt;
    }

    public void setMaxHealt(float maxHealt) {
        this.maxHealt = maxHealt;
        if (maxHealt < 1) {
            maxHealt = 1;
        }
        if (maxHealt < healt) {
            healt = maxHealt;
        }
    }

    // ------------------------------
    //   ACTIONS
    // ------------------------------
    public void die() {
        // a generic object can die not only when the 
        // healt reach 0 (ex. heart failure, malicious bonus) 
        // then we need to set the healt to 0
        healt = 0; // we can't use "setHealt()" or we'll make a recursion
        destroy();
    }

    public void recover(float recoverPower) {
        setHealt( healt + recoverPower );
    }

    public void hit(float damagePower) {
        // when the object is hitted we simply decrease its healt
        // if the object will die or not is a problem of the setHealt method
        setHealt( healt - damagePower );
    }

}

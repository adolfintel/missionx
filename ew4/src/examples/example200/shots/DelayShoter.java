/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package examples.example200.shots;

import org.easyway.utils.Timer.SyncTimer;

/**
 *
 * @author Daniele Paggi
 */
public class DelayShoter extends SyncTimer {
    CanShotInterface shoter;

    public DelayShoter(CanShotInterface shoter, int delay) {
        super(delay);
        this.shoter = shoter;
    }

    @Override
    public void onTick() {
        shoter.setCanShot(true);
    }


}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.art;

import mygame.CommonVar;
import mygame.logic.CamController;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.BaseObject;
import org.easyway.system.Core;

/**
 *
 * @author Do$$e
 */
public class StarEmitter extends BaseObject implements ILoopable {

    float extraParticles = 0,strength;
    int minY,maxY;
    public StarEmitter(int minY, int maxY,float strength) {
        if(CommonVar.gfxQuality==0) return;
        this.minY=minY;
        this.maxY=maxY;
        this.strength=strength;
        for (int i = 0; i < 1024; i += 4*(1/CommonVar.gfxQuality)) {
            new Star(i, (int) (Math.random() * (maxY-minY))+minY,strength);
        }
    }

    @Override
    public void loop() {
        if (CommonVar.destroyAll) {
            destroy();
        }
        if (!Core.getInstance().isGamePaused()) {
            if (CamController.getDefaultInstance().getSpeedX() == 0) {
                return;
            } else {
                float speedMul = Core.getInstance().getSpeedMultiplier();
                float particlesPerFrame = (float) (CamController.getDefaultInstance().getSpeedX() * speedMul*0.4f*CommonVar.gfxQuality);
                float i;
                if (particlesPerFrame >= 1f) {
                    for (i = 0; i < particlesPerFrame; i++) {
                        new Star((int) (CamController.getDefaultInstance().getX() + 520), (int) (Math.random() * (maxY-minY))+minY,strength);
                    }
                } else {
                    i = 0;
                }
                extraParticles += Math.abs(i - particlesPerFrame);
                if (extraParticles >= 1f) {
                    new Star((int) (CamController.getDefaultInstance().getX() + 520), (int) (Math.random() * (maxY-minY))+minY,strength);
                    extraParticles = 0f;
                }

            }
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame.logic;

import mygame.CommonVar;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.system.Core;

/**
 *
 * @author Do$$e
 */
public class SpeedChanger extends Sprite implements ILoopable{
    float newSpeed;
    public SpeedChanger(int x, float newSpeed) {
        super(x,0,null,7);
        this.newSpeed=newSpeed;
    }

    @Override
    public void loop() {
        if(CommonVar.destroyAll) destroy();
        if(!Core.getInstance().isGamePaused()){
            if(this.getXOnScreen()<1024){
                CamController.getDefaultInstance().setSpeedX(newSpeed);
                destroy();
            }
        }
    }
}

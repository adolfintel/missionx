/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame.art;

import mygame.CommonVar;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;
import org.easyway.system.Core;

/**
 *
 * @author Do$$e
 */
public class SlidingSprite extends Sprite implements ILoopable{
    float speedX;
    public SlidingSprite(int x,String path,float speedX) {
        super(x,0,Texture.getTexture(path),7);  //inffeciency rulez!
        this.speedX=speedX;
    }

    @Override
    public void loop() {
        if(CommonVar.destroyAll) destroy();
        if(!Core.getInstance().isGamePaused()){
            if(this.getXOnScreen()<1024){
                move(speedX,0);
                if(this.getXOnScreen()+this.getWidth()<0) destroy();
            }
        }
    }
}

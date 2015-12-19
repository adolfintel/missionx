/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame.logic;

import mygame.CommonVar;
import mygame.sounds.MusicLoop;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.system.Core;

/**
 *
 * @author Do$$e
 */
public class MusicChanger extends Sprite implements ILoopable{
    String newMusic; int msLenght;
    public MusicChanger(int x, String newMusic, int msLenght) {
        super(x,0,null,7);
        this.newMusic=newMusic; this.msLenght=msLenght;
    }

    @Override
    public void loop() {
        if(CommonVar.destroyAll) destroy();
        if(!Core.getInstance().isGamePaused()){
            if(this.getXOnScreen()<1024){
                if(!CommonVar.music) return;
                if(CommonVar.musicL!=null){ CommonVar.musicL.stop(); CommonVar.musicL=null;}
                CommonVar.musicL=new MusicLoop(newMusic,msLenght);
                destroy();
            }
        }
    }
}
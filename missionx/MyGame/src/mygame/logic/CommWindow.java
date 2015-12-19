/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame.logic;

import mygame.CommonVar;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;
import org.easyway.system.StaticRef;
import org.easyway.utils.Timer.SyncTimer;

/**
 *
 * @author Do$$e
 */
public class CommWindow extends Sprite implements ILoopable{
    private boolean destroy=false;
private class Chronometer extends SyncTimer{

        public Chronometer(int ms) {
            super(ms);
        }

        @Override
        public void onTick() {
            destroy=true;
            stop();
        }
    
}
    public CommWindow(Texture t,int ms) {
        super(CommonVar.xRes-t.getWidth()/StaticRef.getCamera().getZoom2D(),CommonVar.yRes-t.getHeight()/StaticRef.getCamera().getZoom2D(),t,7);
        setScaleX(1/StaticRef.getCamera().getZoom2D()); setScaleY(1/StaticRef.getCamera().getZoom2D());
        setFixedOnScreen(true);
        setIdLayer(7);
        new Chronometer(ms).start();
    }

    @Override
    public void loop() {
        if(CommonVar.destroyAll||destroy) destroy();
    }

}

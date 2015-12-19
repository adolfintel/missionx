/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.art;

import mygame.*;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;

/**
 *
 * @author Do$$e
 */
public class ParallaxStarsMenu extends Sprite implements ILoopable {

    private Emitter background, foreground;
    private int yEmitter;

    public ParallaxStarsMenu(int y) {
        super(0, 0, Texture.getTexture("images/nullCursor.png"));
        yEmitter = y;
        background = new Emitter();
        background.setY(y);
        background.setParticle(Texture.getTexture("images/smallStar.png"));
        background.setUseanimazione(false);
        background.setxSpeedMax(-0.1f);
        background.setxSpeedMin(-0.5f);
        background.setySpeedMax(0);
        background.setySpeedMin(0);
        background.setxAccelerationMax(0);
        background.setxAccelerationMin(0);
        background.setyAccelerationMax(0);
        background.setyAccelerationMin(0);
        background.setRMax(1);
        background.setRMin(1);
        background.setGMax(1);
        background.setGMin(1);
        background.setBMax(1);
        background.setBMin(1);
        background.setxSizeIncMax(0);
        background.setxSizeIncMin(0);
        background.setySizeIncMax(0);
        background.setySizeIncMin(0);
        background.setFadeIn(0);
        background.setFadeOut(0);
        background.setLifeMax(100000);
        background.setLifeMin(100000);
        background.setDrawScale(1);
        background.setScale(1);
        background.setInitialRotationMax(0);
        background.setInitialRotationMin(0);
        background.setRotRateIncMax(0);
        background.setRotRateIncMin(0);
        background.setRotRateMax(0);
        background.setRotRateMin(0);
        background.setParticlesLayer(0);
        background.setLayer(0);
        background.setyOffsetMax(768);
        background.setyOffsetMin(0);
        background.setParticlesPerSecond(5);
        /*background.setAutoDestroyMaxX(1024);
        background.setAutoDestroyMaxY(768);
        background.setAutoDestroyParticles(true);*/
        for (int i = 0; i < 1024; i += 5/CommonVar.gfxQuality) {
            background.setxOffsetMax(i);
            background.setxOffsetMin(i);
            background.makeParticle();
        }
        background.setxOffsetMax(0);
        background.setxOffsetMin(0);

        foreground = new Emitter();
        foreground.setY(y);
        foreground.setParticle(Texture.getTexture("images/bigStar.png"));
        foreground.setUseanimazione(false);
        foreground.setxSpeedMax(-0.6f);
        foreground.setxSpeedMin(-1);
        foreground.setySpeedMax(0);
        foreground.setySpeedMin(0);
        foreground.setxAccelerationMax(0);
        foreground.setxAccelerationMin(0);
        foreground.setyAccelerationMax(0);
        foreground.setyAccelerationMin(0);
        foreground.setRMax(1);
        foreground.setRMin(1);
        foreground.setGMax(1);
        foreground.setGMin(1);
        foreground.setBMax(1);
        foreground.setBMin(1);
        foreground.setxSizeIncMax(0);
        foreground.setxSizeIncMin(0);
        foreground.setySizeIncMax(0);
        foreground.setySizeIncMin(0);
        foreground.setFadeIn(0);
        foreground.setFadeOut(0);
        foreground.setLifeMax(40000);
        foreground.setLifeMin(40000);
        foreground.setDrawScale(1);
        foreground.setScale(1);
        foreground.setInitialRotationMax(0);
        foreground.setInitialRotationMin(0);
        foreground.setRotRateIncMax(0);
        foreground.setRotRateIncMin(0);
        foreground.setRotRateMax(0);
        foreground.setRotRateMin(0);
        foreground.setParticlesLayer(2);
        foreground.setLayer(0);
        foreground.setyOffsetMax(768);
        foreground.setyOffsetMin(0);
        foreground.setParticlesPerSecond(10);
        /*foreground.setAutoDestroyMaxX(1024);
        foreground.setAutoDestroyMaxY(768);
        foreground.setAutoDestroyParticles(true);*/
        for (int i = 0; i < 1024; i += 8/CommonVar.gfxQuality) {
            foreground.setxOffsetMax(i);
            foreground.setxOffsetMin(i);
            foreground.makeParticle();
        }
        foreground.setxOffsetMax(0);
        foreground.setxOffsetMin(0);
        background.setX(1040);
        foreground.setX(1040);
    }

    @Override
    public void loop() {
        if (CommonVar.destroyAll) {
            destroy();
        }
    }

    @Override
    protected void onDestroy() {
        background.destroy();
        foreground.destroy();
    }
}

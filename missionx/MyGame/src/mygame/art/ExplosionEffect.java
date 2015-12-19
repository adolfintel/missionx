package mygame.art;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import mygame.CommonVar;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;
import org.easyway.system.Core;

/**
 *
 * @author prog5ia1
 */
public class ExplosionEffect extends Sprite implements ILoopable {

    private float i = 0; private int size=40;
    private float initx, inity, speed;
    private Emitter explosion;

    public ExplosionEffect(float x, float y, int size, float speed) {
        super(x, y, Texture.getTexture("images/shockWave.png"));
        setIdLayer(5);
        setLayer(0);
        setScaleX(0);
        setScaleY(0);
        initx = x;
        inity = y;
        this.size=size;
        this.speed=speed;
        explosion = new Emitter(getX(), getY(),500);
        explosion.setUseanimazione(true);
        explosion.setAnimazione(CommonVar.exp);
        explosion.setParticlesLayer(5);
        explosion.setParticlesPerSecond(120);
        explosion.setX(getX());
        explosion.setY(getY());
        explosion.setScale(0.5f);
        explosion.setDrawScale(1);
        explosion.setxOffsetMax(0);
        explosion.setxOffsetMin(0);
        explosion.setyOffsetMax(0);
        explosion.setyOffsetMin(0);
        explosion.setxSpeedMin(-5);
        explosion.setxSpeedMax(2);
        explosion.setySpeedMin(-5);
        explosion.setySpeedMax(2);
        explosion.setRMin(1);
        explosion.setRMax(1);
        explosion.setGMin(1);
        explosion.setGMax(1);
        explosion.setBMin(1);
        explosion.setBMax(1);
        explosion.setLifeMin(700);
        explosion.setLifeMax(800);
        explosion.setFadeIn(100);
        explosion.setFadeOut(200);
        explosion.setxSizeIncMin(12);
        explosion.setxSizeIncMax(15);
        explosion.setySizeIncMin(12);
        explosion.setySizeIncMax(15);
        explosion.setInitialRotationMin(0);
        explosion.setInitialRotationMax(0);
        explosion.setRotRateMin(-0.002f);
        explosion.setRotRateMax(0.002f);
    }

    @Override
    public void loop() {
        setScaleX((float) i / 3f);
        setScaleY((float) i / 3f);
        explosion.setxOffsetMax(getWidth()+50);
        explosion.setxOffsetMin(-getHeight()-50);
        explosion.setyOffsetMax(getWidth()+50);
        explosion.setyOffsetMin(-getHeight()-50);
        if(size>5)explosion.setParticlesPerSecond(400+(int)(Math.E*(float)i/40)*1000); else explosion.setParticlesPerSecond(120);
        setXY(initx - getWidth() / 2f, inity - getHeight() / 2f);
        setRGBA(1, 1, 0.6f, 3 * ((float)size - i) / size);
        i += Core.getInstance().getSpeedMultiplier()*speed;
        if (i >= size) {
            explosion.destroy();
            destroy();
        }
    }
}

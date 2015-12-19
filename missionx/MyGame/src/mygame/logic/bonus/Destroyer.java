/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.logic.bonus;

import mygame.CommonVar;
import mygame.art.Emitter;
import mygame.logic.CamController;
import mygame.sounds.SoundBox;
import org.easyway.collisions.GroupCollision;
import org.easyway.collisions.methods.HardWarePixelMethod;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.sprites2D.SpriteColl;
import org.easyway.objects.texture.Texture;
import org.easyway.system.Core;

/**
 *
 * @author prog5ia1
 */
public class Destroyer extends SpriteColl implements ILoopable {

    private float i = 0;
    private float initx, inity;
    private Emitter explosion;
    private boolean effectOnly=false;

    public Destroyer(float x, float y,boolean effectOnly) {
        super(x, y, Texture.getTexture("images/shockWave.png"));
        setIdLayer(5);
        setLayer(0);
        setScaleX(0);
        setScaleY(0);
        initx = x;
        inity = y;
        explosion = new Emitter(getX(), getY(),500);
        explosion.setUseanimazione(true);
        explosion.setAnimazione(CommonVar.exp);
        explosion.setParticlesLayer(5);
        explosion.setParticlesPerSecond(200);
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
        this.effectOnly=effectOnly;
        if(!effectOnly)GroupCollision.getGroup("playerShots").addToSource(this);
        SoundBox.play(16);
        CamController.shake(700,32);
        if (Core.getGLIntVersion() < 15) {
            setCollisionMethod(org.easyway.collisions.methods.RectangleCollisionMethod.getDefaultInstance());
        } else if (CommonVar.swCollisions) {
            setCollisionMethod(org.easyway.collisions.methods.SoftwareCollisionMethod.getDefaultInstance());
        } else {
            setCollisionMethod(HardWarePixelMethod.getDefaultInstance());
        }
    }

    @Override
    public void loop() {
        setScaleX((float) i / 3f);
        setScaleY((float) i / 3f);
        explosion.setxOffsetMax(getWidth()+50);
        explosion.setxOffsetMin(-getHeight()-50);
        explosion.setyOffsetMax(getWidth()+50);
        explosion.setyOffsetMin(-getHeight()-50);
        explosion.setParticlesPerSecond(400+(int)(Math.E*(float)i/40)*1000);
        setXY(initx - getWidth() / 2f, inity - getHeight() / 2f);
        setRGBA(1, 1, 0.6f, 3 * (40f - i) / 40);
        i += Core.getInstance().getSpeedMultiplier();
        if (i >= 40) {
            explosion.destroy();
            if(!effectOnly){ GroupCollision.getGroup("playerShots").removeFromSource(this); setImage(Texture.getTexture("images/nullCursor.png"));}
            destroy();
        }
    }


}

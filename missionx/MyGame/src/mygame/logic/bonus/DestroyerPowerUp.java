/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.logic.bonus;

import mygame.*;
import mygame.art.Emitter;
import org.easyway.collisions.GroupCollision;
import org.easyway.collisions.ManagedGroupCollision;
import org.easyway.collisions.methods.HardWarePixelMethod;
import org.easyway.collisions.methods.RectangleCollisionMethod;
import org.easyway.collisions.quad.QuadTree;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.sprites2D.SpriteColl;
import org.easyway.objects.texture.Texture;
import org.easyway.system.Core;

/**
 *
 * @author Do$$e
 */
public class DestroyerPowerUp extends SpriteColl implements ILoopable {

    float cycle;
    float scale, alpha = 1;

    public DestroyerPowerUp(float x, float y, float scale) {
        super(x, y, Texture.getTexture("images/destroyerIcon.png"),5);
        scale*=1.25;
        this.setSmoothImage(CommonVar.smoothing);
        setCollisionMethod(RectangleCollisionMethod.getDefaultInstance());
        setSize(getWidth() * scale,getHeight() * scale);
        if (QuadTree.isUsingQuadTree()) {
                ManagedGroupCollision.get("powerUp").addToDestination(this);
            } else {
                GroupCollision.getGroup("powerUp").addToDestination(this);
            }
        this.scale = scale;
    }

    public DestroyerPowerUp() {
        this(0, 0, 1);
    }

    public void sposta() {
        move((float)Math.sin((float)cycle/15f),(float)-Math.cos((float)cycle/15f));
    }

    @Override
    public void loop() {
        if (CommonVar.destroyAll == true) {
                    kill();
                }
        if (!Core.getInstance().isGamePaused()) {
            float speedMul = Core.getInstance().getSpeedMultiplier();
            setRGBA((float) Math.abs(Math.sin((float) cycle / 20)) + 0.2f, (float) Math.abs(Math.sin((float) cycle / 20)) + 0.2f, 1, alpha);
            cycle+=speedMul;
            sposta();
            alpha -= 0.0025f * speedMul;
            if (alpha <= 0.05f) {
                GroupCollision.getGroup("powerUp").removeFromDestination(this);
                kill();
                return;
            }
        }
    }

    @Override
    public void onCollision() {
        if (!Core.getInstance().isGamePaused()) {
            destroy();
        }
    }

    @Override
    public void onDestroy() {
        GroupCollision.getGroup("powerUp").removeFromDestination(this);
        new Destroyer(getX(),getY(),false);
        Emitter e;
        e = new Emitter(getX(),getY(), 100);
        e.setParticle(Texture.getTexture("images/whiteBloom.png"));
        e.setParticlesLayer(5);
        e.setParticlesPerSecond(500);
        e.setLifeMin(1000);
        e.setLifeMax(1000);
        e.setFadeIn(250);
        e.setFadeOut(250);
        e.setxSpeedMax(2);
        e.setxSpeedMin(-2);
        e.setySpeedMax(2);
        e.setySpeedMin(-2);
        e.setDrawScale(0.5f * scale);
        e.setRotRateIncMax(0);
        e.setRotRateIncMin(0);
        e.setInitialRotationMin(0);
        e.setInitialRotationMax(0);
        e.setBMax(1);
        e.setBMin(1);
        e.setGMax(1);
        e.setGMin(1);
        e.setRMax(1);
        e.setRMin(1);
        e.setxSizeIncMax(0);
        e.setxSizeIncMin(0);
        e.setySizeIncMax(0);
        e.setySizeIncMin(0);
        e.setOpacityMax(1);
        e.setOpacityMin(0.5f);
    }
}

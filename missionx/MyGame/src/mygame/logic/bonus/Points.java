/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.logic.bonus;

import mygame.sounds.SoundBox;
import mygame.*;
import mygame.art.Emitter;
import mygame.art.Particle;
import org.easyway.collisions.GroupCollision;
import org.easyway.collisions.ManagedGroupCollision;
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
public class Points extends SpriteColl implements ILoopable {

    float cycle;
    float scale, alpha = 1;
    float speedX = (float) (Math.random() * 2) - 1, speedY = (float) (Math.random() * 2) - 1;

    public Points(float x, float y, float scale) {
        super(x, y, Texture.getTexture("images/points.png"),5);
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

    public Points() {
        this(0, 0, 1);
    }

    public void sposta(float speedX, float speedY) {
        move(speedX, speedY);
    }

    @Override
    public void loop() {
        if (CommonVar.destroyAll == true) {
                    kill();
                }
        if (!Core.getInstance().isGamePaused()) {
            float speedMul = Core.getInstance().getSpeedMultiplier();
            sposta(speedX * speedMul, speedY * speedMul);
            setRGBA((float) Math.abs(Math.sin((float) cycle / 20)) + 0.2f, (float) Math.abs(Math.sin((float) cycle / 20)) + 0.2f, 1, alpha);
            cycle+=speedMul;
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
        destroy();
    }

    @Override
    public void onDestroy() {
        GroupCollision.getGroup("powerUp").removeFromDestination(this);
        if (!Core.getInstance().isGamePaused()) {
            SoundBox.play(4);
            Emitter e;
            e = new Emitter(getX(),getY(), 100);
            e.setParticle(Texture.getTexture("images/blueBloom.png"));
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
            new Particle(Texture.getTexture("images/points.png"), null, false, getX(),getY(), 0, 0, 250, 12, 25, 150, 850, 1, 1, 1, 1, 0.9f, 6, 0, 0, 0, 0, 0, 0.01f).setIdLayer(6);;
        }
    }
}

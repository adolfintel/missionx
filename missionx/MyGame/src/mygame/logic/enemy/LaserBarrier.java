/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.logic.enemy;

import mygame.*;
import mygame.art.Emitter;
import org.easyway.collisions.GroupCollision;
import org.easyway.collisions.ManagedGroupCollision;
import org.easyway.collisions.methods.HardWarePixelMethod;
import org.easyway.collisions.quad.QuadTree;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.sprites2D.SpriteColl;
import org.easyway.objects.texture.Texture;
import org.easyway.system.Core;
import org.easyway.system.state.OpenGLState;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Do$$e
 */
public class LaserBarrier extends SpriteColl implements ILoopable {

    boolean up;
    Sprite img;

    public LaserBarrier(float x, float y, boolean up) {
        super(x + 32, y, Texture.getTexture("images/laserBarrier_mask.png"), 1);
        this.setSmoothImage(CommonVar.smoothing);
        if (Core.getGLIntVersion() < 15) {
            setCollisionMethod(org.easyway.collisions.methods.RectangleCollisionMethod.getDefaultInstance());
        } else if (CommonVar.swCollisions) {
            setCollisionMethod(org.easyway.collisions.methods.SoftwareCollisionMethod.getDefaultInstance());
        } else {
            setCollisionMethod(HardWarePixelMethod.getDefaultInstance());
        }
        this.up = up;
        img = new Sprite(x, y, Texture.getTexture("images/laserBarrier.png"));
        if (QuadTree.isUsingQuadTree()) {
            ManagedGroupCollision.get("enemyShots").addToSource(this);
        } else {
            GroupCollision.getGroup("enemyShots").addToSource(this);
        }

    }

    public LaserBarrier() {
        this(0, 0, true);
    }

    public void onDestroy() {
        img.destroy();
        GroupCollision.getGroup("enemyShots").removeFromSource(this);
        Emitter e = new Emitter(getX(), getY(), 100);
        e.setParticle(Texture.getTexture("images/bloom2.png"));
        e.setScale(1);
        e.setBMax(.3f);
        e.setBMin(0);
        e.setGMax(.3f);
        e.setGMin(0);
        e.setRMax(1);
        e.setRMin(1);
        e.setLifeMax(1000);
        e.setLifeMin(700);
        e.setFadeIn(100);
        e.setFadeOut(500);
        e.setOpacityMin(0.5f);
        e.setOpacityMax(0.7f);
        e.setxSpeedMax(2);
        e.setxSpeedMin(-2);
        e.setyOffsetMin(0);
        e.setyOffsetMax(768);
        e.setParticlesLayer(2);
        e.setParticlesPerSecond(2500);
        //e.setAdditiveRender(true);
        if (up) {
            e.setySpeedMax(3);
            e.setySpeedMax(1);
            e.setyAccelerationMin(0.3f);
            e.setyAccelerationMax(0.3f);
        } else {
            e.setySpeedMax(-1);
            e.setySpeedMin(-3);
            e.setyAccelerationMax(-0.3f);
            e.setyAccelerationMin(-0.3f);
        }
        e.setxSizeIncMax(2);
        e.setxSizeIncMin(2);
        e.setySizeIncMax(2);
        e.setySizeIncMin(2);
        e.setDrawScale(0.3f);
    }

    @Override
    public void loop() {
        if (CommonVar.destroyAll == true) {
            destroy();
        }
        float val=(float) Math.abs(Math.sin(System.nanoTime()))+0.3f;
        img.setRGBA(1,1,1,val);
    }

}

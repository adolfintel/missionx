package mygame.logic.shots;

import mygame.*;
import mygame.art.Emitter;
import org.easyway.collisions.GroupCollision;
import org.easyway.collisions.ManagedGroupCollision;
import org.easyway.collisions.methods.HardWarePixelMethod;
import org.easyway.collisions.quad.QuadTree;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.animo.Animo;
import org.easyway.objects.sprites2D.SpriteColl;
import org.easyway.objects.texture.Texture;
import org.easyway.system.Core;

public class EnemyGreenLaser extends SpriteColl implements ILoopable {

    float speedX, speedY;

    public EnemyGreenLaser(float x, float y, float scale, boolean isSource, float speedX, float speedY, int layer) {
        super(x, y, Texture.getTexture("images/greenLaser.png"), layer);
        setIdLayer(layer);
        this.setSmoothImage(CommonVar.smoothing);
        if (Core.getGLIntVersion() < 15) {
            setCollisionMethod(org.easyway.collisions.methods.RectangleCollisionMethod.getDefaultInstance());
        } else if (CommonVar.swCollisions) {
            setCollisionMethod(org.easyway.collisions.methods.SoftwareCollisionMethod.getDefaultInstance());
        } else {
            setCollisionMethod(HardWarePixelMethod.getDefaultInstance());
        }
        this.speedX = speedX * scale;
        this.speedY = speedY * scale;
        setSize(getWidth() * scale, getHeight() * scale);

        if (isSource) {
            if (QuadTree.isUsingQuadTree()) {
                ManagedGroupCollision.get("enemyShots").addToSource(this);
            } else {
                GroupCollision.getGroup("enemyShots").addToSource(this);
            }
        } else {
            if (QuadTree.isUsingQuadTree()) {
                ManagedGroupCollision.get("enemyShots").addToDestination(this);
            } else {
                GroupCollision.getGroup("enemyShots").addToDestination(this);
            }
        }
    }

    public EnemyGreenLaser() {
    }

    @Override
    public void loop() {
        if (!Core.getInstance().isGamePaused()) {
            if (CommonVar.destroyAll == true) {
                kill();
                return;
            }
            if (getXOnScreen() >= 1050 || getXOnScreen()+getWidth() <=-20 ) {
                    destroy();
                }
            float speedMul = Core.getInstance().getSpeedMultiplier();
            move(speedX * speedMul, speedY * speedMul);
        }
    }

    @Override
    public void onDestroy() {
        Emitter explosion = new Emitter(getX() - 10, getY() - 10, 40);
        explosion.setParticlesPerSecond(300);
        explosion.setLifeMax(350);
        explosion.setLifeMin(300);
        explosion.setParticle(Texture.getTexture("images/bloom2.png"));
        explosion.setFadeOut(100);
        explosion.setRMax(0.1f);
        explosion.setRMin(0f);
        explosion.setGMax(1f);
        explosion.setGMin(1f);
        explosion.setBMax(0.1f);
        explosion.setBMin(0f);
        explosion.setxSpeedMax(2f);
        explosion.setxSpeedMin(-2f);
        explosion.setySpeedMax(2);
        explosion.setySpeedMin(-2f);
        explosion.setInitialRotationMin(-1);
        explosion.setInitialRotationMin(1);
        explosion.setParticlesLayer(6);
        explosion.setDrawScale(1f);
        explosion.setxSizeIncMax(0);
        explosion.setxSizeIncMin(0);
        explosion.setySizeIncMax(0);
        explosion.setySizeIncMin(0);
    }

    @Override
    public void onCollision() {
        if (!Core.getInstance().isGamePaused()) {
            destroy();
        }
    }
}

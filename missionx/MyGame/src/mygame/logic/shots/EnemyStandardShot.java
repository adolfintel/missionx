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

public class EnemyStandardShot extends SpriteColl implements ILoopable {

    float speedX, speedY;

    public EnemyStandardShot(float x, float y, float scale, boolean isSource, float speedX, float speedY, int layer) {
        super(x, y, Texture.getTexture("images/shot2.png"), layer);
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

    public EnemyStandardShot() {
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
        Emitter explosion = new Emitter(getX(),getY() - 10, 20);
        explosion.setParticlesPerSecond(120);
        explosion.setLifeMax(350);
        explosion.setLifeMin(300);
        explosion.setUseanimazione(true);
        explosion.setAnimazione(CommonVar.fastExp);
        explosion.setFadeOut(100);
        explosion.setRMax(1f);
        explosion.setRMin(1f);
        explosion.setGMax(1f);
        explosion.setGMin(1f);
        explosion.setBMax(1f);
        explosion.setBMin(1f);
        explosion.setxSpeedMax(0.5f);
        explosion.setxSpeedMin(-0.5f);
        explosion.setySpeedMax(1);
        explosion.setySpeedMin(0.5f);
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

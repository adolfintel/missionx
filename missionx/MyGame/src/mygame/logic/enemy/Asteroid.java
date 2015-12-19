/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.logic.enemy;

import mygame.sounds.SoundBox;
import mygame.logic.Player;
import mygame.logic.CamController;
import mygame.logic.bonus.LaserPowerUp;
import mygame.logic.bonus.RocketsPowerUp;
import mygame.logic.bonus.SuperFirePowerUp;
import mygame.logic.bonus.ShieldPowerUp;
import mygame.logic.bonus.SpeedPowerUp;
import mygame.art.Particle;
import mygame.logic.bonus.Points;
import mygame.logic.bonus.OneUP;
import mygame.*;
import mygame.art.Emitter;
import java.util.ArrayList;
import mygame.logic.bonus.EnergyPowerUp;
import org.easyway.collisions.GroupCollision;
import org.easyway.collisions.ManagedGroupCollision;
import org.easyway.collisions.methods.HardWarePixelMethod;
import org.easyway.collisions.quad.QuadTree;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.interfaces.sprites.ISpriteColl;
import org.easyway.objects.animo.Animo;
import org.easyway.objects.sprites2D.SpriteColl;
import org.easyway.objects.texture.Texture;
import org.easyway.system.Core;
import org.easyway.utils.Timer.SyncTimer;

/**
 *
 * @author Do$$e
 */
public class Asteroid extends SpriteColl implements ILoopable {

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
    float scale = 1f, rot = (float) (Math.random() * 0.5f) - 0.25f;
    Emitter explosion, shockWave;
    Texture defImg, hitImg;
    int cycle = 0, health = 10, rotation = 0;
    boolean canShot = true, explode = false, toDestroy = false, lockControls = false, makeCollisions = true;

    public Asteroid(float x, float y, int health, Texture defImg, Texture hitImg) {
        super(x, y, Texture.getTexture("images/enemyIcon.png"));
        if (Core.getGLIntVersion() < 15) {
            setCollisionMethod(org.easyway.collisions.methods.RectangleCollisionMethod.getDefaultInstance());
        } else if (CommonVar.swCollisions) {
            setCollisionMethod(org.easyway.collisions.methods.SoftwareCollisionMethod.getDefaultInstance());
        } else {
            setCollisionMethod(HardWarePixelMethod.getDefaultInstance());
        }
        this.defImg = defImg;
        this.hitImg = hitImg;
    }

    public Asteroid() {
        this(600, 300, 10, Texture.getTexture("images/asteroids/1.png"), Texture.getTexture("images/asteroids/1_hit.png"));
    }

    public void init() {
        this.health = (int) (health * CommonVar.difficulty);
        this.setSmoothImage(CommonVar.smoothing);
        if (QuadTree.isUsingQuadTree()) {
            ManagedGroupCollision.get("ships").addToDestination(this);
        } else {
            GroupCollision.getGroup("ships").addToDestination(this);
        }
        if (QuadTree.isUsingQuadTree()) {
            ManagedGroupCollision.get("playerShots").addToDestination(this);
        } else {
            GroupCollision.getGroup("playerShots").addToDestination(this);
        }
        setImage(defImg);
        setWidth((int) (defImg.getWidth() * scale));
        setHeight((int) (defImg.getHeight() * scale));
        setRotation((float) (Math.random() * 360));

    }

    private class ExplosionChronometer extends SyncTimer {

        public ExplosionChronometer() {
            super(60);
        }

        @Override
        public void onTick() {
            toDestroy = true;
            stop();
        }
    }

    @Override
    public void loop() {
        if (CommonVar.destroyAll == true) {
            destroy();
        }
        if (!Core.getInstance().isGamePaused()) {
            setImage(defImg);
            float centerX = CamController.getDefaultInstance().getX();
            if (getX() < centerX - 1000) {
                destroy();
            }
            setRotation(this.getRotation() + rot * Core.getInstance().getSpeedMultiplier());
            if (getX() < centerX + 512 && getX() > centerX - 1000) {
                if (cycle == 0) {
                    init();
                }
                cycle++;
                float speedMul = Core.getInstance().getSpeedMultiplier();
                if (health < 0) {
                    SoundBox.play(11);
                    explode = true;
                    health = 99999999;
                    makeCollisions = false;
                }
                //----------------------------------INIZIO AI---------------------------------
                if (getX() < centerX - 1024) {
                    explode = true;  //la siddetta esplosione inutile xD
                }
            }
            //----------------------------------FINE AI---------------------------------
            if (explode) {
                lockControls = true;
                explosion = new Emitter(getX(), getY(), 200);
                new ExplosionChronometer().start();
                explosion.setUseanimazione(true);
                explosion.setAnimazione(CommonVar.exp);
                explosion.setParticlesLayer(5);
                explosion.setParticlesPerSecond(100);
                explosion.setScale(scale / 2);
                explosion.setDrawScale(scale / 2);
                explosion.setxOffsetMax(128);
                explosion.setxOffsetMin(0);
                explosion.setyOffsetMax(128);
                explosion.setyOffsetMin(0);
                explosion.setxSpeedMin(-5);
                explosion.setxSpeedMax(2);
                explosion.setySpeedMin(-5);
                explosion.setySpeedMax(2);
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
                explosion.setRMax(1);
                explosion.setRMin(1);
                explosion.setGMax(1);
                explosion.setGMin(1);
                explosion.setBMax(1);
                explosion.setBMin(1);
                //shockwave
                new Particle(Texture.getTexture("images/shockWave.png"), null, false, getX() + getWidth() / 2, getY() + getHeight() / 2, 0, 0, 1000, 10, 10, 100, 500, scale, 1, 1, 1, 1, 3, 0, 0, 0, 0, 0, 0.01f);
            }
            if (toDestroy) {
                destroy();
            }
            explode = false;
        }
    }

    @Override
    public void onDestroy() {
        try {
            explosion.destroy();
        } catch (Exception e) {
        }       //Who the fuck cares about emitter errors???
        for (int i = 0; i < 3; i++) {
            new Points((float) (getX() + ((Math.random() * 100) * scale)), (float) (getY() + ((Math.random() * 100) * scale)), 0.5f * scale);
        }
        int r = (int) (Math.random() * 20);
        switch (r) {
            case 1:
                new ShieldPowerUp((float) (getX() + ((Math.random() * 100) * scale)), (float) (getY() + ((Math.random() * 100) * scale)), 0.5f * scale);
                break;
            case 3:
                new OneUP((float) (getX() + ((Math.random() * 100) * scale)), (float) (getY() + ((Math.random() * 100) * scale)), 0.5f * scale);
                break;
            case 5:
                new LaserPowerUp((float) (getX() + ((Math.random() * 100) * scale)), (float) (getY() + ((Math.random() * 100) * scale)), 0.5f * scale);
                break;
            case 7:
                new SuperFirePowerUp((float) (getX() + ((Math.random() * 100) * scale)), (float) (getY() + ((Math.random() * 100) * scale)), 0.5f * scale);
                break;
            case 11:
                new SpeedPowerUp((float) (getX() + ((Math.random() * 100) * scale)), (float) (getY() + ((Math.random() * 100) * scale)), 0.5f * scale);
                break;
            case 13:
                new RocketsPowerUp((float) (getX() + ((Math.random() * 100) * scale)), (float) (getY() + ((Math.random() * 100) * scale)), 0.5f * scale);
                break;
            case 15:
                new EnergyPowerUp((float) (getX() + ((Math.random() * 100) * scale)), (float) (getY() + ((Math.random() * 100) * scale)), 0.5f * scale);
                break;
        }
    }

    @Override
    public void onCollision() {
        if (makeCollisions) {
            setImage(hitImg);
            health -= 5;
            ArrayList<ISpriteColl> lista = getCollisionList();
            for (ISpriteColl spr : lista) {

                if (spr instanceof Player) {
                    health = -1;
                    makeCollisions = false;
                } else if (spr instanceof Player.playerLaser) {
                    health -= 5;
                } else if (spr instanceof Player.playerRocket) {
                    health -= 20;
                }
            }
        }
    }
}

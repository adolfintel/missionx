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
import mygame.art.ExplosionEffect;
import mygame.logic.bonus.Destroyer;
import mygame.logic.bonus.DestroyerPowerUp;
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
public class Enemy6 extends SpriteColl implements ILoopable {

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
    float scale = 1f;
    Emitter explosion, shockWave;
    Texture ship, ship_hit;
    int cycle = 0, shotsPerSecond = 2, health = 20, rotation = 0;
    LaserBarrier b;
    boolean canShot = true, explode = false, toDestroy = false, lockControls = false, makeCollisions = true;

    public Enemy6(float x, float y, int health) {
        super(x, y,Texture.getTexture("images/enemyIcon.png"),3);
        if (Core.getGLIntVersion() < 15) {
            setCollisionMethod(org.easyway.collisions.methods.RectangleCollisionMethod.getDefaultInstance());
        } else if (CommonVar.swCollisions) {
            setCollisionMethod(org.easyway.collisions.methods.SoftwareCollisionMethod.getDefaultInstance());
        } else {
            setCollisionMethod(HardWarePixelMethod.getDefaultInstance());
        }
        this.health=health;
    }

    public Enemy6() {
        this(600, 300, 20);
    }

    public void init() {
        setIdLayer(3);
        this.health = (int) (health * CommonVar.difficulty);
        this.shotsPerSecond *= CommonVar.difficulty;
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
        ship = Texture.getTexture("images/enemy6/enemy6.png");
        ship_hit = Texture.getTexture("images/enemy6/enemy6_hit.png");
        setImage(ship);
        setWidth((int) (getWidth() * scale));
        setHeight((int) (getHeight() * scale));
        
        b = new LaserBarrier(getX() + (getWidth() / 2 - 35) * scale,getY()-640, false);
    }

    private class Cronometro extends SyncTimer {

        public Cronometro() {
            super(1000 / shotsPerSecond);
        }

        @Override
        public void onTick() {
            canShot = true;
            stop();
        }
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
            float centerX = CamController.getDefaultInstance().getX();
            if (getX() < centerX - 1000) {
                destroy();
            }
            if (getX() < centerX + 512 && getX() > centerX - 1000) {
                setImage(ship);
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
                if (explode) {

                    lockControls = true;
                    new ExplosionEffect(getX()+getWidth()/2,getY()+getHeight()/2, 4,0.3f);
//                    explosion = new Emitter(getX(), getY(), 200);
                    new ExplosionChronometer().start();
//                    explosion.setUseanimazione(true);
//                    explosion.setAnimazione(exp);
//                    explosion.setParticlesLayer(5);
//                    explosion.setParticlesPerSecond(100);
//                    explosion.setScale(scale/2);
//                    explosion.setDrawScale(scale);
//                    explosion.setxOffsetMax(getWidth()*2);
//                    explosion.setxOffsetMin(0);
//                    explosion.setyOffsetMax(getHeight()*2);
//                    explosion.setyOffsetMin(0);
//                    explosion.setxSpeedMin(-5);
//                    explosion.setxSpeedMax(2);
//                    explosion.setySpeedMin(-5);
//                    explosion.setySpeedMax(2);
//                    explosion.setLifeMin(700);
//                    explosion.setLifeMax(800);
//                    explosion.setFadeIn(100);
//                    explosion.setFadeOut(200);
//                    explosion.setxSizeIncMin(12);
//                    explosion.setxSizeIncMax(15);
//                    explosion.setySizeIncMin(12);
//                    explosion.setySizeIncMax(15);
//                    explosion.setInitialRotationMin(0);
//                    explosion.setInitialRotationMax(0);
//                    explosion.setRotRateMin(-0.002f);
//                    explosion.setRotRateMax(0.002f);
//                    explosion.setRMax(1);
//                    explosion.setRMin(1);
//                    explosion.setGMax(1);
//                    explosion.setGMin(1);
//                    explosion.setBMax(1);
//                    explosion.setBMin(1);
//
//                    new Particle(Texture.getTexture("images/shockWave.png"), null, false, getX() + getWidth() / 2, getY() + getHeight() / 2, 0, 0, 1000, 10, 10, 100, 500, scale, 1, 1, 1, 1, 3, 0, 0, 0, 0, 0, 0.01f);
                    /*
                    shockWave=new Emitter(x,y,100);
                    shockWave.setParticle(Texture.getTexture("images/shockWave.png"));
                    shockWave.setxOffsetMax(128);
                    shockWave.setxOffsetMin(0);
                    shockWave.setyOffsetMax(128);
                    shockWave.setyOffsetMin(56);
                    shockWave.setScale(scale);
                    shockWave.setDrawScale(0.01f);
                    shockWave.setxSizeIncMax(10);
                    shockWave.setxSizeIncMin(10);
                    shockWave.setySizeIncMax(10);
                    shockWave.setySizeIncMin(10);
                    shockWave.setLifeMin(500);
                    shockWave.setLifeMax(1500);
                    shockWave.setParticlesLayer(3);
                    shockWave.setParticlesPerSecond(50);
                    shockWave.setxSpeedMax(0);
                    shockWave.setxSpeedMin(0);
                    shockWave.setySpeedMax(0);
                    shockWave.setySpeedMin(0);
                    shockWave.setRMax(1);
                    shockWave.setRMin(1);
                    shockWave.setGMax(1);
                    shockWave.setGMin(1);
                    shockWave.setBMax(1);
                    shockWave.setBMin(1);
                    shockWave.setInitialRotationMin(0);
                    shockWave.setInitialRotationMax(0);
                    shockWave.setRotRateMin(0);
                    shockWave.setRotRateMax(0);
                     */                }
                if (toDestroy) {
                    destroy();
                }
                explode = false;
            }
        }
    }

    @Override
    public void onDestroy() {
        GroupCollision.getGroup("ships").removeFromDestination(this);
        try {
            if(explosion!=null) explosion.destroy();
            if(b!=null) b.destroy();
        } catch (Exception e) {
        }       //Who the fuck cares about emitter errors???
        for (int i = 0; i < 10; i++) {
            new Points((float) (getX() + ((Math.random() * 100) * scale)), (float) (getY() + ((Math.random() * 100) * scale)), 0.5f * scale);
        }
        int r = (int) (Math.random() * 20);
        switch (r) {
            case 1:
                if(Math.random()<0.5) new ShieldPowerUp((float) (getX() + ((Math.random() * 100) * scale)), (float) (getY() + ((Math.random() * 100) * scale)), 0.5f * scale);
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
            case 17:
                if(Math.random()<0.5) new DestroyerPowerUp((float) (getX() + ((Math.random() * 100) * scale)), (float) (getY() + ((Math.random() * 100) * scale)), 0.5f * scale);
                break;
        }
    }

    @Override
    public void onCollision() {
        if (!Core.getInstance().isGamePaused()) {
            if (makeCollisions) {
                health -= 5;
                setImage(ship_hit);
                ArrayList<ISpriteColl> lista = getCollisionList();
                for (ISpriteColl spr : lista) {

                    if (spr instanceof Player) {
                        health = -1;
                        makeCollisions = false;
                    } else if (spr instanceof Player.playerLaser) {
                        health -= 5;
                    } else if (spr instanceof Player.playerRocket) {
                        health -= 20;
                    }else if (spr instanceof Destroyer) {
                        health -= 100;
                    }
                }
            }
        }
    }
}


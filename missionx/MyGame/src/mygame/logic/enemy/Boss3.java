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
import mygame.logic.bonus.Points;
import mygame.logic.bonus.OneUP;
import mygame.art.LifeBar;
import mygame.logic.shots.EnemyGreenLaser;
import mygame.art.Emitter;
import mygame.*;
import java.util.ArrayList;
import mygame.art.ExplosionEffect;
import mygame.art.Particle;
import mygame.logic.bonus.EnergyPowerUp;
import mygame.logic.shots.EnemyPlasmaBall;
import mygame.logic.shots.EnemyStandardRocket;
import org.easyway.collisions.GroupCollision;
import org.easyway.collisions.ManagedGroupCollision;
import org.easyway.collisions.methods.HardWarePixelMethod;
import org.easyway.collisions.quad.QuadTree;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.interfaces.sprites.ISpriteColl;
import org.easyway.objects.sprites2D.SpriteColl;
import org.easyway.objects.texture.Texture;
import org.easyway.system.Core;
import org.easyway.utils.Timer.SyncTimer;

/**
 *
 * @author Do$$e
 */
public class Boss3 extends SpriteColl implements ILoopable {

    private boolean timer3=true;
    private boolean genExp=false;
private class ExpGenerator extends SyncTimer{

        public ExpGenerator() {
            super(250);
        }
        @Override
    public void onTick(){
        genExp=true;
        stop();
    }
}
    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
    float scale = 1f;
    Emitter explosion, shockWave;
    Texture bloom, ship, ship_hit;
    int cycle = 0, shotsPerSecond = 10, health = 1000, randY = (int) (Math.random() * 100);
    boolean canShot = true, explode = false, toDestroy = false, lockControls = false, makeCollisions = true, timer2 = true, inBattle = false;
    LifeBar lb;

    public Boss3(float x, float y, int health) {
        super(x, y, Texture.getTexture("images/enemyIcon.png"));
        if (Core.getGLIntVersion() < 15) {
            setCollisionMethod(org.easyway.collisions.methods.RectangleCollisionMethod.getDefaultInstance());
        } else if (CommonVar.swCollisions) {
            setCollisionMethod(org.easyway.collisions.methods.SoftwareCollisionMethod.getDefaultInstance());
        } else {
            setCollisionMethod(HardWarePixelMethod.getDefaultInstance());
        }
        this.health=health;
    }

    public Boss3() {
        this(7600, 384, 1000);
    }

    public void init() {
        setIdLayer(4);
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
        ship = Texture.getTexture("images/boss3/boss3.png");
        ship_hit = Texture.getTexture("images/boss3/boss3_hit.png");
        setImage(ship);
        setWidth((int) (getWidth() * scale));
        setHeight((int) (getHeight() * scale));

        
        new Timer2().start();
        lb = new LifeBar(getX() + getWidth() / 2 - 64, getY() - 32, health);
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

    private class Cronometro1 extends SyncTimer {

        public Cronometro1() {
            super((int)(500 / CommonVar.difficulty));
        }

        @Override
        public void onTick() {
            timer3 = true;
            stop();
        }
    }


    private class Timer2 extends SyncTimer {

        public Timer2() {
            super(700);
        }

        @Override
        public void onTick() {
            timer2 = !timer2;
        }
    }

    private class ExplosionChronometer extends SyncTimer {

        public ExplosionChronometer() {
            super(1000);
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
                if (!inBattle) {
                    new Particle(Texture.getTexture("images/fight_" + CommonVar.loc + ".png"), null, false, centerX, 384, 0, 0, 1500, 25, 25, 150, 850, 1, 1, 1, 1, 0.9f, 6, 0, 0, 0, 0, 0, 0.01f);
                }
                inBattle = true;
                CamController.setMoving(false);
                setImage(ship);
                if (cycle == 0) {
                    init();
                    cycle=1;
                }
                float speedMul = Core.getInstance().getSpeedMultiplier();
                cycle+=speedMul;
                if (health < 0) {
                    CamController.shake(2000, 5);
                    makeCollisions = false;
                    SoundBox.play(11);
                    SoundBox.play(9);
                    explode = true;
                    health = 999999999;
                }

                //----------------------------------INIZIO AI---------------------------------
                //DOVE SI TROVA IL GIOCATORE????
                final Player player = Player.getDefaultInstance();
                float playerX = player.getX();
                float playerY = (float) (player.getY() + 256*Math.sin((double) cycle / 35));
                //SPOSTIAMOCI!
                if (getY() + getHeight() / 2 < playerY + player.getHeight() / 2 && playerX - 50 < getX() && !lockControls && getX() < centerX + 512 && getX() > centerX - 512&&makeCollisions) {
                    move(0, 5 * speedMul);
                }
                if (getY() + getHeight() / 2 > playerY + player.getHeight() / 2 && playerX - 50 < getX() && !lockControls && getX() < centerX + 512 && getX() > centerX - 512&&makeCollisions) {
                    move(0, -5 * speedMul);
                }
                if(makeCollisions)move((float) Math.sin((double) cycle / 60) * 2,0);
                if (getY() < 0) {
                    setY(0);
                }
                if(playerX+getWidth()/2>centerX&&makeCollisions) move(2*speedMul,0);
                if (getY() > 768 - getHeight()&&makeCollisions) {
                    setY(768 - getHeight());
                }
                if (centerX < getX()&&makeCollisions) {
                    move(-2 * speedMul, 0);
                }
                if (getX() > centerX + 384&&makeCollisions) {
                    setX(centerX + 384);
                }
                if(getX()<centerX-200&&makeCollisions) setX(centerX-200);
                if (getX() < centerX - 1024&&makeCollisions) {

                    explode = true;
                } //la siddetta esplosione inutile xD
                lb.sposta((int) (getX() + getWidth() / 2) - 64, (int) getY() - 32);
                lb.setLife(health);
                //SHOOOOOOOOOOOOOOOT!!!!
                //if((canShot&&!moving)||(canShot&&playerY+(player.getHeight()/2)>y-(32*scale) && playerY+(player.getHeight()/2)<y+(32*scale) && playerX<x && x<centerX+512 && x>centerX-512)){
                if (playerX + player.getWidth() <= getX()) {
                    if (canShot && playerY + player.getHeight() >= getY() + (32 * scale) && playerY <= getY() + getHeight() - (32 * scale) && timer2) {
                        SoundBox.play(15);
                        new EnemyGreenLaser(getX() , getY() + (261 * scale), scale * 0.5f, true, -40, 0, 5);
                        new EnemyGreenLaser(getX() , getY() + (280 * scale), scale * 0.5f, true, -40, 0, 5);
                        canShot = false;
                        new Cronometro().start();
                    }
                }
                
                if (timer3&&!timer2) {
                    SoundBox.play(13);
                    new EnemyStandardRocket(getX() + (278 * scale), getY() + (52 * scale), scale * 0.9f, true, -20, (float)(Math.random()-0.5)*2, 5);
                    new EnemyStandardRocket(getX() + (278 * scale), getY() + (442 * scale), scale * 0.9f, true, -20, (float)(Math.random()-0.5)*2, 5);
                    if(health<500){
                        if(Math.random()<0.25){
                            new EnemyPlasmaBall(getX() + 163 * scale, getY() + 226 * scale, scale, true, -9, -1, 2);
                        new EnemyPlasmaBall(getX() + 163 * scale, getY() + 226 * scale, scale, true, -10, 0, 2).setIdLayer(5);
                        new EnemyPlasmaBall(getX() + 163 * scale, getY() + 226 * scale, scale, true, -9, 1, 2).setIdLayer(5);
                        }
                        if(Math.random()<0.5){
                            new EnemyPlasmaBall(getX() + 163 * scale, getY() + 280 * scale, scale, true, -9, -1, 2).setIdLayer(5);
                        new EnemyPlasmaBall(getX() + 163 * scale, getY() + 280 * scale, scale, true, -10, 0, 2).setIdLayer(5);
                        new EnemyPlasmaBall(getX() + 163 * scale, getY() + 280 * scale, scale, true, -9, 1, 2).setIdLayer(5);
                        }
                        
                    }
                    timer3 = false;
                    new Cronometro1().start();
                }
                //----------------------------------FINE AI---------------------------------
                if(genExp){new ExplosionEffect(getX()+getWidth()/2,getY()+getHeight()/2, 30,0.7f); genExp=false; new ExpGenerator().start();}
                if (explode) {

                    CamController.shake(2000, 5);
                    CamController.setMoving(true);
                    lockControls = true;
                    new ExpGenerator().start();
//                    explosion = new Emitter(getX(), getY(), 1200);
                    new ExplosionChronometer().start();
//                    explosion.setUseanimazione(true);
//                    explosion.setAnimazione(exp);
//                    explosion.setParticlesLayer(5);
//                    explosion.setParticlesPerSecond(150);
//                    explosion.setScale(scale /2);
//                    explosion.setDrawScale(scale/2);
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
//                    shockWave = new Emitter(getX(), getY(), 100);
//                    shockWave.setParticle(Texture.getTexture("images/shockWave.png"));
//                    shockWave.setxOffsetMax(128);
//                    shockWave.setxOffsetMin(0);
//                    shockWave.setyOffsetMax(128);
//                    shockWave.setyOffsetMin(0);
//                    shockWave.setDrawScale(0.01f);
//                    shockWave.setxSizeIncMax(10);
//                    shockWave.setxSizeIncMin(10);
//                    shockWave.setySizeIncMax(10);
//                    shockWave.setySizeIncMin(10);
//                    shockWave.setLifeMin(500);
//                    shockWave.setLifeMax(1500);
//                    shockWave.setParticlesLayer(3);
//                    shockWave.setParticlesPerSecond(50);
//                    shockWave.setxSpeedMax(0);
//                    shockWave.setxSpeedMin(0);
//                    shockWave.setySpeedMax(0);
//                    shockWave.setySpeedMin(0);
//                    shockWave.setRMax(1);
//                    shockWave.setRMin(1);
//                    shockWave.setGMax(1);
//                    shockWave.setGMin(1);
//                    shockWave.setBMax(1);
//                    shockWave.setBMin(1);
//                    shockWave.setInitialRotationMin(0);
//                    shockWave.setInitialRotationMax(0);
//                    shockWave.setRotRateMin(0);
//                    shockWave.setRotRateMax(0);
                }
                if (toDestroy) {
                    destroy();
                }
                explode = false;
            }
        }
    }

    @Override
    public void onDestroy() {
        /*try{
        explosion.destroy();
        }catch(Exception e){}*/
        GroupCollision.getGroup("ships").removeFromDestination(this);
        if (lb != null) {
            lb.destroy();
        }
        CamController.setMoving(true);
        for (int i = 0; i < 30; i++) {
            new Points((float) (getX() + ((Math.random() * 400) * scale)), (float) (getY() + ((Math.random() * 300) * scale)), 0.5f * scale);
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
        if (!Core.getInstance().isGamePaused()) {
            if (makeCollisions) {
                setImage(ship_hit);
                health -= 5;
                ArrayList<ISpriteColl> lista = getCollisionList();
                for (ISpriteColl spr : lista) {
                    if (spr instanceof Player.playerLaser) {
                        health -= 5;
                    } else if (spr instanceof Player.playerRocket) {
                        health -= 20;
                    } else if (spr instanceof Player.Shield) {
                        health += 5;     //praticamente ignora i danni da scudo k sconfiggono il boss all'istante (o quasi)
                    }
                }
            }
        }
    }
}


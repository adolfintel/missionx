/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.logic;

import java.awt.MouseInfo;
import mygame.sounds.SoundBox;
import mygame.*;
import mygame.logic.bonus.LaserPowerUp;
import mygame.logic.bonus.RocketsPowerUp;
import mygame.logic.bonus.SuperFirePowerUp;
import mygame.logic.bonus.ShieldPowerUp;
import mygame.logic.bonus.SpeedPowerUp;
import mygame.logic.bonus.Points;
import mygame.logic.bonus.OneUP;
import mygame.logic.enemy.LaserBarrier;
import mygame.logic.shots.EnemyPlasmaBall;
import mygame.logic.shots.EnemyGreenLaser;
import mygame.logic.enemy.Enemy3;
import mygame.logic.enemy.Enemy2;
import mygame.logic.enemy.Enemy5;
import mygame.logic.enemy.Enemy1;
import mygame.art.Emitter;
import mygame.logic.enemy.Boss1;
import java.util.ArrayList;
import mygame.art.ExplosionEffect;
import mygame.logic.bonus.EnergyPowerUp;
import mygame.logic.enemy.Asteroid;
import mygame.logic.enemy.Boss2;
import mygame.logic.enemy.Macron;
import mygame.logic.shots.EnemyBlueLaser;
import org.easyway.collisions.GroupCollision;
import org.easyway.collisions.ManagedGroupCollision;
import org.easyway.collisions.methods.HardWarePixelMethod;
import org.easyway.collisions.quad.QuadTree;
import org.easyway.input.Keyboard;
import static org.easyway.input.Keyboard.*;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.interfaces.sprites.ISpriteColl;
import org.easyway.objects.animo.Animo;
import org.easyway.objects.sprites2D.SpriteColl;
import org.easyway.objects.texture.Texture;
import org.easyway.system.Core;
import org.easyway.utils.Timer.SyncTimer;
import org.lwjgl.opengl.Display;

/**
 *
 * @author Do$$e
 */
public class Player extends SpriteColl implements ILoopable {

    float scale = 0.40f, realXSize, realYSize, lastDX = 0, lastDY = 0;
    Emitter engine1, engine2, engine3, explosion, shockWave;
    Texture bloom, ship, ship_hit;
    int cycle = 0, shotsPerSecond = 7, health = 99, lives = 3, points = 0, rocketCount = 20;
    boolean canShot = true, superFire = false, explode = false, toDestroy = false, lockControls = false, justCreated = true, takeDamage = true, lasers = false, canUseRockets = true, oneUpEnabled = true;
    Shield s;
    protected static Player thisInstance;

    public int getHealth() {
        return health;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public int getRocketCount() {
        return rocketCount;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Player(float x, float y, int lives) {
        super(x, y, null, 3);
        this.lives = lives;
        //setCollisionMethod(CircleCollisionMethod.getDefaultInstance());
        if(CommonVar.swCollisions) setCollisionMethod(org.easyway.collisions.methods.SoftwareCollisionMethod.getDefaultInstance()); else setCollisionMethod(HardWarePixelMethod.getDefaultInstance());
        thisInstance = this;
    }

    private void readObject(java.io.ObjectInputStream in)
            throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        thisInstance = this;
    }

    public static Player getDefaultInstance() {
        if (thisInstance == null) {
            thisInstance = new Player();
        }
        return thisInstance;
    }

    public Player() {
        this(0, 200, 3);
    }

    public void init() {
        // <editor-fold defaultstate="collapsed" desc="init-body">
        this.setSmoothImage(CommonVar.smoothing);
        ship = Texture.getTexture("images/ship/ship.png");
        ship_hit = Texture.getTexture("images/ship/ship_hit.png");
        setImage(ship);
        if (justCreated) {
            realXSize = getWidth();
            realYSize = getHeight();
            engine1 = new Emitter();
            engine2 = new Emitter();
            engine3 = new Emitter();
            if (QuadTree.isUsingQuadTree()) {
                ManagedGroupCollision.get("ships").addToSource(this);
            } else {
                GroupCollision.getGroup("ships").addToSource(this);
            }

            if (QuadTree.isUsingQuadTree()) {
                ManagedGroupCollision.get("enemyShots").addToDestination(this);
            } else {
                GroupCollision.getGroup("enemyShots").addToDestination(this);
            }

            if (QuadTree.isUsingQuadTree()) {
                ManagedGroupCollision.get("powerUp").addToSource(this);
            } else {
                GroupCollision.getGroup("powerUp").addToSource(this);
            }

            justCreated = false;
        }
        setWidth((int) (realXSize * scale));
        setHeight((int) (realYSize * scale));
        bloom = new Texture("images/bloom2.png");
        engine1.setX(getX());
        engine1.setY(getY());
        engine1.setxOffsetMax(67);
        engine1.setxOffsetMin(60);
        engine1.setyOffsetMax(170);
        engine1.setyOffsetMin(170);
        engine1.setParticle(bloom);
        engine1.setRMax(1f);
        engine1.setGMax(0.9f);
        engine1.setBMax(0.4f);
        engine1.setRMin(1f);
        engine1.setGMin(0.8f);
        engine1.setBMin(0.3f);
        engine1.setLifeMax(300);
        engine1.setLifeMin(180);
        engine1.setFadeIn(70);
        engine1.setFadeOut(110);
        engine1.setySpeedMin(-0.1f);
        engine1.setySpeedMax(0.1f);
        engine1.setxSpeedMin(-30f);
        engine1.setxSpeedMax(-28f);
        engine1.setParticlesPerSecond(100);
        engine1.setScale(scale);
        engine1.setParticlesLayer(5);
        engine1.setDrawScale(scale * 1.15f);
        engine1.setRotRateMax(0.002f);
        engine1.setRotRateMin(-0.002f);
        engine1.setRotRateIncMax(0.002f);
        engine1.setRotRateIncMin(-0.002f);

        engine2.setX(getX());
        engine2.setY(getY());
        engine2.setxOffsetMax(70);
        engine2.setxOffsetMin(62);
        engine2.setyOffsetMax(224);
        engine2.setyOffsetMin(224);
        engine2.setParticle(bloom);
        engine2.setRMax(1f);
        engine2.setGMax(0.9f);
        engine2.setBMax(0.4f);
        engine2.setRMin(1f);
        engine2.setGMin(0.8f);
        engine2.setBMin(0.3f);
        engine2.setLifeMax(300);
        engine2.setLifeMin(180);
        engine2.setFadeIn(70);
        engine2.setFadeOut(110);
        engine2.setySpeedMin(-0.1f);
        engine2.setySpeedMax(0.1f);
        engine2.setxSpeedMin(-30f);
        engine2.setxSpeedMax(-28f);
        engine2.setParticlesPerSecond(100);
        engine2.setScale(scale);
        engine2.setParticlesLayer(5);
        engine2.setDrawScale(scale * 1.15f);
        engine2.setRotRateMax(0.002f);
        engine2.setRotRateMin(-0.002f);
        engine2.setRotRateIncMax(0.002f);
        engine2.setRotRateIncMin(-0.002f);

        engine3.setX(getX());
        engine3.setY(getY());
        engine3.setxOffsetMax(17);
        engine3.setxOffsetMin(7);
        engine3.setyOffsetMax(180);
        engine3.setyOffsetMin(180);
        engine3.setParticle(bloom);
        engine3.setRMax(0.15f);
        engine3.setGMax(0.8f);
        engine3.setBMax(0.8f);
        engine3.setRMin(0.12f);
        engine3.setGMin(0.7f);
        engine3.setBMin(0.7f);
        engine3.setLifeMax(500);
        engine3.setLifeMin(300);
        engine3.setFadeIn(200);
        engine3.setFadeOut(100);
        engine3.setySpeedMin(-0.1f);
        engine3.setySpeedMax(0.1f);
        engine3.setxSpeedMin(-30f);
        engine3.setxSpeedMax(-28f);
        engine3.setParticlesPerSecond(30);
        engine3.setScale(scale);
        engine3.setParticlesLayer(5);
        engine3.setDrawScale(scale * 2f);
        engine3.setxSizeIncMax(8);
        engine3.setySizeIncMax(8);
        engine3.setxSizeIncMin(7);
        engine3.setySizeIncMin(7);
        engine3.setInitialRotationMin(-1);
        engine3.setInitialRotationMax(1);
        engine3.setRotRateMax(0.002f);
        engine3.setRotRateMin(-0.002f);
        engine3.setRotRateIncMax(0.002f);
        engine3.setRotRateIncMin(-0.002f);
        engine3.setOpacityMin(0.5f);
        engine3.setOpacityMin(1);
        //</editor-fold>
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public boolean hasSF() {
        return superFire;
    }

    public void setSF(boolean newVal) {
        superFire = newVal;
    }

    public boolean hasLasers() {
        return lasers;
    }

    public void setLasers(boolean newVal) {
        lasers = newVal;
    }

    private class ShotChronometer extends SyncTimer {

        public ShotChronometer() {
            super(1000 / shotsPerSecond);
        }

        @Override
        public void onTick() {
            canShot = true;
            stop();
        }
    }

    private class RocketsChronometer extends SyncTimer {

        public RocketsChronometer() {
            super(120);
        }

        @Override
        public void onTick() {
            canUseRockets = true;
            stop();
        }
    }

    private class ExplosionChronometer extends SyncTimer {

        public ExplosionChronometer() {
            super(100);
        }

        @Override
        public void onTick() {
            toDestroy = true;
            stop();
        }
    }

    private class oneUpRepeatChronometer extends SyncTimer {

        public oneUpRepeatChronometer() {
            super(100);
        }

        @Override
        public void onTick() {
            oneUpEnabled = true;
            stop();
        }
    }

    @Override
    public void loop() {
        if (!Core.getInstance().isGamePaused()) {
            if (points > 999999) {
                points = 999999;
            }
            CommonVar.score = points;
            if (CommonVar.difficulty == CommonVar.DIFF_EASY) {
                if (points > CommonVar.hiScore1) {
                    CommonVar.hiScore1 = points;
                }
            }
            if (CommonVar.difficulty == CommonVar.DIFF_NORMAL) {
                if (points > CommonVar.hiScore2) {
                    CommonVar.hiScore2 = points;
                }
            }
            if (CommonVar.difficulty == CommonVar.DIFF_HARD) {
                if (points > CommonVar.hiScore3) {
                    CommonVar.hiScore3 = points;
                }
            }
            if (CommonVar.destroyAll == true) {
                destroy();
            }
            if (cycle == 0) {
                init();
            }
            if (lives > 99) {
                lives = 99;
            }
            if (rocketCount > 999) {
                rocketCount = 999;
            }
            float speedMul = Core.getInstance().getSpeedMultiplier();
            setImage(ship);
            //VEDIAMO UN PO DOVE CI TROVIAMO NEL MONDO...
            final CamController cctrl = CamController.getDefaultInstance();
            float centerX = cctrl.getX();
            if (CamController.isMoving()) {
                move(cctrl.getSpeedX(), 0);
            }
            /*if(disableCollisions){
            GroupCollision.getGroup("ships").source.remove(this);
            GroupCollision.getGroup("enemyShots").destination.remove(this);
            GroupCollision.getGroup("powerUp").source.remove(this);
            new CollisionChronometer().start();
            disableCollisions=false;
            setRGBA(1,1,1,0.5f);
            }
            if(enableCollisions){
            GroupCollision.getGroup("ships").source.add(this);
            GroupCollision.getGroup("enemyShots").destination.add(this);
            GroupCollision.getGroup("powerUp").source.add(this);
            enableCollisions=false;
            setRGBA(1,1,1,1);
            }*/
            cycle++;
            if (health < 0) {
                explode = true;
                health = 999;
            }
            //controlli
            /*if (Keyboard.isKeyDown(KEY_DOWN) && !lockControls) {
            lastDY += 5 * speedMul;
            }
            if (Keyboard.isKeyDown(KEY_UP) && !lockControls) {
            lastDY += -5 * speedMul;
            }
            if (Keyboard.isKeyDown(KEY_RIGHT) && !lockControls) {
            lastDX += 5 * speedMul;
            }
            if (Keyboard.isKeyDown(KEY_LEFT) && !lockControls) {
            lastDX += -5 * speedMul;
            }*/
            if (CommonVar.useGamePad) {
                lastDX=Core.lerp(lastDX, speedMul*CommonVar.mouseSpeed * GamePad.getXAxis()*16, 0.8f * 1 / speedMul);
                lastDY=Core.lerp(lastDY, speedMul*CommonVar.mouseSpeed * GamePad.getYAxis()*16, 0.7f * 1 / speedMul);
            } else {
                lastDX = Core.lerp(lastDX, CommonVar.mouseSpeed * (org.easyway.input.Mouse.getX() - 512), 0.8f * 1 / speedMul);
                lastDY = Core.lerp(lastDY, CommonVar.mouseSpeed * (org.easyway.input.Mouse.getY() - 384), 0.7f * 1 / speedMul);
            }
            /*if(lastDX>12) lastDX=12;
            if(lastDX<-12) lastDX=-12;
            if(lastDY>12) lastDY=12;
            if(lastDY<-12) lastDY=-12;*/
            move(lastDX, lastDY);
            org.easyway.input.Mouse.setXY(CommonVar.xRes/2,CommonVar.yRes/2);
            if(getX()<centerX-225*1/scale) setX(centerX-225*1/scale);
            //if (getX()+getWidth() > centerX + 50) setX(centerX + 50);
            if(getX()>centerX+getWidth()+250*scale) setX(centerX+getWidth()+250*scale);
            if (getY() > 1950*scale - getHeight()) setY(1950*scale - getHeight());
            if (getY() < -62*scale) setY(-62*scale);
            if (getY() != 1950*scale - getHeight() && getY() != -62*scale) {
                engine1.setY(getY() + lastDY * 1.5f);
                engine2.setY(getY() + lastDY * 1.5f);
                engine3.setY(getY() + lastDY * 1.5f);
            } else {
                engine1.setY(getY());
                engine2.setY(getY());
                engine3.setY(getY());
            }
            if (getX() != centerX -225*1/scale && getX() != centerX+getWidth()+250*scale) {
                engine1.setX(getX() + lastDX * 1.5f);
                engine2.setX(getX() + lastDX * 1.5f);
                engine3.setX(getX() + lastDX * 1.5f);
            } else {
                engine1.setX(getX());
                engine2.setX(getX());
                engine3.setX(getX());
            }
            if ((/*Keyboard.isKeyDown(KEY_SPACE) || */(!CommonVar.useGamePad&&org.easyway.input.Mouse.isLeftDown())||(CommonVar.useGamePad&&GamePad.isButtonPressed(CommonVar.GPFire))) && canShot && !lockControls) {
                if (lasers) {
                    SoundBox.play(3);
                    new playerLaser(getX() + (492 * scale) + 1, getY() + (310 * scale), scale, true, 40, 0, 0, 2);
                    new playerLaser(getX() + (492 * scale), getY() + (115 * scale), scale, true, 40, 0, 0, 2);
                    if (superFire) {
                        new playerLaser(getX() + (252 * scale), getY() + (-8 * scale), scale, true, 30, -20, -40, 2);
                        new playerLaser(getX() + (272 * scale), getY() + (355 * scale), scale, true, 30, 20, 35, 2);
                    }
                } else {
                    SoundBox.play(1);
                    new playerShot(getX() + (492 * scale) + 1, getY() + (310 * scale), scale, true, 40, 0, 0, 2);
                    new playerShot(getX() + (492 * scale), getY() + (115 * scale), scale, true, 40, 0, 0, 2);
                    if (superFire) {
                        new playerShot(getX() + (247 * scale), getY() + (2 * scale), scale, true, 30, -20, -40, 2);
                        new playerShot(getX() + (253 * scale), getY() + (335 * scale), scale, true, 30, 20, 35, 2);
                    }
                }
                canShot = false;
                new ShotChronometer().start();
            }
            if ((/*Keyboard.isKeyDown(KEY_M)||*/(!CommonVar.useGamePad&&org.easyway.input.Mouse.isRightDown())||(CommonVar.useGamePad&&GamePad.isButtonPressed(CommonVar.GPRockets))) && canUseRockets && !lockControls && rocketCount > 0) {
                new playerRocket(getX() + (302 * scale), getY() + (150 * scale), scale * 1.8f, true, 20, (float) Math.random() * 3 - 1.5f, 2);
                new playerRocket(getX() + (307 * scale), getY() + (236 * scale), scale * 1.8f, true, 20, (float) Math.random() * 3 - 1.5f, 2);
                SoundBox.play(12);
                rocketCount -= 2;
                canUseRockets = false;
                new RocketsChronometer().start();
            }
            //cheats
            if (CommonVar.useCheats) {
                if (Keyboard.isKeyPressed(KEY_F1)) {
                    superFire = !superFire;
                }
                if (Keyboard.isKeyPressed(KEY_F2)) {
                    lives++;
                }
                if (Keyboard.isKeyPressed(KEY_F3)) {
                    lives--;
                }
                if (Keyboard.isKeyPressed(KEY_F4)) {
                    lasers = !lasers;
                }
                if (Keyboard.isKeyPressed(KEY_F5)) {
                    rocketCount += 50;
                }
                if (Keyboard.isKeyPressed(KEY_F6)) {
                    health += 30;
                }
            }
            //fine cheats
            if (explode) {
                // <editor-fold defaultstate="collapsed" desc="Effetto di esplosione!">
                CamController.shake(1000, 5);

                explode = false;
                lockControls = true;
                new ExplosionEffect(getX()+getWidth()*0.75f,getY()+getHeight()/2, 12,0.7f);
//                explosion = new Emitter(getX(), getY(), 300);
                new ExplosionChronometer().start();
//                explosion.setUseanimazione(true);
//                explosion.setAnimazione(exp);
//                explosion.setParticlesLayer(5);
//                explosion.setParticlesPerSecond(200);
//                explosion.setX(getX());
//                explosion.setY(getY());
//                explosion.setScale(scale);
//                explosion.setDrawScale(scale * 2);
//                explosion.setxOffsetMax(900);
//                explosion.setxOffsetMin(400);
//                explosion.setyOffsetMax(450);
//                explosion.setyOffsetMin(20);
//                explosion.setxSpeedMin(-5);
//                explosion.setxSpeedMax(2);
//                explosion.setySpeedMin(-5);
//                explosion.setySpeedMax(2);
//                explosion.setRMin(1);
//                explosion.setRMax(1);
//                explosion.setGMin(1);
//                explosion.setGMax(1);
//                explosion.setBMin(1);
//                explosion.setBMax(1);
//                explosion.setLifeMin(700);
//                explosion.setLifeMax(800);
//                explosion.setFadeIn(100);
//                explosion.setFadeOut(200);
//                explosion.setxSizeIncMin(12);
//                explosion.setxSizeIncMax(15);
//                explosion.setySizeIncMin(12);
//                explosion.setySizeIncMax(15);
//                explosion.setInitialRotationMin(0);
//                explosion.setInitialRotationMax(0);
//                explosion.setRotRateMin(-0.002f);
//                explosion.setRotRateMax(0.002f);
//
//
//                shockWave = new Emitter(getX(), getY(), 100);
//                shockWave.setParticle(Texture.getTexture("images/shockWave.png"));
//                shockWave.setxOffsetMax(128);
//                shockWave.setxOffsetMin(0);
//                shockWave.setyOffsetMax(128);
//                shockWave.setyOffsetMin(0);
//                shockWave.setDrawScale(0.01f);
//                shockWave.setxSizeIncMax(10);
//                shockWave.setxSizeIncMin(10);
//                shockWave.setySizeIncMax(10);
//                shockWave.setySizeIncMin(10);
//                shockWave.setLifeMin(500);
//                shockWave.setLifeMax(1500);
//                shockWave.setParticlesLayer(3);
//                shockWave.setParticlesPerSecond(50);
//                shockWave.setxSpeedMax(0);
//                shockWave.setxSpeedMin(0);
//                shockWave.setySpeedMax(0);
//                shockWave.setySpeedMin(0);
//                shockWave.setRMax(1);
//                shockWave.setRMin(1);
//                shockWave.setGMax(1);
//                shockWave.setGMin(1);
//                shockWave.setBMax(1);
//                shockWave.setBMin(1);
//                shockWave.setInitialRotationMin(0);
//                shockWave.setInitialRotationMax(0);
//                shockWave.setRotRateMin(0);
//                shockWave.setRotRateMax(0);
                //</editor-fold>
            }
            if (toDestroy) {
                try {
                    explosion.destroy();
                } catch (Exception e) {
                }       //Who the fuck cares about emitter errors???
                if (takeDamage) {
                    lives--;
                }
                setX(-1);
                setY(200);
                superFire = false;
                shotsPerSecond = 7;
                health = 99;
                cycle = 0;
                lockControls = false;
                toDestroy = false;
                if (s != null) {
                    s.destroy();
                }
                s = new Shield(scale, 0.01f);
                lasers = false;
                rocketCount = 20;
                SoundBox.play(10);
                SoundBox.play(11);
                if (lives < 0) {
                    destroy();
                }
            }
        }
    }

    public Texture getBloom() {
        return bloom;
    }

    public void setBloom(Texture bloom) {
        this.bloom = bloom;
    }

    public int getShotsPerSecond() {
        return shotsPerSecond;
    }

    public void setShotsPerSecond(int shotsPerSecond) {
        this.shotsPerSecond = shotsPerSecond;
    }
//--------------PLAYERSHOT

    public class playerShot extends SpriteColl implements ILoopable {

        float speedX, speedY;

        public playerShot(float x, float y, float scale, boolean isSource, float speedX, float speedY, int rotation, int layer) {
            super(x, y, Texture.getTexture("images/shot_" + rotation + ".png"), layer);
            this.setSmoothImage(CommonVar.smoothing);
            if(CommonVar.swCollisions) setCollisionMethod(org.easyway.collisions.methods.SoftwareCollisionMethod.getDefaultInstance()); else setCollisionMethod(HardWarePixelMethod.getDefaultInstance());
            this.speedX = speedX * scale * 1.25f;
            this.speedY = speedY * scale * 1.25f;
            setSize(getWidth() * scale * 1.25f, getHeight() * scale * 1.25f);
            if (isSource) {
                if (QuadTree.isUsingQuadTree()) {
                    ManagedGroupCollision.get("playerShots").addToSource(this);
                } else {
                    GroupCollision.getGroup("playerShots").addToSource(this);
                }
            } else {
                if (QuadTree.isUsingQuadTree()) {
                    ManagedGroupCollision.get("playerShots").addToDestination(this);
                } else {
                    GroupCollision.getGroup("playerShots").addToDestination(this);
                }
            }

        }

        public playerShot() {
        }

        @Override
        public void loop() {
            if (!Core.getInstance().isGamePaused()) {
                if (CommonVar.destroyAll == true) {
                    kill();
                    return;
                }
                float speedMul = Core.getInstance().getSpeedMultiplier();
                if (getXOnScreen() >= 1250 || getXOnScreen()+getWidth() <=-20 ) {
                    destroy();
                }
                move(speedX * speedMul, speedY * speedMul);
            }
        }

        @Override
        public void onDestroy() {
            Emitter explosion = new Emitter(getX() + 10, getY() - 10, 30);
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
                ArrayList<ISpriteColl> l=getCollisionList();
                for(ISpriteColl spr:l) if(!(spr instanceof Macron)) destroy(); else onDestroy();
            }
        }
    }
//-------------FINE PLAYERSHOT
//--------------PLAYERLASER

    public class playerLaser extends SpriteColl implements ILoopable {

        float speedX, speedY;

        public playerLaser(float x, float y, float scale, boolean isSource, float speedX, float speedY, int rotation, int layer) {
            super(x, y, Texture.getTexture("images/laser_" + rotation + ".png"), layer);
            this.setSmoothImage(CommonVar.smoothing);
            if(CommonVar.swCollisions) setCollisionMethod(org.easyway.collisions.methods.SoftwareCollisionMethod.getDefaultInstance()); else setCollisionMethod(HardWarePixelMethod.getDefaultInstance());
            this.speedX = speedX * scale * 1.25f;
            this.speedY = speedY * scale * 1.25f;
            setSize(getWidth() * scale * 1.25f, getHeight() * scale * 1.25f);

            if (isSource) {
                if (QuadTree.isUsingQuadTree()) {
                    ManagedGroupCollision.get("playerShots").addToSource(this);
                } else {
                    GroupCollision.getGroup("playerShots").addToSource(this);
                }
            } else {
                if (QuadTree.isUsingQuadTree()) {
                    ManagedGroupCollision.get("playerShots").addToDestination(this);
                } else {
                    GroupCollision.getGroup("playerShots").addToDestination(this);
                }
            }
        }

        public playerLaser() {
        }

        @Override
        public void loop() {
            if (!Core.getInstance().isGamePaused()) {
                if (CommonVar.destroyAll == true) {
                    GroupCollision.getGroup("playerShots").removeFromSource(this);
                    kill();
                    return;
                }
                if (cycle == 0) {
                }
                float speedMul = Core.getInstance().getSpeedMultiplier();
                if (getXOnScreen() >= 1250 || getXOnScreen()+getWidth() <=-20 ) {
                    destroy();
                }
                move(speedX * speedMul, speedY * speedMul);

                cycle++;
            }
        }

        @Override
        public void onDestroy() {
            Emitter explosion = new Emitter(getX() + 10, getY() - 10, 40);
            explosion.setParticlesPerSecond(300);
            explosion.setLifeMax(350);
            explosion.setLifeMin(300);
            explosion.setParticle(Texture.getTexture("images/bloom2.png"));
            explosion.setFadeOut(100);
            explosion.setRMax(1f);
            explosion.setRMin(1f);
            explosion.setGMax(0.2f);
            explosion.setGMin(0.1f);
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
            ArrayList<ISpriteColl> l=getCollisionList();
                for(ISpriteColl spr:l) if(!(spr instanceof Macron)){GroupCollision.getGroup("playerShots").removeFromSource(this); destroy();} else onDestroy();
        }
    }
//-------------FINE PLAYERLASER
//--------------PLAYERROCKET

    public class playerRocket extends SpriteColl implements ILoopable {

        float speedX, speedY;

        public playerRocket(float x, float y, float scale, boolean isSource, float speedX, float speedY, int layer) {
            super(x, y, Texture.getTexture("images/rocket.png"), layer);
            this.setSmoothImage(CommonVar.smoothing);
            if(CommonVar.swCollisions) setCollisionMethod(org.easyway.collisions.methods.SoftwareCollisionMethod.getDefaultInstance()); else setCollisionMethod(HardWarePixelMethod.getDefaultInstance());
            this.speedX = speedX * scale * 1.25f;
            this.speedY = speedY * scale * 1.25f;
            setSize(getWidth() * scale * 1.25f, getHeight() * scale * 1.25f);
            if (isSource) {
                if (QuadTree.isUsingQuadTree()) {
                    ManagedGroupCollision.get("playerShots").addToSource(this);
                } else {
                    GroupCollision.getGroup("playerShots").addToSource(this);
                }
            } else {
                if (QuadTree.isUsingQuadTree()) {
                    ManagedGroupCollision.get("playerShots").addToDestination(this);
                } else {
                    GroupCollision.getGroup("playerShots").addToDestination(this);
                }
            }

        }

        public playerRocket() {
        }

        @Override
        public void loop() {
            if (!Core.getInstance().isGamePaused()) {
                if (CommonVar.destroyAll == true) {
                    GroupCollision.getGroup("playerShots").removeFromSource(this);
                    kill();
                    return;
                }
                float speedMul = Core.getInstance().getSpeedMultiplier();
                if (getXOnScreen() >= 1250 || getXOnScreen()+getWidth() <=-20 ) {
                    destroy();
                }
                move(speedX * speedMul, speedY * speedMul);
                //new Particle(null, miniExp, true, shot.getX() + (48 * scale), shot.getY() - 8, 0, -5, 240, 1, 1, 60, 100, scale * 0.3125f, 1, 1, 1, 1, 2, (float) Math.random(), 0, 0, 0, 0, scale * 0.05f);
                cycle++;
            }
        }

        @Override
        public void onDestroy() {
            GroupCollision.getGroup("playerShots").removeFromSource(this);
            Emitter explosion = new Emitter(getX() + 10, getY() - 10, 250);
            explosion.setParticlesPerSecond(120);
            explosion.setLifeMax(500);
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
                SoundBox.play(10);
                destroy();
            }
        }
    }
//-------------FINE PLAYERROCKET
//-------------SHIELD

    public class Shield extends SpriteColl implements ILoopable {

        float alpha = 1, scale = 1, alphaDecrement = 0.01f;

        public Shield(float scale, float alphaDecrement) {
            super(0, 768, Texture.getTexture("images/ship/shield.png"));
            this.setSmoothImage(CommonVar.smoothing);
            if(CommonVar.swCollisions) setCollisionMethod(org.easyway.collisions.methods.SoftwareCollisionMethod.getDefaultInstance()); else setCollisionMethod(HardWarePixelMethod.getDefaultInstance());
            if (QuadTree.isUsingQuadTree()) {
                ManagedGroupCollision.get("ships").addToSource(this);
            } else {
                GroupCollision.getGroup("ships").addToSource(this);
            }
            if (QuadTree.isUsingQuadTree()) {
                ManagedGroupCollision.get("enemyShots").addToDestination(this);
            } else {
                GroupCollision.getGroup("enemyShots").addToDestination(this);
            }
            setWidth((int) (getWidth() * scale));
            setHeight((int) (getHeight() * scale));
            this.scale = scale;
            setIdLayer(6);
            this.alphaDecrement = alphaDecrement;
        }

        @Override
        public void loop() {
            if (!Core.getInstance().isGamePaused()) {
                if (CommonVar.destroyAll == true) {
                    kill();
                }
                final Player p = Player.getDefaultInstance();
                setX(p.getX()-300*scale);
                setY(p.getY());
                setRGBA(1, 1, 1, alpha);
                float speedMul = Core.getInstance().getSpeedMultiplier();
                if (alpha <= 0) {
                    takeDamage = true;
                    kill();
                } else {
                    takeDamage = false;
                }
                alpha -= alphaDecrement * speedMul;
            }
        }
    }
//------------FINE SHIELD

    @Override
    public void onDestroy() {
        if (!Core.getInstance().isGamePaused()) {
            if (engine1 != null) {
                engine1.destroy();
            }
            if (engine2 != null) {
                engine2.destroy();
            }
            if (engine3 != null) {
                engine3.destroy();
            }
        }
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setRocketCount(int rocketCount) {
        this.rocketCount = rocketCount;
    }

    @Override
    public void onCollision() {
        if (!Core.getInstance().isGamePaused()) {
            int initHealth = health;
            health -= 5;
            setImage(ship_hit);
            ArrayList<ISpriteColl> lista = getCollisionList();
            for (ISpriteColl spr : lista) {

                if (spr instanceof LaserPowerUp) {
                    setImage(ship);
                    lasers = true;
                    health = initHealth;
                    points += 100;
                } else if (spr instanceof Enemy1) {
                    health -= 5;
                } else if (spr instanceof Enemy2) {
                    health -= 5;
                } else if (spr instanceof Points) {
                    health = initHealth;
                    setImage(ship);
                    points += 50;
                } else if (spr instanceof OneUP) {
                    setImage(ship);
                    health = initHealth;
                    if (oneUpEnabled) {
                        lives++;
                        oneUpEnabled = false;
                        new oneUpRepeatChronometer().start();
                    }
                    points += 100;
                } else if (spr instanceof SuperFirePowerUp) {
                    setImage(ship);
                    health = initHealth;
                    superFire = true;
                    points += 100;
                } else if (spr instanceof ShieldPowerUp) {
                    setImage(ship);
                    health = initHealth;
                    if (s != null) {
                        s.destroy();
                    }
                    s = new Shield(scale, 0.001f);
                    points += 100;
                } else if (spr instanceof SpeedPowerUp) {
                    setImage(ship);
                    health = initHealth;
                    if(shotsPerSecond<12) shotsPerSecond++;
                    points += 100;
                } else if (spr instanceof RocketsPowerUp) {
                    setImage(ship);
                    health = initHealth;
                    points += 100;
                    rocketCount += 50;
                } else if (spr instanceof EnergyPowerUp) {
                    setImage(ship);
                    health += 30;
                    initHealth += 30;
                    points += 100;
                } else if (spr instanceof Enemy3) {
                    health -= 5;
                    //if(health<0){ makeCollisions=false;}
                } else if (spr instanceof Boss1) {
                    health -= 30;
                    //if(health<0){ makeCollisions=false;}
                } else if (spr instanceof Boss2) {
                    health -= 30;
                    //if(health<0){ makeCollisions=false;}
                } else if (spr instanceof EnemyGreenLaser) {
                    health -= 2.5f;
                    //if(health<0){ makeCollisions=false;}
                } else if (spr instanceof EnemyBlueLaser) {
                    health -= 5f;
                    //if(health<0){ makeCollisions=false;}
                } else if (spr instanceof Enemy5) {
                    health = initHealth - 3;
                    //if(health<0){ makeCollisions=false;}
                } else if (spr instanceof EnemyPlasmaBall) {
                    health = initHealth - 2;
                    //if(health<0){ makeCollisions=false;}
                } else if (spr instanceof LaserBarrier) {
                    health = initHealth - 2;
                    //if(health<0){ makeCollisions=false;}
                } else if (spr instanceof Asteroid) {
                    health = initHealth - 8;
                }
                if (!takeDamage) {
                    setImage(ship);
                    health = initHealth;
                }
            }
        }
    }
}


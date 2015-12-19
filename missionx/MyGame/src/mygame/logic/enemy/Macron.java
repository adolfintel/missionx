/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.logic.enemy;

import java.util.ArrayList;
import mygame.CommonVar;
import mygame.art.LifeBar;
import mygame.logic.CamController;
import mygame.logic.Player;
import mygame.logic.bonus.Destroyer;
import org.easyway.collisions.GroupCollision;
import org.easyway.collisions.methods.HardWarePixelMethod;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.interfaces.sprites.ISpriteColl;
import org.easyway.objects.sprites2D.Mask;
import org.easyway.objects.sprites2D.SpriteColl;
import org.easyway.objects.texture.Texture;
import org.easyway.system.Core;
import org.easyway.utils.Timer.SyncTimer;

/**
 *
 * @author Do$$e
 */
public class Macron extends SpriteColl implements ILoopable {

    float health, initHealth;
    boolean inBattle = false, dead = false;
    Texture defImg, destroyed, mask, hitImg;
    private EnemyEmitter EE;
    private boolean genDestroyer;
    private LifeBar LB;

    public Macron(int x, int y, float health) {
        super(x, y);
        setIdLayer(2);
        setLayer(2);
        setXY(x,y);
        this.health = health*CommonVar.difficulty;
        initHealth = this.health;
        defImg = Texture.getTexture("images/macron/defImg.png");
        destroyed = Texture.getTexture("images/macron/destroyedImg.png");
        hitImg = Texture.getTexture("images/macron/hitImg.png");
        mask = Texture.getTexture("images/macron/defImg_collisionMask.png");
        setImage(defImg);
        setMask(new Mask(mask));
        setSmoothImage(CommonVar.smoothing);
        if (Core.getGLIntVersion() < 15) {
            setCollisionMethod(org.easyway.collisions.methods.RectangleCollisionMethod.getDefaultInstance());
        } else if (CommonVar.swCollisions) {
            setCollisionMethod(org.easyway.collisions.methods.SoftwareCollisionMethod.getDefaultInstance());
        } else {
            setCollisionMethod(HardWarePixelMethod.getDefaultInstance());
        }
    }

    private class ExplChrono extends SyncTimer {

        int i = 0, n = 5;

        public ExplChrono(int explCount) {
            super(300);
            n = explCount;
            setImage(hitImg);
            dead = true;
            LB.destroy();
        }

        @Override
        public void onTick() {
            if (i == n) {
                setImage(destroyed);
                setIdLayer(1);
                setLayer(1);
                genDestroyer = true;

                CamController.setMoving(true);
                stop();
            }
            genDestroyer = true;
            i++;
        }
    }

    private class EnemyEmitter extends SyncTimer {

        @Override
        public void onTick() {
            if (Core.getInstance().isGamePaused()) {
                return;
            }
            double r = Math.random();
            if (r < 0.25) {
                new Enemy1(getX() + 380, (float) (getY() + (getHeight() / 2) + Math.random() * 100 - 50), 20);
            } else if (r < 0.5) {
                new Enemy2(getX() + 380, (float) (getY() + (getHeight() / 2) + Math.random() * 100 - 50), 30);
            } else if (r < 0.75) {
                new Enemy3(getX() + 380, (float) (getY() + (getHeight() / 2) + Math.random() * 100 - 50), 25);
            } else {
                new Enemy4(getX() + 375, (float) (getY() + (getHeight() / 2) + Math.random() * 100 - 50), 20);
            }



        }

        public EnemyEmitter(int delay) {
            super(delay);
        }
    }

    @Override
    public void loop() {
        if (!Core.getInstance().isGamePaused() && getXOnScreen() <= 1050) {
            if (getXOnScreen() <= 640 && !inBattle) {
                if (CommonVar.difficulty <= CommonVar.DIFF_NORMAL) {
                    EE = new EnemyEmitter((int) (900 / CommonVar.difficulty));

                } else {
                    EE = new EnemyEmitter(650);

                }
                EE.start();
                CamController.setMoving(false);
                inBattle = true;
                LB=new LifeBar(getX()+getWidth()/2-80,getY()-16, (int) health);
                GroupCollision.getGroup("playerShots").addToDestination(this);
            }
            if (health <= 0) {
                health = 999999;
                EE.stop();
                GroupCollision.getGroup("playerShots").removeFromDestination(this);
                new ExplChrono(5).start();
            }
            if (CommonVar.destroyAll) {
                destroy();
            }
            if (genDestroyer) {
                genDestroyer = false;
                new Destroyer(getX() + getWidth() / 2, getY() + getHeight() / 2,true);
            }
            if(LB!=null) LB.setLife((int)health);
            if (!dead) {
                setImage(defImg);
            }
        }
    }

    @Override
    public void onCollision() {
        ArrayList<ISpriteColl> l=getCollisionList();
        for(ISpriteColl spr:l){
            if(spr instanceof Player.playerShot){setImage(hitImg); health -= 10 * Core.getInstance().getSpeedMultiplier();}
            if(spr instanceof Player.playerLaser){setImage(hitImg); health -= 20 * Core.getInstance().getSpeedMultiplier();}
            if(spr instanceof Destroyer){setImage(hitImg); health -= 30 * Core.getInstance().getSpeedMultiplier();}
        }

        
    }

    @Override
    public void onDestroy() {
        GroupCollision.getGroup("playerShots").removeFromDestination(this);
        
        if (EE != null) {
            EE.stop();
            EE = null;
        }
        if(CommonVar.destroyAll&&LB!=null) LB.destroy();
    }
}

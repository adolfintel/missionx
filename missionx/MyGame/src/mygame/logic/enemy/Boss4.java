/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.logic.enemy;

import java.util.ArrayList;
import mygame.CommonVar;
import mygame.art.LifeBar;
import mygame.art.Particle;
import mygame.logic.CamController;
import mygame.logic.Player;
import mygame.logic.bonus.Destroyer;
import org.easyway.collisions.GroupCollision;
import org.easyway.collisions.methods.HardWarePixelMethod;
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
public class Boss4 extends SpriteColl implements ILoopable {

    float health, initHealth,cycle=0;
    boolean inBattle = false, dead = false;
    Texture defImg, hitImg;
    private EnemyEmitter EE;
    private boolean genDestroyer;
    private LifeBar LB;

    public Boss4(int x, int y, float health) {
        super(x, y);
        setIdLayer(5);
        setLayer(5);
        setXY(x,y);
        this.health = health*CommonVar.difficulty;
        initHealth = this.health;
        defImg = Texture.getTexture("images/boss4/boss4.png");
        hitImg = Texture.getTexture("images/boss4/boss4_hit.png");
        setImage(defImg);
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
            setIdLayer(1);setLayer(1);
        }

        @Override
        public void onTick() {
            if (i == n) {
                setScaleX(0);
                setScaleY(0);
                CamController.setMoving(true);
                destroy();
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
                new Enemy1(getX() + 125, (float) (getY() + (getHeight() / 2) + Math.random() * 100 - 50), 20).setKamikaze(true);
            } else if (r < 0.5) {
                new Enemy2(getX() + 125, (float) (getY() + (getHeight() / 2) + Math.random() * 100 - 50), 30).setKamikaze(true);
            } else if (r < 0.75) {
                new Enemy3(getX() + 125, (float) (getY() + (getHeight() / 2) + Math.random() * 100 - 50), 25).setKamikaze(true);
            } else {
                new Enemy4(getX() + 125, (float) (getY() + (getHeight() / 2) + Math.random() * 100 - 50), 20).setKamikaze(true);
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
                new Particle(Texture.getTexture("images/fight_" + CommonVar.loc + ".png"), null, false, CamController.getDefaultInstance().getX(),384, 0, 0, 1500, 25, 25, 150, 850, 1, 1, 1, 1, 0.9f, 6, 0, 0, 0, 0, 0, 0.01f).setIdLayer(6);
                if (CommonVar.difficulty <= CommonVar.DIFF_NORMAL) {
                    EE = new EnemyEmitter((int) (1500 / CommonVar.difficulty));

                } else {
                    EE = new EnemyEmitter(750);

                }
                EE.start();
                CamController.setMoving(false);
                inBattle = true;
                LB=new LifeBar(getX()+getWidth()/2-80,getY()+16, (int) health);
                LB.setIdLayer(6);
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
                cycle+=Core.getInstance().getSpeedMultiplier();
                setY(-5+20*(float)Math.sin((double)cycle/60));
                setImage(defImg);
            }
        }
    }

    @Override
    public void onCollision() {
        ArrayList<ISpriteColl> l=getCollisionList();
        for(ISpriteColl spr:l){
            if(spr instanceof Player.playerShot){setImage(hitImg); health -= 10;}
            if(spr instanceof Player.playerLaser||spr instanceof Player.playerRocket){setImage(hitImg); health -= 20;}
            if(spr instanceof Destroyer){setImage(hitImg); health -= 30;}
        }


    }

    @Override
    public void onDestroy() {
        GroupCollision.getGroup("playerShots").removeFromDestination(this);
        GroupCollision.getGroup("ships").removeFromDestination(this);
        if (EE != null) {
            EE.stop();
            EE = null;
        }
        if(CommonVar.destroyAll&&LB!=null) LB.destroy();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.logic;

import mygame.*;
import mygame.art.Hud;
import org.easyway.input.Keyboard;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;

/**
 *
 * @author Do$$e
 */
public class WorldInfo extends Sprite implements ILoopable {

    private float levelSize = 2048, speedX;
    private CamController i;
    private Hud hud;
    private Player p;
    private boolean gameOverSent = false;
    private int l = 0;

    public WorldInfo(float x, float y) {
        super(x, y, Texture.getTexture("images/WorldInfo.png"));
        this.setSmoothImage(CommonVar.smoothing);
        i = new CamController();
        i.setMaxX(levelSize - 512);
        CamController.setMoving(true);
        i.setSpeedX(0);
        /*GroupCollision.createGroup("playerShots");
        GroupCollision.createGroup("enemyShots");
        GroupCollision.createGroup("ships");
        GroupCollision.createGroup("powerUp");*/
        hud = new Hud();
        //StaticRef.getCamera().centerOn(i,true);
        p = new Player();
    }

    public WorldInfo() {
        this(0, 768);
    }

    public int getScore() {
        return p.getPoints();
    }

    public void setScore(int newScore) {
        p.setPoints(newScore);
    }

    public int getLives() {
        return p.getLives();
    }

    public void setLives(int newLives) {
        p.setLives(newLives);
    }

    public int getHealth() {
        return p.getHealth();
    }

    public void setHealth(int newHealth) {
        p.setHealth(newHealth);
    }

    public int getShotsPerSecond() {
        return p.getShotsPerSecond();
    }

    public void setShotsPerSecond(int newSPS) {
        p.setShotsPerSecond(newSPS);
    }

    public int getRocketCount() {
        return p.getRocketCount();
    }

    public void setRocketCount(int newRC) {
        p.setRocketCount(newRC);
    }

    public boolean getSuperFire() {
        return p.hasSF();
    }

    public void setSuperFire(boolean SF) {
        p.setSF(SF);
    }

    public boolean getLasers() {
        return p.hasLasers();
    }

    public void setLasers(boolean las) {
        p.setLasers(las);
    }

    public float getLevelSize() {
        return levelSize;
    }

    public void setLevelSize(float levelSize) {
        this.levelSize = levelSize;
        if (levelSize < 512) {
            System.out.println("Warning: small levelSize is not recommended (use at least 512)");
        }
        i.setMaxX(levelSize - 512);
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        if (speedX < 0) {
            System.out.println("Warning: negative SpeedX is not allowed!");
            return;
        }
        /*if(speedX>1){
        System.out.println("Warning: SpeedX>1 is not allowed!");
        return;
        }*/
        this.speedX = speedX;
        i.setSpeedX(speedX);
    }

    @Override
    public void loop() {
        if (p.lives < 0) {
            if (!gameOverSent) {
                CommonVar.gameOver = true;
                gameOverSent = true;
            }
        }
        if (CommonVar.destroyAll == true) {
            endLevel();
        }
        if (DebugManager.isDebugging() && Keyboard.isKeyPressed(Keyboard.KEY_F8)) {
            i.setX(512 + CommonVar.getLevelX(l++));
        }
        if (i.getX() + 512 >= levelSize) {
            CommonVar.GOSprite.setImage(Texture.getTexture("images/menu/" + CommonVar.skin + "/" + CommonVar.loc + "/endGame.png"));
        }
        //System.out.println("rightCorner= "+i.getX()+512+" levelSize= "+levelSize);
    }

    public void endLevel() {
        hud.destroy();
        i.destroy();
        destroy();
    }
}

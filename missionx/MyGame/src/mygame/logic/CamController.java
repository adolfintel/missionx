/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.logic;

import mygame.*;
import org.easyway.input.Keyboard;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;
import org.easyway.utils.Timer.SyncTimer;

/**
 *
 * @author Do$$e
 */
public class CamController extends Sprite implements ILoopable {

    private float speedX = 1, maxX = 3584, initY = 384;
    private static boolean shaking = false, moving = true, useStaticSpeed = false;
    private static float maxShakeY = 5, staticSpeed = (float) 0;
    protected static CamController thisInstance;
    private final static long serialVersionUID = -6734851136205611598L;

    public CamController() {
        super(512, 384, Texture.getTexture("images/nullCursor.png"));
        setWidth(0); setHeight(0);
        this.setSmoothImage(CommonVar.smoothing);
        thisInstance = this;
        StaticRef.getCamera().centerOn(this);
    }

    public static CamController getDefaultInstance() {
        if (thisInstance == null) {
            System.out.println("Huge amount of bullshit detected: wrong use of getDefaultInstance in class CamController!");
            return new CamController();
        }
        return thisInstance;
    }

    private static class ShakeChronometer extends SyncTimer {

        public ShakeChronometer(int delay) {
            super(delay);
        }

        @Override
        public void onTick() {
            shaking = false;
            stop();
        }
    }

    public static void shake(int ms, float y) {
        shaking = true;
        maxShakeY = y;
        new ShakeChronometer(ms).start();
    }

    public float getMaxX() {
        return maxX;
    }

    public void setMaxX(float maxX) {
        this.maxX = maxX;
    }

    public float getSpeedX() {
        if (isMoving()) {
            return speedX;
        }
        return 0;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public static void setMoving(boolean moving) {
        CamController.moving = moving;
    }

    public static boolean isMoving() {
        return moving;
    }

    public static void setStaticSpeed(float speed, boolean enabled) {
        CamController.staticSpeed = speed;
        CamController.useStaticSpeed = enabled;
    }

    @Override
    public void loop() {
        if (!Core.getInstance().isGamePaused()) {
            float speedMul = Core.getInstance().getSpeedMultiplier();
            if (getX() >= maxX) {
                moving = false;
                CommonVar.gameOver = true;
            }
            /*if(useStaticSpeed)
            move(staticSpeed*speedMul,0);
            else*/
            if (moving) {
                move(speedX * speedMul, 0);
            }
            if (DebugManager.isDebugging()) {
                if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                    move(8, 0);
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                    move(-8, 0);
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                    move(0, -8);
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                    move(0, 8);
                }
                if (Keyboard.isKeyPressed(Keyboard.KEY_SPACE)) {
                    moving = !moving;
                }
            }
            if (shaking) {
                setY(initY + (float) Math.random() * maxShakeY - maxShakeY / 2);
                //move((float)Math.random()*maxShakeX-maxShakeX/2,0);
            } else {
                setY(initY);
            }
        }
    }
}

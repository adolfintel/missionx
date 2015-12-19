/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example200;

import examples.example200.shots.CanShotInterface;
import examples.example200.shots.DelayShoter;
import examples.example200.shots.SimpleShot;
import org.easyway.collisions.GroupCollision;
import org.easyway.input.Mouse;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.interfaces.sprites.ISpriteColl;
import org.easyway.objects.Camera;
import org.easyway.objects.texture.Texture;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;

/**
 *
 * @author Daniele Paggi
 */
public class Player extends HittableObject implements ILoopable, CanShotInterface {

    boolean canShot = true;
    float damagePower = 100;
    DelayShoter delay;

    public Player() {
        super(0, 0, Texture.getTexture("images/shipGame/player_gray.png"));

        /*if (GroupCollision.getGroup("Player-Enemy") == null) {
        GroupCollision.createGroup("Player-Enemy");
        }*/
        GroupCollision.getGroup("Player-Enemy").addToSource(this);

        // place it on middle y
        setY(StaticRef.getCamera().getHeight() / 2 - getHeight() / 2);
        // resize the object: it's a very big image!
        setSize(getWidth() * 0.2f, getHeight() * 0.2f);

        // the coordinate sprite are relative to game camera space: not to coordinate space world
        setFixedOnScreen(true);

        // set color
        setRGBA(0.7f, 0.7f, 1, 1);
        delay = new DelayShoter(this, 250);
        delay.start();

        // set the healt
        setMaxHealt(100);
        fillHealt();
    }
    // ------------------------------
    //  SET - GET
    // ------------------------------

    public float getDamagePower() {
        return damagePower;
    }

    public void setDamagePower(float damage) {
        this.damagePower = Math.max(damage, 1f);
    }

    // ------------------------------
    //  CORE METHODS
    // ------------------------------
    @Override
    public void loop() {
        super.loop();
        // moves the sprite with the mouse
        moveWithMouse();
        // check that the sprite should be inside the game camera
        boundInsideCamera();
        // shot test
        if (canShot() && shouldShot()) {
            shot();
        }
    }

    @Override
    public void onCollision() {
        for (ISpriteColl spr : getCollisionList()) {
            if (spr instanceof Hittable) {
                Hittable target = (Hittable) spr;
                target.hit(getDamagePower());
            }
        }
    }

    @Override
    public void destroy() {
        if (isDestroyed()) {
            return;
        }
        delay.stop();
        delay = null;
        super.destroy();
    }
    
    // ------------------------------
    //   ACTIONS
    // ------------------------------
    public void shot() {
        new SimpleShot(getWidth() + getX(), getHeight() / 2 + getY());
        setCanShot(false);
        delay.start();
    }

    public boolean canShot() {
        return canShot;
    }

    public void setCanShot(boolean value) {
        canShot = value;
    }

    private boolean shouldShot() {
        // shot only when left is pressed
        return Mouse.isLeftDown();
    }

    public void fillHealt() {
        setHealt(getMaxHealt());
    }

    // ------------------------------
    //  HELPER METHODS
    // ------------------------------
    private void moveWithMouse() {
        // with StaticRef.core.getWindth() we get the width of the window.
        // it's a bit different from StaticRef.getCamera().getWidth(): this will return the
        // width of the camera.
        // but in very much cases the width of game camera is the same of width of game window
        int centerXScreen = Core.getInstance().getWidth() / 2;
        // with StaticRef.core.getHeight() we get the width of the window
        int centerYScreen = Core.getInstance().getHeight() / 2;
        float deltaX = (float)Mouse.getX() - centerXScreen;
        float deltaY = (float)Mouse.getY() - centerYScreen;

        // replace the window cursor at the center of game window
        Mouse.setXY(centerXScreen, centerYScreen);

        // move the object of deltaX, deltaY
        move(deltaX, deltaY);
    }

    @Override
    protected void boundInsideCamera() {
        Camera camera = StaticRef.getCamera();
        if (getXOnScreen() <= 0) { // left check
            setX(camera.getX());
        }
        if (getYOnScreen() <= 0) { // top check
            setY(camera.getY());
        }
        // right check
        if (getXOnScreen() + getWidth() >= camera.getWidth()) {
            setX(camera.getX() + camera.getWidth() - getWidth());
        }
        // bottom check
        if (getYOnScreen() + getHeight() >= camera.getHeight()) {
            setY(camera.getY() + camera.getHeight() - getHeight());
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example21bis;

import org.easyway.input.Mouse;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.Camera;
import org.easyway.objects.sprites2D.SpriteColl;
import static org.easyway.objects.texture.Texture.getTexture;
import static org.easyway.input.Keyboard.*;

/**
 *
 * @author RemalKoil
 */
public class Player extends SpriteColl implements ILoopable {

    float speedx = 4f;
    float speedy = 4f;

    public Player(float x, float y) {
        super(x, y, getTexture("examples/example21/images/player.png"));
        move(-getWidth() / 2, -getHeight() / 2);
        setRGBA(1, 1, 0, 1);
        Camera.getCurrentCamera().centerOn(this);
    }

    @Override
    public void loop() {
        moveWASD();
        checkBounds();
        if (canShot() && shouldShot()) {
            shot();
        }
    }

    private void moveWASD() {
        float incx = 0f, incy = 0f;
        if (isKeyDown(KEY_D)) {
            incx += speedx;
        }
        if (isKeyDown(KEY_A)) {
            incx -= speedx;
        }
        if (isKeyDown(KEY_S)) {
            incy += speedy;
        }
        if (isKeyDown(KEY_W)) {
            incy -= speedy;
        }
        move(incx, incy);
    }

    private void checkBounds() {
        float newx = getX(), newy = getY();
        final Camera camera = Camera.getCurrentCamera();
        if (getX() < 0) {
            newx = 0;
        }
        if (getX() + getWidth() > MainKillThemAllBis.backgroundMap.getWidth()) {
            newx = camera.getRightBound() - getWidth();
        }
        if (getY() < 0) {
            newy = 0;
        }
        if (getY() + getHeight() > MainKillThemAllBis.backgroundMap.getHeight()) {
            newy = camera.getBottomBound() - getHeight();
        }
        setXY(newx, newy);
    }

    private boolean canShot() {
        return true;
    }

    private void shot() {
        new Shot(getX(), getY());
    }

    private boolean shouldShot() {
        return Mouse.isLeftPressed();
    }
}

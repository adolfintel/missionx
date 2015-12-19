/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example16;

import org.easyway.interfaces.extended.ILoopable;
import org.easyway.interfaces.sprites.IClickable;
import org.easyway.objects.Camera;
import org.easyway.objects.sprites2D.SpriteColl;
import org.easyway.system.state.GameState;

/**
 *
 * @author RemalKoil
 */
public class BasketBall extends SpriteColl implements IClickable, ILoopable {

    static int count = 0;
    static float gravity = 0.2f;
    float speedx;
    float speedy;
    static final Camera camera = GameState.getCurrentGEState().getCamera();

    public BasketBall() {
        setImage("images/basket_ball.png");
        // final Camera camera = GameState.getCurrentGEState().getCamera();
        setXY(camera.getWidth(), (float) Math.random() * camera.getHeight() / 2);
        setSize((float) Math.random() * 64 + 64, (float) Math.random() * 64 + 64);
        speedx = -((float) Math.random() * 10 + 2);
        speedy = 0;
        ++count;
        setRGBA(1, 1, 1, 0.5f);
    }

    @Override
    public void loop() {
        float newY = getY();
        float newX = getX();

        speedy += gravity;
        newY += speedy;
        if (getY() + getHeight() > camera.getHeight()) {
            newY = camera.getHeight() - getHeight();
            speedy = -speedy;
        }
        newX += speedx;
        if (getX() + getWidth() <= 0) {
            newX = GameState.getCurrentGEState().getCamera().getWidth();
        }
        setXY(newX, newY);
    }

    @Override
    protected void onDestroy() {
        new BasketBall();
        if (count < 100) {
            new BasketBall();
        }
    }

    @Override
    public void onEnter() {
        setRGBA(1, 0, 0, 1);
    }

    @Override
    public void onExit() {
        setRGBA(1, 1, 1, 0.5f);
    }

    @Override
    public void onRelease(int x, int y) {
        destroy();
    }

    // <editor-fold defaultstate="collapsed" desc="Not used methods">
    public void onDown(int x, int y) {
    }

    public void onClick(int x, int y) {
    }

    public void onDrag(int incx, int incy) {
    }

    public void onOver(int nx, int ny) {
    }
    //</editor-fold>
}

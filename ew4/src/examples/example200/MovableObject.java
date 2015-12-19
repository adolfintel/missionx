/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example200;

import examples.example200.art.DestroyEffect;
import org.easyway.geometry2D.VectorSpeed;
import org.easyway.interfaces.base.ITexture;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.Camera;
import org.easyway.objects.sprites2D.SpriteColl;
import org.easyway.system.StaticRef;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author Daniele Paggi
 */
public class MovableObject extends SpriteColl implements ILoopable {

    Vector2f direction = new Vector2f();
    float speed;

    public MovableObject(float x, float y, ITexture img) {
        super(x, y, img);
    }

    // ------------------------------
    //  SET - GET
    // ------------------------------
    public void setDirection(double angle) {
        double[] out = VectorSpeed.getCartesian(angle, 1);
        direction.set((float) out[0], (float) out[1]);
        direction.normalise();
    }

    public double getDirection() {
        return VectorSpeed.getAngle(direction.getX(), direction.getY());
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    // ------------------------------
    //  CORE METHODS
    // ------------------------------
    public void loop() {
        move(direction.getX() * speed, direction.getY() * speed);
        boundInsideCamera();
    }

    @Override
    protected void onDestroy() {
        new DestroyEffect(this);
        super.onDestroy();
    }

    // ------------------------------
    //  HELPER METHODS
    // ------------------------------
    protected void boundInsideCamera() {
        Camera camera = StaticRef.getCamera();
        if (getXOnScreen() + getWidth() <= 0) { // left check
            kill();
        }
        if (getYOnScreen() + getHeight() <= 0) { // top check
            kill();
        }
        // right check
        if (getXOnScreen() >= camera.getWidth()) {
            kill();
        }
        // bottom check
        if (getYOnScreen() >= camera.getHeight()) {
            kill();
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example21bis;

import org.easyway.collisions.CollisionUtils;
import org.easyway.collisions.GroupCollision;
import org.easyway.input.Mouse;
import org.easyway.interfaces.base.ITexture;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.Camera;
import static org.easyway.objects.texture.Texture.getTexture;
import org.easyway.objects.sprites2D.SpriteColl;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author RemalKoil
 */
public class Shot extends SpriteColl implements ILoopable {

    private static ITexture imgReference = getTexture("examples/example21/images/shot.png");
    Vector2f direction;
    float speed = imgReference.getWidth();

    public Shot(float x, float y) {
        super(x, y, imgReference);
        direction = new Vector2f(Mouse.getXinWorld() - getX(), Mouse.getYinWorld() - getY());
        direction.normalise();
        direction.scale(speed);
        GroupCollision.getGroup("Shots").addToDestination(this);
        setRGBA(1, 0, 0, 1);
    }

    @Override
    public void loop() {
        move(direction.getX(), direction.getY());

        if (checkBounds()) {
            return;
        }
    }

    private boolean checkBounds() {
        final Camera camera = Camera.getCurrentCamera();

        //if (getWidth() < 0 || getXOnScreen() > camera.getWidth() || getHeight() < 0 || getYOnScreen() > camera.getHeight()) {
        
        // out of screen test!
        if (!CollisionUtils.rectangleHit(this, Camera.getCurrentCamera())) {
            destroy();
            return true;
        }
        return false;
    }

    @Override
    public void onCollision() {
        destroy();
    }
}

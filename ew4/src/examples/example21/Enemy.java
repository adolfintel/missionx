/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example21;

import org.easyway.collisions.CollisionUtils;
import org.easyway.collisions.ManagedGroupCollision;
import org.easyway.interfaces.base.ITexture;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.Camera;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.sprites2D.SpriteColl;
import org.lwjgl.util.vector.Vector2f;
import static org.easyway.objects.texture.Texture.getTexture;

/**
 *
 * @author RemalKoil
 */
public class Enemy extends SpriteColl implements ILoopable {

    static ITexture defaultimage = getTexture("examples/example21/images/enemy.png");
    Vector2f direction;

    public Enemy() {
        super((float) Math.random() * Camera.getCurrentCamera().getWidth(),
                (float) Math.random() * Camera.getCurrentCamera().getHeight(),
                defaultimage);
        ManagedGroupCollision.get("Shots").addToSource(this);
        direction = new Vector2f((float) Math.random(), (float) Math.random());
        direction.normalise();
        direction.scale(3);
    }

    @Override
    public void loop() {
        move(direction.getX(), direction.getY());
        if (getXOnScreen() < 0) {
            direction.x = -direction.x;
        }
        if (getXOnScreen() + getWidth() > Camera.getCurrentCamera().getWidth()) {
            direction.x = -direction.x;
        }
        if (getYOnScreen() < 0 ) {
            setY(0);
            direction.y = -direction.y;
        }
        if ( getYOnScreen() + getHeight() > Camera.getCurrentCamera().getHeight()) {
            setY(Camera.getCurrentCamera().getBottomBound()-getHeight());
            direction.y = -direction.y;
        }

    }

    @Override
    public void onCollision() {
        die();
    }

    @Override
    protected void onDestroy() {
        new Enemy();
    }

    private void die() {
        // no more other collisions!
        ManagedGroupCollision.get("Shots").remove(this);
        // die image!
        // setImage("...");

        // stop looping and rendering but not destroy it!
        disactivate();

        Sprite blood;

        for (int i = 0; i <
                6; ++i) {
            float incx = (float) Math.random() * 64 - 32f;
            float incy = (float) Math.random() * 64 - 32f;
            blood = new Sprite(getX() + getWidth() / 2 + incx, 
                    getY() + getHeight() / 2 + incy,
                    getTexture("examples/example21/images/blood.png"));
            blood.move(-blood.getWidth() / 2, -blood.getHeight() / 2);
            blood.setRotation((float) Math.random() * 360);
            MainKillThemAllExample.background.drawOnBackground(blood);
            if (i == 3) {
                // draw the image on the background
                MainKillThemAllExample.background.drawOnBackground(this);
            }
        }

    }
}

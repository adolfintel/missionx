/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example21bis;

import org.easyway.collisions.GroupCollision;
import org.easyway.collisions.ManagedGroupCollision;
import org.easyway.interfaces.base.ITexture;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.BaseObject;
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
        super((float) Math.random() * MainKillThemAllBis.backgroundMap.getWidth(),
                (float) Math.random() * MainKillThemAllBis.backgroundMap.getHeight(),
                defaultimage);
        /*Managed*/GroupCollision.getGroup("Shots").addToSource(this);
        direction = new Vector2f((float) Math.random()-0.5f, (float) Math.random()-0.5f);
        direction.normalise();
        direction.scale(3);
        move(0,0);
    }

    @Override
    public void loop() {
        
        move(direction.getX(), direction.getY());
        float newx = getX();
        float newy = getY();
        if (getX() < 0) {
            direction.x = -direction.x;
            newx = 0;
        }
        if (getX() + getWidth() > MainKillThemAllBis.backgroundMap.getWidth()) {
            direction.x = -direction.x;
            newx = MainKillThemAllBis.backgroundMap.getWidth() - getWidth();
        }
        if (getY() < 0) {
            direction.y = -direction.y;
            newy = 0;
        }
        if (getY() + getHeight() > MainKillThemAllBis.backgroundMap.getHeight()) {

            direction.y = -direction.y;
            newy = MainKillThemAllBis.backgroundMap.getHeight() - getHeight();
        }
        setXY(newx, newy);

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
        // GroupCollision.getGroup("Shots").remove(this);
        // die image!
        // setImage("...");

        // stop looping and rendering but not destroy it!
        disactivate();

        Sprite blood;

        BaseObject.autoAddToLists = false;
        for (int i = 0; i < 6; ++i) {
            float incx = (float) Math.random() * 64 - 32f;
            float incy = (float) Math.random() * 64 - 32f;
            blood = new Sprite(getX() + getWidth() / 2 + incx,
                    getY() + getHeight() / 2 + incy,
                    getTexture("examples/example21/images/blood.png"));
            blood.move(-blood.getWidth() / 2, -blood.getHeight() / 2);
            blood.setRotation((float) Math.random() * 360);
            //MainKillThemAllExampleBis..background.drawOnBackground(blood);
            MainKillThemAllBis.backgroundMap.drawOnBackground(blood);
            if (i == 3) {
                // draw the image on the background
                MainKillThemAllBis.backgroundMap.drawOnBackground(this);
            }
        }
        BaseObject.autoAddToLists = true;
        destroy();
    }
}

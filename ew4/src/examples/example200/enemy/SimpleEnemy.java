/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example200.enemy;

import org.easyway.objects.texture.Texture;
import org.easyway.system.StaticRef;

/**
 *
 * @author Daniele Paggi
 */
public class SimpleEnemy extends BaseEnemy {

    final double PI2 = Math.PI * 0.5;
    final double PI4 = Math.PI * 0.25;

    public SimpleEnemy(float x, float y) {
        super(x, y, Texture.getTexture("images/shipGame/enemy.png"));
        float size = (float) Math.random() * 0.5f + 0.5f;
        setSize(getWidth() * size, getHeight() * size);
        setRGBA(size, size, size, 1);

        double randDir = Math.random() * PI2 - PI4;
        //randDir = randDir * PI2;
        setDirection(Math.PI + randDir);// move to left
        setSpeed(3);
    }

    @Override
    public void loop() {
        super.loop();
    }

    @Override
    protected void boundInsideCamera() {
        //
        // repleace the top\bottom test with the following two:
        //
        if (getYOnScreen() <= 0) {
            setY(StaticRef.getCamera().getY());
            // mirror direction along x axis
            setDirection(-getDirection());
        }
        if (getYOnScreen() + getHeight() >= StaticRef.getCamera().getHeight()) {
            setY(StaticRef.getCamera().getY() + StaticRef.getCamera().getHeight() - getHeight());
            // mirror direction along x axis
            setDirection(-getDirection());
        }
        // do again the usually test (right\left test will be really userfull only)
        super.boundInsideCamera();
    }
}

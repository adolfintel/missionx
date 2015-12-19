/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example200.art;

import examples.example200.MovableObject;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.system.StaticRef;

/**
 *
 * @author RemalKoil
 */
public class DestroyEffect extends MovableObject implements ILoopable {

    int effectTimer = 0;

    public DestroyEffect(MovableObject source) {
        super(source.getX(), source.getY(), source.getImage());
        setSize(source.getWidth(), source.getHeight());
        setIdLayer(4);
        setDirection(source.getDirection());
        setSpeed(source.getSpeed());
        setRGBA(source.getColorRed(), source.getColorGreen(), source.getColorBlue(), 0.5f);
    }

    @Override
    public void loop() {
        float stepColor = 1f / 255f;
        setRGBA(getColorRed() - stepColor, getColorGreen() - stepColor,
                getColorBlue() - stepColor, getColorAlpha() - stepColor);
        incY((float) Math.sin(++effectTimer / 100f));
        setRotation(effectTimer * 4);
        setSize(getWidth() - 2, getHeight() - 2);
        super.loop();

        if (getYOnScreen() > StaticRef.getCamera().getHeight() || getColorAlpha()<=0.01f) {
            kill();
            return;
        }
    }
}

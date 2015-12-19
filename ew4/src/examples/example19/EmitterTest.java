/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example19;

import org.easyway.input.Keyboard;
import org.easyway.objects.animo.Animo;
import org.easyway.objects.particles.Emitter;
import org.easyway.objects.text.Text;
import org.easyway.objects.texture.Texture;
import org.easyway.system.state.Game;
import org.easyway.system.state.GameState;

/**
 *
 * @author RemalKoil
 */
public class EmitterTest extends Game {

    public static void main(String args[]) {
        new EmitterTest();
    }
    //Emitter emitter;
    Emitter e;
    Emitter explosion;
    Text text;

    @Override
    public void creation() {
        text = new Text(0, 0, "");
        Animo animo = new Animo();
        animo.add(Texture.getTexture("images/red.bmp"), 100);
        animo.add(Texture.getTexture("images/blue.bmp"), 100);
        //emitter = new Emitter(800, 300);
        //emitter.setAnimazione(animo);
        //emitter.set

        explosion = new Emitter(500, 300, 2000);

        explosion.setParticlesPerSecond(120);
        explosion.setLifeMax(350);
        explosion.setLifeMin(300);
        explosion.setUseanimazione(true);
        explosion.setAnimazione(animo);
        explosion.setFadeOut(100);
        explosion.setRMax(1f);
        explosion.setRMin(1f);
        explosion.setGMax(1f);
        explosion.setGMin(1f);
        explosion.setBMax(1f);
        explosion.setBMin(1f);
        explosion.setxSpeedMax(0.5f);
        explosion.setxSpeedMin(-0.5f);
        explosion.setySpeedMax(1);
        explosion.setySpeedMin(0.5f);
        explosion.setInitialRotationMin(-1);
        explosion.setInitialRotationMin(1);
        explosion.setParticlesLayer(6);
        explosion.setDrawScale(1f);
        explosion.setxSizeIncMax(0);
        explosion.setxSizeIncMin(0);
        explosion.setySizeIncMax(0);
        explosion.setySizeIncMin(0);
    }

    @Override
    public void loop() {
        text.setText("size: " + GameState.getCurrentGEState().getLoopList().size());
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            e = new Emitter(400, 300, 100);
            e.setParticle(Texture.getTexture("images/apple.png"));
            e.setParticlesLayer(5);
            e.setParticlesPerSecond(500);
            e.setLifeMin(1000);
            e.setLifeMax(1000);
            e.setFadeIn(250);
            e.setFadeOut(250);
            e.setxSpeedMax(2);
            e.setxSpeedMin(-2);
            e.setySpeedMax(2);
            e.setySpeedMin(-2);
            e.setDrawScale(0.5f * 1);
            e.setRotRateIncMax(0);
            e.setRotRateIncMin(0);
            e.setInitialRotationMin(0);
            e.setInitialRotationMax(0);
            e.setBMax(1);
            e.setBMin(1);
            e.setGMax(1);
            e.setGMin(1);
            e.setRMax(1);
            e.setRMin(1);
            e.setxSizeIncMax(0);
            e.setxSizeIncMin(0);
            e.setySizeIncMax(0);
            e.setySizeIncMin(0);
            e.setOpacityMax(1);
            e.setOpacityMin(0.5f);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            if (e != null) {
                e.destroy();
            }
        }
    }

    public void render() {
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.art;

import mygame.logic.Player;
import mygame.*;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;

/**
 *
 * @author Do$$e
 */
public class Hud extends Sprite implements ILoopable {

    private Sprite e, l, r, s,b;
    private String color = "";
    private Sprite energy[], lives[], rockets[], score[];
//private Text energy;

    public Hud() {
        super(0, CommonVar.yRes-64, Texture.getTexture("images/nullCursor.png"));
        this.setSmoothImage(CommonVar.smoothing);
        b=new Sprite(getX()-8,CommonVar.yRes-70,Texture.getTexture("images/hud/bkg.png"),6);
        b.setIdLayer(6);
        e = new Sprite(getX() + 8, getY(), Texture.getTexture("images/hud/health.png"), 6);
        l = new Sprite(e.getX() + e.getWidth() + 8, getY(), Texture.getTexture("images/hud/lives.png"), 6);
        r = new Sprite(l.getX() + l.getWidth() + 8, getY(), Texture.getTexture("images/hud/rockets.png"), 6);
        s = new Sprite(r.getX() + r.getWidth() + 8, getY(), Texture.getTexture("images/hud/score.png"), 6);
        e.setIdLayer(7); l.setIdLayer(7); r.setIdLayer(7); s.setIdLayer(7);
        e.setSmoothImage(CommonVar.smoothing);
        l.setSmoothImage(CommonVar.smoothing);
        r.setSmoothImage(CommonVar.smoothing);
        s.setSmoothImage(CommonVar.smoothing);
        b.setSmoothImage(CommonVar.smoothing);
        b.setFixedOnScreen(true);
        e.setFixedOnScreen(true);
        l.setFixedOnScreen(true);
        r.setFixedOnScreen(true);
        s.setFixedOnScreen(true);
        energy = new Sprite[3];
        lives = new Sprite[2];
        rockets = new Sprite[3];
        score = new Sprite[6];
        for (int i = 0; i < energy.length; i++) {
            if (i == 0) {
                energy[i] = new Sprite(e.getX() + 8, CommonVar.yRes-44, Texture.getTexture("images/hud/0.png"), 6);
            } else {
                energy[i] = new Sprite(energy[i - 1].getX() + energy[i - 1].getWidth(), CommonVar.yRes-44, Texture.getTexture("images/hud/0.png"), 6);
            }
            energy[i].setFixedOnScreen(true);
            energy[i].setSmoothImage(CommonVar.smoothing);
            energy[i].setIdLayer(7);
        }
        for (int i = 0; i < lives.length; i++) {
            if (i == 0) {
                lives[i] = new Sprite(l.getX() + 16, CommonVar.yRes-44, Texture.getTexture("images/hud/0.png"), 6);
            } else {
                lives[i] = new Sprite(lives[i - 1].getX() + lives[i - 1].getWidth(), CommonVar.yRes-44, Texture.getTexture("images/hud/0.png"), 6);
            }
            lives[i].setFixedOnScreen(true);
            lives[i].setSmoothImage(CommonVar.smoothing);
            lives[i].setIdLayer(7);
        }
        for (int i = 0; i < rockets.length; i++) {
            if (i == 0) {
                rockets[i] = new Sprite(r.getX() + 8, CommonVar.yRes-44, Texture.getTexture("images/hud/0.png"), 6);
            } else {
                rockets[i] = new Sprite(rockets[i - 1].getX() + rockets[i - 1].getWidth(), CommonVar.yRes-44, Texture.getTexture("images/hud/0.png"), 6);
            }
            rockets[i].setFixedOnScreen(true);
            rockets[i].setSmoothImage(CommonVar.smoothing);
            rockets[i].setIdLayer(7);
        }
        for (int i = 0; i < score.length; i++) {
            if (i == 0) {
                score[i] = new Sprite(s.getX() + 12, CommonVar.yRes-44, Texture.getTexture("images/hud/0.png"), 6);
            } else {
                score[i] = new Sprite(score[i - 1].getX() + score[i - 1].getWidth(), CommonVar.yRes-44, Texture.getTexture("images/hud/0.png"), 6);
            }
            score[i].setFixedOnScreen(true);
            score[i].setSmoothImage(CommonVar.smoothing);
            score[i].setIdLayer(7);
        }

        e.setFixedOnScreen(true);
        //energy.setSize(28);
        //color=energy.createColor(25, 255, 25);
    }

    @Override
    public void loop() {
        if (CommonVar.destroyAll == true) {
            destroy();
        }
        Player p = Player.getDefaultInstance();
        String aux;
        int health = p.getHealth() + 1;
        if (health < 0) {
            health = 0;
        }
        if (health < 100 && health >= 10) {
            aux = "0" + health;
        } else if (health < 10) {
            aux = "00" + health;
        } else {
            aux = "" + health;
        }
        //energy.setText(color+aux);
        for (int i = 0; i < energy.length; i++) {
            energy[i].setImage(Texture.getTexture("images/hud/" + aux.charAt(i) + ".png"));
        }
        if (p.getLives() >= 10) {
            aux = "" + p.getLives();
        } else if (p.getLives() < 0) {
            aux = "00";
        } else {
            aux = "0" + p.getLives();
        }
        for (int i = 0; i < lives.length; i++) {
            lives[i].setImage(Texture.getTexture("images/hud/" + aux.charAt(i) + ".png"));
        }
        if (p.getRocketCount() < 100 && p.getRocketCount() >= 10) {
            aux = "0" + p.getRocketCount();
        } else if (p.getRocketCount() < 10) {
            aux = "00" + p.getRocketCount();
        } else {
            aux = "" + p.getRocketCount();
        }
        for (int i = 0; i < rockets.length; i++) {
            rockets[i].setImage(Texture.getTexture("images/hud/" + aux.charAt(i) + ".png"));
        }
        if (p.getPoints() < 100000 && p.getPoints() >= 10000) {
            aux = "0" + p.getPoints();
        } else if (p.getPoints() < 10000 && p.getPoints() >= 1000) {
            aux = "00" + p.getPoints();
        } else if (p.getPoints() < 1000 && p.getPoints() >= 100) {
            aux = "000" + p.getPoints();
        } else if (p.getPoints() < 100 && p.getPoints() >= 10) {
            aux = "0000" + p.getPoints();
        } else if (p.getPoints() < 10) {
            aux = "00000" + p.getPoints();
        } else {
            aux = "" + p.getPoints();
        }
        for (int i = 0; i < score.length; i++) {
            score[i].setImage(Texture.getTexture("images/hud/" + aux.charAt(i) + ".png"));
        }
    }

    @Override
    protected void onDestroy() {
        b.destroy();
        e.destroy();
        l.destroy();
        r.destroy();
        s.destroy();
        for (int i = 0; i < energy.length; i++) {
            energy[i].destroy();
        }
        for (int i = 0; i < lives.length; i++) {
            lives[i].destroy();
        }
        for (int i = 0; i < rockets.length; i++) {
            rockets[i].destroy();
        }
        for (int i = 0; i < score.length; i++) {
            score[i].destroy();
        }
    }
}

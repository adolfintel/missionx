/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.art;

import mygame.CommonVar;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.text.EWFont;
import org.easyway.objects.text.Text;
import org.easyway.objects.texture.Texture;
import org.easyway.system.StaticRef;
import org.easyway.utils.MathUtils;

/**
 *
 * @author Do$$e
 */
public class LifeBar extends Sprite /*implements ILoopable*/ {

    int life, maxLife;
    Sprite lifeMeter;
    float rh = 0, gh = 1, bh = 0, rm = 1, gm = 1, bm = 0, rl = 1, gl = 0, bl = 0, r = 0, b = 1, g = 0;
    //Text t;

    public LifeBar(float x, float y, int maxLife) {
        super(x, y, Texture.getTexture("images/hud/bossLBBk.png"), 5);
        this.maxLife = maxLife;
        this.life = maxLife;
        lifeMeter = new Sprite(x, y, Texture.getTexture("images/hud/bossLB.png"), 7);
        lifeMeter.setIdLayer(7);
        lifeMeter.setSmoothImage(CommonVar.smoothing);
        /*EWFont f = new EWFont("Tahoma");
        f.changeColor(1, 0, 0);
        f.setSize(7);
        t = new Text(0, 0, "HP: " + life + "/" + maxLife);
        t.setFont(f);
        t.setIdLayer(7);
        t.setFixedOnScreen(true);*/
        setWidth(128);
        lifeMeter.setWidth(128);
        lifeMeter.setHeight(16);
        //t.setXY(getXOnScreen(),getYOnScreen()-16);
    }

    public void setLife(int life) {
        if (life > maxLife) {
            life = 0;
        }
        double s = (double) life / maxLife;
        if (s >= 0.5) {
            r = MathUtils.lerp(rh, rm, (float) (s * 2 - 1));
            g = MathUtils.lerp(gh, gm, (float) (s * 2 - 1));
            b = MathUtils.lerp(bh, bm, (float) (s * 2 - 1));
        } else {
            r = MathUtils.lerp(rm, rl, (float) (s * 2));
            g = MathUtils.lerp(gm, gl, (float) (s * 2));
            b = MathUtils.lerp(bm, bl, (float) (s * 2));
        }
        lifeMeter.setRGBA((float) Math.pow(r, 2), (float) Math.pow(g, 2), (float) Math.pow(b, 2), 1);
        lifeMeter.setWidth((int) (128 * s));
        this.life = life;

        //t.setText("HP: " + life + "/" + maxLife);
    }

    public void sposta(int x, int y) {
        setXY(x / StaticRef.getCamera().getZoom2D(), y / StaticRef.getCamera().getZoom2D());
        lifeMeter.setXY(getX(), getY());
        //t.setXY(getXOnScreen(),getYOnScreen()-16);
    }

    @Override
    protected void onDestroy() {
        lifeMeter.destroy();
        //t.destroy();
    }

    /*@Override
    public void loop() {
        t.setXY(getXOnScreen(),getYOnScreen()-16);
    }*/
}

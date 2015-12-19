/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.util.Vector;
import mygame.sounds.MusicLoop;
import org.easyway.objects.animo.Animo;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;

/**
 *
 * @author Do$$e
 */
public class CommonVar {
    //questa classe contiene delle variabili statiche utilizzate e modificate dal gioco durante l'esecuzione
    public static float DIFF_EASY=0.85f, DIFF_NORMAL=1.25f, DIFF_HARD=1.9f;
    public static boolean destroyAll = false, gameOver = false, CDMode = false, music = true, useSFX = true, gameStarted = false, VSynch = true, smoothing = true, usePProc=false, preLoadBackgrounds=true, fullscreen=false;
    public static int score = 0, hiScore1 = 0, hiScore2 = 0, hiScore3 = 0;
    public static float difficulty = DIFF_NORMAL, mouseSpeed = 0.85f;
    public static String loc = "en", skin = "defSkin";
    public static final boolean useQuadTree=false;
    public static float gfx_vlow=0.25f, gfx_low=0.5f, gfx_mid=1f, gfx_hi=1f, gfx_ultra=1.15f, gfxQuality=-1;
    public static boolean lowRes=false;
    public static Sprite GOSprite=null;
    public static MusicLoop musicL;
    public static String level="maps/missionx.txt";
    public static boolean useCheats=false,swCollisions=false;
    private static Vector<String> lev=new Vector<String>();
    public static boolean useGamePad=false;
    public static int GPFire=0,GPRockets=1,GPPause=5;
    public static Animo exp=null, fastExp=null;
    public static int xRes=1024,yRes=768;
    public static float getLevelX(int i) {
        if(i<lev.size())return Float.parseFloat(lev.get(i)); else return 0;
    }

    protected static void addLevel(String ln) {
        lev.addElement(ln);
    }

    protected static void resetLevels() {
        lev.removeAllElements();
    }

    protected static void initAnimos() {
        exp = new Animo();
        exp.add(Texture.getTexture("images/explosion/exp1.png"), 100);
        exp.add(Texture.getTexture("images/explosion/exp2.png"), 100);
        exp.add(Texture.getTexture("images/explosion/exp3.png"), 100);
        exp.add(Texture.getTexture("images/explosion/exp4.png"), 100);
        exp.add(Texture.getTexture("images/explosion/exp5.png"), 100);
        exp.add(Texture.getTexture("images/explosion/exp6.png"), 100);
        exp.add(Texture.getTexture("images/explosion/exp7.png"), 100);
        exp.add(Texture.getTexture("images/explosion/exp8.png"), 100);
        fastExp = new Animo();
        fastExp.add(Texture.getTexture("images/explosion/exp1.png"), 50);
        fastExp.add(Texture.getTexture("images/explosion/exp2.png"), 50);
        fastExp.add(Texture.getTexture("images/explosion/exp3.png"), 50);
        fastExp.add(Texture.getTexture("images/explosion/exp4.png"), 50);
        fastExp.add(Texture.getTexture("images/explosion/exp5.png"), 50);
        fastExp.add(Texture.getTexture("images/explosion/exp6.png"), 50);
        fastExp.add(Texture.getTexture("images/explosion/exp7.png"), 50);
        fastExp.add(Texture.getTexture("images/explosion/exp8.png"), 50);

    }
}

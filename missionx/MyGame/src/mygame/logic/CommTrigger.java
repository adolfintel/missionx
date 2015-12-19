/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.logic;

import mygame.*;
import mygame.sounds.MP3Sound;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;

/**
 *
 * @author Do$$e
 */
public class CommTrigger extends Sprite implements ILoopable {

    String img;
    String snd;
    int ms;

    public CommTrigger(float x, String img, String snd, int ms) {
        super(x, 0,Texture.getTexture("images/nullCursor.png"));
        this.snd = snd;
        this.ms = ms;
        this.img = img;
    }

    @Override
    public void loop() {
        if (getXOnScreen() <= 1024) {
            new CommWindow(Texture.getTexture("images/story/" + CommonVar.loc + "/" + img.replace("%GP%", "" + CommonVar.useGamePad)), ms);
            if (CommonVar.useSFX) {
                new MP3Sound("dialogs/" + CommonVar.loc + "/" + snd);
            }
            destroy();
        }
        if (CommonVar.destroyAll) {
            destroy();
        }
    }
}

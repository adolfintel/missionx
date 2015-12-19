/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.menu;

import mygame.*;
import mygame.logic.GamePad;
import mygame.sounds.SoundBox;
import org.easyway.interfaces.base.ITexture;
import org.easyway.interfaces.sprites.IClickable;
import org.easyway.objects.sprites2D.Mask;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;
import org.easyway.sounds.WaveCore;

/**
 *
 * @author Do$$e
 */
public class ClickableSprite extends Sprite implements IClickable {

    private boolean clicked = false, selected = false;
    private ITexture def, sel;

    public ClickableSprite(float x, float y, ITexture img, ITexture selected) {
        super(x, y, img, 6);
        def = img;
        sel = selected;
        this.setSmoothImage(CommonVar.smoothing);
    }

    public ClickableSprite() {
        this(0, 0, Texture.getTexture("images/menu/newGame.png"), Texture.getTexture("images/menu/newGameSelected.png"));
        this.setSmoothImage(CommonVar.smoothing);
    }

    @Override
    public void onClick(int x, int y) {
        clicked = true;
        SoundBox.playForced("sfx/" + CommonVar.skin + "/click.mp3");
    }

    @Override
    public void onRelease(int x, int y) {
    }

    @Override
    public void onDown(int x, int y) {
    }

    @Override
    public void onEnter() {
        selected = true;
        setImage(sel);
        setWidth(sel.getWidth());
        setHeight(sel.getHeight());
        SoundBox.playForced("sfx/" + CommonVar.skin + "/select.mp3");
    }

    @Override
    public void onExit() {
        selected = false;
        setImage(def);
        setWidth(def.getWidth());
        setHeight(def.getHeight());
    }

    @Override
    public void onDrag(int incx, int incy) {
    }

    @Override
    public void onOver(int nx, int ny) {
        if(CommonVar.useGamePad&&GamePad.isButtonPressed(CommonVar.GPFire)) onClick((int)getX(),(int)getY());
    }

    @Override
    public Mask getMask() {
        if (selected) {
            return sel.getMask();
        } else {
            return def.getMask();
        }
    }

    public boolean hasBeenClicked() {
        boolean retValue = clicked;
        clicked = false;
        return retValue;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
}

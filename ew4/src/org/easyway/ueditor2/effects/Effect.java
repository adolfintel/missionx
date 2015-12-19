/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.ueditor2.effects;

import org.easyway.objects.sprites2D.Sprite;
import org.easyway.system.StaticRef;

/**
 *
 * @author Daniele
 */
public abstract class Effect extends Sprite implements IEffect {

    public Effect() {
        this(0,0);
    }

    public Effect(float x, float y) {
        super(false, x, y);
        EffectList.getSingelton().add(this);
    }

    @Override
    public void destroy() {
        if (!isDestroyed()) {
            EffectList.getSingelton().remove(this);
            super.destroy();
        }
    }

    @Override
    public final void setIdLayer(int id) {
    }

    @Override
    public final void setLayer(int layer) {
    }

    @Override
    public final void readdToDrawingLists() {
    }

    public void render() {
        StaticRef.getCamera().setCollectingObjectsOnScreen(false);
        super.render();
        StaticRef.getCamera().setCollectingObjectsOnScreen(true);
    }
    public void loop() {destroy();}
    public void create() {}
}

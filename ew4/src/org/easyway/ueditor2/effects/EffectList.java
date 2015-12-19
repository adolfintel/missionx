/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.ueditor2.effects;

import java.util.ConcurrentModificationException;
import java.util.Vector;

/**
 *
 * @author Daniele
 */
public class EffectList extends Vector<IEffect> {

    transient private final Vector<IEffect> toRemove = new Vector<IEffect>(50);
    transient private final Vector<IEffect> toCreate = new Vector<IEffect>(50);
    private static EffectList thisInstance;

    public static EffectList getSingelton() {
        return thisInstance == null ? thisInstance = new EffectList() : thisInstance;
    }

    public EffectList() {
        super(20);
    }

    public synchronized void render() {
        for (int i = 0; i < size(); ++i) {
            get(i).render();
        }
    }

    public synchronized void loop() {
        synchronized (toCreate) {
            IEffect effect;
            for (int i=toCreate.size()-1; i>=0; --i) {
                effect = toCreate.get(i);
                if (!contains(effect)) {
                    super.add(effect);
                    effect.create();
                }
            }
            toCreate.clear();
        }

        for (int i = size() - 1; i >= 0; --i) {
            get(i).loop();
        }

        synchronized (toRemove) {
            IEffect effect;
            for (int i=toRemove.size()-1; i>=0; --i) {
                effect = toRemove.get(i);
                if (contains(effect)) {
                    super.remove(effect);
                }
            }
            toRemove.clear();
        }
    }

    @Override
    public synchronized boolean add(IEffect effect) {
        synchronized (toCreate) {
            if (!toCreate.contains(effect)) {
                toCreate.add(effect);
                return true;
            }
            return false;
        }
    }

    @Override
    public synchronized boolean remove(Object obj) {
        if (!(obj instanceof IEffect)) {
            throw new RuntimeException("obj Should be an IEffect");
        }
        synchronized (toRemove) {
            if (!toRemove.contains((IEffect) obj)) {
                toRemove.add((IEffect) obj);
                return true;
            }
            return false;
        }
    }
}

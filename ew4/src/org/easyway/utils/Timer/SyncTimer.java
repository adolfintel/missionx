/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.utils.Timer;

import java.io.Serializable;
import org.easyway.interfaces.base.IDestroyable;
import org.easyway.system.Core;
import org.easyway.system.state.GameState;

/**
 *
 * @author RemalKoil
 */
public abstract class SyncTimer implements IDestroyable, Serializable {

    private long delayTimer;
    private long elaspedTime;
    private boolean stopped;
    private boolean destroyed;

    public SyncTimer(int delayTimer) {
        this.delayTimer = delayTimer * 1000000L;
        stopped = true;
        destroyed = false;
        GameState.getCurrentGEState().getTimerList().timers.add(this);
    }

    public long getDelay() {
        return delayTimer / 1000000L;
    }

    public void setDelay(int delayTimer) {
        this.delayTimer = delayTimer * 1000000L;
    }

    public void start() {
        elaspedTime = 0;
        stopped = false;
    }

    public void stop() {
        stopped = true;
    }

    public void resume() {
        stopped = false;
    }

    public boolean isStopped() {
        return stopped;
    }

    public long getElaspedTime() {
        return elaspedTime;
    }

    void loop() {
        if (stopped || destroyed) {
            return;
        }
        elaspedTime += Core.getInstance().getElaspedTime();
        if (elaspedTime >= delayTimer) {
            elaspedTime = elaspedTime - delayTimer;
            onTick();
        }
    }

    public abstract void onTick();

    @Override
    public void destroy() {
        if (isDestroyed()) {
            return;
        }
        GameState.getCurrentGEState().getTimerList().shouldRemove.add(this);
        destroyed = true;
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }
}

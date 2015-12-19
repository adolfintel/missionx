/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.utils.Timer;

import java.io.Serializable;
import java.util.ArrayList;
import org.easyway.interfaces.base.IPureLoopable;

/**
 *
 * @author RemalKoil
 */
public class TimerList implements IPureLoopable, Serializable {

    ArrayList<SyncTimer> shouldRemove = new ArrayList<SyncTimer>(25);
    ArrayList<SyncTimer> timers = new ArrayList<SyncTimer>(50);

    public void add(SyncTimer timer) {
        timers.add(timer);
    }

    public void removeAll() {
        shouldRemove.clear();
        timers.clear();
    }

    public SyncTimer get(int index) {
        return timers.get(index);
    }

    public int size() {
        return timers.size();
    }

    public void loop() {

        for (SyncTimer timer : timers) {
            timer.loop();
        }
        if (shouldRemove.size() > 0) {
            for (SyncTimer timer : shouldRemove) {
                timers.remove(timer);
            }
            shouldRemove.clear();
        }
    }
}

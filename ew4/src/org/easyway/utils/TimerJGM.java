/* EasyWay Game Engine
 * Copyright (C) 2006 Daniele Paggi.
 *  
 * Written by: 2006 Daniele Paggi<dshnt@hotmail.com>
 *   
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 * 
 * You should have received a copy of the GNU Library General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 * * @Author Daniele Paggi, Do$$e
 */
package org.easyway.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * class that manage the temporized event<br>
 * this class run the onTick method every <b>'delay'</b> milli-seconds.<br>
 * <br>
 * example:<br>
 * if we must increase the score of 10 points every 2 seconds...this is the code<br>
 * <br>
 * class MyTimer extends TimerJGM {<br>
 * <br>
 * public MyTimer() {<br> // 1000 ms = 1 second.<br>
 * super( 2000 );<br> // starts the timers. start(); }<br>
 * <br>
 * public void onTick() {<br>
 * score += 10;<br> }<br>
 * <br> }<br>
 * <br>
 * <br>
 * this is an old class from another my game engine: Java Game Maker.
 */
public abstract class TimerJGM {

    Timer timer;
    protected int delay;
    //private boolean remind=true; //test dosse

    /**
     * creates a new instance.<br>
     * 
     * @param delay
     *            indicates how many time the onTick method will be self-runned
     */
    public TimerJGM(int delay) {
        timer = new Timer();
        if(delay<0) delay=0;
        this.delay = delay;
    }

    /** starts the timer */
    public void start() {
        // timer.scheduleAtFixedRate( new RemindTask(), (long)1, (long)delay );
        try {
            timer.schedule(new RemindTask(), (long) delay, (long) delay);
        } catch (IllegalStateException iexp) {
            timer = new Timer();
            timer.schedule(new RemindTask(), (long) delay, (long) delay);
        }
    }

    /** stops the timer */
    public void stop() {
        timer.cancel();
        /*//test dosse
        delay=99999999;
        remind=false;
        start();*/
    }

    /**
     * changes the timer's delay
     * 
     * @param delay
     *            new delay
     */
    public void setDelay(int delay) {
        stop();
        this.delay = delay;
        start();
    }
    private  long lastTime,   ElTime;

    /**
     * class to implements the timer
     * 
     * @author Daniele Paggi
     * 
     */
    class RemindTask extends TimerTask {

        /**
         * creates a new instance
         */
        public RemindTask() {
            lastTime = System.currentTimeMillis();
            ElTime = 0;
        }

        public void run() {
            ElTime = System.currentTimeMillis() - lastTime;
            lastTime = System.currentTimeMillis();
            //if(remind) onTick(); //test Dosse
            onTick();
        }
    }

    /**
     * this method is autocalled every delay ms
     */
    public abstract void onTick();

    /**
     * returns the time elasped from last loop
     * 
     * @return the time elasped from last loop
     */
    public long getElaspedTime() {
        return ElTime;
    }
}

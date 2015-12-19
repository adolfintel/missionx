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
 */
package org.easyway.system;

import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;
import org.lwjgl.opengl.Display;

/**
 * @author Daniele Paggi,Do$$e
 * 
 */
class Sincro extends Thread {

    public static final int frameTimeArraySize = StaticRef.frameRateSmoothing;
    /** indicates if the game is looping or not */
    static boolean loop = true, delayDone = false, speedCompensation = true;
    /** game's frame per second */
    static int fps = 60;
    /**
     * timeOut
     *
     * @see Core.setTimeOut(int)
     */
    protected int timeOut = 10000;
    /** reference to the Game Core */
    protected Core core;
    /** used for the fps */
    protected int cont = 0, lastFps = 60;
    protected double lastFpsHP = 60.0;
    /** last time: used for calculate the fps */
    protected long last, frameTime[] = new long[frameTimeArraySize];
    private boolean fpsCounterReady = false;
    /** reference to the TOOC */
    private TimeOutOpController tc = null;

    {
        loop = true;
        core = Core.getInstance();
    }

    /** main loop of game */
    private class Logo extends Sprite implements ILoopable{

        public Logo() {
            super(0,Display.getDisplayMode().getHeight()-96,Texture.getTexture("images/logo.png"));
            setIdLayer(15);
            setFixedOnScreen(true);
        }
        float a=1;
        @Override
        public void loop() {
            a-=0.008*Core.getInstance().getSpeedMultiplier();
            setRGBA(1,1,1,a);
            if(a<=0) destroy();
        }

    }
    protected long renderT;
    @Override
    public void run() {
        core.init(); // creation
        last = System.nanoTime();// currentTimeMillis();
        CurrentTime = LastTime = System.nanoTime();// currentTimeMillis();
        ElaspedTime = 0;

        if (timeOut >= 1000) {
            tc = new TimeOutOpController(timeOut, this);
            tc.start();
        }
        //if (Display.isCreated()) {
        core.creation();
                new Logo();
        while (loop) {
            if (Display.isCloseRequested()) {// close on ALT + F4
                core.endGame(); // loop = false
            }
            ricaveElaspedTime();
            if (tc != null) {
                tc.stateA();
            }
            core.coreLoop();
            renderT=System.nanoTime();
            core.coreRender();
            renderT=System.nanoTime()-renderT;
            if (tc != null) {
                tc.stateB();
            }

            Display.sync(fps); // speed down the loop
            Display.update(); // draw at screen
            // calculates the fps
            fps();
        }
        //}
        core.cleanup();
    }

    /** calculate the fps */
    /* protected void fps() {
    ++cont;
    if (System.nanoTime() - last >= 1000000000L) {
    lastFps = cont;
    cont = 0;
    last = System.nanoTime();
    }
    }*/
    private class WarmUpDelay extends org.easyway.utils.TimerJGM {

        public WarmUpDelay() {
            super(1000);
        }

        @Override
        public void onTick() {
            delayDone = true;
            stop();
        }
    }

    protected void fps() {
        if (!fpsCounterReady) {
            new WarmUpDelay().start();
            fpsCounterReady = true;
            for (int i = 0; i < frameTimeArraySize; i++) {
                frameTime[i] = 1L;
            }
        }
        for (int i = 1; i < frameTimeArraySize; i++) {
            frameTime[i - 1] = frameTime[i];
        }
        //long aux=System.nanoTime()-last;
        //if(aux<20000000L) aux=20000000L;
        if (System.nanoTime() - last >= 1000000000L || !speedCompensation) {
            frameTime[frameTimeArraySize - 1] = frameTime[frameTimeArraySize - 2];
        } else {
            frameTime[frameTimeArraySize - 1] = System.nanoTime() - last;
        }
        last = System.nanoTime();
        long averageFrameTime = 0;
        for (int i = 0; i < frameTimeArraySize; i++) {
            averageFrameTime += frameTime[i];
        }
        averageFrameTime /= frameTimeArraySize;
        lastFps = (int) (1000000000L / averageFrameTime) + 1;
        lastFpsHP = (double) 1000000000L / averageFrameTime;
    }

    public void initFpsCounter() {
        for (int i = 0; i < frameTimeArraySize; i++) {
            frameTime[i] = 1000000000L / 60;
        }
    }

    /** returns how much FPS has got the Game in the last cycle */
    public int getFps() {
        if (fpsCounterReady && delayDone) {
            return lastFps;
        } else {
            return fps;
        }
    }

    public double getFpsHP() {
        if (fpsCounterReady && delayDone) {
            return lastFpsHP;
        } else {
            return fps;
        }
    }
    /*
    protected void fps() {
    if (cont == 10) {
    cont = 0;
    long diff=System.nanoTime()-last;
    lastFps=(int)(1000000000L/(diff/10));
    last = System.nanoTime();
    }
    cont++;
    }

    public int getFps() {
    if(lastFps<=0||lastFps>fps) return fps; else return lastFps;
    }
     */
    protected long LastTime, CurrentTime, ElaspedTime;

    /** calculates the elasped time */
    protected void ricaveElaspedTime() {
        LastTime = CurrentTime;
        CurrentTime = System.nanoTime();// .currentTimeMillis();
        ElaspedTime = CurrentTime - LastTime;
    }

    /** returns the elasped time */
    public long getElaspedTime() {
        return ElaspedTime;
    }

    /** returns the Time out */
    public int getTimeOut() {
        return timeOut;
    }

    /**
     * sets the time out
     *
     * @param timeOut
     *            time in ms
     */
    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
        if (timeOut > 1000) {
            tc = new TimeOutOpController(timeOut, this);
            tc.start();
        } else {
            if (tc != null) {
                tc.stop();
            }
            tc = null;
        }
    }
}

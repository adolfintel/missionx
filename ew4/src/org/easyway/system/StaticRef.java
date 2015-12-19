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

import java.util.ArrayList;

import org.easyway.collisions.CoreCollision;
import org.easyway.interfaces.base.ITexture;
import org.easyway.lists.CollisionableLoopList;
import org.easyway.lists.DrawingLayaredList;
import org.easyway.lists.FinalLoopList;
import org.easyway.objects.Camera;
import org.easyway.system.state.GameState;
import org.easyway.utils.Utility;

/**
 * This is a class whith static fields that refereces to the game engine
 * apparates.
 *
 * @author Daniele Paggi, Do$$e
 *
 */
public final class StaticRef {

    /** rendering thread */
    static Thread renderThread;
    /** indicates if the game engine is in DEBUG mode or not*/
    public static boolean DEBUG = true;
    /** select if the game engine will use the shaders or not */
    public static boolean use_shaders = false;

    // TODO: Comment
    public static int frameRateSmoothing = 10;
    /** reference to game core */
    //public static Core core;
    /** reference to the music core */
    // public static OggPlayer music = new OggPlayer();
    /**
     * objects that Must call "onCollision" method
     */
    public static CollisionableLoopList collisionableLoopList;
    /** loops the Loopable Objects */
    // public static LoopList loopList;
    /** loops the finalLoopable Objects */
    public static FinalLoopList finalLoopList;
    /**
     * draws the Drawingable Objects<br>
     * to defaults there are 7 layers:<br>
     * from layers[0] to layers[6]
     */
    // public static DrawingLayaredList layers[];
    /** the game collision manager */
    // public static CoreCollision coreCollision;
    /**
     * the quad manager<br>
     * the quad system is an architecture that loops and render only the
     * "onCamera" objects.
     *
     * @see QuadManager
     */
    //public static QuadManager quadManager;
    //public static
    /** a list of all animos */
    //public static AnimoList animoList;

    // public static TimerList timerList;
    /** references to camera */
    // protected static Camera camera;
    /** current number of particles managed by the game engine */
    public static int particleCount = 0;

    /**
     * retusn the Camera of GameEngine
     * @return the Camera of GameEngine
     */
    @Deprecated
    public static Camera getCamera() {
        return GameState.getCurrentGEState().getCamera();
    }

    /**
     * sets the GameEngine's camera
     * @param camera the camera to set
     */
    @Deprecated
    public static void setCamera(Camera camera) {
        GameState.getCurrentGEState().setCamera(camera);
    }

    /** rendering thread */
    public static Thread getRenderThread() {
        return renderThread;
    }
    /** a list of all textures */
    public static ArrayList<ITexture> textures;
    /**
     * indicates if the StaticRef is initialized or it's in phase of
     * initilization (initialized=false)
     */
    public static boolean initialized = false;
}

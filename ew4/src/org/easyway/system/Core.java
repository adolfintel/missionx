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
/** packages that contains the System Classes as the Core.*/
package org.easyway.system;

import java.awt.Canvas;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.IntBuffer;
import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.easyway.collisions.CollisionUtils;
import org.easyway.collisions.quad.QuadTree;
import org.easyway.debug.DebugManager;
import org.easyway.ueditor2.commands.CommandList;
import org.easyway.input.Mouse;
import org.easyway.interfaces.base.ITexture;
import org.easyway.lists.CollisionableLoopList;
import org.easyway.lists.DrawingLayaredList;
import org.easyway.lists.FinalLoopList;
import org.easyway.lists.LoopList;
import org.easyway.objects.BaseObject;
import org.easyway.objects.Camera;
//import org.easyway.sounds.OggPlayer;
import org.easyway.objects.texture.Texture;
import org.easyway.objects.texture.TextureFBO;
import org.easyway.shader.Shader;
import org.easyway.shader.ShaderEffect;
import org.easyway.system.state.GameState;
import org.easyway.system.state.OpenGLState;
import org.easyway.utils.Utility;
import org.easyway.system.state.Room;
import org.easyway.tiles.TileMap;
import org.easyway.utils.MathUtils;
import org.easyway.utils.Timer.TimerList;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.PixelFormat;

/**
 * The main class: extends your project from this<br>
 * This is an abstract class that implements IDrawing and ILoopable interfaces<br>
 * you MUST override the methods:<br>
 * public void loop();<br>
 * public void render();<br>
 * public void creation();<br>
 * <br>
 * <br>
 * <b>Example:</b> <br>
 * <font color=GREE>// import the class Core</font><br>
 * <b>import</b> org.easyway.system.Core;<br>
 * <br>
 * <b>public</b> Game <b>extends</b> Core {<br>
 * <br>
 * <b>public</b> Game() {<br>
 * <font color=GREEN>// width = 1024, height = 768, fullscreen = true</font><br>
 * <b>super</b>( 1024, 768, true );<br> }<br>
 * <br>
 * <b>public void</b> creation() {<br>
 * <font color=GREEN>// insert here your initialization code</font><br> }<br>
 * <br>
 * <b>public void</b> loop() {<br>
 * <font color=GREEN>// insert here your data-loop code</font><br> }<br>
 * <br>
 * <b>public void</b> render() {<br>
 * <font color=GREEN>// insert here your (opengl) rendering code</font><br>
 * <font color=GREEN>// this method is usually not used</font><br> }<br>
 * <br>
 * <b>public static</b> main( String args[] ) {<br>
 * <font color=GREEN>// creates a new instance of the Game</font><br>
 * <b>new</b> Game();<br>
 * <font color=GREEN>// Caution: the initialization code MUST written in
 * creation Method</font><br>
 * <font color=GREEN>// and NOT in the 'main' method</font><br> }<br>
 * <br> }<br>
 *
 * @author Daniele Paggi,Do$$e
 * @version 1
 */
public abstract class Core extends BaseObject {

    /**
     * the game engine version
     */
    public static final String VERSION = "0.4.5.0";
    private static String savingOperationPath;
    private static String savingOperationFile;
    public static boolean noOggSound = false;
    private static String title = "EasyWay Game Engine " + VERSION + "!";
    /** Thread that manages the game */
    private Sincro sincro;
    /** width, height and bit per pixel */
    private int width, height, bpp;
    /** used display mode */
    private DisplayMode displayMode;
    /** is fullscreen or windowed? */
    private boolean fullscreen;
    /** indicates if the game is in pause */
    private boolean paused;
    /** the contsiner that contains the OpenGL Window */
    protected Canvas container;
    /** number of cpu */
    private int numberCpu;
    /**
     * the current room state of the game
     */
    private Room currentRoom;
    /**
     * indicates if the game engine will automatically loads the LWJGL libs
     */
    public static boolean autoInitLibs = true;
    /**
     * indicates if the game engine is using the Lwjgl for the input
     */
    public static boolean useLwjgl;
    /**
     * indcates the number of drawing layers
     */
    public final static int NUMBER_OF_LAYERS = 16;
    /**
     * the kind of operative system<br/>
     * 0: windows, 1: linux, 2: mac, -1: unknow/uninitialized
     */
    static private int os = -1;
    /** indicates if the Game Engine is already initialized */
    boolean initialized = false;
    /** OpenGL version on current machine */
    static private String glVersion;
    /** OpenGL major version */
    static private int glMajorVersion = -1;
    /** OpenGL minor version */
    static private int glMinorVersion = -1;
    /** shader used by the Game Engine on the final draw */
    private Shader shader;
    /** start time of game engine*/
    private long startTime;
    /** frame buffer object texture */
    private TextureFBO fboTexture = null;
    /** the default instance */
    private static Core thisInstance;
    /** indicates if should use the openAL or not */
    private boolean useOpenAL = false;

    /** returns the Current game engine's core */
    public static Core getInstance() {
        return thisInstance;
    }
    private boolean fBOSupported;

    /**
     * creates a new instance of Core<br/>
     * the game's window'll have the dimension: 800x600 and will not be in fullscreen mode.
     */
    public Core() {
        this(800, 600, false);
    }

    /**
     * create a new instance of core
     *
     * @param width
     *            the width of screen
     * @param height
     *            the height of screen
     * @param fullscreen
     *            fullscreen or window mode?
     */
    public Core(int width, int height) {
        this(width, height, 32, false);
    }

    /**
     * create a new instance of core
     *
     * @param width
     *            the width of screen
     * @param height
     *            the height of screen
     * @param fullscreen
     *            fullscreen or window mode?
     */
    public Core(int width, int height, boolean fullscreen) {
        this(width, height, 32, fullscreen);
    }

    /**
     * create a new instance of core
     *
     * @param width
     *            the width of screen
     * @param height
     *            the height of screen
     * @param bpp
     *            bit per pixel
     */
    public Core(int width, int height, int bpp, boolean fullscreen) {
        thisInstance = this;
        useLwjgl = true;
        if (autoInitLibs) {
            setLibrary();
        }
        // StaticRef.core = this;
        sincro = new Sincro();
        this.width = width;
        this.height = height;
        this.bpp = bpp;
        this.fullscreen = fullscreen;
        initCoreData();
        start();
    }

    public Core(DisplayMode displayMode, boolean fullscreen) {
        thisInstance = this;
        useLwjgl = true;
        if (autoInitLibs) {
            setLibrary();
        }
        // StaticRef.core = this;
        sincro = new Sincro();
        this.displayMode = displayMode;
        this.width = displayMode.getWidth();
        this.height = displayMode.getHeight();
        this.bpp = displayMode.getBitsPerPixel();
        this.fullscreen = fullscreen;
        initCoreData();
        start();
    }

    /**
     * create a new instance of core
     *
     * @param width
     *            the width of screen
     * @param height
     *            the height of screen
     * @param bpp
     *            bit per pixel
     * @param container
     *            the canvas in witch the game engine will be displayed
     */
    public Core(int width, int height, int bpp, Canvas container) {
        thisInstance = this;
        useLwjgl = false;
        if (autoInitLibs) {
            setLibrary();
        }
        // StaticRef.core = this;
        this.container = container;
        sincro = new Sincro();
        this.width = width;
        this.height = height;
        this.bpp = bpp;
        this.fullscreen = false;
        initCoreData();
        start();
    }

    /**
     * starts the game engine
     */
    private void start() {
        sincro.start();
    }

    private void initCoreData() {
        StaticRef.initialized = false;
        // StaticRef.loopList = new LoopList();
        StaticRef.finalLoopList = new FinalLoopList();
        // TODO: for defaults we adds 7 drawing list
        // we can customize this decision?
        // StaticRef.layers = new DrawingLayaredList[NUMBER_OF_LAYERS];
        // for (int i = 0; i < NUMBER_OF_LAYERS; ++i) {
        //    StaticRef.layers[i] = new DrawingLayaredList();
        // }
        StaticRef.collisionableLoopList = new CollisionableLoopList();
        //StaticRef.animoList = new AnimoList();
        // StaticRef.timerList = new TimerList();
        StaticRef.textures = new ArrayList<ITexture>(50);
        // StaticRef.camera = new Camera(0, 0, width, height);
        // StaticRef.coreCollision = new CoreCollision();
        numberCpu = Runtime.getRuntime().availableProcessors();
        LoopList.init();
        StaticRef.initialized = true;
        startTime = System.currentTimeMillis();
    }

    /** initializes the Game Engine */
    void init() {
        System.out.println("\n\n---------------------------------------"
                + "--------------------------------------");
        System.out.println("EasyWay Game Engine V. " + VERSION + "\nhttp://easyway.sf.net");
        System.out.println("---------------------------------------"
                + "--------------------------------------\n\n");
        //OggPlayer.init();
        if (!AL.isCreated() && useOpenAL) {
            try {
                AL.create();
            } catch (LWJGLException e) {
                Utility.error("can't create AL", "OggPlayer()", e);
                noOggSound = true;
            }
        }
        //if (container == null) {
        createWindow();
        StaticRef.renderThread = Thread.currentThread();
        GameState.getCurrentGEState();

        try {
            Keyboard.create();
            org.easyway.input.Keyboard.create();
            org.lwjgl.input.Mouse.create();
            Camera.getCurrentCamera().initGL(width, height);
            // org.lwjgl.input.Mouse.
            // Mouse.enableBuffer(); // old
        } catch (LWJGLException e) {
            Utility.error("Error creating the Keyboard or the Mouse",
                    "Core.init()", e);
        }
        if (container != null) {
            try {
                Display.setParent(container);
                getCamera().setDrawingArea(container.getWidth(), container.getHeight());
            } catch (LWJGLException ex) {
                Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        getOpenGLVersions();
        Shader.getDefaultShader();
        ShaderEffect.getDefaultShaderEffect();
        fBOSupported = GLContext.getCapabilities().GL_EXT_framebuffer_object;
        CollisionUtils.init();
        getCamera().setBackgroundColor(100, 149, 237);
        MathUtils.init();
        initialized = true;
    }

    public static void getOpenGLVersions() {
        if (glVersion != null) {
            System.out.println("OpenGL version was forced or already detected...");
            // resolution forced!
            return;
        }
        glVersion = GL11.glGetString(GL11.GL_VERSION);
        System.out.println("Stringver: " + glVersion);
        glMajorVersion = glVersion.charAt(0) - '0';
        System.out.println("Majorver: " + glMajorVersion);
        glMinorVersion = glVersion.charAt(2) - '0';
        System.out.println("Minorver: " + glMinorVersion);
    }

    // --------------------------------------------------------------------------------
    /** creates a new window */
    private void createWindow() {
        try {
            if (displayMode == null) {
                if (findDisplayMode() == null) {
                    Utility.error(
                            "Can't find a correct OpenGL window configuration: try to change the game's resolution\n",
                            "Core.createWindow");
                    if (bpp == 32) {
                        Utility.error(
                                "You have selected 32bpp: I'll try to downgrade to 24bpp.. please wait",
                                "Core.createWindow");

                        bpp = 24;
                    }
                } else {
                    Utility.error(
                            "You have selected a resolution that isn't supported: I'll try to change the resolution.. please wait",
                            "Core.createWindow");
                    if (findDisplayMode() == null) {
                        if (findMagDisplayMode() == null) {
                            System.exit(-1); // error
                        }
                    }
                }
            } else {
                Display.setDisplayMode(displayMode);
            }
            System.out.println("Creating window..");
            // Display.create();
            Display.create(new PixelFormat(8, 8, 8));

            System.out.println("..window created!");
            /*ImageData img = new ImageData("org/easyway/system/EW_icon.ico");

            Display.setIcon(new ByteBuffer[]{img.getData()});*/
            Display.setFullscreen(fullscreen);

            // Game Engine
            Display.setVSyncEnabled(true);
            Display.makeCurrent();
            Display.setTitle(title);// default
            // Display.setLocation(0, 0);   //moves the window to the default coordinates (why on the top-left of scren?)
            // title
        } catch (LWJGLException e) {
            JOptionPane.showMessageDialog(new JOptionPane(), "Sorry, your video card or your video driver is incompatible with accelerated OpenGL graphics");
            Utility.error("Can't init OpenGL: update your graphics drivers",
                    "Core.createWindow", e);
            System.exit(-1);
        }
    }

    /** finds and 'set' the display mode selected */
    private DisplayMode findMagDisplayMode() {
        DisplayMode[] modes;
        try {
            modes = Display.getAvailableDisplayModes();
        } catch (LWJGLException e) {
            e.printStackTrace();
            return null;
        }
        for (int i = 0; i < modes.length; i++) {
            if (modes[i].getWidth() >= width && modes[i].getHeight() >= height && modes[i].getBitsPerPixel() >= bpp && modes[i].getFrequency() <= 100) { // XXX: getFrequency <=
                // 85 it's correct?
                try {
                    Display.setDisplayMode(modes[i]);
                } catch (LWJGLException e) {
                    e.printStackTrace();
                }
                return modes[i];
            }
        }
        return null;
    }

    /** finds and 'set' the display mode selected */
    private DisplayMode findDisplayMode() {
        DisplayMode[] modes;
        try {
            modes = Display.getAvailableDisplayModes();
        } catch (LWJGLException e) {
            e.printStackTrace();
            return null;
        }
        for (int i = 0; i < modes.length; i++) {
            if (modes[i].getWidth() == width && modes[i].getHeight() == height && modes[i].getBitsPerPixel() >= bpp && modes[i].getFrequency() <= 100) { // XXX: getFrequency <=
                // 85 it's correct?
                try {
                    Display.setDisplayMode(modes[i]);
                } catch (LWJGLException e) {
                    e.printStackTrace();
                }
                return modes[i];
            }
        }
        return null;
    }

    // --------------------------------------------------------------------------------
    /** cleanup the Engine */
    protected void cleanup() {
        Keyboard.destroy();
        org.lwjgl.input.Mouse.destroy();
        if (useLwjgl) {
            Display.destroy();
        }
        System.out.println("Game Engine Cleaned Up");
        setTimeOut(0);
        System.exit(0);
    }

    /**
     * sets the window's title
     *
     * @param title
     *            the new title of game window
     */
    public void setTitle(String title) {
        Display.setTitle(Core.title = title);
    }

    public String getTitle() {
        return title;
    }
    public long getLoopTime(){
        return loopT/1000000;
    }
    public long getRenderTime(){
        return sincro.renderT/1000000;
    }
    long loopT;
    /** data loop */
    protected void coreLoop() {
        loopT=System.nanoTime();
        if (Keyboard.isCreated()) {
            org.easyway.input.Keyboard.loop();
            Keyboard.poll();
            while (Keyboard.next()) {
                if (Keyboard.getEventKey() != Keyboard.KEY_NONE) {
                    org.easyway.input.Keyboard.sendEvent(Keyboard.getEventKey(), Keyboard.getEventKeyState());
                }
            }
        }
        if (org.lwjgl.input.Mouse.isCreated()) {
            org.lwjgl.input.Mouse.poll();
        }
        Mouse.loop();

        if (DebugManager.debug) {
            CommandList.loop();
            return;
        }

        // loop();
        if (currentRoom != null) {
            currentRoom.loop();
        }

        getCamera().setCollectingObjectsOnScreen(false);
        getCamera().clearCollectedObjectsOnScreen();

        final GameState currentState = GameState.getCurrentGEState();
        currentState.getTimerList().loop();
        currentState.getAnimoList().loop();
        currentState.getLoopList().loop();
        //StaticRef.loopList.size();

        // StaticRef.collisionableLoopList.resetCollisioni(); // old
        // checks collision

        currentState.getCoreCollision().loop(); // test collision
        StaticRef.collisionableLoopList.loop(); // calls "onCollision" methods
        currentState.getFinalLoopList().finalLoop();

        getCamera().setCollectingObjectsOnScreen(true);

        getCamera().center(); // center the camera if it's attracked
        getCamera().getObjectsOnScreen().removeAll();
        loopT=System.nanoTime()-loopT;
    }

    
    
    /** drawing loop */
    protected void coreRender() {
        OpenGLState.enableRendering();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glLoadIdentity();

        if (fboTexture != null) {
            fboTexture.startDrawing();
        }
        //synchronized (StaticRef.layers) {
        // StaticRef.drawingList.render();
        final DrawingLayaredList[] layers = GameState.getCurrentGEState().getLayers();
        for (int i = 0; i < layers.length; ++i) {
            layers[i].render();
        }
        GameState.getCurrentGEState().getMainGuiFather().render();
        //}
        // render();
        // if (currentRoom != null) {
        //     currentRoom.render();
        // }
        if (fboTexture != null) {
            fboTexture.endDrawing();
            ITexture texture = fboTexture.getTexture();
            if (shader != null) {
                shader.bind();
                // shader.update(this);
            }
            texture.bind();
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(texture.getXStart(), texture.getYStart());
            GL11.glVertex2f(0, 0);
            GL11.glTexCoord2f(texture.getXEnd(), texture.getYStart());
            GL11.glVertex2f(getWidth(), 0);
            GL11.glTexCoord2f(texture.getXEnd(), texture.getYEnd());
            GL11.glVertex2f(getWidth(), getHeight());
            GL11.glTexCoord2f(texture.getXStart(), texture.getYEnd());
            GL11.glVertex2f(0, getHeight());
            GL11.glEnd();
            Shader.unBind();
        }
        OpenGLState.disableRendering();
    }

    /**
     * initializes the resources<br>
     * this method must be oveerided<br>
     */
    public abstract void creation();

    // --------------------------------------------------------------------------------
    // -------------------------------USER_METHODS-------------------------------------
    // --------------------------------------------------------------------------------
    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public Shader getShader() {
        return shader;
    }

    public boolean isUsePostProcessing() {
        return fboTexture != null;
    }

    public void usePostProcessing(boolean value) {
        if (fboTexture != null) {
            if (value) {
                return;
            }
            // value == false
            fboTexture.getTexture().destroy();
            fboTexture = null;
        } else if (value) {// fboTexture == null
            //if (TextureFBO.checkFBOSupport()) {
            fboTexture = new TextureFBO(new Texture(getWidth(), getHeight()));
            //}
        }

    }

    /**
     * return the screenshot texture of the screen
     * @return
     */
    public ITexture getScreenshot() {
        if (fboTexture != null) {
            return fboTexture.getTexture();
        }
        return null;
    }

    /**
     * returns the OpenGL version on the current machine
     * @return a string rappresenting the current OpenGL version
     */
    public static String getGLVersion() {
        if (glVersion == null) {
            getOpenGLVersions();
        }
        return glVersion;
    }

    public static int getGLMajorVersion() {
        if (glMajorVersion == -1) {
            getOpenGLVersions();
        }
        return glMajorVersion;
    }

    public static int getGLMinorVersion() {
        if (glMinorVersion == -1) {
            getOpenGLVersions();
        }
        return glMinorVersion;
    }

    public static void forceGlVersion(int major, int minor) {
        glMajorVersion = major;
        glMinorVersion = minor;
        glVersion = major + "." + minor + ".0";
    }

    public static int getGLIntVersion() {
        return getGLMajorVersion() * 10 + getGLMinorVersion();
    }

    /**
     * returns if can or not support the not power of two texture
     * @return true: support NPT texture
     */
    public static boolean supportNPTTexture() {
        return getGLMajorVersion() >= 2;
    }

    @Override
    public void destroy() {
        if (isDestroyed()) {
            return;
        }
        super.destroy();
        endGame();
    }

    /** close the game */
    public void endGame() {
        Sincro.loop = false;
    }

    /**
     * frozes the game<br>
     *
     * @see resumeGame()
     * @see isGamePaused()
     */
    public void pauseGame() {
        paused = true;
    }

    /**
     * sets the game to the normal status <br>
     *
     * @see pauseGame()
     * @see isGamePaused()
     */
    public void resumeGame() {
        paused = false;
    }

    /**
     * returns if the game is paused or not
     *
     * @see pauseGame()
     * @see resumeGame()
     */
    public boolean isGamePaused() {
        return paused;
    }

    /** switch from FullScreen mode to window mode and viceversa */
    public void switchMode() {
        fullscreen = !fullscreen;
        try {
            Display.setFullscreen(fullscreen);
        } catch (LWJGLException e) {
            Utility.error(" fullscreen value: " + fullscreen, e);
        }
    }

    /**
     * enable or disable the VSync<br>
     * disabling the VSync you'll get a speed up in your game
     *
     * @param value
     */
    public void setVSync(boolean value) {
        Display.setVSyncEnabled(value);
    }

    /**
     * sets the fps of game<br>
     * the default value is 60<br>
     *
     * @param fps
     *            frame per second
     * @see getFps()
     */
    public void setFps(int fps) {
        Sincro.fps = fps;
    }

    /**
     * gets the fps of game
     *
     * @return the current frame per second that the game has done.
     * @see setFps()
     */
    public int getFps() {
        return sincro.getFps();
    }

    public double getFpsHP() {
        return sincro.getFpsHP();
    }

    public static void setSpeedCompensation(boolean value) {
        Sincro.speedCompensation = value;
    }

    public void initFpsCounter() {
        sincro.initFpsCounter();
    }

    public float getSpeedMultiplier() {
        return 60f / (float) getFpsHP();
    }

    /**
     * sets the time of timeOutOperation.<br>
     * if time is less than 1000, the timeout will be disabled.<br>
     * I should you sets this to 0 when you are going to make your final
     * distribution<br>
     *
     * @param time
     *            time in ms
     */
    public void setTimeOut(int time) {
        //sincro.timeOut = time;
        sincro.setTimeOut(time);
    }

    /**
     * returns if the game is in fullscreen mode or not
     *
     * @return is the game in fullscreen mode?
     */
    public boolean isFullscreen() {
        return fullscreen;
    }

    /**
     * returns number of cpu
     * @return the number of cpu
     */
    public int getNumberCpu() {
        return 1;//numberCpu;
    }

    /**
     * returns the time of timeOutOperation.<br/>
     * if time is less than 1000, the timeout will be disabled.<br>
     * I should you sets this to 0 when you are going to make your final
     * distribution<br>
     *
     * @return the time in ms
     */
    public int getTimeOut() {
        return sincro.timeOut;
    }

    /**
     * returns the game size
     *
     * @return game window's Width
     * @see getHeight()
     * @see getBpp()
     */
    public int getWidth() {
        return container == null ? width : container.getWidth();
    }

    /**
     * returns the game size
     *
     * @return game window's Height
     * @see getWidth()
     * @see getBpp()
     */
    public int getHeight() {
        return container == null ? height : container.getHeight();
    }

    /**
     * returns the game bpp<br>
     *
     * @return game bit per pixel
     * @see getWidth()
     * @see getHeight()
     */
    public int getBpp() {
        return bpp;
    }

    /**
     * returns the game's camera
     * @return the game's camera
     */
    public Camera getCamera() {
        return GameState.getCurrentGEState().getCamera();
    }

    /**
     * sets the game's camera
     * @param camera the new camera to set
     */
    // public void setCamera(Camera camera) {
    //     camera.rebind();
    // }
    /**
     * sets if enable or not the kill message<br>
     * if the kill message is enabled the onCollision method will be not
     * executed.
     *
     * @param enable
     *            indicates if the kill message is to enable or not
     */
    public void setKillMessage(boolean enable) { // 0.1.6
        BaseObject.KILL = enable;
    }

    /**
     * changes the current game room
     * @param level the new game room
     */
    public void setRoom(Room level) {
        if (level != null) {
            level.restoreState();
        }
        this.currentRoom = level;
    }

    /**
     * returns the current game room
     * @return the current game room
     */
    public Room getRoom() {
        return currentRoom;
    }

    /**
     * returns the current game state
     * @return
     */
    public GameState getGameState() {
        return GameState.getCurrentGEState();
    }

    /**
     * returns if the game engine is initialized or not
     * @return if the game engine is initialized or not
     */
    public boolean isInitialized() {
        return initialized;
    }

    public static String getSavingOperationFile() {
        return savingOperationFile;
    }

    public static String getSavingOperationPath() {
        return savingOperationPath;
    }

    /**
     * saves current game state - not completed at 100%
     *
     * @see load(String)
     */
    public synchronized static void save(String file) {
        System.out.println();
        System.out.println();
        System.out.println("GAME SAVING...as: " + file);

        int lastIndex = file.lastIndexOf(File.separatorChar) + 1;
        savingOperationPath =
                file.substring(0, lastIndex);
        savingOperationFile =
                file.substring(lastIndex);
        try {

            // fix the path
            {
                if (file.startsWith("/")) {
                    file = file.substring(1);
                }
                int index;
                while ((index = file.indexOf("\\")) != -1) {
                    file = file.substring(0, index) + '/' + file.substring(index + 1);
                }
            }


            FileOutputStream fout = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            // out.writeObject(StaticRef.layers);
            // out.writeObject(StaticRef.loopList);
            out.writeObject(StaticRef.finalLoopList);
            // out.writeObject(StaticRef.animoList);
            // out.writeObject(StaticRef.timerList);
            // out.writeObject(StaticRef.coreCollision);
            out.writeObject(TileMap.tileMapList);
            out.writeObject(QuadTree.getDefaultInstance());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("..End Saving");
        }
        System.out.println();
        System.out.println();
    }

    public synchronized static void loadIncremental(InputStream is) {
        if (is == null) {
            throw new RuntimeException("InputStream is null");
        }
        try {
            // FileInputStream fin = new FileInputStream(is);
            ObjectInputStream ois = new ObjectInputStream(is);

            DrawingLayaredList temp[] = (DrawingLayaredList[]) ois.readObject();
            // if (temp.length > StaticRef.layers.length) {
            //     DrawingLayaredList switcher[] = temp;
            //     temp = StaticRef.layers;
            //     StaticRef.layers = switcher;
            // }

            // for (int i = 0; i < temp.length; ++i) {
            //     StaticRef.layers[i].addElementsFrom(temp[i]);
            // }
            // StaticRef.loopList.addElementsFrom((LoopList) ois.readObject());
            StaticRef.finalLoopList.addElementsFrom((FinalLoopList) ois.readObject());

            /* -- unchecked warning -- */
            // StaticRef.animoList.addElementsFrom((AnimoList) ois.readObject());
            TimerList timerList = (TimerList) ois.readObject();
            // for (int i = timerList.size() - 1; i >= 0; --i) {
            //     StaticRef.timerList.add(timerList.get(i));
            // }
            // StaticRef.coreCollision.loadIncremental((CoreCollision) ois.readObject());
            TileMap.tileMapList.addAll((ArrayList<TileMap>) ois.readObject());
            QuadTree.setDefaultInstance((QuadTree) ois.readObject());
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("..End Loading");
        }

        System.out.println();
        System.out.println();
    }

    public synchronized static void loadIncremental(String file) {
        loadIncremental(file, true);
    }

    /**
     * loads current game state
     *
     * @see save(String)
     */
// @SuppressWarnings("unchecked")//
    public synchronized static void loadIncremental(String file, boolean local) {
        System.out.println();
        System.out.println();
        System.out.println("GAME LOADING (incremental mode)...");

        int lastIndex = file.lastIndexOf(File.separatorChar) + 1;
        savingOperationPath =
                file.substring(0, lastIndex);
        savingOperationFile =
                file.substring(lastIndex);
        InputStream is = null;
        if (local) {
            if (file.startsWith("/")) {
                file = file.substring(1);
            }
            int index;
            while ((index = file.indexOf("\\")) != -1) {
                file = file.substring(0, index) + '/' + file.substring(index + 1);
            }
            try {
                is = Thread.currentThread().getContextClassLoader().getResource(file).openStream();
            } catch (Exception e) {
                Utility.error("file " + file + " was not found!", e);
                return;
            }
        } else {

            if (file.startsWith("/")) {
                file = file.substring(1);
            }
            int index;
            // path = path.replaceAll("\\", "/");
            while ((index = file.indexOf("\\")) != -1) {
                file = file.substring(0, index) + '/' + file.substring(index + 1);
            }
            try {
                File rfile = new File(file);
                is =
                        new FileInputStream(rfile);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        loadIncremental(is);
    }

    public synchronized static void load(String file, boolean local) {
        // remakes all
        // StaticRef.layers = new DrawingLayaredList[NUMBER_OF_LAYERS];
        // for (int i = 0; i < NUMBER_OF_LAYERS; ++i) {
        //    StaticRef.layers[i] = new DrawingLayaredList();
        // }
        // StaticRef.loopList = new LoopList(false);
        StaticRef.finalLoopList = new FinalLoopList(false);
        // StaticRef.animoList = new AnimoList();
        // StaticRef.timerList = new TimerList();
        QuadTree.setDefaultInstance(new QuadTree(0, 0, thisInstance.getWidth() * 2, thisInstance.getHeight() * 2));
        TileMap.tileMapList.clear();
        // StaticRef.coreCollision = new CoreCollision();
        Texture.clearTextures();
        loadIncremental(file, local);
    }

    /**
     * loads current game state
     *
     * @see save(String)
     */
    @SuppressWarnings("unchecked")
    public synchronized static void load(String file) {
        load(file, true);
    }

    /** returns the elaspedTime from the last loop */
    public long getElaspedTime() {
        return sincro.getElaspedTime();
    }

    public long getElaspedTimeMilli() {
        return sincro.getElaspedTime() / 1000000;
    }

    public long getTotalElaspedTime() {
        return System.currentTimeMillis() - startTime;
    }

    @Override
    public String toString() {
        return "Core <width: " + width + " Height: " + height + " bpp: " + bpp + " initialized: " + initialized + " >";
    }

    /**
     *
     * @return 0: windows, 1: linux, 2: mac
     */
    public static int getOS() {
        if (os == -1) {
            String osName = System.getProperty("os.name");
            if (osName.startsWith("Windows")) {
                os = 0;
            } else if (osName.startsWith("Linux")) {
                os = 1;
            } else {
                throw new LinkageError("Unknown platform: " + osName);
            }
        }
        return os;
    }

    public static void setLibrary() {
        String dir;
        String osName = System.getProperty("os.name");
        String path = "";
        ArrayList<String> libs = new ArrayList<String>(10);
        System.out.println("Initializing EasyWay Library...");
        try {
            // dir = Core.class.getClassLoader().getResource("").getPath();
            dir = Core.class.getResource("Core.class").getPath();
            System.out.println(" |  Starting_path: " + dir);
            dir = dir.substring(0, dir.indexOf("org"));
            System.out.println(" |  Jar_path: " + dir);
            // dir = dir.substring(0, dir.length() - 4);
        } catch (NullPointerException e) {
            // if the game is inside an jar file
            // TODO check if path correct
            // dir = Core.class.getResource("Core.class").getPath();
            // dir = dir.substring(0, dir.indexOf("!"));
            // dir = dir.substring(0, dir.lastIndexOf("/") + 1);
            dir = Core.class.getResource("Core.class").getPath();
            dir = dir.substring(0, dir.indexOf("org"));
            System.out.println(" |  NullPointerException captured in Core.setLibrary(); dir: " + dir);
        }


        if (dir.indexOf("file://") != -1) {
            dir = dir.substring(7);
        } else if (dir.indexOf("file:/") != -1) {
            dir = dir.substring(6);
        }
        System.out.println(" |  fixed_path: " + dir);

        if (osName.startsWith("Windows")) {
            path = "nativelibrary/windows";
            libs.add("OpenAL32.dll");
            libs.add("OpenAL64.dll");
            libs.add("jinput-dx8.dll");
            libs.add("jinput-dx8_64.dll");
            libs.add("jinput-raw.dll");
            libs.add("jinput-raw_64.dll");
            libs.add("lwjgl64.dll");
            libs.add("lwjgl.dll");

        } else if (osName.startsWith("Linux") || osName.startsWith("FreeBSD")) {
            path = "nativelibrary/linux";
            libs.add("libjinput-linux.so");
            libs.add("libjinput-linux64.so");
            libs.add("liblwjgl.so");
            libs.add("liblwjgl64.so");
            libs.add("libopenal.so");
            libs.add("libopenal64.so");
        } else {
            path = "nativelibrary/macosx";
            libs.add("libjinput-osx.jnlib");
            libs.add("liblwjgl.jnlib");
            libs.add("openal.jnlib");
        }
        dir = dir.replace("%20", " ");
        try {
            System.setProperty("org.lwjgl.librarypath", dir + path);
            System.setProperty("java.library.path", dir + path);
            Utility.error("Using driver location: " + dir + path,
                    "Core.setLibrary()");
            // System.loadLibrary(dir + path);
            for (String s : new File(dir + path).list()) {
                System.out.println("    |  found: " + s);
            }
        } catch (NullPointerException e) {
            Utility.error("Jar detected", "Core.setLibrary()");
            File file = null;
            File nativeFile = new File(path);
            nativeFile.mkdirs();

            for (String myLibName : libs) {
                FileOutputStream fileOutputStream = null;
                InputStream inputStream = Core.class.getClassLoader().getResourceAsStream(path + "/" + myLibName);
                try {

                    file = new File(path + "/" + myLibName);
                    System.out.println(" |  Creating: " + myLibName + " result: " + file);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    fileOutputStream = new FileOutputStream(file);
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (Exception ioe) {
                    ioe.printStackTrace();
                } finally {
                    try {
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
            assert file != null;
            System.out.println(" |  File_: " + file);
            System.out.println(" |  File_abs: " + file.getAbsoluteFile());
            System.out.println(" |  File_absParent: " + file.getAbsoluteFile().getParent());
            String workingDir = file.getAbsoluteFile().getParent();
            System.out.println(" |  Working Directory: " + workingDir);
            for (String s : new File(workingDir).list()) {
                System.out.println("    |  found: " + s);
            }
            System.setProperty("org.lwjgl.librarypath", workingDir);
            System.setProperty("java.library.path", workingDir);

        }
    }

    public Canvas getContainer() {
        return container;
    }

    /** 
     * This method executes a linear interpolation between 2 floats<br/>
     * <b>Use: MathUrils.lerp instead of this!</b>
     */
    @Deprecated
    public static float lerp(float a, float b, float value) {
        return MathUtils.lerp(a, b, value);
    }

    public boolean isFBOSupported() {
        return fBOSupported;
    }
}

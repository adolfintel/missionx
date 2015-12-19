/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.system.state;

import org.easyway.collisions.CoreCollision;
import org.easyway.gui.base.GuiContainer;
import org.easyway.gui.base.IContainer;
import org.easyway.gui.base.MainGuiFather;
import org.easyway.input.Mouse;
import org.easyway.input.MouseState;
import org.easyway.lists.DrawingLayaredList;
import org.easyway.lists.FinalLoopList;
import org.easyway.lists.GameList;
import org.easyway.lists.LoopList;
import org.easyway.objects.Camera;
import org.easyway.objects.animo.AnimoList;
import org.easyway.system.Core;
import org.easyway.utils.Timer.TimerList;

/**
 *
 * @author Daniele Paggi
 */
public class GameState {

    static GameState gameEngineGameState = new GameState(true);
    Camera camera;
    LoopList loopList;
    FinalLoopList finalLoopList;
    DrawingLayaredList layers[];
    CoreCollision coreCollision;
    AnimoList animoList;
    TimerList timerList;
    // GameList<IContainer> guiChildren;
    // GameList<IPlain2D> objectsOnScreen;
    MouseState mouseState;
    MainGuiFather mainGuiFather;

    private GameState(boolean fromEngine) {
        if (fromEngine) {
            gameEngineGameState = this;
        }

        loopList = new LoopList(false);
        timerList = new TimerList();
        finalLoopList = new FinalLoopList(false);
        layers = new DrawingLayaredList[Core.NUMBER_OF_LAYERS];
        for (int i = 0; i < Core.NUMBER_OF_LAYERS; ++i) {
            layers[i] = new DrawingLayaredList();
        }
        animoList = new AnimoList();
        coreCollision = new CoreCollision();
        camera = new Camera(0, 0, Core.getInstance().getWidth(), Core.getInstance().getHeight());
        // Camera.getMainFather().children = guiChildren = new GameList<IContainer>();
        // objectsOnScreen = camera.getObjectsOnScreen();
        Mouse.resetClickableObjects();
        mainGuiFather = new MainGuiFather();
        mouseState = Mouse.getMouseState();
    }

    public GameState() {
        this(false);
    }

    public void setThisState() {
        System.out.println("Setted state");
        /*StaticRef.animoList = animoList;
        StaticRef.timerList = timerList;
        StaticRef.layers = layers;
        StaticRef.loopList = loopList;
        StaticRef.finalLoopList = finalLoopList;
        StaticRef.coreCollision = coreCollision;
        StaticRef.setCamera(camera);
        Camera.getMainFather().children = guiChildren;
        StaticRef.getCamera().setObjectsOnScreen(objectsOnScreen);
        Mouse.setMouseState(mouseState);*/

        gameEngineGameState = this;
        camera.rebind();
        // Camera.getMainFather().children = guiChildren;
        // Camera.setObjectsOnScreen(objectsOnScreen);
        Mouse.setMouseState(mouseState);
    }

    public static GameState getCurrentGEState() {
        return gameEngineGameState;
    }

    public Camera getCamera() {
        return camera;
    }

    public AnimoList getAnimoList() {
        return animoList;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
        // Core.getInstance().setCamera(camera);
        camera.rebind();
    }

    public CoreCollision getCoreCollision() {
        return coreCollision;
    }

    public void setCoreCollision(CoreCollision coreCollision) {
        this.coreCollision = coreCollision;
    }

    public FinalLoopList getFinalLoopList() {
        return finalLoopList;
    }

    public void setFinalLoopList(FinalLoopList finalLoopList) {
        this.finalLoopList = finalLoopList;
    }

    public DrawingLayaredList[] getLayers() {
        return layers;
    }

    public void setLayers(DrawingLayaredList[] layers) {
        this.layers = layers;
    }

    public LoopList getLoopList() {
        return loopList;
    }

    public void setLoopList(LoopList loopList) {
        this.loopList = loopList;
    }

    public TimerList getTimerList() {
        return timerList;
    }

    public void setTimerList(TimerList timerList) {
        this.timerList = timerList;
    }

    public MainGuiFather getMainGuiFather() {
        return mainGuiFather;
    }

    /*    public static void setNewState() {
    StaticRef.animoList = new AnimoList();
    StaticRef.timerList = new TimerList();
    StaticRef.layers = new DrawingLayaredList[Core.NUMBER_OF_LAYERS];
    for (int i = 0; i < Core.NUMBER_OF_LAYERS; ++i) {
    StaticRef.layers[i] = new DrawingLayaredList();
    }
    StaticRef.loopList = new LoopList(false);
    StaticRef.finalLoopList = new FinalLoopList(false);
    StaticRef.coreCollision = new CoreCollision();
    Mouse.resetClickableObjects();
    StaticRef.setCamera(new Camera(0, 0, Core.getInstance().getWidth(), Core.getInstance().getHeight()));
    Camera.getMainFather().children = new GameList<IContainer>();
    StaticRef.getCamera().clearCollectedObjectsOnScreen();
    }*/
}

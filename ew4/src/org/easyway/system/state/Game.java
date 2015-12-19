package org.easyway.system.state;

import java.awt.Canvas;
import org.easyway.interfaces.base.ITexture;
import org.easyway.objects.Camera;
import org.easyway.shader.Shader;
import org.easyway.system.Core;
import org.lwjgl.opengl.DisplayMode;

/**
 *
 * @author Daniele Paggi
 */
public abstract class Game extends Room {
    
    public Game() {
        this(800, 600, 32, false);
    }

    public Game(int width, int height) {
        this(width, height, 32, false);
    }

    public Game(DisplayMode displayMode, boolean fullscreen) {
        new Core(displayMode, fullscreen) {

            @Override
            public void creation() {
                setRoom(Game.this);
            }
        };
    }

    public Game(int width, int height, boolean fullscreen) {
        this(width, height, 32, fullscreen);
    }

    public Game(int width, int height, int bpp, Canvas container) {

        new Core(width, height, bpp, container) {

            @Override
            public void creation() {
                setRoom(Game.this);
            }
        };
    }

    public Game(int width, int height, int bpp, boolean fullscreen) {
        new Core(width, height, bpp, fullscreen) {

            @Override
            public void creation() {
                setRoom(Game.this);
            }
        };
    }

    @Override
    public abstract void creation();

    @Override
    public abstract void loop();

    public void setTitle(String title) {
        Core.getInstance().setTitle(title);
    }

    public String getTitle() {
        return Core.getInstance().getTitle();
    }

    public void endGame() {
        Core.getInstance().endGame();
    }

    public void pauseGame() {
        Core.getInstance().pauseGame();
    }

    public void resumeGame() {
        Core.getInstance().resumeGame();
    }

    public boolean isGamePaused() {
        return Core.getInstance().isGamePaused();
    }

    public Camera getCamera() {
        return Core.getInstance().getCamera();
    }

    public int getWidth() {
        return Core.getInstance().getWidth();
    }

    public int getHeight() {
        return Core.getInstance().getHeight();
    }

    public void setTimeOut(int timer) {
        Core.getInstance().setTimeOut(timer);
    }

    public int getTimeOut() {
        return Core.getInstance().getTimeOut();
    }

    public void usePostProcessing(boolean b) {
        Core.getInstance().usePostProcessing(b);
    }

    public boolean isUsePostProcessing() {
        return Core.getInstance().isUsePostProcessing();
    }

    public void setShader(Shader shader) {
        Core.getInstance().setShader(shader);
    }

    public Shader getShader() {
        return Core.getInstance().getShader();
    }

    public void setVSync(boolean value) {
        Core.getInstance().setVSync(value);
    }

    public void switchMode() {
        Core.getInstance().switchMode();
    }

    public boolean isFullscreen() {
        return Core.getInstance().isFullscreen();
    }

    public void setFullscreen(boolean value) {
        if (value) {
            if (!isFullscreen()) {
                switchMode();
            }
        } else if (isFullscreen()) { // !value
            assert value == false;
            switchMode();
        }
    }

    public ITexture getScreenshot() {
        return Core.getInstance().getScreenshot();
    }
}

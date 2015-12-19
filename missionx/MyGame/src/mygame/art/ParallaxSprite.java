package mygame.art;

import mygame.CommonVar;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;
import org.lwjgl.opengl.Display;

public class ParallaxSprite extends Sprite implements ILoopable {   //THIS CLASS IS STILL INCOMPLETE AND IT'S PROBABLY FULL OF BUGS! BE CAREFUL!

    private float distance;
    private float lastCamX = Display.getDisplayMode().getWidth() / 2, lastCamY = Display.getDisplayMode().getHeight() / 2;
    private float camSpeedX = 0, camSpeedY = 0;
    private boolean useScale = true;

    public ParallaxSprite(int x, int y, String path, int idLayer, float distance, boolean useScale, int centerX, int centerY) {
        super(x, y, Texture.getTexture(path), idLayer);
        if (distance > 1) {
            System.out.println("ParallaxSprite: Maximum distance is 1!");
            distance = 1;
        }
        this.distance = distance;
        this.useScale = useScale;
        lastCamX = centerX;
        lastCamY = centerY;
        setIdLayer(idLayer); //precauzione
    }

    @Override
    public final void loop() {
        if (!Core.getInstance().isGamePaused()) {
            if(CommonVar.destroyAll) destroy();
            customCode();
            if (useScale) {
                super.setScaleX((1 - distance) * 2);
                super.setScaleY((1 - distance) * 2);
            } else {
                super.setScaleX(1);
                super.setScaleY(1);
            }
            //if(simpleMode) if(getXOnScreen()>Display.getDisplayMode().getWidth()||getXOnScreen()+getWidth()<0||getYOnScreen()>Display.getDisplayMode().getHeight()||getYOnScreen()+getWidth()<0) return;
            camSpeedX = StaticRef.getCamera().x + Display.getDisplayMode().getWidth() / 2 - lastCamX;
            lastCamX = StaticRef.getCamera().x + Display.getDisplayMode().getWidth() / 2;
            camSpeedY = StaticRef.getCamera().y + Display.getDisplayMode().getHeight() / 2 - lastCamY;
            lastCamY = StaticRef.getCamera().y + Display.getDisplayMode().getHeight() / 2;
            move(camSpeedX, camSpeedY);
            //setLayer(65536 - (int) (distance * 65536));
        }
    }

    @Override
    public final void move(float incx, float incy) {  //moves the sprite using parallax effect
        super.move(Core.lerp(incx, -incx, distance), Core.lerp(incy, -incy, distance));
    }

    public final float getDistance() {
        return distance;
    }

    public final void setDistance(float distance) {
        this.distance = distance;
    }

    public void customCode() {
    }

    public final boolean isUseScale() {
        return useScale;
    }

    public final void setUseScale(boolean useScale) {
        this.useScale = useScale;
    }

    /*public final void setScaleX(float scale) {
        System.out.println("This method cannot be used applied to a ParallaxSprite (setScaleX)");
    }

    public final void setScaleY(float scale) {
        System.out.println("This method cannot be used applied to a ParallaxSprite (setScaleY)");
    }

    public final void setWidth(int width) {
        System.out.println("This method cannot be used applied to a ParallaxSprite (setWidth)");
    }

    public final void setHeight(int width) {
        System.out.println("This method cannot be used applied to a ParallaxSprite (setHeight)");
    }

    public final void setX(float x) {
        System.out.println("This method cannot be used applied to a ParallaxSprite (setX)");
    }

    public final void setY(float y) {
        System.out.println("This method cannot be used applied to a ParallaxSprite (setY)");
    }

    public final void setXY(float x, float y) {
        System.out.println("This method cannot be used applied to a ParallaxSprite (setXY)");
    }

    public final void setSize(float x, float y) {
        System.out.println("This method cannot be used applied to a ParallaxSprite (setSize, float)");
    }

    public final void setSize(int x, int y) {
        System.out.println("This method cannot be used applied to a ParallaxSprite (setSize, int)");
    }*/

    /*public void render() {
        if (getXOnScreen() > Display.getDisplayMode().getWidth()/StaticRef.getCamera().getZoom2D() || getXOnScreen() + getWidth() < 0 || getYOnScreen() > Display.getDisplayMode().getHeight()/StaticRef.getCamera().getZoom2D() || getYOnScreen() + getHeight() < 0) {
            return;
        } else {
            super.render();
        }
    }*/
}


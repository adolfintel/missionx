package examples.example08;

import org.easyway.input.Dragger;
import org.easyway.input.Mouse;
import org.easyway.interfaces.sprites.IClickable;
import org.easyway.objects.sprites2D.SpriteColl;
import org.easyway.objects.texture.Texture;

/**
 * implementing the interface IClickable to a sprite we can manage the mouse's
 * interaction with the SpriteColl
 * 
 * @author Daniele Paggi
 * 
 */
public class ClickableSprite extends SpriteColl implements IClickable {

    private static final long serialVersionUID = 1L;
    Dragger dragger;

    public ClickableSprite(float x, float y, Texture image) {
        super(x, y, image);
        setSize(100, 100);
    }

    @Override
    public void onClick(int x, int y) {
    }

    @Override
    public void onDown(int x, int y) {
        setRGBA(1, 1, 0, 1);
        // start dragging the object
        Dragger.startDrag(this, Dragger.LEFT);
        // else you can use:
        // Mouse.startDrag(this);
    }

    @Override
    public void onDrag(int incx, int incy) {
        setRGBA(0, 1, 1, 1);
        move(incx, incy);
    }

    @Override
    public void onEnter() {
        setRGBA(0, 1, 0, 1);
    }

    @Override
    public void onExit() {
        setRGBA(1, 0, 0, 1);
    }

    @Override
    public void onRelease(int x, int y) {
        setRGBA(0, 1, 0, 1);
    }

    public void onOver(int x, int y) {
    }
}

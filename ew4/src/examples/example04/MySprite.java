package examples.example04;

import org.easyway.input.Keyboard;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;

// Create a new Sprite and implements ILoopable for using the loop() method
public class MySprite extends Sprite implements ILoopable {

    private static final long serialVersionUID = 1L;
    int kind;

    /**
     * kind = 0: shot on keyPressed<br>
     * kind = 1: shot on keyDown<br>
     * kind = 2: shot on keyReleased<br>
     * kind = 3: shot on keyUp
     */
    public MySprite(float x, float y, int kind) {
        setXY(x,y);
        setImage("/images/particella.png");
        this.kind = kind;
        switch (kind) {
            case 0:
                setRGBA(1, 0, 0, 1);
                break;
            case 1:
                setRGBA(0, 1, 0, 1);
                break;
            case 2:
                setRGBA(0, 0, 1, 1);
                break;
            case 3:
            default:
                setRGBA(1, 1, 0, 1);
        }
    }

    @Override
    // if the sprite implements ILoopable this method is self-called by the game
    // engine!!
    public void loop() {
        switch (kind) {
            case 0:
                if (Keyboard.isKeyPressed(Keyboard.KEY_RETURN)) {
                    new Shoot(getX(), getY());
                }
                break;
            case 1:
                if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
                    new Shoot(getX(), getY());
                }

                break;
            case 2:
                if (Keyboard.isKeyReleased(Keyboard.KEY_RETURN)) {
                    new Shoot(getX(), getY());
                }
                break;
            case 3:
            default:
                if (Keyboard.isKeyUp(Keyboard.KEY_RETURN)) {
                    new Shoot(getX(), getY());
                }
        }
    }
}

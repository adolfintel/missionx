package examples.example03;

import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;
import org.easyway.system.StaticRef;
import org.easyway.system.state.Game;

public class SpriteExample extends Game {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        new SpriteExample();
    }

    public SpriteExample() {
        super(800, 600, 24, false);

    }
    Sprite spr, spr2;
    /**
     * speed of sprite 2
     */
    float speedy = 6;

    @Override
    public void creation() {
        // load an image
        Texture image = Texture.getTexture("/images/apple.png");
        // create and show a new sprite with the loaded image
        new Sprite(10, 0, image);

        new Sprite(300, 100, image);

        // create a sprite..
        spr = new Sprite(10, 200, image);
        // change the sprite's saturation
        // Red = 1, Green = 1, blue = 0.2, alpha = 0.7
        spr.setRGBA(1, 1, 0.2f, 1);
        // change the sprite's size
        spr.setSize(200, 100);

        spr2 = new Sprite(10, 300, image);
        spr2.setRGBA(0, 1, 1, 0.5f);
        // change the Layer of the sprite:
        // the layer is the "depth"..
        // if we have 2 sprite: A and B
        // A have layer 0 and B have layer 1
        // if A and B are one on the other (same coordinates x,y)
        // B will be on the Top
        // the default layer is 0
        // Note: IDLayer is different from Layer.. I'll show in a future example the difference
        spr2.setLayer(1);
    }

    @Override
    public void loop() {
        // rotate the sprite1
        spr.setRotation(spr.getRotation() + 1);

        // move the sprite2
        spr2.incY(speedy);
        // the sprite2 bounce on the top\bottom
        if (spr2.getY() <= 0 || spr2.getY() + spr2.getHeight() >= 600) {
            speedy = -speedy;
        }
    }

}

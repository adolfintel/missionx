package examples.example05;

import org.easyway.input.Keyboard;
import org.easyway.objects.Camera;
import org.easyway.objects.sprites2D.SimpleSprite;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.text.Text;
import org.easyway.objects.texture.Texture;
import org.easyway.system.StaticRef;
import org.easyway.system.state.Game;

public class CameraExample extends Game {

    private static final long serialVersionUID = 1L;

    public static void main(String args[]) {
        new CameraExample();
    }

    public CameraExample() {
        super(800, 600, 24, false);
    }
    Text infoText;

    @Override
    public void creation() {
        Texture image = Texture.getTexture("images/tileset/mattone.png");
        for (int x = 0; x < 8000; x += 200) {
            for (int y = 0; y < 6000; y += 200) {
                // a simple sprite is like a sprite but more "lightweight"
                // a SimpleSprite can't be roteated, saturated and have a
                // rendering process more fast
                new SimpleSprite(x, y, image);
            }
        }

        Sprite spr = new Sprite(300, 150);
        spr.setImage("images/apple.png");
        // the sprite is fixed on screen:
        // the coordinates are relative to the screen and not to the  world's coordinate
        spr.fixedOnScreen = true;
        // then:
        // spr.getX() will be equal to spr.getXOnScreen()

        // change the saturation
        spr.setRGBA(1, 1, 0, 0.5f);

        // double the dimension
        // as default the dimension is equal to the image's dimension
        spr.setSize(spr.getWidth() * 2, spr.getHeight() * 2);

        // place the sprite on top
        spr.setLayer(1);

        infoText = new Text(0, 0, "", null);
    }

    @Override
    public void loop() {
        // StaticRef is a special class that point to the game engine's apparate
        // for example we can obtain the game engine's camera with "getCamera()"
        final Camera camera = StaticRef.getCamera();
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            camera.x -= 6;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            camera.x += 6;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            camera.y -= 6;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            camera.y += 6;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_A) && camera.getZoom2D() > 0.1f) {
            camera.setZoom2D(camera.getZoom2D() - 0.01f);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
            camera.setZoom2D(camera.getZoom2D() + 0.01f);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            camera.setZoom2D(1);
        }
        infoText.setText("camera X: " + StaticRef.getCamera().x);
        infoText.append("\ncamera Y: " + StaticRef.getCamera().y);
        infoText.append("\ncamera Zoom: " + StaticRef.getCamera().getZoom2D());
        infoText.append("\nuse the arrow key to move,\nuse A,Z for change the zoom and [SPACE] for reset it");
    }
}

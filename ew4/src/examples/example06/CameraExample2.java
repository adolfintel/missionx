package examples.example06;

import static org.easyway.input.Keyboard.KEY_DOWN;
import static org.easyway.input.Keyboard.KEY_LEFT;
import static org.easyway.input.Keyboard.KEY_RIGHT;
import static org.easyway.input.Keyboard.KEY_SPACE;
import static org.easyway.input.Keyboard.KEY_UP;
import static org.easyway.input.Keyboard.isKeyDown;
import static org.easyway.input.Keyboard.isKeyReleased;

import java.awt.Color;

import org.easyway.objects.sprites2D.SimpleSprite;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.text.Text;
import org.easyway.objects.texture.Texture;
import org.easyway.system.StaticRef;
import org.easyway.system.state.Game;

public class CameraExample2 extends Game {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new CameraExample2();
	}

	public CameraExample2() {
		super(800, 600, 24, false);
	}

	Sprite spr;

	@Override
	public void creation() {
		// change the window's title
		setTitle("Camera Example n.2");
		// set the background color: (red,green,blue)
		StaticRef.getCamera().setBackgroundColor(125,125,125);
		Texture image = Texture.getTexture("images/particella.png");

		// grid
		for (int x = 0; x < 8000; x += 200) {
			for (int y = 0; y < 6000; y += 200) {
				new SimpleSprite(x, y, image);
			}
		}

		// movable sprite
		spr = new Sprite(0, 0, image);
		spr.setRGBA(1, 1, 0, 1);
		spr.setLayer(1);
		// the camera will follow the sprite Spr
		StaticRef.getCamera().centerOn(spr);
		
		String message = "Use the arrow to move the sprite";
		String m2 =  "\nPush space to set\\remove the camera centered on the sprite";
		message = Text.createGradientText(message, Color.BLUE, Color.BLACK);
		message += Text.createGradientText(m2,Color.BLUE,Color.BLACK);
		
		new Text(0,0,message,null).setSize(26);
	}

	@Override
	public void loop() {
		// move the sprite
		if (isKeyDown(KEY_LEFT)) {
			spr.incX(-6);
		}
		if (isKeyDown(KEY_RIGHT)) {
			spr.incX(6);
		}
		if (isKeyDown(KEY_UP)) {
			spr.incY(-6);
		}
		if (isKeyDown(KEY_DOWN)) {
			spr.incY(6);
		}
		if (isKeyReleased(KEY_SPACE)) {
			if (StaticRef.getCamera().isCentered()) {
				// stop following the sprite
				StaticRef.getCamera().centerOn(null);
			} else {
				// resume following the sprite
				StaticRef.getCamera().centerOn(spr);
			}
		}
	}

}

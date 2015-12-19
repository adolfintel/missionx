package examples.example09;

import org.easyway.input.Keyboard;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.animo.Animo;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;

public class Player extends Sprite implements ILoopable {

	private static final long serialVersionUID = 1L;
	static Animo aniLeft, aniRight, aniUp, aniDown;

	public static void init() {
		if (aniLeft != null)
			return;
		aniLeft = new Animo();
		aniRight = new Animo();
		aniUp = new Animo();
		aniDown = new Animo();

		Texture image;
		for (int i = 1; i < 7; ++i) {
			image = Texture.getTexture("images/player/down" + i + ".png");
                        image.setAlphaForeach(255, 255, 255, 0);
			aniDown.add(image, 100);
			image = Texture.getTexture("images/player/left" + i + ".png");
                        image.setAlphaForeach(255, 255, 255, 0);
			aniLeft.add(image, 100);
			image = Texture.getTexture("images/player/right" + i + ".png");
                        image.setAlphaForeach(255, 255, 255, 0);
			aniRight.add(image, 100);
			image = Texture.getTexture("images/player/up" + i + ".png");
                        image.setAlphaForeach(255, 255, 255, 0);
			aniUp.add(image, 100);

		}

		aniLeft.start();
		aniRight.start();
		aniUp.start();
		aniDown.start();
	}

	public Player(float x, float y) {
		super(x, y, aniRight.get(0));
		setAnimo(aniRight);
	}

	@Override
	public void loop() {
		float speed = 2.0f;

		// no key pressed
		if (Keyboard.getDownKeys().isEmpty()) {
			// A) if no key are pressed pause the animation
			getAnimo().pause();
		} else {
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				incX(-speed);
				setAnimo(aniLeft);
				// resume the animation because it can be paused in the point A)
				aniLeft.resume();
			} else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				incX(speed);
				setAnimo(aniRight);
				aniRight.resume();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				incY(-speed);
				setAnimo(aniUp);
				aniUp.resume();
			} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				incY(speed);
				setAnimo(aniDown);
				aniDown.resume();
			}
		}

	}
	
	
	@Override
	public void incY(float incy) {
		super.incY(incy);
		setLayer((int)getY());
	}

}

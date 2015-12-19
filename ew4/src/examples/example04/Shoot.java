package examples.example04;

import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;

public class Shoot extends Sprite implements ILoopable {

	private static final long serialVersionUID = 1L;

	public Shoot(float x, float y) {
		super(x, y, Texture.getTexture("/images/particella.png"));
	}

	@Override
	public void loop() {
		incY(-6); // move the object
		if (getY() + getHeight() <= 0) {
			destroy(); // destroy the object when go out of screen
                        return;
		}
	}

}

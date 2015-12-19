package examples.example11;

import org.easyway.collisions.BruteForceCollision;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.sprites2D.SpriteColl;
import org.easyway.objects.texture.Texture;

public class FallingBox extends SpriteColl implements ILoopable {

	private static final long serialVersionUID = 1L;

	public FallingBox(float x, Texture image) {
		super(x, -image.getHeight(), image);
		BruteForceCollision.getGroup("GroupA").add(this);
	}

	float speedy = 2.0f;

	@Override
	public void loop() {
		incY(speedy);
		if (getY() >= 600 - getHeight()) {
			setY(600 - getHeight());
			speedy = 0.0f;
		}
	}

	@Override
	public void onCollision() {
		// replace in the oldy
		if (speedy>0.0f)
			incY(-2);

		// stop moving
		speedy = 0.0f;
	}

}

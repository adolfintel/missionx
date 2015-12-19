package examples.example09;

import org.easyway.objects.animo.Animo;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;
import org.easyway.objects.text.Text;
import org.easyway.system.state.Game;

public class AnimationExample extends Game {

	private static final long serialVersionUID = 1L;

	public AnimationExample() {
		super(800, 600, 24, false);
	}

	@Override
	public void creation() {
                new Text(0,0,"Use the arrows key for move the player");
		// creates a new animation object
		Animo animo = new Animo();
		Texture image;
		// load the data in the animation
		// in the true an animation is composed from an AnimoData object:
		// the AnimoData contains the sequence of images,
		// the Animo define what is it the current frame to show.. 
		for (int i = 1; i <= 6; ++i) {
			image = Texture.getTexture("images/player/left" + i + ".png");
                        image.setAlphaForeach(255, 255, 255, 0);
			// add the image to the animation's data: "animo.getData().add(100,image)"
			// (image, speed in ms)
			animo.add(image,100);
		}
		
		Sprite spr = new Sprite(400,300);
		spr.setAnimo(animo);
		animo.start();
		spr.setLayer((int)spr.getY());
		
		Player.init();
		new Player(100,100);
	}

	@Override
	public void loop() {
		// TODO Auto-generated method stub

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new AnimationExample();
	}

}

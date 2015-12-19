package examples.example10;

import org.easyway.lists.GameList;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.text.Text;
import org.easyway.objects.texture.Texture;
import org.easyway.system.state.Game;

/**
 * In this example I want create a list of objects that should be managed.<br>
 * Of course I can implement ILoopable to all the objects.. but in this example
 * I want illustrate the BaseList class
 * 
 * @author remalkoil
 * 
 */
public class BaseListExample extends Game {

	private static final long serialVersionUID = 1L;

	public final static void main(String args[]) {
		new BaseListExample();
	}

	public BaseListExample() {
		super(800, 600, 24, false);
	}
	
	// the list of all the objects
	GameList<Sprite> list;
	
	Text text;

	@Override
	public void creation() {
		text = new Text(0,0,"",null);
		
		// create the list of sprites
		list = new GameList<Sprite>();

		// load the image
		Texture image = Texture.getTexture("images/marwy/mattone.png");
		
		Sprite spr;
		for (int i = 0; i < 100; ++i) {
			// create a sprite in the bottom of the screen
			spr = new Sprite((float) Math.random() * (800 - 32), (float)Math.random()*(300-32)+300, image);
			spr.setSize(10, 10);
			// adds the created sprite to the list
			list.add(spr);
		}
	}
        
	@Override
	public void loop() {
		text.setText("number of sprites in the list: "+list.size());
		// for each sprite contained in the list
		list.startScan();
		while (list.next()) {
			Sprite spr = list.getCurrent();
			
			// move the current sprite taken from the list
			spr.move(0, -2);
			// if the sprite go out of screen: destroy it
			// Note: when a sprite is destroyed the sprite is self-removed 
			// from the list! You don't need to manage the index of scan of the BaseList
			// and you don't need to manage the removes from the list! ;)
			if (spr.getY() + spr.getHeight() <= 0)
				spr.destroy();
		}
	}

}

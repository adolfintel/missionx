package examples.example04;

import org.easyway.input.Keyboard;
import org.easyway.objects.text.Text;
import org.easyway.objects.text.TextAlign;
import org.easyway.system.state.Game;

public class KeyboardExample extends Game {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new KeyboardExample();
	}

	public KeyboardExample() {
		super(800, 600, 24, false);
	}

	Text text, text2, text3;

	@Override
	public void creation() {
		// writing NULL as font the game engine will use the "default" font
		text = new Text(0, 0, "", null);
		text2 = new Text(0,text.getHeight(),"Key Pressed: ",null);
                text3 = new Text(800,600, "Try to press Enter key");
                text3.setAlignV(TextAlign.BOTTOM);
                text3.setAlignH(TextAlign.RIGHT);
		// you can change the Default writing, for example:
		// EWFont.defaultFont = new EWFont("Arial");

                // not more needed from EasyWay4:
		// initializes the resources used by the sprites: MySprite and Shoot
		// MySprite.init();
		// Shoot.init();

		// look the code of the method loop() of the MySprite class.. ;)
		new MySprite(10, 536, 0);
		new MySprite(266, 536, 1);
		new MySprite(533, 536, 2);
		new MySprite(726, 536, 3);
	}

	String colorYellow = Text.createColor(255, 255, 0);
	
	@Override
	public void loop() {
		// write if the RETURN key is down or not
		if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
			text.setText("RETURN IS DOWN");
		}
		if (Keyboard.isKeyUp(Keyboard.KEY_RETURN)) {
			text.setText("RETURN IS UP");
		}
		if (Keyboard.isKeyReleased(Keyboard.KEY_RETURN)) {
			// note: a key is going up if at the time T the key is UP and at time T-1 the key is DOWN
			text.setText(colorYellow+"RETURN IS GOING UP");
		}
		if (Keyboard.isKeyPressed(Keyboard.KEY_RETURN)) {
			// note: a key is going down if at the time T the key is DOWN and at time T-1 the key is UP
			text.setText(colorYellow+"RETURN IS GOING DOWN");
		}
		
		text2.setText("Key Pressed:");
		String keyName;
		for (int keyId : Keyboard.getDownKeys()) {
			keyName = Keyboard.getKeyName(keyId);
			text2.append(keyName + ", ");
		}

	}

}

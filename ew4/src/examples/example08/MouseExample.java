package examples.example08;

import org.easyway.input.Mouse;
import org.easyway.objects.text.Text;
import org.easyway.objects.texture.Texture;
import org.easyway.system.state.Game;

public class MouseExample extends Game {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new MouseExample();
	}

	public MouseExample() {
		super(800, 600, 24, false);
	}

	Text t1, t2, t3, t4;

	@Override
	public void creation() {
		int y = 0;
		t1 = new Text(0, y, "left button is: ", null);
		y += t1.getHeight();
		t2 = new Text(0, y, "middle button is: ", null);
		y += t2.getHeight();
		t3 = new Text(0, y, "right button is: ", null);
		y += t3.getHeight();
		t4 = new Text(0, y, "", null);
		
		Texture image = Texture.getTexture("images/particella.png");
		image.createMask();
		new ClickableSprite(400,300,image);
	}

	String down = Text.createColor(0, 255, 0) + "down";
	String up = Text.createColor(255, 0, 0) + "up";

	@Override
	public void loop() {
		// the mouse, as the keys for the keyboard, has the methods:
		// is<key>Down, is<key>Up, is<key>Released, is<key>Pressed
		// where <key> can be: Left, Right, Middle
		t1.setText("left button is: " + (Mouse.isLeftDown() ? down : up));
		t2.setText("middle button is: " + (Mouse.isMiddleDown() ? down : up));
		t3.setText("right button is: " + (Mouse.isRightDown() ? down : up));
		t4.setText("X: " + Mouse.getX() + " Y: " + Mouse.getY());
	}

}

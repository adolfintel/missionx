package examples.example18;

import org.easyway.collisions.GroupCollision;
import org.easyway.input.Keyboard;
import org.easyway.input.Mouse;
import org.easyway.objects.sprites2D.SpriteColl;
import org.easyway.objects.sprites2D.sentry.Sentry;
import org.easyway.objects.text.Text;
import org.easyway.objects.text.TextAlign;
import org.easyway.system.state.Game;

/**
 *
 * @author Daniele Paggi
 */
public class Main extends Game {

    private Text infoPrinterText;

    public static void main(String args[]) {
        new Main();
    }

    SpriteColl spr;
    Wall wall2;

    @Override
    public void creation() {
        new Player();
        Wall wall = new Wall();
        wall.setY(400);
        wall2 = new Wall();
        wall2.setXY(200,450);
        wall2.setImage(Images.WALL2.getImage());
        wall2.setHeight(32);
        wall2.setRotation(-16);

        spr = new SpriteColl();
        spr.setImage(Images.WALL2.getImage());
        spr.setSize(64, 16);
        GroupCollision.getGroup("Wall").addToDestination(spr);

        initPrintInfo();
    }

    private void initPrintInfo() {
        if (infoPrinterText == null) {
            infoPrinterText = new Text(0, getCamera().getHeight(), "");
            infoPrinterText.setAlignV(TextAlign.BOTTOM);
        }
        infoPrinterText.setText("Press [S] Key for showing\\hiding sentries");
        String color = Text.createColor(Sentry.showSentryes ? 0 : 255, Sentry.showSentryes ? 255 : 0, 128);
        infoPrinterText.append("\nShow Sentry: " + color + Sentry.showSentryes);
    }

    int time = 0;
    public void loop() {
        if (Keyboard.isKeyPressed(Keyboard.KEY_S)) {
            switchShowSentry();
        }
        spr.setXY(Mouse.getXinWorld()-32, Mouse.getYinWorld());
        wall2.setRotation((float)Math.sin(++time/100f)*20);
    }

    private void switchShowSentry() {
        Sentry.showSentryes = !Sentry.showSentryes;
        initPrintInfo();
    }

    public void render() {
    }
}

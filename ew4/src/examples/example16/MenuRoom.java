/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example16;

import org.easyway.gui.base.Button;
import org.easyway.gui.forms.MessageBox;
import org.easyway.gui.forms.YesNoMessageBox;
import org.easyway.objects.text.Text;
import org.easyway.objects.text.TextAlign;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;
import org.easyway.system.state.Room;

/**
 *
 * @author RemalKoil
 */
public class MenuRoom extends Room {

    Button playButton;
    Button exitButton;
    Text counterText;
    int counter = 0;

    @Override
    public void creation() {
        System.out.println("Menu created");

        playButton = new Button(10, 20, "Play the game") {
            @Override
            public void onAction() {
                setRoom(RoomExampleMain.game);
            }
        };
        new Button(playButton.getWidth() + 20, 20, "Show a Gui Dialog Button") {

            @Override
            public void onAction() {
                new MessageBox("Wow, you've just showed this dialog :)");
            }
        };
        exitButton = new Button(10, playButton.getHeight() + 30, "Exit Game") {

            @Override
            public void onAction() {
                new YesNoMessageBox("Do you wanna really close the game?") {

                    @Override
                    protected void onYesAction() {
                        Core.getInstance().endGame();
                    }

                    @Override
                    public void onNoAction() {
                        // nothing
                    }
                };
            }
        };
        counterText = new Text(StaticRef.getCamera().getWidth(), 0, "Counter: " + counter);
        counterText.setAlignH(TextAlign.RIGHT);
    }

    @Override
    public void loop() {
        ++counter;
        counterText.setText("Counter: " + counter);
    }
}

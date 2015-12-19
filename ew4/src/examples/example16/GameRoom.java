/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example16;

import org.easyway.input.Keyboard;
import org.easyway.objects.text.Text;
import org.easyway.system.state.Room;

/**
 *
 * @author RemalKoil
 */
public class GameRoom extends Room {

    @Override
    public void creation() {
        System.out.println("Game created");
        new Text(0, 0, "Press [ESC] to return to the Main Menu\nClick on the Basketball ^_^");
        new BasketBall();
    }

    @Override
    public void loop() {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            setRoom(RoomExampleMain.menu);
        }
    }

}

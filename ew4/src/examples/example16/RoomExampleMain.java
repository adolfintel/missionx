/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example16;
import org.easyway.system.Core;
import org.easyway.system.state.Room;

/**
 *
 * @author RemalKoil
 */
// You can use Game or Core as do you like
public class RoomExampleMain extends Core /* Game */ {

    static Room menu = new MenuRoom();
    static Room game = new GameRoom();

    public RoomExampleMain() {
        super(640, 480, 32, false);
    }

    @Override
    public void creation() {
        setRoom(menu);
    }

    public static final void main(String args[]) {
        new RoomExampleMain();
    }
}

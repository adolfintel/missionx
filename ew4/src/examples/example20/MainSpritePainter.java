/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example20;

import org.easyway.lists.DrawingLayaredList;
import org.easyway.objects.extra.FpsDrawer;
import org.easyway.system.state.Game;
import org.easyway.system.state.GameState;

/**
 *
 * @author RemalKoil
 */
public class MainSpritePainter extends Game {

    public static void main(String[] args) {
        new MainSpritePainter();
    }

    @Override
    public void creation() {
        new TexturePainterObject();
        new FpsDrawer();
    }

    @Override
    public void loop() {
        int count = 0;
        for (DrawingLayaredList layer : GameState.getCurrentGEState().getLayers()) {
            count += layer.size();
        }
        setTitle("DRAWING OBJECTS: " + count);
    }
}

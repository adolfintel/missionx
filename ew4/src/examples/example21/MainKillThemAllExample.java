/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example21;

import org.easyway.collisions.quad.QuadTree;
import org.easyway.input.Keyboard;
import org.easyway.lists.DrawingLayaredList;
import org.easyway.objects.texture.Texture;
import org.easyway.system.StaticRef;
import org.easyway.system.state.Game;
import org.easyway.system.state.GameState;
import org.easyway.utils.ImageUtils;

/**
 *
 * @author RemalKoil
 */
public class MainKillThemAllExample extends Game {

    public static void main(String[] args) {
        QuadTree.USE_QUADTREE = true;
        StaticRef.use_shaders = true;
        new MainKillThemAllExample();
    }
    static Background background;
    private Player player;

    @Override
    public void creation() {
        QuadTree.setDefaultInstance(new QuadTree(0, 0, getWidth(), getHeight()));
        background = new Background();
        player = new Player(getWidth() / 2, getHeight() / 2);
        for (int i = 0; i < 20; ++i) {
            new Enemy();
        }
    }

    @Override
    public void loop() {
        if (Keyboard.isKeyPressed(Keyboard.KEY_F12)) {
            Texture screenShot = ImageUtils.getScreenShot();
            ImageUtils.savePngImage(screenShot, "c:/temp/screen2.png");
        }

        int count = 0;
        for (DrawingLayaredList layer : GameState.getCurrentGEState().getLayers()) {
            count += layer.size();
        }

        setTitle("n. drawing Objects: " + count +
                " | n. looping Objects: " +
                GameState.getCurrentGEState().getLoopList().size());
    }
}

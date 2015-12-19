/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example200;

import examples.example200.enemy.SimpleEnemy;
import org.easyway.objects.Camera;
import org.easyway.objects.extra.FpsDrawer;
import org.easyway.objects.extra.StarsManager;
import org.easyway.objects.text.Text;
import org.easyway.objects.text.TextAlign;
import org.easyway.objects.texture.Texture;
import org.easyway.shader.Shader;
import org.easyway.system.Core;
import org.easyway.system.state.Game;
import org.easyway.system.state.GameState;
import static org.easyway.input.Keyboard.*;

/**
 *
 * @author Daniele Paggi
 */
public class GameExample extends Game {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new GameExample();
    }
    StarsManager stars;
    //Shader shader;
    Shader shader;
    Text textinfo;

    @Override
    public void creation() {
        usePostProcessing(false);
        getCamera().setBackgroundColor(0, 0, 0);

        setVSync(true);
        textinfo = new Text(0, getHeight(), "Press [ESC] button to close the application.");
        textinfo.setAlignV(TextAlign.BOTTOM);

        stars = new StarsManager(Texture.getTexture("images/shipGame/star.png"), 100, 1000, Math.PI);
        stars.setSpeed(3);

        Player player = new Player();
        for (int i = 0; i < 10; ++i) {
            SimpleEnemy enemy = new SimpleEnemy(getWidth(), getHeight() / 2);
        }

        FpsDrawer fps = new FpsDrawer();
        fps.setX(Camera.getCurrentCamera().getWidth() - fps.getWidth());
        fps.setY(Camera.getCurrentCamera().getHeight() - fps.getHeight());
    }

    public void render() {
    }

    @Override
    public void loop() {
        textinfo.setText("Sprites: " + GameState.getCurrentGEState().getLoopList().size());
        textinfo.append("Press [ESC] button to close the application.");
        if (isKeyDown(KEY_ESCAPE)) { // look to the "import static"
            endGame();
        }
    }

}

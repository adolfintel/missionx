/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package examples.example22;

import org.easyway.system.state.Game;
import org.easyway.utils.ScriptInterpeter;

/**
 *
 * @author RemalKoil
 */
public class ScriptMain extends Game {

    public static void main(String[] args) {
        new ScriptMain();
    }

    @Override
    public void creation() {
        new ScriptInterpeter("examples/example22/myscript.txt", true);
        
        ScriptInterpeter.eval("img = getTexture(\"images/apple.png\");");
        ScriptInterpeter.eval("spr = new Sprite(200,100,img);");
        ScriptInterpeter.eval("spr.width *= 2");
        ScriptInterpeter.eval("spr.height *= 2");
        ScriptInterpeter.eval("spr.smoothImage = true;");
        int[][] ints = new int[1][5];
        for (int x=0; x<ints.length; ++x) {
            for (int y=0; y<ints[x].length; ++y) {
                System.out.println("ints..: "+ints[x][y]);
            }
        }
    }

    @Override
    public void loop() {

    }

}

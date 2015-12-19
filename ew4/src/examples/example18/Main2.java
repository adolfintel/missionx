/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package examples.example18;

import org.easyway.collisions.quad.QuadTree;
import org.easyway.system.Core;
import org.easyway.system.state.Game;

/**
 *
 * @author RemalKoil
 */
public class Main2 extends Game{

    public static void main(String[] args) {
        new Main2();
    }

    @Override
    public void creation() {
        QuadTree.USE_QUADTREE = true;
        Core.load("examples/example18/data/EditorLevel.save");
    }

    @Override
    public void loop() {
    }

}

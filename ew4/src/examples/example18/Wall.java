/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package examples.example18;

import org.easyway.collisions.GroupCollision;
import org.easyway.objects.sprites2D.SpriteColl;

/**
 *
 * @author RemalKoil
 */
public class Wall extends SpriteColl{
    public Wall() {
        setImage(Images.WALL1.getImage());
        GroupCollision gc = GroupCollision.getGroup("Wall");
        gc.addToDestination(this);
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.logic;

import mygame.CommonVar;
import mygame.Menu;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;
import org.easyway.system.Core;

/**
 *
 * @author Do$$e
 */
public class LevelChanger extends Sprite implements ILoopable {

    String newLevel = "";

    public LevelChanger(int x, String newLevel) {
        super(x, 0,Texture.getTexture("images/nullCursor.png"));
        this.newLevel = newLevel;
    }

    @Override
    public void loop() {
        if (CommonVar.destroyAll) {
            destroy();
        }
        if (!Core.getInstance().isGamePaused()) {
            if (getXOnScreen() <= 1024) {
                CommonVar.level=newLevel;
                Menu.setPhase(21);
                destroy();
            }

        }
    }
}

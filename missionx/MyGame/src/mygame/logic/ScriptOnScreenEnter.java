/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.logic;

import mygame.CommonVar;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;
import org.easyway.system.Core;

/**
 *
 * @author Do$$e
 */
public class ScriptOnScreenEnter extends Sprite implements ILoopable {

    String file = "";

    public ScriptOnScreenEnter(int x, String file) {
        super(x, 0,Texture.getTexture("images/nullCursor.png"));
        this.file = file;
    }

    @Override
    public void loop() {
        if (CommonVar.destroyAll) {
            destroy();
        }
        if (!Core.getInstance().isGamePaused()) {
            if (getXOnScreen() <= 1024) {
                try {
                    new org.easyway.utils.ScriptInterpreter(file, true);
                } catch (Exception e) {
                    System.out.println("Invalid instruction in script file: " + file);
                }
                destroy();
            }

        }
    }
}

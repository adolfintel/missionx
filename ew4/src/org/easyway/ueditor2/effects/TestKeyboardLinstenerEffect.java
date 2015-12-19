/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.ueditor2.effects;

import javax.swing.JButton;
import org.easyway.ueditor2.effects.test.EndTestEffect;
import static org.easyway.input.Keyboard.isKeyDown;
import static org.easyway.input.Keyboard.KEY_LCONTROL;
import static org.easyway.input.Keyboard.KEY_RSHIFT;
import static org.easyway.input.Keyboard.KEY_Q;

/**
 *
 * @author Daniele Paggi
 */
public class TestKeyboardLinstenerEffect extends Effect {

    JButton testGameButton;

    public TestKeyboardLinstenerEffect(JButton testGameButton) {
        this.testGameButton = testGameButton;
    }

    @Override
    public void loop() {
        if (isKeyDown(KEY_LCONTROL) && isKeyDown(KEY_RSHIFT) && isKeyDown(KEY_Q)) {
            new EndTestEffect(testGameButton);
            destroy();
        }
    }

}

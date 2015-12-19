/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.ueditor2.effects.test;

import javax.swing.JButton;
import org.easyway.debug.DebugManager;
import org.easyway.ueditor2.EditorCore;
import org.easyway.ueditor2.effects.Effect;

/**
 *
 * @author Daniele Paggi
 */
public class EndTestEffect extends Effect {

    JButton testGameButton;

    public EndTestEffect(JButton testGameButton) {
        this.testGameButton = testGameButton;
    }

    @Override
    public void create() {
        EditorCore.load("__testGame__.save", false);
        DebugManager.debug = true;
        destroy();

        testGameButton.setText("Test Game");
        testGameButton.setEnabled(true);
    }
}

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
public class StartTestEffect extends Effect {

    JButton testGameButton;

    public StartTestEffect(JButton testGameButton) {
        this.testGameButton = testGameButton;
    }

    @Override
    public void create() {
        EditorCore.save("__testGame__.save");
        DebugManager.debug = false;
        destroy();
        testGameButton.setText("End Test");
        testGameButton.setEnabled(true);
    }
}

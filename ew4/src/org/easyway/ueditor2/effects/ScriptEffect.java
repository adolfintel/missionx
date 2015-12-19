/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.ueditor2.effects;

import bsh.EvalError;
import bsh.Interpreter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.easyway.ueditor2.EditorCore;
import org.easyway.ueditor2.southPanels.ScripterPanel;

/**
 *
 * @author Administrator
 */
public class ScriptEffect extends Effect {

    static Interpreter inter = new Interpreter();
    static String imports[] = {
        "import org.easyway.objects.*;",
        "import org.easyway.objects.sprites2D.*;",
        "import org.easyway.system.*;",
        "import org.easyway.tiles.*;",
        "import org.easyway.objects.animo.*;",
        "import org.easyway.objects.nativeGraphics.*;",
        "import org.easyway.objects.text.*;",
        "import org.easyway.objects.texture.*;",
        "import org.easyway.objects.extra.*;",
        "import org.easyway.lists.*;",
        "import org.easyway.lists.linked.*;",
        "import org.easyway.geometry2D.*;",
        "import org.easyway.sounds.*;",
        "importCommands(\"/org/easyway/ueditor2/scriptCommands/\");\n "
    };
    private String code;

    public ScriptEffect(String scriptCode) {
        code = scriptCode;
    }

    @Override
    public void create() {

        String header = "";//"clear();";
        for (String _import : imports) {
            header += _import;
        }

        try {
            inter.eval(header + code);
        } catch (final EvalError ex) {

            Logger.getLogger(ScripterPanel.class.getName()).log(Level.SEVERE, null, ex);
            new Thread() {

                public void run() {
                    JOptionPane.showMessageDialog(EditorCore.getMainFrame(), "EXCEPTION:\n " + ex.getMessage(), "EXCEPTION", JOptionPane.ERROR_MESSAGE);
                }
            }.start();
        }

    }
}

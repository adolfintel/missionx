/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.utils;

import bsh.EvalError;
import bsh.Interpreter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.easyway.interfaces.extended.IFinalLoopable;
import org.easyway.objects.BaseObject;
import org.easyway.objects.texture.ImageData;
import org.easyway.objects.texture.TextureNotFoundException;

/**
 *
 * @author RemalKoil
 */
public class ScriptInterpeter extends BaseObject implements IFinalLoopable {

    private static Interpreter sinterpeter = new Interpreter();
    static String imports[];

    static {
        imports = new String[]{
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
                    "importCommands(\"/org/easyway/utils/scriptCommands/\");\n "
                };
        init();
    }

    public static void init() {
        for (String line : imports) {
            try {
                sinterpeter.eval(line);
            } catch (EvalError ex) {
                Utility.error("ScriptInterpeter.init", ex);
            }
        }
    }
    Interpreter interpeter = new Interpreter();
    String code;
    InputStreamReader reader;

    public ScriptInterpeter(String code, boolean fromFile) {
        if (!fromFile) {
            this.code = code;
        } else {

            InputStream is = null;
            {
                if (code.startsWith("/")) {
                    code = code.substring(1);
                }
                int index;
                while ((index = code.indexOf("\\")) != -1) {
                    code = code.substring(0, index) + '/' + code.substring(index + 1);
                }
                URL url = Thread.currentThread().getContextClassLoader().getResource(code);
                if (url != null) {
                    try {
                        is = url.openStream();
                    } catch (IOException ex) {
                        Logger.getLogger(ImageData.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    Utility.error("ScriptInterpeter(String,boolean)", "Scriptfile " + code + " was not found!");
                    throw new TextureNotFoundException(code);
                }
            }
            reader = new InputStreamReader(is);

        }
    }

    public static void eval(String code) {
        try {
            sinterpeter.eval(code);
        } catch (EvalError ex) {
            Utility.error("Code error", "ScriptInterpeter.eval(String)", ex);
        }
    }

    @Override
    public void finalLoop() {
        try {
            if (reader != null) {
                for (String line : imports) {
                    interpeter.eval(line);
                }
                interpeter.eval(reader);
            } else {
                interpeter.eval(code);
            }
        } catch (EvalError ex) {
            Utility.error("Code error", "ScriptInterpeter.finalLoop", ex);
        } finally {
            destroy();
        }
    }
}

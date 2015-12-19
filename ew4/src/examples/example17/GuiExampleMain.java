/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example17;

import org.easyway.gui.base.InputLabel;
import org.easyway.gui.base.InputLabelDecimal;
import org.easyway.gui.base.Label;
import org.easyway.gui.base.Button;
import org.easyway.gui.base.Panel;
import org.easyway.gui.base.Window;
import org.easyway.gui.forms.MessageBox;
import org.easyway.system.state.Game;

/**
 *
 * @author RemalKoil
 */
public class GuiExampleMain extends Game {

    public static void main(String args[]) {
        new GuiExampleMain();
    }

    @Override
    public void creation() {
        System.out.println("creation");
        /*Window window = new Window(null);
        window.setTitle("A simple Window..");*/

        Panel panel = new Panel(0, 0, 500, 500, null);
        panel.setColor(0.7f, 0.5f, 0.3f); // set the panel brown
        //  the field vertexid can be 0,1,2,3 and are used for the vertex:
        //  0---1
        //  |   |
        //  3---2
        panel.setColor(2, 1, 0, 0, 1); // set the vertex on the bottom-right to red
        Label label = new Label(10, 10, "Type here:", panel); // add a new label in the panel
        // add a new inputlabel in the panel
        new InputLabel((int) (label.getWidth() + label.getX()) + 10, 10, 100, label.getHeight(), panel);

        // create a window inside the panel
        Window win = new Window(panel);
        win.setTitle("Internal Window");
        win.center();

        // cerate a window inside the game desktop
        Window win2 = new Window(null);
        win2.setTitle("\"Desktop\" window");
        win2.center();
        // put the window in front of all
        win2.gainFocus();
        new InputLabelDecimal(10, win2.getCaptionHeight() + 10, 100, 20, win2);
        new Label(110, win2.getCaptionHeight() + 10, "<-- type numbers!", win2);
        new InputLabel(10, win2.getCaptionHeight() + 40, 100, 20, win2);
        new Button(10, win2.getCaptionHeight() + 70, "CLICK IT!", win2) {

            @Override
            public void onAction() {
                new MessageBox("Button clicked!");
            }
        };
    }

    public void loop() {
    }

}

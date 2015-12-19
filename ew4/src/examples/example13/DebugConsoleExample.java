package examples.example13;

import org.easyway.debug.ScriptConsole;
import org.easyway.objects.text.Text;
import org.easyway.system.state.Game;

/**
 *
 * @author Daniele Paggi
 */
public class DebugConsoleExample extends Game {

    //
    // One you have executed thix example try to write in the DebugConsole:
    // img = Texture.getTexture( "images/apple.png" );
    // spr = new Sprite( img );
    // spr.setXY( 100, 200 );
    //
    // You can write your test code at running time! in BeanShell language!
    // For example, if you have a "MySprite" class inside the package "com.mygame"
    // you can write:
    // import com.mygame.*;
    // myspr = new MySprite();
    //
    // look at http://www.beanshell.org/ for more information about the BeanShell language!
    //
    ScriptConsole debugConsole = new ScriptConsole();

    @Override
    public void creation() {
        new Text(0,0,"Read the Comment instruction in the source!");
    }

    @Override
    public void loop() {
        // this is the more important line!
        // don't miss it!
        debugConsole.loop();
    }

    public static final void main(String args[]) {
        new DebugConsoleExample();
    }

}

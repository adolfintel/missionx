package examples.example01;

import java.awt.Canvas;
import org.easyway.system.Core;
import org.easyway.system.state.Game;

/**
 * the main class of the game engine should be extended from the Core class
 * 
 * @author remalkoil
 * @version 1
 *
 */
public class MainWindow extends Game {

    private static final long serialVersionUID = 1L;

    public static void main(String args[]) {
        // creates the game engine
        // note: the game engine self start itself
        //       and I should change this in the future..
        new MainWindow();

    }

    public MainWindow() {
        // width = 640,
        // height = 480,
        // depth (bit per pixel) = 24,
        // full screen = false (window mode)
        super(640, 480);
    }

    // you can use the following constructor for create a new Game's panel inside an AWT Canvas
    public MainWindow(int width, int height, int bpp, Canvas container) {
        super(width, height, bpp, container);
    }

    @Override
    public void creation() {
        // this method is executed 1 time only
        System.out.println("created");
    }
    /** cycle counter */
    int step = 0;

    @Override
    public void loop() {
        // this method is executed infinitely times BEFORE the render method
        System.out.println("loop cycle: " + step);
        ++step;
    }

}

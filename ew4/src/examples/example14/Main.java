/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example14;

import java.awt.Color;
import javax.swing.JButton;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Java2DTexture;
import org.easyway.system.Core;

/**
 *
 * @author Daniele
 */
public class Main extends Core {

    public static void main(String args[]) {
        new Main();
    }
    Sprite spr;

    @Override
    public void creation() {
        final Java2DTexture texture = new Java2DTexture(256, 256);

        texture.getGraphics().setColor(new Color(255, 0, 0));
        texture.getGraphics().fillRect(2, 50, 40, 20);
        texture.getGraphics().setColor(new Color(255, 255, 0));
        texture.getGraphics().drawString("Hello! I'm a Java2DTexture!", 100, 100);
        
        JButton btn = new JButton("Test button");
        btn.setSize(100, 20);

        btn.paint(texture.getGraphics()); // we can draw Swing component too
                    // we can only render it: we can't manage any interaction..

        texture.update();

        spr = new Sprite(100, 100, texture);
        System.out.println("spr w:"+spr.getWidth());
        System.out.println("spr h:"+spr.getHeight());
    }

    public void render() {
    }

    public void loop() {
        spr.move(1, 0.5f);
    }
}

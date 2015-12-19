/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.ueditor2.effects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import org.easyway.input.Mouse;
import org.easyway.objects.texture.Texture;
import org.easyway.objects.texture.Java2DTexture;
import org.easyway.system.StaticRef;

/**
 *
 * @author Daniele
 */
public class MoveCamera extends Effect implements IEffect {

    static int size = 24;
    static Font textFont = new Font("Arial", Font.BOLD, size);
    static String moveCameraImage = "org/easyway/ueditor2/images/MoveCamera.PNG";

    static Java2DTexture img;

    public MoveCamera(float x, float y) {
        super(x, y);
        // Mouse.setCursor(Texture.getTexture("org/easyway/editor2/images/MoveCamera.PNG"));
        setImage(Texture.getTexture(moveCameraImage));
        move(-getWidth() / 2, -getHeight() / 2);
        Mouse.hide();

        if (img == null) {
            img = new Java2DTexture(256, 128);
        }
        setImage(img);
        setAutoReSizeDimensions(true);
    }

    public void loop() {
        if (img != null) {
            img.clear();
            {
                final Graphics2D g2d = img.getGraphics();
                g2d.setFont(textFont);
                g2d.setColor(new Color(0,0.5f,0.25f,0.3f)); // few cyan
                g2d.fillRect(0, 0, 256, 128);
                g2d.setColor(Color.YELLOW);
                g2d.drawString(" Camera position", 0, size);
                g2d.setColor(Color.GREEN);
                g2d.drawString("   X: " + (int) StaticRef.getCamera().getX(), 0, size * 2);
                g2d.setColor(Color.RED);
                g2d.drawString("   Y: " + (int) StaticRef.getCamera().getY(), 0, size * 3);
            }
            img.update();
        }
    }

    @Override
    public void onDestroy() {
        Mouse.show();
    }

    @Override
    public void render() {
        setImage(Texture.getTexture(moveCameraImage));
        super.render();

        float oldx = getX();
        incX(getWidth());
        setImage(img);
        super.render();
        setX(oldx);
    }


}

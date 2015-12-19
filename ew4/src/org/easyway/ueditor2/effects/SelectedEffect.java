/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.ueditor2.effects;

import org.easyway.input.Keyboard;
import org.easyway.interfaces.sprites.IPlain2D;
import org.easyway.interfaces.sprites.IRotation;
import org.easyway.objects.sprites2D.SimpleSprite;
import org.easyway.objects.texture.Texture;
import org.easyway.ueditor2.EditorCore;
import static java.lang.Math.sin;

/**
 *
 * @author Daniele
 */
public class SelectedEffect extends Effect {

    static String SelectedEffectImage = "org/easyway/ueditor2/images/SelectedImage.png";
    static int margin = 10;
    private static final long serialVersionUID = -2803498685996611459L;
    IPlain2D targetPlain;

    public SelectedEffect(IPlain2D plain) {
        super(plain.getX() - margin, plain.getY() - margin);
        targetPlain = plain;
        setSize(plain.getWidth() + margin * 2, plain.getHeight() + margin * 2);
        if (plain instanceof SimpleSprite) {
            setImage(((SimpleSprite) plain).getImage());
        } else {
            setImage(Texture.getTexture(SelectedEffectImage));
        }
    }

    @Override
    public void loop() {
        if (targetPlain.isDestroyed()) {
            // the "destroy()" method is called by the method "EditorCore.setSelected()"
            // destroy();
            EditorCore.getInstance().setSelected(null);
            return;
        }
        setXY(targetPlain.getX() - margin, targetPlain.getY() - margin);
        setSize(targetPlain.getWidth() + margin * 2, targetPlain.getHeight() + margin * 2);
        if (targetPlain instanceof IRotation) {
            setRotation(((IRotation) targetPlain).getRotation());
        }
        float newAlpha = (float) (sin(System.currentTimeMillis() / 200.0) * 0.4) + 0.2f;

        setRGBA(1, 1, 1, newAlpha);
        if (Keyboard.isKeyPressed(Keyboard.KEY_DELETE)) {
            targetPlain.destroy();
            EditorCore.getInstance().setSelected(null);
        }
    }
}

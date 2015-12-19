/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easyway.objects.sprites2D;

import java.awt.image.BufferedImage;
import org.easyway.objects.texture.Java2DTexture;

/**
 *
 * @author Daniele
 */
public class Java2DSprite extends SpriteColl {

    BufferedImage bufferedImage;
    
    @Override
    public void render() {
        Java2DTexture.getDefault().getGraphics().drawImage(bufferedImage, (int)getX(), (int)getY(), null);
    }

}

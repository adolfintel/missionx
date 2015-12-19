/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easyway.objects.texture;

/**
 *
 * @author Administrator
 */
public class TextureNotFoundException extends RuntimeException {

    public TextureNotFoundException(String name) {
        super("Image "+name+" not found! ");
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easyway.objects.texture;

/**
 *
 * @author Administrator
 */
public class TextureNameAlreadyInUseException extends RuntimeException {

    public TextureNameAlreadyInUseException(String name) {
        super("Texture Name " + name + " is already in use");
    }



}

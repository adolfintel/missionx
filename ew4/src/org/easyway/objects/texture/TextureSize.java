/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.objects.texture;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 *
 * @author Daniele Paggi
 */
public enum TextureSize {

    T1D(GL11.GL_TEXTURE_1D),
    T2D(GL11.GL_TEXTURE_2D),
    T3D(GL12.GL_TEXTURE_3D);
    
    private final int id;

    TextureSize(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }
}

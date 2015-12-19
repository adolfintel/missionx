/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easyway.shader;
import java.util.Iterator;
/**
 *
 * @author Daniele Paggi
 */
public class ShaderEffectItreable implements Iterator<Shader> {

    int currentPass;
    ShaderEffect effect;
    public ShaderEffectItreable(ShaderEffect effect) {
        currentPass = 0;
        this.effect = effect;
    }

    public boolean hasNext() {
        return currentPass < effect.size();
    }

    public Shader next() {
        return effect.getPass(currentPass++);
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }


}

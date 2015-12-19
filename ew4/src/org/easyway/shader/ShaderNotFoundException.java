/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.shader;

/**
 *
 * @author RemalKoil
 */
class ShaderNotFoundException extends RuntimeException {

    public ShaderNotFoundException(String name) {
        super("Shader " + name + " not found! ");
    }
}

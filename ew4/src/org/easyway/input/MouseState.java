/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.input;

/**
 *
 * @author RemalKoil
 */
public class MouseState {

    EWMouseList objects;
    EWMouseList guiObjects;
    EWMouseList lastGuiObjs;

    public MouseState(EWMouseList objects, EWMouseList guiObjects, EWMouseList lastGuiObjs) {
        this.objects = objects;
        this.guiObjects = guiObjects;
        this.lastGuiObjs = lastGuiObjs;
    }
}

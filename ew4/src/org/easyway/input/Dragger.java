/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.input;

import org.easyway.interfaces.extended.IFinalLoopable;
import org.easyway.interfaces.sprites.IPlain2D;
import org.easyway.objects.BaseObject;

/**
 *
 * @author RemalKoil
 */
public class Dragger extends BaseObject implements IFinalLoopable {

    IPlain2D target;
    float incx;
    float incy;
    int buttonNumber;
    public static final int LEFT = 0;
    public static final int MIDDLE = 2;
    public static final int RIGHT = 3;

    public Dragger(IPlain2D target, int buttonNumber) {
        this.target = target;
        incx = target.getXOnScreen() - Mouse.getX();
        incy = target.getYOnScreen() - Mouse.getY();
        this.buttonNumber = buttonNumber;
    }

    public void finalLoop() {
        target.setXY(Mouse.getXinWorld() + incx, Mouse.getYinWorld() + incy);
        if (!Mouse.isButtonDown(buttonNumber)) {
            target = null;
            destroy();
        }
    }
    private static Dragger dragger;

    public static void startDrag(IPlain2D target, int buttonNumber) {
        if (dragger != null && !dragger.isDestroyed()) {
            if (dragger.target == target && buttonNumber == buttonNumber) {
                return;
            }
            dragger.destroy();
        }
        dragger = new Dragger(target, buttonNumber);
    }
}

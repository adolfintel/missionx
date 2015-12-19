/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easyway.gui.base;

import org.easyway.input.Mouse;
import org.easyway.system.state.GameState;

/**
 *
 * @author RemalKoil
 */
public class MainGuiFather extends GuiContainer {

    public static MainGuiFather getCurrentInstance() {
        return GameState.getCurrentGEState().getMainGuiFather();
    }

    public MainGuiFather() {
        super();

        setType("$_CameraContainer");
        setName("$_CameraContainer");
        setNullFather();
        Mouse.addGuiObject(this);
    }

    @Override
    public int getHeight() {
        return GameState.getCurrentGEState().getCamera().getHeight();
    }

    @Override
    public int getWidth() {
        return GameState.getCurrentGEState().getCamera().getWidth();
    }

    @Override
    public void customDraw() {
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.ueditor2.commands.editor;

import org.easyway.ueditor2.effects.Effect;
import org.easyway.objects.Camera;
import org.easyway.system.StaticRef;

/**
 *
 * @author Daniele
 */
public class ZoomCommand extends Effect {
    private static final long serialVersionUID = 8383203248357537785L;

    float zoomStep;
    boolean relative;

    public ZoomCommand(float zoomStep) {
        this(zoomStep, true);
    }

    public ZoomCommand(float zoomStep, boolean relative) {
        super();
        this.zoomStep = zoomStep;
        this.relative = relative;
    }


    @Override
    public void create() {
        final Camera camera = StaticRef.getCamera();
        if (relative) {
            camera.setZoom2D(camera.getZoom2D() + zoomStep);
        } else {
            camera.setZoom2D(zoomStep);
        }
        destroy();
    }
}

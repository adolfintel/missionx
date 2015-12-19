/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.input;

import java.util.ArrayList;
import org.easyway.interfaces.base.IDestroyable;
import org.easyway.interfaces.base.IType;
import org.easyway.interfaces.extended.ILayer;
import org.easyway.interfaces.sprites.IMaskerable;
import org.easyway.interfaces.sprites.IPlain2D;
import org.easyway.lists.Entry;
import org.easyway.objects.sprites2D.Mask;

/**
 *
 * @author Daniele Paggi
 */
public class PreciseClickableObjectWrapper implements IPlain2D, ILayer, IMaskerable {

    public Object obj;

    public PreciseClickableObjectWrapper(Object obj) {
        this.obj = obj;
    }

    @Override
    public int getHeight() {
        return ((IPlain2D) obj).getHeight();
    }

    @Override
    public int getWidth() {
        return ((IPlain2D) obj).getWidth();
    }

    @Override
    public void setHeight(int height) {
        ((IPlain2D) obj).setHeight(height);
    }

    @Override
    public void setSize(int width, int height) {
        ((IPlain2D) obj).setSize(width, height);
    }

    @Override
    public void setWidth(int width) {
        ((IPlain2D) obj).setWidth(width);
    }

    @Override
    public void setX(float x) {
        ((IPlain2D) obj).setX(x);
    }

    @Override
    public void setXY(float x, float y) {
        ((IPlain2D) obj).setXY(x, y);
    }

    @Override
    public void setY(float y) {
        ((IPlain2D) obj).setY(y);
    }

    @Override
    public float getX() {
        return ((IPlain2D) obj).getX();
    }

    @Override
    public float getXOnScreen() {
        return ((IPlain2D) obj).getXOnScreen();
    }

    @Override
    public float getY() {
        return ((IPlain2D) obj).getY();
    }

    @Override
    public float getYOnScreen() {
        return ((IPlain2D) obj).getYOnScreen();
    }

    @Override
    public int getLayer() {
        return ((ILayer) obj).getLayer();
    }

    @Override
    public void destroy() {
        ((IDestroyable) obj).destroy();
    }

    @Override
    public boolean isDestroyed() {
        return ((IDestroyable) obj).isDestroyed();
    }

    @Override
    public String getType() {
        return ((IType) obj).getType();
    }

    @Override
    public Mask getMask() {
        return ((IMaskerable) obj).getMask();
    }

    @Override
    public ArrayList<Entry> getEntries() {
        return ((ILayer) obj).getEntries();
    }

    @Override
    public ArrayList getQuadEntries() {
        return ((IPlain2D) obj).getQuadEntries();
    }

    @Override
    public ArrayList getUsedInQuadNodes() {
        return ((IPlain2D) obj).getUsedInQuadNodes();
    }

    @Override
    public boolean isQuadTreeUsable() {
        return ((IPlain2D) obj).isQuadTreeUsable();
    }

    @Override
    public float getScaleX() {
        return ((IPlain2D) obj).getScaleX();
    }

    @Override
    public float getScaleY() {
        return ((IPlain2D) obj).getScaleY();
    }
}

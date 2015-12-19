/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.objects.extra;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import org.easyway.interfaces.extended.IDrawing;
import org.easyway.interfaces.extended.ILayerID;
import org.easyway.objects.BaseObject;
import org.easyway.objects.texture.Java2DTexture;
import org.easyway.shader.Shader;
import org.easyway.system.Core;
import org.easyway.system.state.GameState;
import org.easyway.system.state.OpenGLState;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Daniele Paggi
 */
public class FpsDrawer extends BaseObject implements IDrawing, ILayerID {

    int layer = 0;
    int layerId = -1;
    Java2DTexture image;
    float x, y;
    float width, height;
    protected int innerWidth = 128, innerHeight = 64;
    protected int[] lastFps = new int[innerWidth];
    /** the used shader in the rendering process */
    private Shader shader = Shader.getDefaultShader();

    public FpsDrawer() {
        image = new Java2DTexture(innerWidth, innerHeight);
        width = innerWidth;
        height = innerHeight;
        setIdLayer(GameState.getCurrentGEState().getLayers().length - 1);
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public void setLayer(int layer) {
        this.layer = layer;
        readdToDrawingLists();
    }

    @Override
    public int getLayer() {
        return layer;
    }

    protected void renderFps(Graphics2D g) {

        g.setBackground(new Color(1, 0.2f, 0.2f, 0.5f));
        g.clearRect(0, 0, innerWidth, innerHeight);
        Color colorA = new Color(0.7f, 0.7f, 1, 0.6f);
        Color colorB = new Color(0.7f, 1, 1, 0.6f);
        for (int i = 0; i < innerWidth; ++i) {
            g.setColor(i % 2 == 0 ? colorA : colorB);
            g.drawLine(i, innerHeight, i, innerHeight - lastFps[i]);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 13));
        String text = "FPS: "+ Core.getInstance().getFps();
        Rectangle2D fontSize = g.getFont().getStringBounds(text, g.getFontRenderContext());
        float textX = innerWidth /2 - (float)fontSize.getWidth()/2;
        float textY = innerHeight * 0.8f;

        g.drawString(text, textX, textY);
    }

    @Override
    public void render() {
        if (shader != null) {
            shader.bind();
            shader.update(this);
        }
        int fps = Core.getInstance().getFps();

        System.arraycopy(lastFps, 1, lastFps, 0, lastFps.length - 1);
        lastFps[lastFps.length - 1] = fps;

        renderFps(image.getGraphics());

        image.update();
        image.bind();
        OpenGLState.enableBlending();
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(image.getXStart(), image.getYStart());
        GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(image.getXEnd(), image.getYStart());
        GL11.glVertex2f(x + width, y);
        GL11.glTexCoord2f(image.getXEnd(), image.getYEnd());
        GL11.glVertex2f(x + width, y + height);
        GL11.glTexCoord2f(image.getXStart(), image.getYEnd());
        GL11.glVertex2f(x, y + height);
        GL11.glEnd();
    }

    @Override
    public int getIdLayer() {
        return layerId;
    }

    @Override
    public void setIdLayer(int id) {
        if (id < 0) {
            id = 0;
        } else if (id > GameState.getCurrentGEState().getLayers().length - 1) {
            id = GameState.getCurrentGEState().getLayers().length - 1;
        }
        if (layerId == -1) {
            GameState.getCurrentGEState().getLayers()[layerId = id].add(this);
        } else {
            GameState.getCurrentGEState().getLayers()[layerId].remove(this);
            GameState.getCurrentGEState().getLayers()[layerId = id].add(this);
        }
    }
}

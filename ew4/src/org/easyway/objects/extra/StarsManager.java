/* EasyWay Game Engine
 * Copyright (C) 2006 Daniele Paggi.
 *  
 * Written by: 2006 Daniele Paggi<dshnt@hotmail.com>
 *   
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 * 
 * You should have received a copy of the GNU Library General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.easyway.objects.extra;

import java.nio.FloatBuffer;
import org.easyway.geometry2D.VectorSpeed;
import org.easyway.interfaces.base.ITexture;
import org.easyway.interfaces.extended.IDrawing;
import org.easyway.interfaces.extended.ILayerID;
import org.easyway.objects.BaseObject;
import org.easyway.objects.Camera;
import org.easyway.shader.Shader;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;
import org.easyway.system.state.GameState;
import org.easyway.system.state.OpenGLState;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class StarsManager extends BaseObject implements IDrawing, ILayerID {

    static final long serialVersionUID = 28;
    ITexture image;
    Star stars[];
    int layer;
    int movingStars;
    int numStars;
    int maxStars;
    float globalSpeed;
    Vector2f globalDirection = new Vector2f();
    int renderIndex;
    float windowCenterX, windowCenterY;
    float windowSquaredDiag, windowDiag;
    /** the drawing sheet */
    private int idLayer = -1;
    FloatBuffer vertexBuffer;
    FloatBuffer coordBuffer;
    private int removedCounter;
    private int maxRemovedCounter;
    private Shader shader = Shader.getDefaultShader();

    public StarsManager(ITexture image, int currentNumStars, int maxStars, double angleDir) {
        this.image = image;
        this.movingStars = this.numStars = currentNumStars;
        this.maxStars = maxStars;
        setDirection(angleDir);
        initData();

        stars = new Star[maxStars];
        for (int i = 0; i < maxStars; i++) {
            stars[i] = new Star();
        }
        vertexBuffer = BufferUtils.createFloatBuffer(maxStars * 4 * 3);
        coordBuffer = BufferUtils.createFloatBuffer(maxStars * 4 * 2);
        setIdLayer(0);
    }

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public int getNumStars() {
        return numStars;
    }

    public int getMovingStars() {
        return movingStars;
    }

    public void setNumStars(int numStars) {
        if (numStars > maxStars) {
            numStars = maxStars;
        } else if (numStars < 0) {
            numStars = 0;
        }
        if (movingStars < numStars) {
            for (int i = movingStars; i < numStars; ++i) {
                stars[i].relocate();
            }
            movingStars = numStars;
        }

        this.numStars = numStars;
    }

    public void setDirection(double angleDir) {
        double[] out = VectorSpeed.getCartesian(angleDir, 1);
        globalDirection.set((float) out[0], (float) out[1]);
        globalDirection.normalise(); // maybe we can avoid this line
        initData();
    }

    public double getDirection() {
        return VectorSpeed.getAngle(globalDirection.getX(), globalDirection.getY());
    }

    private void initData() {
        windowCenterX = Camera.getCurrentCamera().getWidth() / 2f;
        windowCenterY = Camera.getCurrentCamera().getHeight() / 2f;
        windowSquaredDiag = windowCenterX * windowCenterX + windowCenterY * windowCenterY;
        windowDiag = (float) Math.sqrt(windowSquaredDiag);
    }

    @Override
    public void render() {
        GL11.glLoadIdentity();
        //GL11.glScalef(0.5f, 0.5f, 1);
        //GL11.glTranslatef(StaticRef.getCamera().getWidth() / 2, StaticRef.getCamera().getHeight() / 2, 0);

        OpenGLState.enableBlending();
        if (shader != null) {
            shader.bind();
            shader.update(this);
        }
        image.bind();
        GL11.glColor4f(1f, 1f, 1f, 1f);

        vertexBuffer.rewind();
        coordBuffer.rewind();

        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        initData();
        removedCounter = 0;
        maxRemovedCounter = Math.max((int) (0.05f * movingStars), 50);
        for (renderIndex = 0; renderIndex < movingStars; renderIndex++) {
            stars[renderIndex].render();
        }

        vertexBuffer.rewind();
        GL11.glVertexPointer(3, 0, vertexBuffer);
        coordBuffer.rewind();
        GL11.glTexCoordPointer(2, 0, coordBuffer);

        GL11.glDrawArrays(GL11.GL_QUADS, 0, movingStars * 4);

        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
         GL11.glLoadIdentity();
    }

    public void setLayer(int layer) {
        this.layer = layer;
        this.readdToDrawingLists();
    }

    @Override
    public int getLayer() {
        return layer;
    }

    public void setSpeed(float speed) {
        this.globalSpeed = speed;
    }

    class Star {

        float x, y;
        float width, height;
        float speed;
        static private final double PI2 = Math.PI / 2;

        public Star() {
            relocate();
            double rnd = Math.random();
            float sizeFactor = (float) (rnd + 0.5f);
            speed = (float) (rnd * sizeFactor);
            width = sizeFactor * image.getWidth();
            height = sizeFactor * image.getHeight();
        }

        public void render() {
            move();
            replaceInCamera();
            generateData();
        }

        public void relocate() {
            Vector2f dirFromCenter;
            dirFromCenter = (Vector2f) new Vector2f(-globalDirection.x, -globalDirection.y).normalise();
            double alpha = VectorSpeed.getAngle(dirFromCenter.x, dirFromCenter.y);
            double rndVal = Math.random();
            double beta; // = rndVal * Math.PI - PI2;
            beta = Math.acos(1 - rndVal*2) - PI2;
            //beta = rndVal * Math.PI - PI2;
            
            double[] out = VectorSpeed.getCartesian(alpha + beta, windowDiag);
            dirFromCenter.x = (float) out[0];
            dirFromCenter.y = (float) out[1];
            x = dirFromCenter.x + windowCenterX;
            y = dirFromCenter.y + windowCenterY;
        }

        protected void generateData() {
            vertexBuffer.put(x).put(y).put(0).
                    put(x + width).put(y).put(0).
                    put(x).put(y + height).put(0).
                    put(x + width).put(y + height).put(0);

            coordBuffer.put(image.getXStart()).put(image.getYStart()).
                    put(image.getXEnd()).put(image.getYStart()).
                    put(image.getXStart()).put(image.getYEnd()).
                    put(image.getXEnd()).put(image.getYEnd());
        }

        protected void move() {
            float finalSpeed = speed + globalSpeed;
            x += (globalDirection.x * finalSpeed) * Core.getInstance().getSpeedMultiplier();
            y += (globalDirection.y * finalSpeed) * Core.getInstance().getSpeedMultiplier();
        }

        protected void replaceInCamera() {
            float distanceX = windowCenterX - x;
            float distanceY = y - windowCenterY;//windowCenterY - y;
            float distanceSquared = distanceX * distanceX + distanceY * distanceY;
            if (distanceSquared > windowSquaredDiag) {
                relocate();
                if (movingStars > numStars) {
                    remove();
                }
            }
        }

        protected void remove() {
            if (movingStars <= 1) {
                // don't swap!
                movingStars = 0;
                return;
            }
            if (renderIndex >= movingStars) { // already removed
                return;
            }
            if (removedCounter >= maxRemovedCounter) {
                return;
            }
            //swap!
            stars[renderIndex] = stars[movingStars - 1];
            stars[movingStars - 1] = this;
            --movingStars;
            ++removedCounter;
        }
    }

    // -----------------------------------------------------------------
    // ---------------------------- IDLAYER-----------------------------
    // -----------------------------------------------------------------
    @Override
    public int getIdLayer() {
        return idLayer;
    }

    @Override
    public void setIdLayer(int id) {
        if (idLayer != -1) {
            GameState.getCurrentGEState().getLayers()[idLayer].remove(this);
        }
        if (id < 0) {
            id = 0;
        } else if (id > GameState.getCurrentGEState().getLayers().length) {
            id = GameState.getCurrentGEState().getLayers().length;
        }
        idLayer = id;
        GameState.getCurrentGEState().getLayers()[idLayer].add(this);
    }
}

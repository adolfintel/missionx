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
package org.easyway.objects.texture;

import java.nio.IntBuffer;

import java.util.Stack;
import org.easyway.interfaces.base.ITexture;
import org.easyway.objects.Camera;
import org.easyway.system.Core;
import org.easyway.system.state.OpenGLState;
import org.easyway.utils.ImageUtils;
import org.easyway.utils.Utility;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;

public class TextureFBO {

    static int lastID = 0;
    int id;
    IntBuffer bufferId;
    ITexture texture;
    boolean useAlternateMode = false;
    //ITexture backupScreen;
    Stack<Integer> stack = new Stack<Integer>();
    //TextureCompact texturec;

    public TextureFBO(ITexture destination) {
        texture = destination;
        if (texture == null) {
            return;
        }
        // if (texture. <= 0)
        // return;

        // check
        if (!checkFBOSupport()) {           
            useAlternateMode = true;
            id = 0;
            //backupScreen = new Texture(Core.getInstance().getWidth(), Core.getInstance().getHeight());
            return;
        }

        // generate the ID
        bufferId = BufferUtils.createIntBuffer(1);
        EXTFramebufferObject.glGenFramebuffersEXT(bufferId);
        id = bufferId.get();

        EXTFramebufferObject.glBindFramebufferEXT(
                EXTFramebufferObject.GL_FRAMEBUFFER_EXT, id);
        EXTFramebufferObject.glFramebufferTexture2DEXT(
                EXTFramebufferObject.GL_FRAMEBUFFER_EXT,
                EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT,
                GL_TEXTURE_2D, texture.getID(), 0);
        EXTFramebufferObject.glBindFramebufferEXT( // un bind
                EXTFramebufferObject.GL_FRAMEBUFFER_EXT, lastID);
    }

    public static final boolean checkFBOSupport() {

        if (!Core.getInstance().isFBOSupported()) {
            Utility.error("Your graphic card doesn't support the FBO",
                    "TextureFBO()");
            return false;
        }
        return true;
    }

    public void startDrawing() {
        this.startDrawing(true);
    }
    boolean collecting;

    public void startDrawing(boolean clear) {
        if (!useAlternateMode) {
            stack.push(lastID);
            lastID = id;
            EXTFramebufferObject.glBindFramebufferEXT(
                    EXTFramebufferObject.GL_FRAMEBUFFER_EXT, id);

            glPushAttrib(GL_VIEWPORT_BIT);

            glMatrixMode(GL_PROJECTION);
            glPushMatrix();
            glLoadIdentity();

            GLU.gluOrtho2D(0, texture.getWidth(), 0, texture.getHeight());
            //GL11.glOrtho(0, texture.getWidth(), texture.getHeight(), 0, -1000, 1000);

            glMatrixMode(GL_MODELVIEW);

            glViewport(0, 0, texture.getWidth(), texture.getHeight());

            if (clear) {
                glClear(GL_COLOR_BUFFER_BIT);
            }
            glLoadIdentity();
        } else {
            glPushAttrib(GL_VIEWPORT_BIT);
            glMatrixMode(GL_PROJECTION);
            glPushMatrix();
            glLoadIdentity();
            // GL11.glOrtho(0, texture.getWidth(), 0, texture.getHeight(), -1000, 1000);
            GLU.gluOrtho2D(0, texture.getWidth(), 0, texture.getHeight());

            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
            glViewport(0, 0, texture.getWidth(), texture.getHeight());
            //glClearColor(0, 0, 0, 0);
            glClear(GL_COLOR_BUFFER_BIT);
            //Camera.getCurrentCamera().setClearColor(100 / 255f, 149 / 255f,  237 / 255f,  0f);
            
            if (!clear) {

                texture.bind();
                OpenGLState.disableBlending();
                
                GL11.glColor4f(1f, 1f, 1f, 1f);
                GL11.glBegin(GL11.GL_QUADS);
                {
                    GL11.glTexCoord2f(texture.getXStart(), texture.getYStart());
                    GL11.glVertex2f(0, 0);
                    GL11.glTexCoord2f(texture.getXEnd(), texture.getYStart());
                    GL11.glVertex2f(texture.getWidth(), 0);
                    GL11.glTexCoord2f(texture.getXEnd(), texture.getYEnd());
                    GL11.glVertex2f(texture.getWidth(), texture.getHeight());
                    GL11.glTexCoord2f(texture.getXStart(), texture.getYEnd());
                    GL11.glVertex2f(0, texture.getHeight());
                }
                GL11.glEnd();
            }
            GL11.glColor4f(1f, 1f, 1f, 1f);
                            OpenGLState.enableBlending();


            /*if (id == 0) {
            id = -1;
            texture.flipY();
            }*/

        }
        collecting = Camera.getCurrentCamera().isCollectingObjectsOnScreen();
        Camera.getCurrentCamera().setCollectingObjectsOnScreen(false);
    }

    public void endDrawing() {
        if (!useAlternateMode) {
            glPopAttrib();
            glMatrixMode(GL_PROJECTION);
            glPopMatrix();
            glMatrixMode(GL_MODELVIEW);
            lastID = stack.pop();
            EXTFramebufferObject.glBindFramebufferEXT(
                    EXTFramebufferObject.GL_FRAMEBUFFER_EXT, lastID);
            Camera.getCurrentCamera().setCollectingObjectsOnScreen(collecting);
        } else {

            // glViewport(0, 0, texture.getWidth(), texture.getHeight());
            GL11.glColor4f(1f, 1f, 1f, 1f);
            
            ImageUtils.getScrenShot(texture,true);
            glPopAttrib();

            glMatrixMode(GL_PROJECTION);
            glPopMatrix();
            glMatrixMode(GL_MODELVIEW);

            Camera.getCurrentCamera().rebind();
            // glPopMatrix();
            // glMatrixMode(GL_MODELVIEW);
            glClear(GL_COLOR_BUFFER_BIT);
        }
        // GL11.glColorMask(true, true, true, true);
    }

    public static boolean isDrawingOnTextureFBO() {
        return lastID != 0;
    }

    public ITexture getTexture() {
        return texture;
    }

    public boolean isDestroyed() {
        return bufferId == null;
    }

    public void destroy() {
        if (texture == null || bufferId == null) {
            return;
        }
        texture = null;
        bufferId.rewind();
        EXTFramebufferObject.glDeleteFramebuffersEXT(bufferId);
        bufferId = null;
    }
}

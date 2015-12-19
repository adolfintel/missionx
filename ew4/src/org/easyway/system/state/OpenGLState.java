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
package org.easyway.system.state;

import org.easyway.objects.texture.TextureID;
import org.easyway.system.Core;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public final class OpenGLState {

	public static int lastTextureID = -1;

	protected static boolean alphaTest = false;

	protected static boolean blending = false;

	protected static boolean zTest = false;

	protected static boolean vertexArray = false;

	protected static boolean colorArray = false;

	protected static boolean textureArray = false;

	protected static boolean normalArray = false;

	protected static boolean scissorTest = false;

	protected static int scissorTestCounter = 0;

        protected static int currentActiveTextureUnit;
        
	public static final void enableScissor(int x, int y, int width, int height) {
		if (!scissorTest) {
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			scissorTest = true;
		}
		++scissorTestCounter;
		if (scissorTestCounter > 1)
			GL11.glPushClientAttrib(GL11.GL_SCISSOR_BOX);
		GL11.glScissor(x, Core.getInstance().getHeight() - (y+height), width, height);
	}

	public static final void disableScissor() {
		--scissorTestCounter;
		if (scissorTest) {
			if (scissorTestCounter > 0) {
				GL11.glPopClientAttrib();
			} else {
				GL11.glDisable(GL11.GL_SCISSOR_TEST);
				scissorTest = false;
			}
		}
	}

	public static final void enableAlphaTest() {
		if (alphaTest)
			return;
		alphaTest = true;
		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}

	public static final void disableAlphaTest() {
		if (!alphaTest)
			return;
		alphaTest = false;
		GL11.glDisable(GL11.GL_ALPHA_TEST);
	}

	public static final void enableBlending() {
		if (blending)
			return;
		blending = true;
		GL11.glEnable(GL11.GL_BLEND);
	}

        public static final void useTranspBlendingMode() {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA,
                        GL11.GL_ONE_MINUS_SRC_ALPHA);
        }
        public static final void useGlassBlendingMode() {
            GL11.glBlendFunc(GL11.GL_ONE_MINUS_SRC_ALPHA,
                        GL11.GL_SRC_ALPHA);
        }
        public static final void useAdditiveBlendingMode() {
            GL11.glBlendFunc(GL11.GL_ONE,
                        GL11.GL_ONE);
        }
        public static final void useSaturateDstBlendingMode() {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA_SATURATE,
                        GL11.GL_DST_COLOR);
        }
        public static final void useSaturateSrcBlendingMode() {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA_SATURATE,
                        GL11.GL_SRC_COLOR);
        }

	public static final void disableBlending() {
		if (!blending)
			return;
		blending = false;
		GL11.glDisable(GL11.GL_BLEND);
	}

	public static final void enableZtest() {
		if (zTest)
			return;
		zTest = true;
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public static final void disableZtest() {
		if (!zTest)
			return;
		zTest = false;
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	public static final void enableVertexArray() {
		if (vertexArray)
			return;
		vertexArray = true;
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
	}

	public static final void disableVertexArray() {
		if (!vertexArray)
			return;
		vertexArray = false;
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
	}

	public static final void enableColorArray() {
		if (colorArray)
			return;
		colorArray = true;
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
	}

	public static final void disableColorArray() {
		if (!colorArray)
			return;
		colorArray = false;
		GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
	}

	public static final void enableTextureArray() {
		if (textureArray)
			return;
		textureArray = true;
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
	}

	public static final void disableTextureArray() {
		if (!textureArray)
			return;
		textureArray = false;
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
	}

	public static final void enableNormalArray() {
		if (normalArray)
			return;
		normalArray = true;
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
	}

	public static final void disableNormalArray() {
		if (!normalArray)
			return;
		normalArray = false;
		GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
	}

        public static final void useTextureUnit(int unit) {
            GL13.glActiveTexture(unit);
            currentActiveTextureUnit = unit;
        }

        public static final int getUsedTextureUnit() {
            return currentActiveTextureUnit;
        }

        public static final boolean isUsedTexureUnit( int unit ) {
            return currentActiveTextureUnit == unit;
        }

        public static final void enableRendering() {
            GL11.glDisable(GL11.GL_STENCIL_TEST);
            GL11.glColorMask(true, true, true, true);
        }

        public static final void disableRendering() {
            GL11.glColorMask(false, false, false, false);
            GL11.glEnable(GL11.GL_STENCIL_TEST);
        }

        public static final void setAdditiveRendering() {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        }


    public static void setSmoothImageState(boolean value) {
        if (value) {
            if (TextureID.USE_MIPMAP) {
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
                        GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            } else {
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
                        GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            }
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
                    GL11.GL_LINEAR);
        } else {

            if (TextureID.USE_MIPMAP) {
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
                        GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_NEAREST);
            } else {
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
                        GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            }
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
                    GL11.GL_NEAREST);
        }
    }

}

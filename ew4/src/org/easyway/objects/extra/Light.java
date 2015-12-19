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

import org.easyway.system.StaticRef;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

// TODO : SOLO AMBIENTE X ORA
/**
 * Manages the lights. <br>
 * Class in under costruction
 */
@Deprecated
public class Light {

	/** position of light */
	public float x = 0, y = 0, z = 0;

	/** color of light */
	private float red, green, blue, alpha;

	/**
	 * the value must in: GL11.GL_LIGH0 / 1 / 2 / 3 / 4 / 5 / 6 / 7
	 */
	public int number = GL11.GL_LIGHT0;

	public void setRGBA(float r, float g, float b, float a) {
		red = r;
		green = g;
		blue = b;
		alpha = a;
	}

	public void update() {
		// abilita la nuova luce
		// GL11.glDisable(number);
		if (!GL11.glIsEnabled(number)) // TODO : CONTROLLI
			GL11.glEnable(number);
		FloatBuffer diff = BufferUtils.createFloatBuffer(4);
		float lightAmbient[] = { red, green, blue, alpha };
		diff.put(lightAmbient);

		FloatBuffer pos = BufferUtils.createFloatBuffer(4);

		FloatBuffer fb = BufferUtils.createFloatBuffer(16);
		fb.rewind();
		StaticRef.getCamera().matrix.store(fb);
		fb.flip();
		GL11.glLoadMatrix(fb);
		float lightPosition[] = { x - StaticRef.getCamera().x,
				y - StaticRef.getCamera().y, z - StaticRef.getCamera().z, 1.0f };
		pos.put(lightPosition);

		// TODO : ...DA FARE...
		// FloatBuffer ambi = BufferUtils.createFloatBuffer(4);
		// float lightDiffuse[] = { 0.2f, 0.2f, 0.2f, 1f };
		// ambi.put(lightDiffuse);
		//		
		FloatBuffer spec = BufferUtils.createFloatBuffer(4);
		float lightSpecular[] = { 0.5f, 0.5f, 0.5f, 1.0f };
		spec.put(lightSpecular);

//		GL11.glLight(number, GL11.GL_AMBIENT, (FloatBuffer) ambi.flip());
		// TODO: da generalizzare
		GL11.glLight(number, GL11.GL_DIFFUSE, (FloatBuffer) diff.flip());
		// TODO .. DA FARE..
		GL11.glLightf(number, GL11.GL_CONSTANT_ATTENUATION, 0.1f);
		GL11.glLightf(number, GL11.GL_LINEAR_ATTENUATION, 0.05f);
		GL11.glLight(number, GL11.GL_SPECULAR, (FloatBuffer) spec.flip());
		// // diff.rewind();
		// GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SHININESS,
		// (FloatBuffer)diff);
		GL11.glMateriali(GL11.GL_FRONT, GL11.GL_SHININESS, 96);
		GL11.glLight(number, GL11.GL_POSITION, (FloatBuffer) pos.flip());
	}

	public static void init() {
		GL11.glEnable(GL11.GL_LIGHTING);
		// GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glLightModeli(GL11.GL_LIGHT_MODEL_TWO_SIDE, 0);
	}

}

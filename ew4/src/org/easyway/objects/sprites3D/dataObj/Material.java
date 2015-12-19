/* EasyWay Game Engine
 * Copyright (C) 2006 Daniele Paggi.
 *  
 * Written by: 2006 Daniele Paggi<dshnt@hotmail.com>
 *   
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public License
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
package org.easyway.objects.sprites3D.dataObj;

import java.nio.FloatBuffer;

import org.easyway.objects.texture.Texture;
import org.lwjgl.BufferUtils;

public class Material {

	/**
	 * the name of material
	 */
	public String name;

	/**
	 * emission color
	 */
	public float red, green, blue;

	/** specular ??? */
	public float scaleFactor = 1.0f;

	public FloatBuffer specular;

	public FloatBuffer ambient;

	public FloatBuffer diffuse;

	public float shinenss = 0.0f;

	public float transparency = 1.0f;

	public Texture texture;

	public Material() {
		specular = BufferUtils.createFloatBuffer(16);
		specular.rewind();
		ambient = BufferUtils.createFloatBuffer(16);
		ambient.rewind();
		diffuse = BufferUtils.createFloatBuffer(16);
		diffuse.rewind();
	}

}

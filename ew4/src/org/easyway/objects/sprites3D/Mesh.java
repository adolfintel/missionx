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
package org.easyway.objects.sprites3D;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.easyway.interfaces.base.IDestroyable;
import org.easyway.interfaces.base.IPureRender;
import org.lwjgl.opengl.GL11;

public class Mesh implements IPureRender, IDestroyable {

	boolean destroyed = false;

	public FloatBuffer vertexPointer, colorPointer, normalPointer;

	public IntBuffer indexBuffer;
	
	/** vertici */
	public float vertex[];

	public String name;
	
	public void render() {
		// impostazione dati
		assert indexBuffer != null;
		assert vertexPointer != null;

		if (colorPointer != null) {
			GL11.glColorPointer(3, 0, colorPointer);
		}
		//vertexPointer.flip();
		GL11.glVertexPointer(3, 0, vertexPointer);
		if (normalPointer != null) {
			GL11.glNormalPointer(0, normalPointer);
		}

		//indexBuffer.flip();
		// rendering dati
		GL11.glDrawElements(GL11.GL_TRIANGLES, indexBuffer);
	}

	public void destroy() {
		if (destroyed)
			return;
		destroyed = true;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	/** disabled */
	public int getLayer() {
		return 0;
	}

}

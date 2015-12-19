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

/**
 * 
 */
package org.easyway.ueditor2.commands.user;

import org.easyway.ueditor2.commands.Command;
import org.easyway.input.Mouse;
import org.lwjgl.opengl.GL11;

/**
 * @author Proprietario
 * 
 */
public class GetDistance extends Command {

	/**
	 * @return float[0]: signed width<br>
	 *         float[1]: signed height<br>
	 *         float[2]: lenght of diag return <b>null<b> on cancel action<br>
	 * 
	 * 
	 */
	public static float[] getDistance() {
		GetDistance distance = new GetDistance();
		distance.xy = GetPoint.getPoint();
		if (distance.xy == null) {
			return null;
		}

		distance.xy2 = GetPoint.getPoint();
		if (distance.xy2 == null) {
			return null;
		}

		if (distance.exit) {
			return null;
		}
		distance.exit = true;
		float a = distance.xy[0] - distance.xy2[0];
		float b = distance.xy[1] - distance.xy2[1];
		float len = (float) Math.sqrt(a * a + b * b);
		return new float[] { distance.xy[0] - distance.xy2[0],
				distance.xy[1] - distance.xy2[1], len };

	}

	public boolean exit = false;

	public float xy[], xy2[];

	public void creation() {
	}

	public void loop() {
		if (Mouse.isRightDown()) {
			exit = true;
			destroy();
			return;
		}
		if (exit) {
			destroy();
		}
	}

	public void render() {
		if (xy==null)
			return;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glColor3f(1, 0.5f, 0.5f);
		{
			GL11.glVertex2f(xy[0], xy[1]);
			GL11.glVertex2f(Mouse.getX(), Mouse.getY());
			GL11.glColor3f(1, 1, 1);
			GL11.glVertex2f(xy[0], xy[1]);
			GL11.glVertex2f(Mouse.getX(), xy[1]);
			GL11.glVertex2f(xy[0], xy[1]);
			GL11.glVertex2f(xy[0], Mouse.getY());
			GL11.glVertex2f(Mouse.getX(), xy[1]);
			GL11.glVertex2f(Mouse.getX(), Mouse.getY());
			GL11.glVertex2f(xy[0], Mouse.getY());
			GL11.glVertex2f(Mouse.getX(), Mouse.getY());
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

}

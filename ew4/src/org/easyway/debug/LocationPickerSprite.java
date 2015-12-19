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
package org.easyway.debug;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.easyway.interfaces.extended.ILayer;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.interfaces.extended.IRender;
import org.easyway.objects.BaseObject;
import org.easyway.system.StaticRef;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class LocationPickerSprite extends BaseObject implements IRender,
		ILayer, ILoopable {

	private static final long serialVersionUID = 4338976455194073436L;

	public int stepx;

	public int stepy;

	Lock lock;

	Condition condition;

	public LocationPickerSprite(Lock lock, Condition condition) {
		this.lock = lock;
		this.condition = condition;
	}

	public void render() {
		final int sx = stepx;
		final int sy = stepy;
		final int width = StaticRef.getCamera().getWidth() + sx * 2;
		final int height = StaticRef.getCamera().getHeight() + sy * 2;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glLineWidth(2);
		GL11.glColor3f((float) Math.random() * 0.5f,
				(float) Math.random() * 0.5f, (float) Math.random() * 0.5f);
		GL11.glBegin(GL11.GL_LINES);
		for (int x = -(int) StaticRef.getCamera().x % sx; x < width; x += sx) {
			GL11.glVertex2f(x, 0);
			GL11.glVertex2f(x, height);
		}
		for (int y = -(int) StaticRef.getCamera().y % sy; y < height; y += sy) {
			GL11.glVertex2f(0, y);
			GL11.glVertex2f(width, y);
		}
		int kx = (int) StaticRef.getCamera().x % sx;
		int ky = (int) StaticRef.getCamera().y % sy;
		int mx = ((int) ((Mouse.getX() + kx) / sx)) * sx - kx;
		int my = ((int) ((StaticRef.getCamera().getHeight() - (Mouse.getY() - ky)) / sy))
				* sy - ky;
		GL11.glVertex2f(mx, my);
		GL11.glVertex2f(mx + sx, my + sy);
		GL11.glVertex2f(mx + sx, my);
		GL11.glVertex2f(mx, my + sy);

		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	boolean keydown = false;

	boolean lastkey = false;

	public void loop() {
		lastkey = keydown;
		if (Mouse.isButtonDown(0)) { // left
			keydown = true;
		} else
			keydown = false;

		if (lastkey && !keydown) { // onRelease
			int kx = (int) StaticRef.getCamera().x % stepx;
			int ky = (int) StaticRef.getCamera().y % stepy;
			int mx = ((int) ((Mouse.getX() + kx) / stepx)) * stepx - kx;
			int my = ((int) ((StaticRef.getCamera().getHeight() - (Mouse.getY() - ky)) / stepy))
					* stepy - ky;
			Editor.x = mx + (int) StaticRef.getCamera().x;
			Editor.y = my + (int) StaticRef.getCamera().y;
			lock.lock();
			condition.signalAll();
			lock.unlock();
			destroy();
		}

		if (Mouse.isButtonDown(1)) { // righ
			int x = Mouse.getX();
			int y = StaticRef.getCamera().getHeight() - Mouse.getY();
			if (x > StaticRef.getCamera().getWidth() / 2 + 128)
				StaticRef.getCamera().x += 4;

			if (x < StaticRef.getCamera().getWidth() / 2 - 128)
				StaticRef.getCamera().x -= 4;

			if (y > StaticRef.getCamera().getHeight() / 2 + 128)
				StaticRef.getCamera().y += 4;
			else if (y < StaticRef.getCamera().getHeight() / 2 - 128)
				StaticRef.getCamera().y -= 4;
		}
	}

	public int getLayer() {
		// TODO Auto-generated method stub
		return 99999999;
	}

}

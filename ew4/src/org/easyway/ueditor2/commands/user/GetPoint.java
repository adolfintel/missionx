/* EasyWay Game Engine
 * Copyright (C) 2007 Daniele Paggi.
 *  
 * Written by: 2007 Daniele Paggi<dshnt@hotmail.com>
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
package org.easyway.ueditor2.commands.user;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.easyway.ueditor2.commands.Command;
import org.easyway.ueditor2.EditorCore;
// import org.easyway.editor2.forms.southPanel.SouthOptionPanel;
import org.easyway.input.Keyboard;
import org.easyway.input.Mouse;
import org.easyway.system.StaticRef;
import org.easyway.utils.Utility;
import org.lwjgl.opengl.GL11;

public class GetPoint extends Command {

	static Condition getPointCondition;

	static Lock lock;

	/**
	 * TODO: comment
	 * @return [0,1] -> screen [2,3] -> world
	 */
	public static float[] getPoint() {
		System.out.println("command: click in the game for taking a position");
		lock = new ReentrantLock();
		getPointCondition = lock.newCondition();
		GetPoint gp = new GetPoint();
		EditorCore.canSelect = false;
        EditorCore.setCanMoveObject(false);
        EditorCore.setCanSelect(false);
		try {
			lock.lock();
			getPointCondition.await();
			lock.unlock();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(" Position taken      [x:" + gp.xOnScreen + ", y:"
				+ gp.yOnScreen + ", xw:" + gp.xInWorld + ", yw:" + gp.yInWorld
				+ "]");
		EditorCore.setCanSelect(true);
        EditorCore.setCanMoveObject(true);
		if (gp.exit)
			return null;
		return new float[] { gp.xOnScreen, gp.yOnScreen, gp.xInWorld,
				gp.yInWorld };
	}

	public float xOnScreen, yOnScreen, xInWorld, yInWorld;

	public boolean exit = false;

	protected GetPoint() {
		super();

	}

	public void creation() {
		// Mouse.hide();
	}

	boolean down = false;

	float mousex, mousey;

	public void loop() {
		Utility.drawRect(10, 10, 100, 100, 1, 1, 1);
		if (Mouse.isRightDown() || Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			exit = true;
			lock.lock();
			getPointCondition.signal();
			lock.unlock();
			destroy();
			return;
		}
		if (down && !Mouse.isLeftDown()) {
			xOnScreen = mousex;// Mouse.getX();
			xInWorld = mousex + StaticRef.getCamera().getX();// Mouse.getXinWorld();
			yOnScreen = mousey;// Mouse.getY();
			yInWorld = mousey + StaticRef.getCamera().getY();// Mouse.getYinWorld();
			lock.lock();
			getPointCondition.signal();
			lock.unlock();
			destroy();
		}
		down = Mouse.isLeftDown();
	}

	public void render() {
		int w = EditorCore.getWidthGrid();
		int h = EditorCore.getHeightGrid();
		mousex = Mouse.getX();
		mousey = Mouse.getY();
		//System.out.println("mousex: "+mousex);
		if (EditorCore.snapToGrid) {
			float sx = StaticRef.getCamera().getX() % w;
			float sy = StaticRef.getCamera().getY() % h;
			mousex = ((int) ((mousex + sx) / w) * w) - sx;
			mousey = ((int) ((mousey + sy) / h) * h) - sy;
		}
		GL11.glLoadIdentity();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glLineWidth(3);
		GL11.glColor3f((float) Math.random() * 0.5f,
			(float) Math.random() * 0.5f, (float) Math.random() * 0.5f);
		GL11.glBegin(GL11.GL_LINES);
		{
			GL11.glVertex2f(mousex - 15, mousey - 15);
			GL11.glVertex2f(mousex + 15, mousey + 15);

			GL11.glVertex2f(mousex - 15, mousey + 15);
			GL11.glVertex2f(mousex + 15, mousey - 15);
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public void destroy() {
		super.destroy();
		// Mouse.show();
	}

}

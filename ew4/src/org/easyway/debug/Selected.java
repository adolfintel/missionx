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

import org.easyway.interfaces.extended.ILayer;
import org.easyway.interfaces.extended.IRender;
import org.easyway.interfaces.sprites.IPlain2D;
import org.easyway.objects.sprites2D.SimpleSprite;
import org.easyway.system.StaticRef;
import org.easyway.utils.TimerJGM;
import org.lwjgl.opengl.GL11;

public class Selected extends SimpleSprite implements IRender, ILayer {

	private static final long serialVersionUID = 8794618090454791734L;

	public static Selected last;

	IPlain2D attracked;

	public Selected(IPlain2D attracked) {
		this.attracked = attracked;
		if (last != null)
			last.destroy();
		last = this;
		new MyTimeOut(this).start();
	}

    @Override
	public int getLayer() {
		return 999999;
	}

    @Override
	public void render() {
		setXY( attracked.getX() - StaticRef.getCamera().x,attracked.getY() - StaticRef.getCamera().y);
		setSize( attracked.getWidth(), attracked.getHeight());
		//GL11.glLoadIdentity();
		GL11.glLineWidth(5);
		GL11.glColor3f((float) Math.random(), (float) Math.random(),
				(float) Math.random());
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_LINES);
		float w = getWidth();
		float h = getHeight();
                float x = getX();
                float y = getY();
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x+w, y);
		GL11.glVertex2d(x, y+h);
		GL11.glVertex2d(x+w, y+h);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, y+h);
		GL11.glVertex2d(x+w, y);
		GL11.glVertex2d(x+w, y+h);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	class MyTimeOut extends TimerJGM {
		Selected obj;

		public MyTimeOut(Selected obj) {
			super(5000);
			this.obj = obj;
		}

		public void onTick() {
			obj.destroy();
			stop();
		}
	}

}

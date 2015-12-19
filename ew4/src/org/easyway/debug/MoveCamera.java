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

import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.BaseObject;
import org.easyway.system.StaticRef;
import org.lwjgl.input.Mouse;

public class MoveCamera extends BaseObject implements ILoopable {

	private static final long serialVersionUID = 4606516322480211376L;

	public MoveCamera() {
		StaticRef.getCamera().attrack = false;
	}

	public void loop() {
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

}
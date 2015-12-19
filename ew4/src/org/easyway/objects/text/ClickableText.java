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
package org.easyway.objects.text;

import org.easyway.interfaces.sprites.IClickable;
import org.easyway.interfaces.sprites.IFont;
import org.easyway.objects.sprites2D.Mask;

public abstract class ClickableText extends Text implements IClickable {
	
	public ClickableText(int x, int y, String text, IFont font) {
		super(x, y, text, font);
	}

	public Mask getMask() {
		// we don't need a mask:
		// the collision of IClickable can miss the masks
		// caution: the object will be ONLY clickable but
		// it will be NOT collisionable with Sprites.
		return null;
	}
}

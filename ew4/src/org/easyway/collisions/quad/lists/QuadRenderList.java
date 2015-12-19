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
package org.easyway.collisions.quad.lists;

import java.util.ArrayList;

import org.easyway.interfaces.base.IPureRender;
import org.easyway.interfaces.extended.IRender;

public class QuadRenderList extends ArrayList<IPureRender> implements
		IPureRender {

	private static final long serialVersionUID = 6795158874040362804L;

	protected int indexScan = -1;

	public void render() {
		for (indexScan = size() - 1; indexScan >= 0; --indexScan) {
			((IRender) get(indexScan)).render();
		}
		indexScan = -1;

	}

	public boolean remove(Object o) {
		removeElementAt(indexOf(o));
		return true;
	}

	public void removeElementAt(int index) {
		if (indexScan > index) {
			--indexScan;
		}
		super.remove(index);
	}

	public void add(int index, IPureRender obj) {
		if (index < 0)
			return;
		super.add(index, obj);
		if (indexScan >= index)
			++indexScan;
	}

}

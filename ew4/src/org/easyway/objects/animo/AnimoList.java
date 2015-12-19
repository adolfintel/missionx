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
package org.easyway.objects.animo;

import java.io.Serializable;
import java.util.ArrayList;

import org.easyway.interfaces.base.IPureLoopable;

public class AnimoList implements IPureLoopable, Serializable {
	private static final long serialVersionUID = 3102084064231853449L;

	protected ArrayList<Animo> list;

	/**
	 * the total elasped time
	 */
	// protected long totalElTime;
	public AnimoList() {
		list = new ArrayList<Animo>(100);
		// totalElTime = 0;
	}

	public void add(Animo animo) {
		list.add(animo);
		// We can optimize the following code..
		// using heap
		// int t = list.size();
		// if (t == 0) {
		// list.add(animo);
		// totalElTime += animo.getFrame();
		// return;
		// }
		// // 0.3.3.1 getElaspedTile --> getFrame
		// if (list.get(t-1).getFrame()-list.get(t-1).getElaspedTime() <=
		// animo.getFrame()) {
		// list.add(animo);
		// return;
		// }
		// for (int i = 0; i < list.size(); ++i) {
		// // 0.3.3.1 getElaspedTile --> getFrame
		// if (list.get(i).getFrame()-list.get(i).getElaspedTime() >
		// animo.getFrame()) {
		// list.add(i, animo);
		// return;
		// }
		// }
		// Utility.error(
		// "found a BUG in the AnimoList, please report this LINE",
		// "AnimoList.add(Animo)");
		// assert false : "found a bug in the AnimoList.add(Animo)";
	}

	public Animo get(int index) {
		return list.get(index);
	}

	public void remove(Animo animo) {
		// int index = list.indexOf(animo);
		// if (index == 0) {
		// totalElTime = animo.elaspedTime;
		// for (int i=1; i<list.size(); ++i) {
		// list.get(i).elaspedTime += totalElTime;
		// // if elaspedTime > cacheTime we'll remove the animo in the next loop
		// // assert list.get(i).elaspedTime < list.get(i).cacheTime : "Found
		// Bug on AnimoList";
		// }
		// }
		// if (list.size()>1)
		// totalElTime = list.get(1).elaspedTime;
		// list.remove(index);
		list.remove(animo);
	}

	public void remove(int index) {
		// if (index == 0) {
		// Animo animo = list.get(0);
		// totalElTime = animo.elaspedTime;
		// for (int i=1; i<list.size(); ++i) {
		// list.get(i).elaspedTime += totalElTime;
		// // if elaspedTime > cacheTime we'll remove the animo in the next loop
		// // assert list.get(i).elaspedTime < list.get(i).cacheTime : "Found
		// Bug on AnimoList";
		//
		// }
		// }
		// if (list.size()>1)
		// totalElTime = list.get(1).elaspedTime;
		list.remove(index);
	}

	public void loop() {
		for (Animo a : list) {
			a.loop();
		}

		// if (list.size() > 0) {
		// Animo tanimo = list.get(0);
		// // long rem = StaticRef.core.getElaspedTime() - (tanimo.cacheTime
		// // - tanimo.elaspedTime);
		// tanimo.elaspedTime += StaticRef.core.getElaspedTime(); // animo -
		// // loop
		// if (tanimo.elaspedTime >= tanimo.cacheTime) { // animo - loop test
		// ArrayList<Animo> tList = new ArrayList<Animo>(list.size() / 10);
		// totalElTime = list.get(0).elaspedTime;
		// tanimo.next(false);
		//
		// Animo tanimo2;
		// for (int i = 0; i < list.size(); ++i) {
		// (tanimo2 = list.get(i)).elaspedTime += totalElTime;
		// if (tanimo2.elaspedTime >= tanimo2.cacheTime) {
		// tanimo2.next(false);
		// --i;
		// tList.add(tanimo2);
		// }
		// }
		// for (Animo ta : tList) {
		// ta.addToList();
		// }
		// tanimo.addToList();
		// }
		// }
	}
	
	public void addElementsFrom( AnimoList list ) {
		for (int i = list.size() - 1; i >= 0; --i)
			add(list.get(i));
	}
	
	public int size() {
		return list.size();
	}
}

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

import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Editor {
	public static boolean started = false;

	public static final Vector<Class> classes = new Vector<Class>();
	
	public static int x;
	public static int y;

	public void add(Class mclass) {
		classes.add(mclass);
	}

	public void start() {
		if (started)
			return;
		started = true;
		LocationPicker.lastsnapx = 64;
		LocationPicker.lastsnapy = 64;
		new EditorForm();
	}
	
//	public static final ITexture getTexture() {
//		Lock lock = new ReentrantLock();
//		Condition condition = lock.newCondition();
//		ITexture texture = null;
//		TexturePicker tp = new TexturePicker(Thread.currentThread(), lock, condition);
//		lock.lock();
//		try {		
//			condition.await();
//			tp.setVisible(false);
//			
//			texture = tp.tps.texture;
//			tp.tps.destroy();
//			tp.dispose();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		System.out.println("Texture taken");
//		lock.unlock();
//		return texture;
//	}
	
	public static final void getLocation() {
		getLocation(LocationPicker.lastsnapx,LocationPicker.lastsnapy);
	}


	public static final void getLocation(int sizex, int sizey) {
		Lock lock = new ReentrantLock();
		Condition condition = lock.newCondition();
		
		lock.lock();
		try {
			LocationPicker lp = new LocationPicker(Thread.currentThread(), lock, condition,sizex,sizey);
			condition.await();
			lp.setVisible(false);
			lp.dispose();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Position taken (X: "+getX()+" Y: "+getY()+")");
		lock.unlock();
	}
	
	public static final String getText(String message) {
		Lock lock = new ReentrantLock();
		Condition condition = lock.newCondition();
		String text=null;
		lock.lock();
		try {
			StringPicker sp = new StringPicker(Thread.currentThread(), lock, condition,message);
			condition.await();
			text = sp.text;
			sp.setVisible(false);
			sp.dispose();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("String taken: "+text);
		lock.unlock();
		
		return text;
	}
	
	public static final int getX() {
		return x;
	}
	
	public static final int getY() {
		return y;
	}


}

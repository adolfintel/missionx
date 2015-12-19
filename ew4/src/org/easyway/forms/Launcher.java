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

package org.easyway.forms;

import java.lang.reflect.Constructor;

import org.easyway.utils.Utility;

public /*abstract*/ class Launcher {

	protected LauncherForm launcher;

	protected Class gameClass;
	

	protected Constructor gameConstructor;

	public Launcher(String gameName, Class gameClass) {
		this.gameClass = gameClass;
		// TODO: we can think that the Core isn't the first superclass but the second.. third.. etc.
		// we should insert a cycle here.
		if (!gameClass.getSuperclass().getName().equals("org.easyway.system.Core")) {
			Utility
					.error(
							"error using the Launcher class\nVisit our Forum for more information!",
							"public Launcher(String,Class)");
			System.exit(1);
		}
		Constructor[] con = gameClass.getConstructors();
		Class[] par;
		gameConstructor = null;
		for (Constructor c : con) {
			if ((par = c.getParameterTypes()).length != 4)
				continue;
			if (par[0].getName().equals("int") 
					&& par[1].getName().equals("int")
					&& par[2].getName().equals("int")
					&& par[3].getName().equals("boolean")) {
				gameConstructor = c;
				break;
			}
		}
		if (gameConstructor == null) {
			Utility
					.error(
							"error using the Launcher class\nVisit our Forum for more information!\nYou must implement a constructor like: Core(int,int,int,boolean)",
							"public Launcher(String,Class)");
			System.exit(1);
		}

		(launcher = new LauncherForm(this)).setVisible(true);
		launcher.setNameGame(gameName);
	}

	protected void preLunch(int width, int height, int bpp, boolean fullscreen) {
		launcher.setVisible(false); // hide window
		launcher = null; // destry (?)
		try {
			gameConstructor.newInstance(width, height, bpp, fullscreen);
		} catch (Exception e) {
			Utility.error("Error making the Game!", e);
		}
//		lunch();
	}

//	public abstract void lunch();

}

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
package org.easyway.system;

import java.util.Timer;
import java.util.TimerTask;

import org.easyway.system.state.GameState;
import org.easyway.utils.Utility;

/**
 * 
 * The TimeOutOpController is a Class that monitore the correct works of Game
 * Engine<br>
 * If the game engine will break, for example going in a DeadLock, the
 * TimeOutOpController<br>
 * simply close the Game.<br>
 * ~�eta~
 * 
 * @author Daniele Paggi
 * 
 */
public class TimeOutOpController extends TimerTask {
	/** timer that active the warnings */
	Timer timer;

	/** references to Sincro */
	Sincro sincro;

	/** indicates the delay of warnings */
	protected int delay;

	protected boolean blockedA, blockedB;

	/**
	 * crates a new instance of TimeOutOpController
	 * 
	 * @param delay
	 *            delay in ms
	 * @param sincro
	 *            reference to Sincro
	 */
	public TimeOutOpController(int delay, Sincro sincro) {
		timer = new Timer();
		this.delay = delay;
		this.sincro = sincro;
	}

	/** starts the timer */
	public void start() {
		timer.schedule(this, (long) delay, (long) delay);
	}

	/** stops the timer */
	public void stop() {
		timer.cancel();
		timer = null;
	}

	public synchronized void stateA() {
		blockedA = true;
	}

	public synchronized void stateB() {
		blockedB = blockedA = false;
	}

	/** this methods is called by java any 'dalay' ms */
	// @SuppressWarnings("deprecation")
	public synchronized void run() {
		if (!blockedB) {
			blockedB = blockedA;
			return;
		}
		if (!blockedA)
			return;

		System.out.println("number of drawable objects: "
				+ GameState.getCurrentGEState().getLayers()[3].size());
		System.out.println("number of loopable objects: "
				+ GameState.getCurrentGEState().getLoopList().size());
		Utility.ignoreError("BaseList.getCurrent");
		if (GameState.getCurrentGEState().getLoopList().getCurrent() != null) {
		System.out 
			.println("current loopable object: "
					+ GameState.getCurrentGEState().getLoopList().getCurrent()
					+ " | "
					+ GameState.getCurrentGEState().getLoopList().getCurrent().getClass()
						.getCanonicalName());
		}
		Utility.allowsError("BaseList.getCurrent");
		StackTraceElement trace[] = sincro.getStackTrace();
		System.out.println("CURRENT STACK:");
		for (StackTraceElement stack : trace) {
			System.out.println(stack.getLineNumber() + " :\t"
					+ stack.getMethodName() + " ( " + stack.getClassName()+" )");
		}
		System.out.println("The Game has expired the time");
		// stop();
		Runtime.getRuntime().halt(1);
	}

}

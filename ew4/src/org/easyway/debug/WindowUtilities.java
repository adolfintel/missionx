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

import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * A few utilities that simplify using windows in Swing. 1998-99 Marty Hall,
 * http://www.apl.jhu.edu/~hall/java/
 */

public class WindowUtilities {

	/**
	 * Tell system to use native look and feel, as in previous releases. Metal
	 * (Java) LAF is the default otherwise.
	 */

	public static void setNativeLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Error setting native LAF: " + e);
		}
	}

	public static void setJavaLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Error setting Java LAF: " + e);
		}
	}

	public static void setMotifLookAndFeel() {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		} catch (Exception e) {
			System.out.println("Error setting Motif LAF: " + e);
		}
	}

	/**
	 * A simplified way to see a JPanel or other Container. Pops up a JFrame
	 * with specified Container as the content pane.
	 */

	public static JFrame openInJFrame(Container content, int width, int height,
			String title, Color bgColor) {
		JFrame frame = new JFrame(title);
		frame.setBackground(bgColor);
		content.setBackground(bgColor);
		frame.setSize(width, height);
		frame.setContentPane(content);
		//frame.addWindowListener(new javax.swing.event.());
		frame.setVisible(true);
		return (frame);
	}

	/** Uses Color.white as the background color. */

	public static JFrame openInJFrame(Container content, int width, int height,
			String title) {
		return (openInJFrame(content, width, height, title, Color.white));
	}

	/**
	 * Uses Color.white as the background color, and the name of the Container's
	 * class as the JFrame title.
	 */

	public static JFrame openInJFrame(Container content, int width, int height) {
		return (openInJFrame(content, width, height, content.getClass()
				.getName(), Color.white));
	}
}

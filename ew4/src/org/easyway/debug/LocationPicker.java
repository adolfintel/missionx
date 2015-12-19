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

import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import javax.swing.JFrame;

public class LocationPicker extends JFrame {

	private static final long serialVersionUID = 2251403913288874684L;

	LocationPickerSprite lps;
	
	public static int lastsnapx, lastsnapy;

	public LocationPicker(final Thread thread, Lock lock, Condition condition,int sx, int sy) {
		super("Location Picker");
		lps = new LocationPickerSprite(lock, condition);
		lastsnapx = lps.stepx = sx;
		lastsnapy = lps.stepy = sy;
		Button button;
		final TextField textx;
		final TextField texty;

		setSize(200, 200);
		setLayout(new GridLayout(3, 2, 5, 5));
		Container panel = getContentPane();
		panel.setBackground(Color.PINK);
		panel.add(new Label("snap x"));
		panel.add(textx = new TextField(""+sx));
		panel.add(new Label("snap y"));
		panel.add(texty = new TextField(""+sy));
		panel.add(button = new Button("Update"));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lastsnapx = lps.stepx = Integer.parseInt(textx.getText());
				lastsnapy = lps.stepy = Integer.parseInt(texty.getText());
			}
		});
		button.setBackground(Color.GREEN);
		panel.add(button = new Button("Cancel"));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lps.destroy();
				thread.stop();
				setVisible(false);
				dispose();
			}
		});
		button.setBackground(Color.RED);
		setVisible(true);
		// closing
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				lps.destroy();
				thread.stop();
				setVisible(false);
				dispose();
			}
		});
	}
}

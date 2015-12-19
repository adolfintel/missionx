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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JFrame;

public class EditorForm extends JFrame {

	private static final long serialVersionUID = 2660263493686231538L;

	Vector listaThreadAttivi = new Vector();

	MoveCamera moveCamera;

	public EditorForm() {
		super("Editor Form v.0.01beta");
		setSize(200, 200);
		setLayout(new GridLayout(0, 1));
		Button button;

		for (int i = 0; i < Editor.classes.size(); i++) {
			final Object o = Editor.classes.get(i);
			getContentPane().add(button = new Button(o.toString()));
			button.setBackground(Color.PINK);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new Thread() {
						public void run() {
							listaThreadAttivi.add(this);
							Thread.currentThread().setPriority(
									Thread.MIN_PRIORITY);
							try {
								((Class) o).getMethod("EDITOR", new Class[]{}).invoke(new Object[]{}, new Object[]{});
							} catch (Exception ee) {
								ee.printStackTrace();
							}
							listaThreadAttivi.remove(this);
						}
					}.start();
				}
			});
		}

		getContentPane().add(button = new Button("destroy Threads"));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < listaThreadAttivi.size(); i++) {
					((Thread) listaThreadAttivi.get(0)).stop();
				}
			}
		});
		
		getContentPane().add(button = new Button("pick up objects"));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ObjectPicker();
			}
		});


		final Button mbutton;
		getContentPane().add(mbutton = new Button("move Camera"));
		mbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (moveCamera == null) {
					moveCamera = new MoveCamera();
					mbutton.setLabel("STOP move Camera");
				} else {
					mbutton.setLabel("move Camera");
					moveCamera.destroy();
					moveCamera = null;
				}
			}
		});
		
		getContentPane().add(button = new Button("Save C:/data.dat"));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new SaverGame();
			}
		});
		
		getContentPane().add(button = new Button("Load C:/data.dat"));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new LoaderGame();
			}
		});	
	

		validate();
		setVisible(true);
	}
}

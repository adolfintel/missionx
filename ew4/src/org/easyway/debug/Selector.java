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

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.easyway.interfaces.base.IBaseObject;
import org.easyway.lists.BaseList;
import org.easyway.system.StaticRef;

public class Selector extends JFrame {
	private static final long serialVersionUID = -2507439178093988491L;

	/** indicates if the game is in debug mode or not */
	// public static boolean debug = false;
	private BaseList baseList;

	private Vector vector;

	JList list;

	public static Object selected;

	public Field toedit;

	public Object otoedit;

	// TextField input;

	// static IPlain oldCenter;

	// static boolean oldAttrack;

	// static float oldx;

	// static float oldy;
	//
	// static int count = 0;
	//
	// static boolean cameraSetted;

	public Selector(Field toedit, Object otoedit) {
		super("Selector manager");
		this.toedit = toedit;
		this.otoedit = otoedit;
		// count++;
		create();
	}

	public Selector(Vector vector) {
		super("Selector manager");
		this.vector = vector;
		// count++;
		create();
	}

	public Selector(BaseList baseList) {
		super("Selector manager");
		this.baseList = baseList;
		// count++;
		create();
	}

	private void create() {
		getContentPane().removeAll();
		lastObject = currObject;
		if (vector != null)
			currObject = vector;
		else
			currObject = baseList;
		setSize(224, 500);

		Container content = getContentPane();
		content.setBackground(Color.PINK);
		content.setLayout(new FlowLayout());
		JPanel pulsanti = new JPanel();
		pulsanti.setLayout(new GridLayout(0, 2));

		if (baseList != null || vector != null) {
			Vector data = new Vector();

			if (baseList != null) {
				baseList.startScan();
				IBaseObject obj;
				while (baseList.next()) {
					obj = baseList.getCurrent();
					data.add(obj.getClass().getName());
				}
			} else {
				Object obj;
				for (int i = 0; i < vector.size(); i++) {
					obj = (Object) vector.get(i);
					data.add(obj.getClass().getName());
				}
			}

			list = new JList(data);
			JScrollPane scrollPane = new JScrollPane(list);
			scrollPane.getViewport().setView(list);
			content.add(scrollPane);

			// pulsanti
			Button continua = new Button("edit");
			continua.addActionListener(new ActionListener() { // input
						public void actionPerformed(ActionEvent evt) {
							onContinue();
						}
					});
			pulsanti.add(continua);

		}
		Button camera = new Button("open StaticRef");
		camera.addActionListener(new ActionListener() { // input
					public void actionPerformed(ActionEvent evt) {
						if (vector != null)
							currObject = vector;
						if (baseList != null)
							currObject = baseList;
						showFields(new StaticRef());
					}
				});
		pulsanti.add(camera);

		pulsanti.add(new RedroButton());

		content.add(pulsanti);

		setVisible(true);

		// closing
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});

	}

	private void onContinue() {
		Object o;
		if (baseList != null)
			o = baseList.get(baseList.size() - list.getSelectedIndex() - 1);
		else
			o = vector.get(list.getSelectedIndex());
		selected = o;
		showFields(o);
	}

	private boolean isPrimitive(Object o) {
		if (o instanceof Double)
			return true;
		if (o instanceof Float)
			return true;
		if (o instanceof Integer)
			return true;
		if (o instanceof Long)
			return true;
		if (o instanceof Character)
			return true;
		if (o instanceof String)
			return true;
		if (o instanceof Byte)
			return true;
		if (o instanceof Short)
			return true;
		if (o instanceof Boolean)
			return true;
		return o.getClass().isPrimitive();
	}

	private Object lastObject;

	private Object currObject;

	public void showFields(Object o) {
		lastObject = currObject;
		currObject = o;
		selected = o;
		getContentPane().removeAll();
		getContentPane().setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		JPanel spanel = new JPanel();

		panel.setPreferredSize(new Dimension(224, 0));

		panel.setLayout(new GridLayout(0, 2));
		if (o == null)
			return;
		if (o instanceof BaseList) {
			// new DebugManager((BaseList) o);
			this.vector = null;
			this.baseList = (BaseList) o;
			create();
		} else if (o instanceof Vector) {
			// new DebugManager((Vector) o);
			this.vector = (Vector) o;
			this.baseList = null;
			create();

		} else {
			if (isPrimitive(o)) {
				Label l;
				panel.add(l = new Label("OUTPUT:"));
				l.setBackground(Color.CYAN);
				panel.add(l = new Label(o.toString()));
				l.setBackground(Color.CYAN);
			}
			Field[] f = o.getClass().getFields();
			for (int i = 0; i < f.length; i++) {
				if (isPrimitive(f.getClass()))
					continue;
				panel.add(new EditButton(f[i], o));
				try {
					String s;
					if (f[i].get(o) == null)
						s = "null";
					else
						s = f[i].get(o).toString();
					panel.add(new Label(s));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			Method[] m = o.getClass().getMethods();
			for (int i = 0; i < m.length; i++) {
				Class[] parametri = m[i].getParameterTypes();
				if (parametri.length == 0) {
					panel.add(new EditButton(m[i], o));
				}
			}
			spanel.setLayout(new GridLayout(0, 2));
			getContentPane().add(spanel, BorderLayout.SOUTH);
		}
		spanel.add(new RefreshButton(o));
		spanel.add(new RedroButton());
		spanel.add(new OKButton());
		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().validate();
	}

	class RedroButton extends Button {
		/**
		 * 
		 */
		private static final long serialVersionUID = 4878624675483852345L;

		public RedroButton() {
			super("redro");
			if (lastObject == null)
				this.setEnabled(false);
			addActionListener(new ActionListener() { // input
				public void actionPerformed(ActionEvent evt) {
					showFields(lastObject);
				}
			});
		}
	}

	class RefreshButton extends Button {

		private static final long serialVersionUID = 3628562259373916896L;

		public RefreshButton(final Object o) {
			super("refresh");
			addActionListener(new ActionListener() { // input
				public void actionPerformed(ActionEvent evt) {
					showFields(o);
				}
			});
		}
	}

	class OKButton extends Button {
		private static final long serialVersionUID = 8824878606582628707L;

		public OKButton() {
			super("ok");
			addActionListener(new ActionListener() { // input
				public void actionPerformed(ActionEvent evt) {
					ok();
				}
			});
		}
	}

	private void ok() {
		try {
			this.toedit.set(otoedit, selected);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setVisible(false);
	}

	class EditButton extends Button {

		private static final long serialVersionUID = -3756699948245191535L;

		public String name;

		public Field field;

		public Method method;

		public Object object;

		public EditButton(final Method method, final Object object) {
			super(method.getName());
			name = method.getName();
			this.setBackground(Color.LIGHT_GRAY);
			this.object = object;
			this.method = method;
			addActionListener(new ActionListener() { // input
				public void actionPerformed(ActionEvent evt) {
					onClick();
				}
			});

		}

		public EditButton(final Field field, final Object object) {
			super(field.getName());
			name = field.getName();
			this.setBackground(Color.PINK);
			this.object = object;
			this.field = field;
			addActionListener(new ActionListener() { // input
				public void actionPerformed(ActionEvent evt) {
					onClick();
				}
			});
		}

		public void onClick() {
			Object o = object;
			if (field != null)
				try {
					String name = field.getType().getName();
					System.out.println(field.getType());
					o = field.get(object);
					selected = o;
					System.out.println(name);

				} catch (Exception e) {
					e.printStackTrace();
				}
			else {
				try {
					Object[] oo = {};
					Object r = method.invoke(object, oo);
					if (r != null) {
						o = r;
					} else
						return;

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			showFields(o);
		}
	}
}

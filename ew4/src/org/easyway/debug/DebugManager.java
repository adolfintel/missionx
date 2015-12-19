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
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.easyway.interfaces.base.IBaseObject;
import org.easyway.interfaces.sprites.IPlain2D;
import org.easyway.lists.BaseList;
import org.easyway.objects.BaseObject;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;

public class DebugManager extends JFrame {
	private static final long serialVersionUID = -8409654168487451086L;

	/** indicates if the game is in debug mode or not */
	public static boolean debug = false;

	private BaseList baseList;

	private Vector<Object> vector;

	JList list;

	TextField input;

	static IPlain2D oldCenter;

	static boolean oldAttrack;

	static float oldx;

	static float oldy;

	static int count = 0;

	static boolean cameraSetted;

	public DebugManager() {
		super("Debug manager");
		count++;
		create();
	}

	public DebugManager(Vector<Object> vector) {
		super("Debug manager");
		this.vector = vector;
		count++;
		create();
	}

	public DebugManager(BaseList baseList) {
		super("Debug manager");
		this.baseList = baseList;
		count++;
		create();
	}

	private void create() {
		getContentPane().removeAll();
		lastObject = currObject;
		if (vector != null)
			currObject = vector;
		else
			currObject = baseList;
		if (cameraSetted == false) {
			oldAttrack = StaticRef.getCamera().attrack;
			oldCenter = StaticRef.getCamera().center;
			oldx = StaticRef.getCamera().x;
			oldy = StaticRef.getCamera().y;
			cameraSetted = true;
		}

		debug = true;
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
				// XXX XXX XXX XXX -- 0.3.0.0 CAUTION -- XXX XXX XXX
				IBaseObject obj;
				while (baseList.next()) {
					obj = (IBaseObject)baseList.getCurrent();
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

			final Button delete = new Button("try delete");
			delete.addActionListener(new ActionListener() { // input
						public void actionPerformed(ActionEvent evt) {
							Object ob;
							if (baseList != null)
								ob = baseList.get(baseList.size()
										- list.getSelectedIndex() - 1);
							else
								ob = vector.get(list.getSelectedIndex());
							if (ob instanceof BaseObject) {
								((BaseObject) ob).destroy();
								showFields(currObject);
							}

						}
					});
			pulsanti.add(delete);

			Button pos = new Button("show pos.");
			pos.addActionListener(new ActionListener() { // input
						public void actionPerformed(ActionEvent evt) {
							Object o;
							if (baseList != null)
								o = baseList.get(baseList.size()
										- list.getSelectedIndex() - 1);
							else
								o = vector.get(list.getSelectedIndex());
							if (o instanceof IPlain2D) {
								BaseObject.autoAddToLists = true;
								onCenter();
								new Selected((IPlain2D) o);
							}
						}
					});
			pulsanti.add(pos);

		}
		Button camera = new Button("edit StaticRef");
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

		Button centra = new Button("center camera");
		centra.addActionListener(new ActionListener() { // input
					public void actionPerformed(ActionEvent evt) {
						onCenter();
					}
				});
		pulsanti.add(centra);

		Button ripristinaCentro = new Button("restore camera");
		ripristinaCentro.addActionListener(new ActionListener() { // input
					public void actionPerformed(ActionEvent evt) {
						onRestoreCamera();
					}
				});
		pulsanti.add(ripristinaCentro);

		pulsanti.add(new PauseButton());
		pulsanti.add(new ResumeButton());
		pulsanti.add(new LoopButton());

		if (baseList != null) {
			pulsanti.add(new DebugButton(baseList));
			pulsanti.add(new RefreshButton(baseList));
		} else if (vector != null) {
			pulsanti.add(new DebugButton(vector));
			pulsanti.add(new RefreshButton(vector));
		}
		pulsanti.add(new RedroButton());

		content.add(pulsanti);

		setVisible(true);

		// closing
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// count--;
				// if (count == 0)
				debug = false;
				setVisible(false);
				dispose();
			}
		});

	}

	private void onRestoreCamera() {
		StaticRef.getCamera().attrack = oldAttrack;
		StaticRef.getCamera().center = oldCenter;
		StaticRef.getCamera().x = oldx;
		StaticRef.getCamera().y = oldy;
	}

	private void onCenter() {
		Object o;
		if (baseList != null)
			o = baseList.get(baseList.size() - list.getSelectedIndex() - 1);
		else
			o = vector.get(list.getSelectedIndex());
		if (o instanceof IPlain2D) {
			StaticRef.getCamera().centerOn((IPlain2D) o);
		}
	}

	private void onContinue() {
		Object o;
		if (baseList != null)
			o = baseList.get(baseList.size() - list.getSelectedIndex() - 1);
		else
			o = vector.get(list.getSelectedIndex());

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
		if (currObject != o) {
			lastObject = currObject;
			currObject = o;
		}
		getContentPane().removeAll();
		getContentPane().setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		JPanel spanel = new JPanel();

		panel.setPreferredSize(new Dimension(224, 0));

		panel.setLayout(new GridLayout(0, 2));
		if (o == null)
			return;
		if (o.getClass().isArray()) {
			vector = new Vector<Object>();
			baseList = null;
			for (int i = 0; i < Array.getLength(o); ++i) {
				vector.add( o.getClass().getComponentType()
						.cast(Array.get(o, i)));
			}
			create();
		} else if (o instanceof BaseList) {
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
			EditButton eb;
			Field[] f = o.getClass().getFields();
			for (int i = 0; i < f.length; i++) {
				panel.add(eb = new EditButton(f[i], o));
				try {
					String s;
					if (f[i].get(o) == null)
						s = "null";
					else
						s = f[i].get(o).toString();
					// panel.add(new Label(s));
					panel.add(new ChangeButton(f[i], s, eb));
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
			input = new TextField();
			spanel.setLayout(new GridLayout(0, 2));
			getContentPane().add(spanel, BorderLayout.SOUTH);
			spanel.add(input, BorderLayout.SOUTH);
		}
		spanel.add(new LoopButton());
		if (baseList != null)
			spanel.add(new DebugButton(baseList), BorderLayout.SOUTH);
		else
			spanel.add(new DebugButton(vector), BorderLayout.SOUTH);
		spanel.add(new PauseButton());
		spanel.add(new ResumeButton());
		spanel.add(new RefreshButton(o));
		spanel.add(new RedroButton());
		Button pos = new Button("show pos.");
		pos.addActionListener(new ActionListener() { // input
					public void actionPerformed(ActionEvent evt) {
						Object o;
						if (baseList != null)
							o = baseList.get(baseList.size()
									- list.getSelectedIndex() - 1);
						else
							o = vector.get(list.getSelectedIndex());
						if (o instanceof IPlain2D) {
							BaseObject.autoAddToLists = true;
							onCenter();
							new Selected((IPlain2D) o);
						}
					}
				});
		spanel.add(pos);
		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().validate();
	}

	class RedroButton extends Button {

		private static final long serialVersionUID = -351994286646305104L;

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

		private static final long serialVersionUID = -6464282274583850340L;

		public RefreshButton(final Object o) {
			super("refresh");
			addActionListener(new ActionListener() { // input
				public void actionPerformed(ActionEvent evt) {
					showFields(o);
				}
			});
		}
	}

	class LoopButton extends Button {

		private static final long serialVersionUID = -4358487333433886470L;

		public LoopButton() {
			super("loop");
			addActionListener(new ActionListener() { // input
				public void actionPerformed(ActionEvent evt) {
					debug = false;
					try {
						Core.class.getMethod("coreLoop", new Class[]{}).invoke(
								Core.getInstance(), new Object[]{});
					} catch (Exception e) {
						System.out
								.println("You MUST call InitOptimizer.initDebugCore()!!");
						e.printStackTrace();
					}
					// StaticRef.core.coreLoop();
					debug = true;
				}
			});
		}
	}

	class PauseButton extends Button {

		private static final long serialVersionUID = -4032439347458728523L;

		public PauseButton() {
			super("pause");
			addActionListener(new ActionListener() { // input
				public void actionPerformed(ActionEvent evt) {
					debug = true;
				}
			});
		}
	}

	class ResumeButton extends Button {

		private static final long serialVersionUID = -3307759152879186718L;

		public ResumeButton() {
			super("resume");
			addActionListener(new ActionListener() { // input
				public void actionPerformed(ActionEvent evt) {
					debug = false;
				}
			});
		}
	}

	class DebugButton extends Button {

		private static final long serialVersionUID = 2729238673663521519L;

		BaseList baselist;

		Vector vector;

		public DebugButton(BaseList baselist) {
			super("new Window");
			this.baselist = baselist;
			addActionListener(new ActionListener() { // input
				public void actionPerformed(ActionEvent evt) {
					onclick();
				}
			});
		}

		public DebugButton(Vector vector) {
			super("new Window");
			this.vector = vector;
			addActionListener(new ActionListener() { // input
				public void actionPerformed(ActionEvent evt) {
					onclick();
				}
			});
		}

		private void onclick() {
			if (baselist != null)
				new DebugManager(baselist);
			else
				new DebugManager(vector);
		}
	}

	class ChangeButton extends Button {

		private static final long serialVersionUID = 5641924025094720268L;

		Field field;

		EditButton eb;

		public ChangeButton(Field field, String s, EditButton eb) {
			super(s);
			this.eb = eb;
			this.field = field;
			addActionListener(new ActionListener() { // input
				public void actionPerformed(ActionEvent evt) {
					onClick();
				}
			});
		}

		private void onClick() {
			try {
				String name = field.getType().getName();
				System.out.println(field.getType());
				if (name.compareTo("int") == 0) {
					eb.setInteger();
				} else if (name.compareTo("double") == 0) {
					eb.setDouble();
				} else if (name.compareTo("float") == 0) {
					eb.setFloat();
				} else if (name.compareTo("java.lang.String") == 0) {
					eb.setString();
				} else if (name.compareTo("boolean") == 0) {
					eb.setBool();
				} else if (name.compareTo("char") == 0) {
					eb.setChar();
				} else if (name.compareTo("long") == 0) {
					eb.setLong();
				} else if (name.compareTo("short") == 0) {
					eb.setShort();
				} else if (name.compareTo("byte") == 0) {
					eb.setByte();
				} else {
					new Selector(field, eb.object);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			showFields(eb.object);
		}
	}

	class EditButton extends Button {
		/**
		 * 
		 */
		private static final long serialVersionUID = -6674693824240709571L;

		public String name;

		public Field field;

		public Method method;

		public Object object;

		public EditButton(final Method method, final Object object) {
			super(method.getName());
			name = method.getName();
			this.object = object;
			this.method = method;
			this.setBackground(Color.LIGHT_GRAY);
			create();
		}

		public EditButton(final Field field, final Object object) {
			super(field.getName());
			name = field.getName();
			this.object = object;
			this.field = field;
			this.setBackground(Color.PINK);
			create();
		}

		private void create() {
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

					if (name.compareTo("int") == 0) {
						setInteger();
					} else if (name.compareTo("double") == 0) {
						setDouble();
					} else if (name.compareTo("float") == 0) {
						setFloat();
					} else if (name.compareTo("java.lang.String") == 0) {
						setString();
					} else if (name.compareTo("boolean") == 0) {
						setBool();
					} else if (name.compareTo("char") == 0) {
						setChar();
					} else if (name.compareTo("long") == 0) {
						setLong();
					} else if (name.compareTo("short") == 0) {
						setShort();
					} else if (name.compareTo("byte") == 0) {
						setByte();
					} else {
						o = field.get(object);
						System.out.println(name);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			else { // metodo
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

		private void setInteger() throws Exception {
			field.set(object, new Integer(input.getText()));
		}

		private void setDouble() throws Exception {
			field.set(object, new Double(input.getText()));
		}

		private void setFloat() throws Exception {
			field.set(object, new Float(input.getText()));
		}

		private void setString() throws Exception {
			field.set(object, input.getText());
		}

		private void setBool() throws Exception {
			field.set(object, new Boolean(input.getText()));
		}

		private void setLong() throws Exception {
			field.set(object, new Long(input.getText()));
		}

		private void setChar() throws Exception {
			field.set(object, new Character(input.getText().charAt(0)));
		}

		private void setByte() throws Exception {
			field.set(object, new Byte(input.getText()));
		}

		private void setShort() throws Exception {
			field.set(object, new Short(input.getText()));
		}
	}
}

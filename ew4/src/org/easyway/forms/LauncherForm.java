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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import org.easyway.system.Core;

class LauncherForm extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JLabel jLabel = null;

	private JPanel southPanel = null;

	private JPanel centerPanel = null;

	private JButton buttonLunch = null;

	private JButton buttonExit = null;

	private JLabel nameGame = null;

	private JCheckBox fullscreen = null;

	private JComboBox resolution = null;

	private JLabel jLabel1 = null;

	private JLabel jLabel2 = null;

	private JComboBox bpp = null;

	protected Launcher launcher;

	private JTabbedPane jTabbedPane = null;

	private JScrollPane jScrollPane = null;

	/**
	 * This is the default constructor
	 */
	public LauncherForm(Launcher launcher) {
		super();
		this.launcher = launcher;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(433, 228);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabel = new JLabel();
			jLabel.setText("EasyWay Game Engine " + Core.VERSION);
			jLabel.setForeground(new Color(0, 102, 255));
			jLabel.setHorizontalAlignment(SwingConstants.CENTER);
			jLabel.setFont(new Font("Dialog", Font.BOLD, 24));
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(jLabel, BorderLayout.NORTH);
			jContentPane.add(getSouthPanel(), BorderLayout.SOUTH);
			jContentPane.add(getJTabbedPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes southPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getSouthPanel() {
		if (southPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.fill = GridBagConstraints.NONE;
			gridBagConstraints.gridy = 0;
			southPanel = new JPanel();
			southPanel.setLayout(new GridBagLayout());
			southPanel.add(getButtonLunch(), gridBagConstraints1);
			southPanel.add(getButtonExit(), gridBagConstraints);
		}
		return southPanel;
	}

	/**
	 * This method initializes centerPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getCenterPanel() {
		if (centerPanel == null) {
			jLabel2 = new JLabel();
			jLabel2.setBounds(new Rectangle(27, 95, 86, 21));
			jLabel2.setText("bpp");
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(27, 64, 87, 21));
			jLabel1.setText("Resolution");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 0;
			nameGame = new JLabel();
			nameGame.setText("Name Of Game");
			nameGame.setFont(new Font("Dialog", Font.BOLD, 18));
			nameGame.setForeground(Color.red);
			nameGame.setBounds(new Rectangle(15, 13, 270, 15));
			centerPanel = new JPanel();
			centerPanel.setLayout(null);
			centerPanel.setName("");
			centerPanel.add(getResolution(), null);
			centerPanel.add(nameGame, null);
			centerPanel.add(getFullscreen(), null);
			centerPanel.add(jLabel1, null);
			centerPanel.add(jLabel2, null);
			centerPanel.add(getBpp(), null);
		}
		return centerPanel;
	}

	/**
	 * This method initializes buttonLunch
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getButtonLunch() {
		if (buttonLunch == null) {
			buttonLunch = new JButton();
			buttonLunch.setText("Lunch");
			buttonLunch.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int w, h, bpp;
					int resx[] = { 320, 640, 800, 1024, 1280 };
					int resy[] = { 240, 480, 600, 768, 1024 };
					bpp = Integer.parseInt(getBpp().getSelectedItem()
							.toString());
					w = resx[getResolution().getSelectedIndex()];
					h = resy[getResolution().getSelectedIndex()];
					launcher.preLunch(w, h, bpp, getFullscreen().isSelected());
					launcher = null;
				}
			});
		}
		return buttonLunch;
	}

	/**
	 * This method initializes buttonExit
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getButtonExit() {
		if (buttonExit == null) {
			buttonExit = new JButton();
			buttonExit.setText("Exit");
			buttonExit.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setVisible(false);
					System.exit(0);
				}
			});
		}
		return buttonExit;
	}

	/**
	 * This method initializes fullscreen
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getFullscreen() {
		if (fullscreen == null) {
			fullscreen = new JCheckBox();
			fullscreen.setText("Fullscreen");
			fullscreen.setBounds(new Rectangle(27, 39, 129, 15));
		}
		return fullscreen;
	}

	/**
	 * This method initializes resolution
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getResolution() {
		if (resolution == null) {
			resolution = new JComboBox();
			resolution.addItem("320x240"); // 0
			resolution.addItem("640x480"); // 1
			resolution.addItem("800x600"); // 2
			resolution.addItem("1024x768"); // 3
			resolution.addItem("1280x1024");// 4
			resolution.setBounds(new Rectangle(118, 64, 129, 21));
			resolution.setSelectedIndex(2);
		}
		return resolution;
	}

	/**
	 * This method initializes bpp
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getBpp() {
		if (bpp == null) {
			bpp = new JComboBox();
			bpp.addItem("8"); // 0
			bpp.addItem("16"); // 1
			bpp.addItem("24"); // 2
			bpp.addItem("32"); // 3
			bpp.setBounds(new Rectangle(120, 95, 129, 21));
			bpp.setSelectedIndex(2);
		}
		return bpp;
	}

	public void setNameGame(String game) {
		nameGame.setText(game);
	}

	/**
	 * This method initializes jTabbedPane
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.addTab("Standard", null, getCenterPanel(), null);
			jTabbedPane.addTab("Advanced", null, getJScrollPane(), null);
		}
		return jTabbedPane;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
		}
		return jScrollPane;
	}

} // @jve:decl-index=0:visual-constraint="10,10"

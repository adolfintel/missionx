package org.easyway.gui.tileEditor;

import org.easyway.gui.tileEditor.forms.InitialForm;

public class TileEditor {

	MainMenu mainMenu;
	
	LeftMenu leftMenu;
	
	BodyEditor bodyEditor;
	
	public TileEditor() {
		mainMenu = new MainMenu();
		
		leftMenu = new LeftMenu(mainMenu.getHeight());
		
		bodyEditor = new BodyEditor(leftMenu.getWidth(),mainMenu.getHeight());
		
		new InitialForm();
	}

}

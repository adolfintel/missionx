package org.easyway.gui.tileEditor;

import org.easyway.gui.base.Button;
import org.easyway.gui.base.Panel;
import org.easyway.system.Core;

public class LeftMenu extends Panel {

	private static final long serialVersionUID = 1L;
	Button showHideButton;
	
	public LeftMenu(int y) {
		super(0, y, 200, Core.getInstance().getHeight() - y, null);
		
		showHideButton = new Button(5,5,"show/hide layer",this){
		
			private static final long serialVersionUID = 1L;

			@Override
			public void onAction() {

			}
		
		};
		initColor();
	}
	
	
	protected void initColor() {
		setColor(3, 0, 128, 128, 1);
		setColor(1, 128, 64, 64, 0);
		setColor(2, 64, 32, 32, 0);
		setColor(0, 64, 128, 0, 1);
	}

}

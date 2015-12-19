package org.easyway.gui.tileEditor;

import org.easyway.gui.base.Button;
import org.easyway.gui.base.Panel;
import org.easyway.gui.forms.MessageBox;
import org.easyway.gui.tileEditor.forms.layers.ManageLayer;
import org.easyway.gui.tileEditor.forms.tilesets.ManageTileSets;

public class MainMenu extends Panel {

	private static final long serialVersionUID = 1L;

	Button manageLayers;

	Button manageTileSets;

	Button saveTileMap;

	public MainMenu() {
		super(0, 0, 640, 80, null);

		int nx = 0;
		manageLayers = new Button(nx = 5, 5, "Manage Layers", this) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onAction() {
				new ManageLayer();
			}
		};

		manageTileSets = new Button(nx += manageLayers.getWidth() + 5, 5,
				"Manage TileSets", this) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onAction() {
				new ManageTileSets();
			}
		};

		saveTileMap = new Button(nx += manageTileSets.getWidth() + 5, 5,
				"Save TileMap", this) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onAction() {
				new MessageBox("this function is currently disabled");
			}
		};
		
		setHeight(saveTileMap.getHeight() + 10);
		setWidth((int)(saveTileMap.getX()+saveTileMap.getWidth()+10));
		initColor();
	}
	
	protected void initColor() {
		setColor(0, 128, 0, 0, 1);
		setColor(1, 128, 64, 64, 0);
		setColor(2, 64, 32, 32, 0);
		setColor(3, 64, 128, 0, 1);
	}

}

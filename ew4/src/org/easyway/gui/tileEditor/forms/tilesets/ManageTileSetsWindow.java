package org.easyway.gui.tileEditor.forms.tilesets;

import org.easyway.gui.base.GuiContainer;
import org.easyway.gui.base.Button;
import org.easyway.gui.base.Window;
import org.easyway.gui.forms.YesNoMessageBox;
import org.easyway.gui.tileEditor.TileEditorData;
import org.easyway.tiles.TileSet;

public class ManageTileSetsWindow extends Window {

	Button addButton;
	Button removeButton;
	Button editButton;
	Button okButton;

	TileSetsList tileList;

	public ManageTileSetsWindow(GuiContainer container) {
		super(container);
		setSize(415, 310);
		center();

		int y = 5 + getCaptionHeight();

		addButton = new Button(210, y, "Add", this) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onAction() {
				new AddTileSetWindow().tileSetsList = tileList;
			}

		};
		addButton.setWidth(200);

		removeButton = new Button(210, y += addButton.getHeight(), "Remove",
				this) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onAction() {
				new YesNoMessageBox("Do You want really remove the selected ("
						+ tileList.selectedButton.getText() + ") TileSet? ") {
					@Override
					protected void onYesAction() {
						tileList.removeChild(tileList.selectedButton);
						for (TileSet ts : TileEditorData.tileSets) {
							if (ts.getName().equals(tileList.selectedButton.getText())) {
								TileEditorData.tileSets.remove(ts);
								break;
							}
						}
						tileList.selectedButton = null;
					}

					@Override
					public void onNoAction() {
						// nothing to do
					}

				};
			}
		};
		removeButton.setWidth(200);

		editButton = new Button(210, y += removeButton.getHeight(), "Edit",
				this) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onAction() {
				if (tileList.selectedButton != null) {
					
				}
			}
		};
		editButton.setWidth(200);

		okButton = new Button(getWidth() / 2 - 75, 0, "Ok", this) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onAction() {
				closeAll();
			}

		};
		okButton.setWidth(150);
		okButton.setY(y = getHeight() - okButton.getHeight()
				- getBottomBorderHeight());

		tileList = new TileSetsList(5, getCaptionHeight(), 200, y
				- getCaptionHeight(), this);

	}

	public void updateData() {
		tileList.updateData();
	}

	public void closeAll() {
		getFather().destroy();
	}
}

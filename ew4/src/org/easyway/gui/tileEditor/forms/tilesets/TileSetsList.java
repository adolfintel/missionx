package org.easyway.gui.tileEditor.forms.tilesets;

import org.easyway.gui.base.GuiContainer;
import org.easyway.gui.base.ScrollPanel;
import org.easyway.gui.base.SelectionableButton;
import org.easyway.gui.tileEditor.TileEditorData;
import org.easyway.tiles.TileSet;

public class TileSetsList extends ScrollPanel {

	SelectionableButton selectedButton = null;

	public TileSetsList(int x, int y, int width, int height,
			GuiContainer container) {

		super(x, y, width, height, container);
		addData();
		setHScrollBarVisible(false);
	}

	private void addData() {
		int py = 0;
		SelectionableButton tButton;
		for (TileSet ts : TileEditorData.tileSets) {
			tButton = new SelectionableButton(0, py, ts.getName(), this) {
				public void onAction() {
					if (selectedButton != null) {
						selectedButton.setSelected(false);
					}
					setSelected(true);
					selectedButton = this;
				}
			};
			py += tButton.getHeight();
			tButton.setWidth(getWidth() - scrollbarSize);
		}
	}

	public void updateData() {
		getChildren().startScan();
		while (getChildren().next()) {
			getChildren().getCurrent().destroy();
		}

		addData();
	}

}

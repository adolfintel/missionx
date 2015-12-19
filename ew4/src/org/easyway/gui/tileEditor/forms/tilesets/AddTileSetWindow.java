package org.easyway.gui.tileEditor.forms.tilesets;

import org.easyway.gui.base.GuiContainer;
import org.easyway.gui.base.InputLabel;
import org.easyway.gui.base.Label;
import org.easyway.gui.base.Button;
import org.easyway.gui.base.Panel;
import org.easyway.gui.base.Window;
import org.easyway.gui.forms.MessageBox;
import org.easyway.gui.tileEditor.TileEditorData;
import org.easyway.system.Core;
import org.easyway.system.state.OpenGLState;
import org.easyway.tiles.TileSet;
import org.lwjgl.opengl.GL11;

public class AddTileSetWindow extends Panel {

	AddTileSetInternalWindow window;
	public TileSetsList tileSetsList;

	public AddTileSetWindow() {
		super(0, 0, Core.getInstance().getWidth(), Core.getInstance().getHeight(), null);
		gainFocus();
		window = new AddTileSetInternalWindow(this);
	}

	public void customDraw() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGLState.enableBlending();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(0, 1, 0.5f, 0.2f);
		GL11.glVertex2f(0, 0);
		GL11.glVertex2f(width - 1, 0);
		GL11.glVertex2f(width - 1, height - 1);
		GL11.glVertex2f(0, height - 1);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	class AddTileSetInternalWindow extends Window {

		private static final long serialVersionUID = 8976845L;

		public AddTileSetInternalWindow(GuiContainer container) {
			super(container);
			setSize(415, 200);
			center();
			setTitle("create new TileSet");
			int y = getCaptionHeight() + 25;
			new Label(15, y, "TileSet Name:", this);
			final InputLabel nameLabel = new InputLabel(getWidth() / 2 + 5, y,
					getWidth() / 2 - 20, 25, this);
			nameLabel.gainFocus();
			Button okButton = new Button(getWidth() / 4 - 75, 0, "OK", this) {

				@Override
				public void onAction() {
					String name = nameLabel.getText();
					if (name == null || name.length() < 1) {
						new MessageBox(
								"You must write a name: an empty string isn't a valid name");
						return;
					}
					for (TileSet ts : TileEditorData.tileSets) {
						if (ts.getName().equals(name)) {
							new MessageBox(
									"the name '"
											+ name
											+ "' is alreay used, select a different name");
							return;
						}
					}
					TileSet ts = new TileSet(name);
					TileEditorData.tileSets.add(ts);
					AddTileSetWindow.this.destroy();
				}

			};
			okButton.setWidth(150);
			okButton.setY(getHeight() - okButton.getHeight()
					- getBottomBorderHeight());

			Button cancelButton = new Button(
					(int) ((float) getWidth() * 0.75f) - 75, 0, "Cancel", this) {

				@Override
				public void onAction() {
					AddTileSetWindow.this.destroy();
				}

			};

			cancelButton.setWidth(150);
			cancelButton.setY(getHeight() - okButton.getHeight()
					- getBottomBorderHeight());

		}

	}

	public void onDestroy() {
		tileSetsList.updateData();
	}

}

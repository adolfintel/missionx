package org.easyway.gui.tileEditor.forms;

import org.easyway.gui.base.GuiContainer;
import org.easyway.gui.base.InputLabel;
import org.easyway.gui.base.InputLabelDecimal;
import org.easyway.gui.base.Label;
import org.easyway.gui.base.Button;
import org.easyway.gui.base.Panel;
import org.easyway.gui.base.Window;
import org.easyway.gui.forms.MessageBox;
import org.easyway.gui.tileEditor.TileEditorData;
import org.easyway.system.Core;
import org.easyway.system.state.OpenGLState;
import org.easyway.tiles.TileMapLayer;
import org.lwjgl.opengl.GL11;

public class InitialForm extends Panel {

	MainForm window;

	InputLabelDecimal widthLabel;
	InputLabelDecimal heightLabel;
	InputLabelDecimal tileWidthLabel;
	InputLabelDecimal tileHeightLabel;
	InputLabel nameLabel;

	Button okButton;

	public InitialForm() {
		super(0, 0, Core.getInstance().getWidth(), Core.getInstance().getHeight(), null);
		gainFocus();
		window = new MainForm(this);
	}

    @Override
	public void customDraw() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGLState.enableBlending();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(1, 0, 1, 0.2f);
		GL11.glVertex2f(0, 0);
		GL11.glVertex2f(width - 1, 0);
		GL11.glVertex2f(width - 1, height - 1);
		GL11.glVertex2f(0, height - 1);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	class MainForm extends Window {
		public MainForm(GuiContainer container) {
			super(container);
			center();
			setSize(420, 420);

			int currentY = 5 + getCaptionHeight();
			int bx = 125;

			new Label(15, currentY, "Width", this).getHeight();
			widthLabel = new InputLabelDecimal(bx, currentY, 200, 25, this);
			currentY += 30;

			new Label(15, currentY, "Height", this).getHeight();
			heightLabel = new InputLabelDecimal(bx, currentY, 200, 25, this);
			currentY += 30;

			new Label(15, currentY, "Tile Width", this).getHeight();
			tileWidthLabel = new InputLabelDecimal(bx, currentY, 200, 25, this);
			currentY += 30;

			new Label(15, currentY, "Tile Height", this).getHeight();
			tileHeightLabel = new InputLabelDecimal(bx, currentY, 200, 25, this);
			currentY += 30;

			new Label(15, currentY, "Name", this).getHeight();
			nameLabel = new InputLabel(bx, currentY, 200, 25, this);
			currentY += 30;

			okButton = new Button(getWidth() / 2 - 75, currentY + 15, "OK", this) {

				@Override
				public void onAction() {
					apply();
				}

			};
			okButton.setWidth(150);
			currentY += okButton.getHeight() + getBottomBorderHeight();
			setSize(getWidth(), currentY);

			// y+=new Label(15,y,"Id-Layer",this).getHeight();
			setDefaultLabels();
		}

		protected void setDefaultLabels() {
			tileWidthLabel.setText("64");
			tileHeightLabel.setText("64");
			nameLabel.setText("TiledMap");
			widthLabel.setText("100");
			heightLabel.setText("100");
		}

		protected void apply() {
			int dWidth, dHeight;
			int dTileWidth, dTileHeight;
			String dName;

			try {
				dWidth = Integer.parseInt(widthLabel.getText());
			} catch (Exception e) {
				new MessageBox("The Width should be an integer number");
				return;
			}
			try {
				dHeight = Integer.parseInt(heightLabel.getText());
			} catch (Exception e) {
				new MessageBox("The Height should be an integer number");
				return;
			}
			try {
				dTileWidth = Integer.parseInt(tileWidthLabel.getText());
			} catch (Exception e) {
				new MessageBox("The Tile Width should be an integer number");
				return;
			}
			try {
				dTileHeight = Integer.parseInt(tileHeightLabel.getText());
			} catch (Exception e) {
				new MessageBox("The Tile Height should be an integer number");
				return;
			}
			try {
				if (nameLabel.getText()==null || nameLabel.getText().length()<1)
					throw new Exception();
				dName = nameLabel.getText();
				
			} catch (Exception e) {
				new MessageBox("The name should be a not-NULL string");
				return;
			}

			TileEditorData.tileManager = new TileMapLayer(dWidth, dHeight,
					dTileWidth, dTileHeight, dName);
			InitialForm.this.destroy();
		}
	}
}

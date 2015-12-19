package org.easyway.gui.tileEditor.forms.tilesets;

import org.easyway.gui.base.GuiContainer;
import org.easyway.gui.base.Panel;
import org.easyway.gui.base.Window;
import org.easyway.system.Core;
import org.easyway.system.state.OpenGLState;
import org.easyway.tiles.TileSet;
import org.lwjgl.opengl.GL11;

public class EditTileSetWindow extends Panel {

	EditTileSetInternalWindow window;
	TileSet tileSet;

	public EditTileSetWindow(TileSet tileSet) {
		super(0, 0, Core.getInstance().getWidth(), Core.getInstance().getHeight(), null);
		gainFocus();
		this.tileSet = tileSet;
		window = new EditTileSetInternalWindow(this);
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

	class EditTileSetInternalWindow extends Window {
		public EditTileSetInternalWindow(GuiContainer container) {
			super(container);
			setSize(415, 200);
			center();
			setTitle("Edit TileSet");
		}
	}

}

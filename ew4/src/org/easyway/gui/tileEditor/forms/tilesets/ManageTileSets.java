package org.easyway.gui.tileEditor.forms.tilesets;

import org.easyway.gui.base.Panel;
import org.easyway.system.Core;
import org.easyway.system.state.OpenGLState;
import org.lwjgl.opengl.GL11;

public class ManageTileSets extends Panel {

	
	ManageTileSetsWindow window;
	
	public ManageTileSets() {
		super(0,0,Core.getInstance().getWidth(),Core.getInstance().getHeight(),null);
		gainFocus();
		window = new ManageTileSetsWindow(this);
	}
	
	public void customDraw() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGLState.enableBlending();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(1, 0, 1, 0.2f);
		GL11.glVertex2f(0, 0);
		GL11.glVertex2f(width-1, 0);
		GL11.glVertex2f(width-1, height-1);
		GL11.glVertex2f(0, height-1);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
}

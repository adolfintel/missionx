package org.easyway.gui.base.scrollBar;

import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.Color;

import org.easyway.gui.base.GuiContainer;
import org.easyway.gui.base.ScrollPanel;
import org.lwjgl.opengl.GL11;

public class HScrollBar extends GuiContainer {

	private static final long serialVersionUID = 1L;

	Color background = Color.DARK_GRAY;

	Color foreground = Color.GRAY;

	ScrollPanel fatherScrollPanel;

	public HScrollBar(int x, int y, int width, int height) {
		super(x, y, width, height, "HScrollBar");
	}

	// float xOnScreen, yOnScreen;

	public void customDraw() {
		xOnScreen += fatherScrollPanel.getScrollX();
		yOnScreen += fatherScrollPanel.getScrollY();

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_QUADS);

		glColor4f(background.getRed() / 255f, background.getGreen() / 255f,
				background.getBlue() / 255f, 0.5f);

		float nx = xOnScreen;
		float ny = yOnScreen;

		glVertex2f(nx, ny);
		glVertex2f(nx + width, ny);
		glVertex2f(nx + width, ny + height);
		glVertex2f(nx, ny + height);

		glColor4f(foreground.getRed() / 255f, foreground.getGreen() / 255f,
				foreground.getBlue() / 255f, 1);
		glVertex2f(nx, ny);
		glVertex2f(nx + height, ny);
		glVertex2f(nx + height, ny + height);
		glVertex2f(nx, ny + height);

		drawCaret((int) nx + height, (int) ny);

		nx += (width - height * 2);
		glColor4f(foreground.getRed() / 255f, foreground.getGreen() / 255f,
				foreground.getBlue() / 255f, 1);

		glVertex2f(nx, ny);
		glVertex2f(nx + height, ny);
		glVertex2f(nx + height, ny + height);
		glVertex2f(nx, ny + height);

		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		// super.draw( x, y );
	}

	void drawCaret(int nx, int ny) {
		int size = fatherScrollPanel.getMaxX() - fatherScrollPanel.getMinX();
		int pixs = 0;
		int wid = width - height * 3;
		if (size == 0) {
			pixs = wid;
		} else {
			pixs = (wid * wid) / size;
			nx += ((float) fatherScrollPanel.getScrollX() / (float) size)
					* (wid - pixs);
		}
		glColor4f(0.7f, 0.7f, 0, 1);
		glVertex2f(nx, ny);
		glVertex2f(nx + pixs, ny);
		glVertex2f(nx + pixs, ny + height);
		glVertex2f(nx, ny + height);
	}

	public void onDown(int x, int y) {
		if (x < height) {
			fatherScrollPanel.setScrollX(fatherScrollPanel.getScrollX() - 1);
		} else if (x > height && x < width - height * 2) {
			float size = fatherScrollPanel.getMaxX()
					- fatherScrollPanel.getMinX();
			if (size != 0) {
				float wid = width - height * 3;
				float pixs = (wid * wid) / size;
				fatherScrollPanel.setScrollX((int) ((x - height - pixs / 2)
						/ (wid - pixs) * size));
			}
		} else if (x > width - height * 2 && x < width - height) {
			fatherScrollPanel.setScrollX(fatherScrollPanel.getScrollX() + 1);
		}
	}

	public void setFather(GuiContainer container) {
		super.setFather(container);
		fatherScrollPanel = (ScrollPanel) getFather();
	}

}

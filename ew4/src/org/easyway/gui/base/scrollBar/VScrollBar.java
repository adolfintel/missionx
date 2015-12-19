package org.easyway.gui.base.scrollBar;

import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.Color;

import org.easyway.gui.base.GuiContainer;
import org.easyway.gui.base.ScrollPanel;
import org.lwjgl.opengl.GL11;

public class VScrollBar extends GuiContainer {

	private static final long serialVersionUID = 1L;

	Color background = Color.DARK_GRAY;

	Color foreground = Color.GRAY;

	ScrollPanel fatherScrollPanel;

	public VScrollBar(int x, int y, int width, int height) {
		super(x, y, width, height, "VScrollBar");
	}

	// float xOnScreen, yOnScreen;

	public void customDraw() {

		xOnScreen += fatherScrollPanel.getScrollX();
		yOnScreen += fatherScrollPanel.getScrollY();
		float nx = xOnScreen;
		float ny = yOnScreen;

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_QUADS);

		glColor4f(background.getRed() / 255f, background.getGreen() / 255f,
				background.getBlue() / 255f, 0.5f);

		glVertex2f(nx, ny);
		glVertex2f(nx + width, ny);
		glVertex2f(nx + width, ny + height);
		glVertex2f(nx, ny + height);

		glColor4f(foreground.getRed() / 255f, foreground.getGreen() / 255f,
				foreground.getBlue() / 255f, 1);
		glVertex2f(nx, ny);
		glVertex2f(nx + width, ny);
		glVertex2f(nx + width, ny + width);
		glVertex2f(nx, ny + width);

		drawCaret((int) nx, (int) ny + width);

		glColor4f(foreground.getRed() / 255f, foreground.getGreen() / 255f,
				foreground.getBlue() / 255f, 1);
		ny += (height - width * 2);

		glVertex2f(nx, ny);
		glVertex2f(nx + width, ny);
		glVertex2f(nx + width, ny + width);
		glVertex2f(nx, ny + width);

		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	void drawCaret(int nx, int ny) {
		int size = fatherScrollPanel.getMaxY() - fatherScrollPanel.getMinY();
		int pixs = 0;
		int heig = height - width * 3;
		if (size == 0) {
			pixs = heig;
		} else {
			pixs = (heig * heig) / size;
			ny += ((float) fatherScrollPanel.getScrollY() / (float) size)
					* (heig - pixs);
		}

		glColor4f(0.7f, 0.7f, 0, 1);

		glVertex2f(nx, ny);
		glVertex2f(nx + width, ny);
		glVertex2f(nx + width, ny + pixs);
		glVertex2f(nx, ny + pixs);
	}

	public void onDown(int x, int y) {

		if (y < width) {
			fatherScrollPanel.setScrollY(fatherScrollPanel.getScrollY() - 1);
		} else if (y > width && y < height - width * 2) {
			float size = fatherScrollPanel.getMaxY()
					- fatherScrollPanel.getMinY();
			if (size != 0) {
				float heig = height - width * 3;
				float pixs = (heig * heig) / size;
				fatherScrollPanel.setScrollY((int) ((y - width - pixs / 2)
						/ (heig - pixs) * size));

			}
		} else if (y > height - width * 2 && y < height - width) {
			fatherScrollPanel.setScrollY(fatherScrollPanel.getScrollY() + 1);
		}
	}

	public void setFather(GuiContainer container) {
		super.setFather(container);
		fatherScrollPanel = (ScrollPanel) getFather();
	}

}

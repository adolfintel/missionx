package org.easyway.gui.forms;

import org.easyway.gui.base.Label;
import org.easyway.gui.base.Button;
import org.easyway.gui.base.Panel;
import org.easyway.gui.base.Window;
import org.easyway.objects.text.TextAlign;
import org.easyway.system.StaticRef;
import org.easyway.system.state.OpenGLState;
import org.lwjgl.opengl.GL11;

public abstract class YesNoMessageBox extends Panel {

	Window win;
	Label label;
	Button yesButton;
	Button noButton;

	public YesNoMessageBox(String message) {
		super(0, 0, StaticRef.getCamera().getWidth(), StaticRef.getCamera()
				.getHeight(), null);

		gainFocus();

		win = new Window(this);
		win.setTitle("EasyWay Message");
		win.setTextAlign(TextAlign.MIDDLE);
		win.setSize(width, height);

		int left = win.getLeftBorderWidth();
		int top = win.getCaptionHeight();

		// TODO: the following code can be optimized

		label = new Label(left + 10, top + 10, message, win);
		win.setWidth(Math.max(label.getWidth() + 20 + left * 2, 200));
		yesButton = new Button(10 + (int) ((float) win.getWidth() / 4) + left,
				label.getHeight() + 20 + top, "Yes", win) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onAction() {
				onYesAction();
				finish();
			}
		};
		noButton = new Button(10 + (int) ((float) win.getWidth() / 4 * 3)
				+ left, label.getHeight() + 20 + top, "No", win) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onAction() {
				onNoAction();
				finish();
			}
		};

		yesButton.setX(yesButton.getX() - yesButton.getWidth() / 2);
		noButton.setX(noButton.getX() - noButton.getWidth() / 2);

		win.setHeight(label.getHeight() + 30 + noButton.getHeight() + top);

		// win.setXY((width - win.getWidth()) / 2, (height - win.getHeight()) /
		// 2);
		win.center();

	}

	protected void finish() {
		destroy();
	}

	protected abstract void onYesAction();

	public abstract void onNoAction();

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

}

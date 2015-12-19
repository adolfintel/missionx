package org.easyway.gui.base;

import org.easyway.objects.nativeGraphic.Rectangle;
import org.easyway.system.state.OpenGLState;

public class Panel extends GuiContainer {

	private static final long serialVersionUID = 1L;
	/**
	 * the rectangle that will be drown from the Panel
	 */
	Rectangle rect;

	public Panel(float x, float y, int width, int height, GuiContainer container) {
		super(x, y, width, height, "Panel");
		setFather(container);

		// create the Rectangle
		boolean oldValue = Rectangle.autoAddToLists;
		Rectangle.autoAddToLists = false;
		rect = new Rectangle(getX(), getY(), getWidth(), getHeight());
		Rectangle.autoAddToLists = oldValue;
	}

	/**
	 * sets the specified color to the specified vertex-id<br>
	 * the vertexs are:<br>
	 * 0: top left<br>
	 * 1: top right<br>
	 * 2: bottom right<br>
	 * 3: bottom left<br>
	 * <br>
	 * a little ascii image to explain better:<br>
	 * (0)----------(1)<br>
	 * ...----------...<br>
	 * ...----------...<br>
	 * (3)----------(2)<br>
	 * 
	 * @param vertexid
	 *            the id of the vertex (0,1,2,3)
	 * @param red
	 *            red component
	 * @param green
	 *            green component
	 * @param blue
	 *            blue component
	 * @param alpha
	 *            alpha component (1: solid, 0: transparent)
	 */
	public void setColor(int vertexid, float red, float green, float blue,
			float alpha) {
		rect.setColor(vertexid, red, green, blue, alpha);
	}

	/**
	 * sets an uniform color
	 * 
	 * @param red
	 *            red component
	 * @param green
	 *            green component
	 * @param blue
	 *            blue component
	 * @param alpha
	 *            alpha component (1: solid, 0: transparent)
	 */
	public void setColor(float red, float green, float blue, float alpha) {
		rect.setColor(red, green, blue, alpha);
	}

	/**
	 * sets an uniform color
	 * 
	 * @param red
	 *            red component
	 * @param green
	 *            green component
	 * @param blue
	 *            blue component
	 * 
	 */
	public void setColor(float red, float green, float blue) {
		rect.setColor(red, green, blue);
	}

	public void draw(int x, int y) {
		OpenGLState.enableScissor((int) xOnScreen, (int) yOnScreen, getWidth(),
				getHeight());
		super.draw(x, y);
		OpenGLState.disableScissor();
	}

	public void customDraw() {
		rect.setXY(xOnScreen, yOnScreen);
		rect.render();
	}

	public void setWidth(int newWidth) {
		super.setWidth(newWidth);
		rect.setWidth(getWidth());
	}

	public void setHeight(int newHeight) {
		super.setHeight(newHeight);
		rect.setHeight(newHeight);
	}
}

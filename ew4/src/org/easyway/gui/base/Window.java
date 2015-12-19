package org.easyway.gui.base;

import static org.easyway.objects.text.TextAlign.MIDDLE;

import org.easyway.input.Mouse;
import org.easyway.interfaces.sprites.IClickable;
import org.easyway.objects.sprites2D.Mask;
import org.easyway.objects.text.Text;
import org.easyway.objects.text.TextAlign;
import org.easyway.objects.texture.Texture;
import org.easyway.objects.texture.TextureID;
import org.lwjgl.opengl.GL11;

public class Window extends GuiContainer implements IClickable, IContainer {

        private static final String blackColor = Text.createColor(0, 0, 0);
	private static final long serialVersionUID = 1L;

	protected static Texture sImages[][];

	protected static Texture[][] initTextures() {
		boolean oldValue = TextureID.USE_MIPMAP;
		TextureID.USE_MIPMAP = false;

		Texture temp = new Texture("/org/easyway/gui/images/window.png", 255,
				255, 255);
		TextureID.USE_MIPMAP = oldValue;
		sImages = temp.split2D(new int[] { 26, 200, 26 }, new int[] { 43, 187,
				26 });
		return sImages;
	}

	Texture images[][];

	int captionHeight;
	int leftBorderWidth;
	int rightBorderWidth;
	int bottomBorderHeight;

	int internalWidth, internalHeight;

	Text captionText;

	boolean moveable;

	boolean atracked = false;

	static boolean dragging = false;

	int atrackX, atrackY;

	/** saturation's color */
	protected float red, green, blue;

	public Window(GuiContainer container) {
		this(sImages == null ? initTextures() : sImages, container);
	}

	public Window(Texture textures[][], GuiContainer container) {
		// if (textures.length != 9) {
		// new Exception("The array should be with a lenght of 9 objects");
		// return;
		// }
		setFather(container);

		// StaticRef.layers[0].add(this);
		moveable = true;

		images = textures;
		// boreder's sizes
		captionHeight = textures[0][0].getHeight();
		leftBorderWidth = textures[0][0].getWidth();
		rightBorderWidth = textures[2][0].getWidth();
		bottomBorderHeight = textures[0][2].getHeight();
		// internal size:
		internalWidth = textures[1][1].getWidth();
		internalHeight = textures[1][1].getHeight();
		// external sizes:
		setSize(internalWidth + leftBorderWidth + rightBorderWidth,
				internalHeight + captionHeight + bottomBorderHeight);
		boolean oldValue = autoAddToLists;
		autoAddToLists = false;
		captionText = new Text(internalWidth / 2 + leftBorderWidth,
				captionHeight / 2, null, null);
		autoAddToLists = oldValue;
		captionText.setAlignH(MIDDLE);
		captionText.setAlignV(MIDDLE);

		setSize(internalWidth + leftBorderWidth + rightBorderWidth,
				internalHeight + captionHeight + bottomBorderHeight);
		setTitle("EasyWay Game Engine's Window");
		red = green = blue = 1.0f;
	}
	
	public void setWidth(int width) {
		setSize(width,height);
	}
	
	public void setHeight(int height) {
		setSize(width,height);
	}

	public void setSize(int width, int height) {
		width -= (leftBorderWidth + rightBorderWidth);
		height -= (captionHeight + bottomBorderHeight);
		if (width <= 0)
			width = 0;
		if (height <= 0)
			height = 0;
		internalWidth = width;
		internalHeight = height;
		super.setSize(width + leftBorderWidth + rightBorderWidth, height
				+ captionHeight + bottomBorderHeight);
	}

	public void setBorderSize(int captionSize, int size) {
		leftBorderWidth = size;
		rightBorderWidth = size;
		bottomBorderHeight = size;
		captionHeight = captionSize;
		setSize(internalWidth + leftBorderWidth + rightBorderWidth,
				internalHeight + captionHeight + bottomBorderHeight);

	}

	public void setBorderSize(int size) {
		leftBorderWidth = size;
		rightBorderWidth = size;
		bottomBorderHeight = size;
		captionHeight = size;
		setSize(internalWidth + leftBorderWidth + rightBorderWidth,
				internalHeight + captionHeight + bottomBorderHeight);

	}

	public void setTitle(String title) {
		captionText.setText(blackColor+ title);
		setTextAlign(captionText.getAlignH());
	}

	public void setTextAlign(int align) {
		switch (align) {
		case TextAlign.LEFT:
			captionText.setAlignH(align);
			captionText.setX(getX() + leftBorderWidth);
			break;
		case TextAlign.RIGHT:
			captionText.setAlignH(align);
			captionText.setX(getX() + leftBorderWidth + internalWidth);
			break;
		case TextAlign.MIDDLE:
		default:
			captionText.setAlignH(TextAlign.MIDDLE);
			captionText.setX(getX() + leftBorderWidth + internalWidth / 2);

		}
	}

	// ---------------------------------------------
	// MOUSE
	// ---------------------------------------------

	@Override
	public void onClick(int x, int y) {
	}

	@Override
	public void onDown(int x, int y) {
		if (!dragging)
			gainFocus();
		if (moveable) {
			if (isOnCaption() && Mouse.isLeftDown() && !atracked) {
				atrackX = x;
				atrackY = y;
				atracked = true;
				dragging = true;
			}
		}
	}

	@Override
	public void onDrag(int incx, int incy) {
	}

	@Override
	public void onEnter() {
	}

	@Override
	public void onExit() {
	}

	@Override
	public void onOver(int nx, int ny) {
	}

	@Override
	public void onRelease(int x, int y) {
	}

	@Override
	public Mask getMask() {
		return null;
	}

	protected void mouseMove() {
		if (atracked) {
			if (Mouse.isLeftDown()) {
				setXY(Mouse.getX() - atrackX, Mouse.getY() - atrackY);
			} else {
				atracked = false;
				dragging = false;
			}
		}
	}

	public void customDraw() {
		float oldx = this.x;
		float oldy = this.y;
		if (moveable) {
			mouseMove();
		}

		float nx = xOnScreen += (this.x - oldx);
		float ny = yOnScreen += (this.y - oldy);

		// GL11.glColor4b((byte)red, (byte)green, (byte)blue, (byte)255);
		GL11.glColor4f(red, green, blue, 1.0f);
		// ---- TOP ----
		// top left
		drawComponent(nx, ny, leftBorderWidth, captionHeight, images[0][0]);
		// top center
		drawComponent(nx = nx + leftBorderWidth, ny, internalWidth,
				captionHeight, images[1][0]);
		// top right
		drawComponent(nx + internalWidth, ny, rightBorderWidth, captionHeight,
				images[2][0]);

		// --- CENTER ---
		// left
		drawComponent(nx = xOnScreen, ny = ny + captionHeight, leftBorderWidth,
				internalHeight, images[0][1]);
		// center
		drawComponent(nx = nx + leftBorderWidth, ny, internalWidth,
				internalHeight, images[1][1]);
		// right
		drawComponent(nx + internalWidth, ny, rightBorderWidth, internalHeight,
				images[2][1]);

		// --- BOTTOM ---
		// bottom left
		drawComponent(nx = xOnScreen, ny = ny + internalHeight - 1,
				leftBorderWidth, bottomBorderHeight, images[0][2]);
		// bottom center
		drawComponent(nx = nx + leftBorderWidth, ny, internalWidth,
				bottomBorderHeight, images[1][2]);
		// bottom right
		drawComponent(nx + internalWidth, ny, rightBorderWidth,
				bottomBorderHeight, images[2][2]);

		captionText.setXY(0, yOnScreen + captionHeight / 2);
		setTextAlign(captionText.getAlignH());
		captionText.render();
	}

	protected boolean isOnCaption() {
		int mx = Mouse.getX();
		int my = Mouse.getY();
		float nx = father.getX() + getX();
		float ny = father.getY() + getY();

		if (mx < nx
				|| mx > nx + leftBorderWidth + internalWidth + rightBorderWidth)
			return false;
		if (my < ny || my > ny + captionHeight)
			return false;
		return true;
	}

	public void onDestroy() {
		captionText.destroy();
		super.onDestroy();
	}

	public boolean isMoveable() {
		return moveable;
	}

	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
	}

	public int getCaptionHeight() {
		return captionHeight;
	}

	public int getLeftBorderWidth() {
		return leftBorderWidth;
	}

	public int getRightBorderWidth() {
		return rightBorderWidth;
	}

	public int getBottomBorderHeight() {
		return bottomBorderHeight;
	}

	public void center() {
		if (father != null)
			setXY((father.getWidth() - getWidth()) / 2,
					(father.getHeight() - getHeight()) / 2);
	}

}

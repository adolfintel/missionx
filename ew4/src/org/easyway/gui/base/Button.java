package org.easyway.gui.base;

import org.easyway.input.Mouse;
import org.easyway.interfaces.sprites.IClickable;
import org.easyway.objects.sprites2D.Mask;
import org.easyway.objects.text.Text;
import org.easyway.objects.text.TextAlign;
import org.easyway.objects.texture.Texture;
import org.easyway.objects.texture.TextureID;
import org.lwjgl.opengl.GL11;

public abstract class Button extends GuiContainer implements IClickable {

    protected static Texture[][] sImages;
    protected static Texture fullTexture;

    public static Texture[][] initImages() {
        boolean oldValue = TextureID.USE_MIPMAP;
        TextureID.USE_MIPMAP = false;
        fullTexture = new Texture("/org/easyway/gui/images/button.png", 255,
                255, 255);
        TextureID.USE_MIPMAP = oldValue;

        return sImages = fullTexture.split2D(new int[]{10, 108, 10},
                new int[]{10, 108, 10});
    }
    /**
     * indicates if the button is enabled or not
     */
    protected boolean enabled = true;
    /**
     * the text of the Button
     */
    protected Text text;
    protected Texture[][] images;
    protected int internalWidth, internalHeight;
    int topHeight;
    int leftBorderWidth;
    int rightBorderWidth;
    int bottomBorderHeight;
    /** saturation's color */
    protected float red, green, blue;
    static final String blackColor = Text.createColor(0, 0, 0);

    /**
     * mouse's intreaction's coordinates
     */
    // protected float mouseX, mouseY;
    /**
     * crates a new instance of the Button
     */
    public Button() {
        this(0, 0, "", null);
    }
        /**
     * creates a new instance of the Button
     *
     * @param x
     *            the coordinate (relative to the father's container)
     * @param y
     *            the coordinate (relative to the father's container)
     * @param text
     *            the label's text
     */
     public Button(int x, int y, String text) {
        this(x, y, text, sImages == null ? initImages() : sImages, null);
    }

    /**
     * creates a new instance of the Button
     *
     * @param x
     *            the coordinate (relative to the father's container)
     * @param y
     *            the coordinate (relative to the father's container)
     * @param text
     *            the label's text
     * @param container
     *            the father of the Button
     */
    public Button(int x, int y, String text, GuiContainer container) {
        this(x, y, text, sImages == null ? initImages() : sImages, container);
        // this.text = new Text(x, y, text, null);
    }

    /**
     * creates a new instance of the Button
     *
     * @param x
     *            the coordinate (relative to the father's container)
     * @param y
     *            the coordinate (relative to the father's container)
     * @param text
     *            the label's text
     * @param textures
     *            the images to use for drawing the button
     * @param container
     *            the father of the Button
     */
    public Button(int x, int y, String text, Texture[][] textures,
            GuiContainer container) {
        setFather(container);
        images = textures;
        topHeight = images[0][0].getHeight();
        leftBorderWidth = images[0][0].getWidth();
        rightBorderWidth = images[2][0].getWidth();
        bottomBorderHeight = images[0][2].getHeight();
        setSize(leftBorderWidth + rightBorderWidth, topHeight + bottomBorderHeight);
        setXY(x, y);

        // place the text
        boolean oldValue = Text.autoAddToLists;
        Text.autoAddToLists = false;
        this.text = new Text(x + getWidth() / 2, y + getHeight() / 2, blackColor+text, null);
        Text.autoAddToLists = oldValue;

        this.text.setAlignH(TextAlign.MIDDLE);
        this.text.setAlignV(TextAlign.MIDDLE);

        internalWidth = this.text.getSize();
        internalHeight = this.text.getHeight();

        setSize(getWidth() + this.text.getLength(), getHeight() + this.text.getHeight());
        this.text.setX(x + getWidth() / 2);
        this.text.setY(y + getHeight() / 2);
        red = green = blue = 1.0f;
    }

    @Override
    public void setSize(int width, int height) {
        width -= (leftBorderWidth + rightBorderWidth);
        height -= (topHeight + bottomBorderHeight);
        if (width <= 0) {
            width = 0;
        }
        if (height <= 0) {
            height = 0;
        }
        internalWidth = width;
        internalHeight = height;
        super.setSize(width + leftBorderWidth + rightBorderWidth, height + topHeight + bottomBorderHeight);
    }

    @Override
    public void setWidth(int width) {
        setSize(width, getHeight());
    }

    @Override
    public void setHeight(int height) {
        setSize(getWidth(), height);
    }

    /**
     * the action to do on click
     */
    public abstract void onAction();

    public void customDraw() {
        float nx = xOnScreen;
        float ny = yOnScreen;

        GL11.glColor4f(red, green, blue, 1.0f);
        text.setXY(getWidth() / 2 + nx, getHeight() / 2 + ny);
        // top
        drawComponent(nx, ny, leftBorderWidth, topHeight, images[0][0]);
        nx += images[0][0].getWidth();
        drawComponent(nx, ny, internalWidth, topHeight, images[1][0]);
        nx += internalWidth;
        drawComponent(nx, ny, rightBorderWidth, topHeight, images[2][0]);
        // middle
        nx = xOnScreen;
        ny += images[0][0].getHeight();
        drawComponent(nx, ny, leftBorderWidth, internalHeight, images[0][1]);
        nx += images[0][1].getWidth();
        drawComponent(nx, ny, internalWidth, internalHeight, images[1][1]);
        nx += internalWidth;
        drawComponent(nx, ny, rightBorderWidth, internalHeight, images[2][1]);
        // bottom
        nx = xOnScreen;
        ny += internalHeight;// images[1][1].getHeight();
        drawComponent(nx, ny, leftBorderWidth, bottomBorderHeight, images[0][2]);
        nx += images[0][2].getWidth();
        drawComponent(nx, ny, internalWidth, bottomBorderHeight, images[1][2]);
        nx += internalWidth;
        drawComponent(nx, ny, rightBorderWidth, bottomBorderHeight,
                images[2][2]);
        text.render();
    }

    /**
     * changes the text of the Button
     *
     * @param newtext
     *            the new text to assign to the button
     */
    public void setText(String newtext) {
        text.setText(blackColor+newtext);
    }

    /**
     * returns the text of the button
     *
     * @return the text of the button
     */
    public String getText() {
        return text.getText();
    }

    @Override
    public void onClick(int x, int y) {
    }

    @Override
    public void onDown(int x, int y) {
        if (Mouse.isLeftDown()) {
            red = green = 1.0f;
            blue = 0.0f;
        }
    }

    @Override
    public void onDrag(int incx, int incy) {
    }

    @Override
    public void onEnter() {
        red = 0.0f;
        green = blue = 1.0f;
    }

    @Override
    public void onExit() {
        red = green = blue = 1.0f;
    }

    @Override
    public void onOver(int nx, int ny) {
    }

    @Override
    public void onRelease(int x, int y) {
        if (Mouse.isLeftReleased()) {
            red = 1.0f; // color's feedback
            green = blue = 1.0f;
            onFeedBack();
            onAction();
        }
    }

    @Override
    public Mask getMask() {
        return null;
    }

    protected void onFeedBack() {
        new ButtonEffect(this);
    }

    @Override
    public void onDestroy() {
        text.destroy();
        super.onDestroy();
    }
//	@Override
//	public float getX() {
//		return mouseX;
//	}
//
//	@Override
//	public float getY() {
//		return mouseY;
//	}
}

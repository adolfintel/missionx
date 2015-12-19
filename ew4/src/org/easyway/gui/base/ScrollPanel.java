package org.easyway.gui.base;

import static java.lang.Math.max;

import org.easyway.gui.base.scrollBar.HScrollBar;
import org.easyway.gui.base.scrollBar.VScrollBar;
import org.easyway.objects.nativeGraphic.Rectangle;
import org.easyway.system.state.OpenGLState;

public class ScrollPanel extends GuiContainer {

    private static final long serialVersionUID = 1L;
    /**
     * the virtual area size
     */
    int areaWidth, areaHeight;
    /**
     * the amount of scrolled area
     */
    private int scrollX, scrollY;
    /**
     * the size in pixels of the scrollbar
     */
    protected int scrollbarSize = 25;
    /**
     * the rectangle that will be drown from the Panel
     */
    Rectangle rect;
    /**
     * indicates if the HScrollBar is visible or not
     */
    boolean HScrollBarVisible = true;
    /**
     * indicates if the VScrollBar is visible or not
     */
    boolean VScrollBarVisible = true;
    /**
     * the HScrollBar of the component
     */
    HScrollBar hScrollBar;
    /**
     * the VScrollBar of the component
     */
    VScrollBar vScrollBar;
    /**
     * max and min values of the scrolling
     */
    int minX = 0, minY = 0, maxX, maxY;

    /**
     * creates a new instance of the ScrollPanel
     *
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     * @param width
     *            the width of the panel
     * @param height
     *            the height of the panel
     * @param container
     *            the GuiContainer father
     */
    public ScrollPanel(int x, int y, int width, int height,
            GuiContainer container) {
        super(x, y, width, height, "Panel");
        setFather(container);

        // create the Rectangle
        boolean oldValue = Rectangle.autoAddToLists;
        Rectangle.autoAddToLists = false;
        rect = new Rectangle(getX(), getY(), getWidth(), getHeight());
        Rectangle.autoAddToLists = oldValue;

        addChild(hScrollBar = new HScrollBar(0, height - scrollbarSize, width,
                scrollbarSize));
        addChild(vScrollBar = new VScrollBar(width - scrollbarSize, 0,
                scrollbarSize, height));
        minX = 0;
        minY = 0;
        maxX = width;
        maxY = height;
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

    /**
     * changes the virtual area of the ScrollPanel
     *
     * @param width
     * @param height
     */
    public void setVirtualArea(int width, int height) {
        areaWidth = max(width, getWidth());
        areaHeight = max(height, getHeight());
    }

    public void draw(float x, float y) {
        OpenGLState.enableScissor((int) (x + this.x), (int) (y + this.y),
                getWidth(), getHeight());
        xOnScreen = x + this.x;
        yOnScreen = y + this.y;
        // super.draw(x, y);
        customDraw();
        OpenGLState.disableScissor();
    }

    public void customDraw() {
        rect.setXY(xOnScreen, yOnScreen);
        rect.render();
        children.startScan();
        while (children.next()) {
            children.getCurrent().draw(xOnScreen - scrollX, yOnScreen - scrollY);
        }

    }

    public boolean isHScrollBarVisible() {
        return HScrollBarVisible;
    }

    public void setHScrollBarVisible(boolean scrollBarVisible) {
        if (HScrollBarVisible = scrollBarVisible) {
            if (!children.contains(hScrollBar)) {
                addChild(hScrollBar);
            }
        } else {
            if (children.contains(hScrollBar)) {
                removeChild(hScrollBar);
            }
        }
    }

    public boolean isVScrollBarVisible() {
        return VScrollBarVisible;
    }

    public void setVScrollBarVisible(boolean scrollBarVisible) {
        if (VScrollBarVisible = scrollBarVisible) {
            if (!children.contains(vScrollBar)) {
                addChild(vScrollBar);
            }
        } else {
            if (children.contains(vScrollBar)) {
                removeChild(vScrollBar);
            }
        }
    }

    public int getScrollX() {
        return scrollX;
    }

    public void setScrollX(int scrollX) {
        this.scrollX = Math.min(maxX, Math.max(minX, scrollX));
    }

    public int getScrollY() {
        return scrollY;
    }

    public void setScrollY(int scrollY) {
        this.scrollY = Math.min(maxY, Math.max(minY, scrollY));
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setInternalSize(int width, int height) {
        maxX = width - this.width;
        maxY = height - this.height;
        if (maxX < 0) {
            maxX = 0;
        }
        if (maxY < 0) {
            maxY = 0;
        }
    }
}

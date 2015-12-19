package org.easyway.gui.base;

import org.easyway.objects.Camera;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.io.Serializable;

import java.util.ArrayList;
import org.easyway.input.MouseAssociation;
import org.easyway.interfaces.base.IPureRender;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.interfaces.sprites.IPlain2D;
import org.easyway.lists.GameList;
import org.easyway.objects.sprites2D.Mask;
import org.easyway.objects.texture.Texture;
import org.easyway.system.state.GameState;

public abstract class GuiContainer extends MouseAssociation implements
        IContainer, IPlain2D, Serializable, IPureRender {

    /**
     * generated serial version id
     */
    private static final long serialVersionUID = -2001400966226380966L;
    /**
     * all the children of the container
     */
    public GameList<IContainer> children = new GameList<IContainer>();
    /**
     * the father of the current container
     */
    public GuiContainer father;// = Camera.getMainFather();
    /**
     * coordinates of the window
     */
    protected float x, y;
    /**
     * the size of the window
     */
    protected int width, height;
    /**
     * coordinates on screen
     */
    protected float xOnScreen, yOnScreen;
    /**
     * the idlayer
     */
    protected int idLayer = -1;
    public static int DEF_IDLAYER = GameState.getCurrentGEState().getLayers().length - 1;

    // -----------------------------------------------
    // -----------------------------------------------
    public GuiContainer() {
        this(0, 0, 0, 0);
    }

    public GuiContainer(String name) {
        this(0, 0, 0, 0, name);
    }

    public GuiContainer(float x, float y) {
        this(x, y, 0, 0);
    }

    public GuiContainer(int width, int height) {
        this(0, 0, width, height);
    }

    public GuiContainer(float x, float y, int width, int height) {
        this(x, y, width, height, "$_CONTAINER");
    }

    public GuiContainer(float x, float y, int width, int height, String name) {
        setType(name);
        setXY(x, y);
        setSize(width, height);
        isDrawable = false;
    }

    @Override
    protected void autoActivate(boolean toAdd) {
        if (!toAdd || !autoAddToLists) {
            return;
        }
        if (isLoopable) {
            GameState.getCurrentGEState().getLoopList().add((ILoopable) this);
        }
        // if (father == null)
        // Mouse.addGuiObject(this);
    }

    // -----------------------------------------------
    // -----------------------------------------------
    public void render() {
        if (father == null) {
            draw(0, 0);// getXOnScreen(), getYOnScreen());
        }
    }

    @Override
    public void draw(float x, float y) {
        xOnScreen = x + this.x;
        yOnScreen = y + this.y;
        customDraw();
        children.startScan();
        while (children.next()) {
            children.getCurrent().draw(this.x + x, this.y + y);
        }
    }

    public abstract void customDraw();

    /**
     * sets a container's father<br>
     * note: using a 'NULL' father the object will be added to the
     * defaultFather.<br>
     * If you want set the father as NULL use the method <b>setNullFather</b>
     *
     * @see #setNullFather()
     * @param container
     */
    public void setFather(GuiContainer container) {
        if (container == null) {
            container = Camera.getMainFather();
        }
        if (father != null) {
            father.removeChild(this);
        }
        father = container;
        // father.addChild(this);
        father.getChildren().add(this);
        // if (container == Camera.getMainFather() || container == null) {
        // if (father != Camera.getMainFather()) {
        // // Mouse.addGuiObject(this);
        // }
        // father = Camera.getMainFather();
        // Camera.getMainFather().addChild(this);
        // } else {
        // container.addChild(this);
        // if (father == Camera.getMainFather()) {
        // // Mouse.removeGuiObject(this);
        // }
        // father = container;
        // }
    }

    public void setNullFather() {
        if (father != null) {
            father.removeChild(this);
            // if (father == Camera.getMainFather()) {
            // // Mouse.removeGuiObject(this);
            // }
        }
        father = null;
    }

    /**
     * adds this container inside another
     *
     * @param container
     */
    public void addChild(GuiContainer container) {
        // container.father = this;
        // children.add(container);
        container.setFather(this);
    }

    /**
     * removes a child from the container
     *
     * @param container
     */
    public void removeChild(GuiContainer container) {
        container.father = null;
        children.remove(container);
    }

    // ------------------------------------------------
    // ------------ IPLAIN2D --------------------------
    // ------------------------------------------------
    public void incX(float incx) {
        x += incx;
        checkBounds();
    }

    public void incY(float incy) {
        y += incy;
        checkBounds();
    }

    public void move(float incx, float incy) {
        x += incx;
        y += incy;
        checkBounds();
    }

    @Override
    public void setHeight(int height) {
        if (height < 0) {
            return;
        }
        this.height = height;
        checkBounds();

        children.startScan();
        while (children.next()) {
            children.getCurrent().checkBounds();
        }
    }

    @Override
    public void setSize(int width, int height) {
        if (width < 0 || height < 0) {
            return;
        }
        this.height = height;
        this.width = width;
        checkBounds();

        children.startScan();
        while (children.next()) {
            children.getCurrent().checkBounds();
        }
    }

    @Override
    public void setWidth(int width) {
        if (width < 0) {
            return;
        }
        this.width = width;
        checkBounds();

        children.startScan();
        while (children.next()) {
            children.getCurrent().checkBounds();
        }
    }

    @Override
    public void setX(float x) {
        this.x = x;
        checkBounds();
    }

    @Override
    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
        checkBounds();
    }

    @Override
    public void setY(float y) {
        this.y = y;
        checkBounds();
    }

    @Override
    public boolean checkBounds() {
        if (father == null) {
            return true;
        }
        if (getX() < father.getX()) {
            x = father.getX();
        }
        if (getX() + getWidth() > father.getX() + father.getWidth()) {
            x = father.getX() + father.getWidth() - getWidth();
        }
        if (getY() < father.getY()) {
            y = father.getY();
        }
        if (getY() + getHeight() > father.getY() + father.getHeight()) {
            y = father.getY() + father.getHeight() - getHeight();
        }
        return false;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public float getX() {
        if (father == null || father == this) {
            return x;
        }
        return x + father.getX();
    }

    @Override
    public float getY() {
        if (father == null || father == this) {
            return y;
        }
        return y + father.getY();
    }

    @Override
    public float getScaleX() {
        return getWidth();
    }

    @Override
    public float getScaleY() {
        return getHeight();
    }

    @Override
    public float getXOnScreen() {
        return xOnScreen;
    }

    @Override
    public float getYOnScreen() {
        return yOnScreen;
    }

    protected void drawComponent(float x, float y, Texture image) {
        drawComponent(x, y, image.getWidth(), image.getHeight(), image);
    }

    protected void drawComponent(float x, float y, int width, int height,
            Texture image) {
        image.bind();
        glBegin(GL_QUADS);
        {
            glTexCoord2f(image.getXStart(), image.getYStart());
            glVertex2f(x, y);

            glTexCoord2f(image.getXEnd(), image.getYStart());
            glVertex2f(x + width, y);

            glTexCoord2f(image.getXEnd(), image.getYEnd());
            glVertex2f(x + width, y + height);

            glTexCoord2f(image.getXStart(), image.getYEnd());
            glVertex2f(x, y + height);
        }
        glEnd();
    }

    @Override
    public void onDestroy() {
        setNullFather();

        // destroy all the children
        children.startScan();
        while (children.next()) {
            children.getCurrent().destroy();
        }
    }

    @Override
    public IContainer getFather() {
        return father;
    }

    // ---------------------------------
    // ---------------------------------
    @Override
    public Mask getMask() {
        return null;
    }

    @Override
    public void onClick(int x, int y) {
    }

    @Override
    public void onDown(int x, int y) {
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
    public GameList<IContainer> getChildren() {
        return children;
    }

    @Override
    public MouseAssociation getMouseAssociation() {
        return this;
    }

    public void gainFocus() {
        if (!isOnFocus()) {
            father.children.remove(this);
            father.children.add(this, 0);
        }
    }

    public boolean isOnFocus() {
        return father != null && father.children.get(0) == this;
    }

    @Override
    public ArrayList getQuadEntries() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList getUsedInQuadNodes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isQuadTreeUsable() {
        return false;
    }
}

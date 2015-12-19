package org.easyway.objects.sprites2D.sentry;

import java.util.ArrayList;

import org.easyway.collisions.CollisionUtils;
import org.easyway.collisions.ICollisionMethod;
import org.easyway.collisions.methods.HardWarePixelMethod;
import org.easyway.collisions.IQuadTreeUsable;
import org.easyway.collisions.ManagedGroupCollision;
import org.easyway.collisions.MgcList;
import org.easyway.collisions.methods.SoftwareCollisionMethod;
import org.easyway.collisions.quad.QuadEntry;
import org.easyway.collisions.quad.QuadNode;
import org.easyway.collisions.quad.QuadTree;
import org.easyway.interfaces.base.IPureRender;
import org.easyway.interfaces.base.ITexture;
import org.easyway.interfaces.extended.IDrawing;
import org.easyway.interfaces.extended.ILayerID;
import org.easyway.interfaces.sprites.ICollisionable;
import org.easyway.interfaces.sprites.IMaskerable;
import org.easyway.interfaces.sprites.IPlain2D;
import org.easyway.interfaces.sprites.ISpriteColl;
import org.easyway.objects.BaseObject;
import org.easyway.objects.sprites2D.IPolyMaskerable;
import org.easyway.objects.sprites2D.Mask;
import org.easyway.objects.sprites2D.PolygonMask;
import org.easyway.objects.sprites2D.StaticFullyMask;
import org.easyway.shader.Shader;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;
import org.easyway.system.state.GameState;
import org.lwjgl.opengl.GL11;

public class Sentry extends BaseObject implements IPureRender, IPlain2D,
        ICollisionable, IDrawing, IMaskerable, IPolyMaskerable, ILayerID, ISpriteColl {

    private static final long serialVersionUID = -3159539610220239454L;
    public static boolean showSentryes = true;
    /**
     * coordinates
     */
    protected float x, y;
    protected float relativeX, relativeY;
    boolean addedAtQuad = false;
    /**
     * size
     */
    protected int width, height;
    /**
     * indicates if the object is added to the collisionable list
     */
    protected boolean isAdded;
    protected ArrayList<ISpriteColl> collisionList = new ArrayList<ISpriteColl>(10);
    protected Mask mask;
    protected PolygonMask polyMask;
    protected float color = 1.0f;
    SentrySprite father;
    /** Depth of sprite */
    private int layer; // private
    /**
     * the drawing sheet
     */
    private int idLayer = -1;
    private ICollisionMethod collisionMethod = Core.getGLIntVersion() >= 15 ?  HardWarePixelMethod.getDefaultInstance() : SoftwareCollisionMethod.getDefaultInstance();
    private ITexture image;

    public Sentry(SentrySprite father, String name) {
        this.father = father;
        type = name;
        setIdLayer(GameState.getCurrentGEState().getLayers().length - 1);
        setLayer(Integer.MAX_VALUE);
    }

    public Sentry(SentrySprite father, String name, float x, float y,
            int width, int height) {
        this.father = father;
        this.name = name;
        type = name;
        relativeX = x;
        relativeY = y;
        setXY(x + father.getX(), y + father.getY());
        setSize(width, height);
        setFullMask();

        // top
        setIdLayer(GameState.getCurrentGEState().getLayers().length - 1);
        setLayer(Integer.MAX_VALUE);
    }

    public Sentry(Sentry obj) {
        super(obj);
        x = obj.x;
        y = obj.y;
        width = obj.width;
        height = obj.height;
        isAdded = obj.isAdded; // should be false..
        mask = obj.mask;
        color = obj.color;
        father = obj.father; // we DON'T add the Sentry to the father.. :)
        setIdLayer(obj.idLayer);
        setLayer(obj.layer);
    }

    @Override
    public void render() {
        if (showSentryes) {
            Shader.unBind();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glColor3f(color, 1 - color, 0);
            GL11.glVertex2f(x - StaticRef.getCamera().x, y - StaticRef.getCamera().y);
            GL11.glVertex2f(x - StaticRef.getCamera().x + width, y - StaticRef.getCamera().y);
            GL11.glVertex2f(x - StaticRef.getCamera().x + width, y - StaticRef.getCamera().y + height);
            GL11.glVertex2f(x - StaticRef.getCamera().x, y - StaticRef.getCamera().y + height);
            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            color = 1.0f;
        }
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    /**
     * sets the size of object
     *
     * @param width
     *            the width
     * @param height
     *            the height
     */
    @Override
    public void setSize(int width, int height) {
        this.width = width;
        if (this.width < 1) {
            this.width = 1;
        }
        this.height = height;
        if (this.height < 1) {
            this.height = 1;
        }
        addQuadsUpdate();
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getXOnScreen() {
        return x;
    }

    @Override
    public float getYOnScreen() {
        return y;
    }

    @Override
    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
        addQuadsUpdate();
    }

    public void setLocalXY(float x, float y) {
        relativeX = x;
        relativeY = y;
        setXY(x + father.getX(), y + father.getY());
    }

    public float getLocalX() {
        return relativeX;
    }

    public float getLocalY() {
        return relativeY;
    }

    public void update() {
        setXY(relativeX + father.getX(), relativeY + father.getY());
    }

    @Override
    public boolean isAddedToCollisionList() {
        return isAdded;
    }

    @Override
    public void setAddedToCollisionList(boolean value) {
        isAdded = value;
    }

    @Override
    public ArrayList<ISpriteColl> getCollisionList() {
        return collisionList;
    }

    @Override
    public void onCollision() {
        color = 0.0f;
        father.sentryCollision(this);

    }

    @Override
    public Mask getMask() {
        return mask;
    }

    /**
     * sets the mask
     *
     * @param mask
     *            the mask to set
     */
    public void setMask(Mask mask) {
        this.mask = mask;
    }

    @Override
    public PolygonMask getPolyMask() {
        if (polyMask != null) {
            return polyMask;
        }
        if (getImage() != null) {
            return getImage().getPolyMask();
        }
        return null;
    }

    /**
     * returns the collision mask
     *
     * @param mask
     *            the collision mask
     */
    public void setPolygonMask(PolygonMask mask) {
        this.polyMask = mask;
    }

    /**
     * sets the default fully static rectangolar mask.
     *
     */
    public void setFullMask() {
        mask = new StaticFullyMask(width, height);
    }

    /** sets the depth */
    public void setLayer(int layer) {
        this.layer = layer;
        readdToDrawingLists();
    }

    @Override
    public int getLayer() {
        return layer;
    }

    @Override
    public int getIdLayer() {
        return idLayer;
    }

    @Override
    public float getScaleX() {
        return width;
    }

    @Override
    public float getScaleY() {
        return height;
    }
    
    @Override
    public void setIdLayer(int id) {
        if (idLayer != -1) {
            GameState.getCurrentGEState().getLayers()[idLayer].remove(this);
        }
        if (id < 0) {
            id = 0;
        } else if (id > GameState.getCurrentGEState().getLayers().length) {
            id = GameState.getCurrentGEState().getLayers().length;
        }
        idLayer = id;
        GameState.getCurrentGEState().getLayers()[idLayer].add(this);
    }

    @Override
    public float getRotation() {
        return 0;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
        addQuadsUpdate();

    }

    @Override
    public void setWidth(int width) {
        this.width = width;
        addQuadsUpdate();
    }

    /* (non-Javadoc)
     * @see org.easyway.interfaces.sprites.IPlain2D#setX(float)
     */
    @Override
    public void setX(float x) {
        this.x = x;
        addQuadsUpdate();
    }

    /* (non-Javadoc)
     * @see org.easyway.interfaces.sprites.IPlain2D#setY(float)
     */
    @Override
    public void setY(float y) {
        this.y = y;
        addQuadsUpdate();
    }

    @Override
    public Object clone() {
        return new Sentry(this);
    }

    @Override
    public ICollisionMethod getCollisionMethod() {
        return collisionMethod;
    }

    public void setCollisionMethod(ICollisionMethod collMethod) {
        this.collisionMethod = collMethod;
    }

    @Override
    public ITexture getImage() {
        return image;
    }

    public void setImage(ITexture image) {
        this.image = image;
        addQuadsUpdate();
    }
    MgcList collisionTesterList = new MgcList();

    @Override
    public MgcList getMgcs() {
        return collisionTesterList;
    }
    ArrayList<QuadNode> quadNodes = new ArrayList<QuadNode>(4);

    @Override
    public ArrayList<QuadNode> getUsedInQuadNodes() {
        return quadNodes;
    }
    ArrayList<QuadEntry> quadEntries = new ArrayList<QuadEntry>(4);

    @Override
    public ArrayList<QuadEntry> getQuadEntries() {
        return quadEntries;
    }

    public boolean testCollision(ISpriteColl spr) {
        return CollisionUtils.trueHardWareHit(this, spr);
    }

    /**
     * adds the plain from the Quad-Engine
     *
     */
    protected void addQuadsUpdate() {
        if (!testQuadTreeUsable()) {
            return;
        }
        IQuadTreeUsable mgci = (IQuadTreeUsable) this;
        QuadTree.getDefaultInstance().add(mgci);
        MgcList list = mgci.getMgcs();
        for (ManagedGroupCollision mgc : list) {
            mgc.update((ISpriteColl) this);
        }
        addedAtQuad = true;
    }

    /**
     * removes the plain from the Quad-Engine<br>
     */
    protected void removeQuadsUpdate() {
        if (!testQuadTreeUsable()) {
            return;
        }
        QuadTree.getDefaultInstance().remove((IQuadTreeUsable) this);
        addedAtQuad = false;
    }

    @Override
    public void destroy() {
        if (isDestroyed()) {
            return;
        }
        if (addedAtQuad) {
            removeQuadsUpdate();
        }
        super.destroy();
    }

    @Override
    public boolean isQuadTreeUsable() {
        return true;
    }

    @Override
    public void setRotation(float newRotation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private boolean testQuadTreeUsable() {
        return isQuadTreeUsable && isQuadTreeUsable();
    }

    @Override
    public void renderAt(float x, float y, float z, int unit) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Shader.unBind();
        GL11.glLoadIdentity();
        GL11.glColor4f(1, 0, 1,1);
        GL11.glTranslatef(0, 0, z);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x, y + height);
        GL11.glEnd();
        GL11.glLoadIdentity();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    @Override
    public Object getCollisionData() {
        switch (getCollisionMethod().getCollisionType()) {
            case PIXEL_SOFTWARE:
                return getMask();
            case VECTORIAL:
                return polyMask;
            default:
                return null;
        }
    }

    @Override
    public Class getCollisionDataType() {
        switch (getCollisionMethod().getCollisionType()) {
            case PIXEL_SOFTWARE:
                return Mask.class;
            case VECTORIAL:
                return PolygonMask.class;
            default:
                return null;
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.art;

import mygame.*;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;
import org.easyway.system.state.OpenGLState;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Do$$e
 */
public class BackgroundSprite extends Sprite implements ILoopable {

    public String img;
    boolean loaded = false;
    private boolean additiveRender=false,destroyTexture=false;

    public BackgroundSprite(float x, float y, String img,boolean additive,boolean destroyTexture) {
        super(x, y, null, 0);
        setIdLayer(1);
        this.img = img;
        this.setSmoothImage(CommonVar.smoothing);
        additiveRender=additive;
        this.destroyTexture=destroyTexture;
        if(CommonVar.lowRes){ this.img=this.img.replace(".png","_lo.png"); this.img=this.img.replace(".bmp","_lo.bmp");}
    }

    public BackgroundSprite() {
        super(0, 0, Texture.getTexture("images/missingBkg.png"));
        this.setSmoothImage(CommonVar.smoothing); 

    }

    public boolean isAdditiveRender() {
        return additiveRender;
    }

    public void setAdditiveRender(boolean additiveRender) {
        this.additiveRender = additiveRender;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public void loop() {
        if (CommonVar.destroyAll == true) {
            destroy();
            return;
        }
        if (getXOnScreen() < CommonVar.xRes*StaticRef.getCamera().getZoom2D()+32 && getXOnScreen()+2048>0 && !loaded) {
            System.out.println("Loading background: " + img);
            loaded = true;
            Core.setSpeedCompensation(false);
            setImage(Texture.getTexture(img));
            if(CommonVar.lowRes){setScaleX(4); setScaleY(4);}
            Core.setSpeedCompensation(true);
        }
        if (getXOnScreen() + getWidth() < 0) {
            //if(destroyTexture){System.out.println("Destroying background: " + img); StaticRef.textures.remove(getImage());}
            this.destroy();
        }
    }

    @Override
    public void customDraw() {
        if(additiveRender){
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        super.customDraw();
        OpenGLState.useTranspBlendingMode();
        }else super.customDraw();
    }
}

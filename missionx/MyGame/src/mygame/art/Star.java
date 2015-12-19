package mygame.art;

import mygame.*;
import mygame.logic.CamController;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;
import org.easyway.utils.MathUtils;

public class Star extends Sprite implements ILoopable {

    float speedX;
    float distance,strength;
    Texture stopped;
    Texture normal;

    public Star(int x, int y,float strength) {
        super(x, y, Texture.getTexture("images/bigStar.png"), 2);
        StaticRef.particleCount++;
        this.strength=strength;
        this.setSmoothImage(CommonVar.smoothing);
        float minD=(float) (0.5 - strength / 2);
        float maxD=(float) (0.5 + strength / 2);
        distance = MathUtils.lerp(minD,maxD,(float)Math.random());
        if (distance > 0.5f) {
            setImage("images/smallStar.png");
            stopped=Texture.getTexture("images/smallStarDot.png");
            normal=Texture.getTexture("images/smallStar.png");
            setIdLayer(0);
        }else{
            stopped=Texture.getTexture("images/bigStarDot.png");
            normal=Texture.getTexture("images/bigStar.png");
        }
        setSize(normal.getWidth(),normal.getHeight());
    }

    @Override
    public void loop() {
        if (CommonVar.destroyAll) {
            destroy();
        }
        if (!Core.getInstance().isGamePaused()) {
            float speedMul = Core.getInstance().getSpeedMultiplier();
            if (getXOnScreen() < 0 - getWidth()) {
                destroy();
            }
            if (CamController.getDefaultInstance().getSpeedX() >0.3f) {
                //speedX = InvisibleSprite.getDefaultInstance().getSpeedX() - (1 - distance) - 0.1f;
                speedX=Core.lerp(CamController.getDefaultInstance().getSpeedX(),-CamController.getDefaultInstance().getSpeedX(),distance-0.1f);
                setImage(normal);
                setWidth((int) ((CamController.getDefaultInstance().getSpeedX() +0.4f) * (normal.getWidth())));
            } else {
                if(CamController.getDefaultInstance().getSpeedX() ==0) speedX = 0; else speedX=Core.lerp(CamController.getDefaultInstance().getSpeedX(),-CamController.getDefaultInstance().getSpeedX(),distance)-0.1f;
                setImage(stopped);
                setWidth(stopped.getWidth());
            }
            move((float) speedX * speedMul, 0);
        }
    }

    @Override
    protected void onDestroy() {
        StaticRef.particleCount--;
    }
}


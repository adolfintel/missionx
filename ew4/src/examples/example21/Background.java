/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example21;

import java.util.ArrayList;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.TextureFBO;
import org.easyway.shader.Shader;
import org.easyway.shader.ShaderEffect;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;
import static org.easyway.objects.texture.Texture.getTexture;

/**
 *
 * @author RemalKoil
 */
public class Background extends Sprite {

    TextureFBO paintableImage;
    ArrayList<Sprite> toRenderList;

    public Background() {
        super(getTexture("/examples/example21/images/terrain.jpg"));
        paintableImage = new TextureFBO(getImage());
        setIdLayer(0);
        setSize(Core.getInstance().getWidth(), Core.getInstance().getHeight());
        toRenderList = new ArrayList<Sprite>(30);
        
        if (StaticRef.use_shaders) {
            ShaderEffect se = new ShaderEffect();
            se.addPass(new Shader(null, "/shaders/GaussBlur.fs"));
            setEffect(se);
        }
    }

    public void drawOnBackground(Sprite spr) {
        toRenderList.add(spr);
    }

    @Override
    public void render() {
        paintableImage.startDrawing(false);
        {
            float scalex = (float) image.getWidth() / (float) getWidth();
            float scaley = (float) image.getHeight() / (float) getHeight();
            for (Sprite spr : toRenderList) {
                // we need to scale the size and position 'cause the
                spr.setSize((float) spr.getWidth() * scalex, (float) spr.getHeight() * scaley);
                spr.setXY(spr.getX() * scalex, spr.getY() * scaley);
                // render it on the background!
                spr.render();

                spr.destroy();
            }
            toRenderList.clear();
        }
        paintableImage.endDrawing();
        // draw the sprite:
        super.render();
    }
}

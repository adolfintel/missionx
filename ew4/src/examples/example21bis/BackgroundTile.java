/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example21bis;

import java.util.ArrayList;
import org.easyway.interfaces.base.ITexture;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.TextureFBO;
import org.easyway.shader.Shader;
import org.easyway.system.StaticRef;
import org.easyway.tiles.Tile;

/**
 *
 * @author RemalKoil
 */
public class BackgroundTile extends Tile {

    TextureFBO paintableImage;
    ArrayList<Sprite> toRenderList;
    Shader effect = null;

    public BackgroundTile(ITexture image) {
        super(image);
        paintableImage = new TextureFBO(getImage());
        toRenderList = new ArrayList<Sprite>(30);

        if (StaticRef.use_shaders) {
            effect = new Shader(null, "/shaders/GaussBlur.fs");
        }
    }

    public void drawOnBackground(Sprite spr) {
        toRenderList.add(spr);
    }

    @Override
    public void render(float x, float y, float width, float height) {
        if (getImage() == null) {
            return;
        }

        paintableImage.startDrawing(false);
        {
            float scalex = (float) getImage().getWidth() / width;
            float scaley = (float) getImage().getHeight() / height;
            for (Sprite spr : toRenderList) {
                // we need to scale the size and position 'cause the
                spr.setSize((float) spr.getWidth() * scalex, (float) spr.getHeight() * scaley);
                spr.setXY( (spr.getX()-x) * scalex, (spr.getY()-y) * scaley);
                // render it on the background!
                spr.render();
                spr.destroy();
            }
            toRenderList.clear();
        }
        paintableImage.endDrawing();

        // prepare shader effect
        if (StaticRef.use_shaders) {
            if (effect != null) {
                effect.bind();
            } else {
                Shader.unBind();
            }
        }
        // render the tile!
        super.render(x, y, width, height);
    }
}

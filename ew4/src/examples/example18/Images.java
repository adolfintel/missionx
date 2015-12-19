/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example18;

import org.easyway.objects.texture.Texture;

/**
 *
 * @author Daniele Paggi
 */
public enum Images {
    // --------------------------------------------------
    BASKET_BALL("images/basket_ball.png"),
    WALL1("examples/example18/wall1.png"),
    WALL2("images/tileset/block.png");

    // --------------------------------------------------
    private String path;
    Images(String path){
        Texture.getTexture(this.path = path);
    }

    public Texture getImage() {
        return Texture.getTexture(path);
    }


}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.shader;

import java.util.ArrayList;
import org.easyway.objects.texture.Texture;
import org.easyway.objects.texture.TextureFBO;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;

/**
 *
 * @author Daniele Paggi
 */
public class ShaderEffect implements Iterable<Shader> {

    private static ShaderEffect defaultShaderEffect;
    //private static ArrayList<TextureFBO> frameBufferObjects = new ArrayList<TextureFBO>();
    static TextureFBO lastPass;

    public static ShaderEffect getDefaultShaderEffect() {
        if (!StaticRef.use_shaders) {
            return null;
        }
        if (defaultShaderEffect == null) {
            System.out.println("loading default shaderEffect..");
            defaultShaderEffect = new ShaderEffect();
            defaultShaderEffect.addPass(Shader.getDefaultShader());
            lastPass = new TextureFBO(new Texture(Core.getInstance().getWidth(), Core.getInstance().getHeight()));
        }
        return defaultShaderEffect;
    }
    ArrayList<Shader> passes = new ArrayList<Shader>();

    public void addPass(Shader shader) {
        if (passes == null) {
            passes = new ArrayList<Shader>();
        }
        passes.add(shader);
    }

    public void setPass(Shader shader, int index) {
        if (passes.get(index) != null) {
            passes.remove(index);
        }
        passes.add(index, shader);
    }

    public Shader getPass(int index) {
        return passes.get(index);
    }

    public void removePass(int index) {
        passes.remove(index);
    }

    public void removePass(Shader shader) {
        passes.remove(shader);
    }

    public int size() {
        return passes.size();
    }

    public ShaderEffectItreable iterator() {
        return new ShaderEffectItreable(this);
    }
}

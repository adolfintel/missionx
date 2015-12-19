/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.shader;

import org.easyway.interfaces.sprites.IPlain2D;
import org.easyway.system.Core;

/**
 *
 * @author Daniele Paggi
 */
public class WaveShader extends Shader<IPlain2D> {

    float timeFraction = 500f;
    float power = 2.0f;
    float lenght;

    public WaveShader() {
        super(null, "shaders/Flag.fs");
        setLenght(50f);

    }

    public void setLenght(float lenght) {
        setUniform("lenght", this.lenght = lenght);
    }

    public void setAmplitude(float amp) {
        setUniform("frequency", amp);
    }

    public void setTime(float time) {
        setUniform("time", time);
    }

    public void setPower(float power) {
        this.power = power;
    }

    public void setFrequency(float frequency) {
        timeFraction = frequency;
    }

    @Override
    public void update(IPlain2D target) {
        float time = Core.getInstance().getTotalElaspedTime() / timeFraction;
        setTime(time);
        float width = target.getWidth();
        setAmplitude(1f / width * power);
    }
}

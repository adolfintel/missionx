package org.easyway.objects.particles;

import org.easyway.interfaces.base.ITexture;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.animo.Animo;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;

public class Particle extends Sprite implements ILoopable {

    float speedX, speedY, incY, incX, scale, r, g, b, rotationRate, xAcceleration, yAcceleration, rotRateInc, opacity;
    boolean useAnimo;
    int fadeInFrames, fadeOutFrames, life;
    float cycle = 0;
    Animo animation;

    public Particle(ITexture img, Animo animazione, boolean useAnimo, float x, float y, float speedX, float speedY, int lifeMs, float incX, float incY, int fadeInFramesMs, int fadeOutFramesMs, float scale, float R, float G, float B, float opacity, int layer, float rotation, float rotationRate, float xAcceleration, float yAcceleration, float rotRateInc, float drawScale) {
        super(x, y, img, layer);
        StaticRef.particleCount++;
        this.speedX = speedX * scale;
        this.speedY = speedY * scale;
        this.life = lifeMs / 20;
        this.incX = incX * scale;
        this.incY = incY * scale;
        this.fadeInFrames = fadeInFramesMs / 20;
        this.fadeOutFrames = fadeOutFramesMs / 20;
        this.scale = scale;
        setWidth((int) (getWidth() * drawScale));
        setHeight((int) (getHeight() * drawScale));
        this.r = R;
        this.g = G;
        this.b = B;
        if (fadeInFramesMs != 0) {
            setRGBA(r, g, b, 0f);
        } else {
            setRGBA(r, g, b, opacity);
        }
        setRotation(rotation * 360);
        this.rotationRate = rotationRate * 360;
        if (useAnimo) {
            this.useAnimo = useAnimo;
            animation = new Animo(animazione);
        }
        this.xAcceleration = xAcceleration;
        this.yAcceleration = yAcceleration;
        this.rotRateInc = rotRateInc;
        this.opacity = opacity;
    }

    private float fades(int cycle, int life, int fadeInFrames, int fadeOutFrames) {
        float returnValue = 1;
        if (cycle <= fadeInFrames) {
            returnValue = (float) cycle / fadeInFrames;
        }
        if (cycle >= life - fadeOutFrames) {
            returnValue = (float) (life - cycle) / fadeOutFrames;
        }
        return returnValue;
    }

    @Override
    public void loop() {
        if (!Core.getInstance().isGamePaused()) {
            float speedMul = Core.getInstance().getSpeedMultiplier();
            if (cycle >= life) {
                StaticRef.particleCount--;
                kill();
            }
            if (cycle == 0 && useAnimo) {
                setAnimo(animation);
                animation.start();
            }
            if (cycle != 0) {
                setWidth((int) (getWidth() + incX * speedMul));
                setHeight((int) (getHeight() + incY * speedMul));
                move(-(incX * speedMul) / 2, -(incY * speedMul) / 2);
            }
            setRGBA(r, g, b, fades((int) cycle, life, fadeInFrames, fadeOutFrames) * opacity);
            setRotation(getRotation() + rotationRate * speedMul);
            move((float) speedX * speedMul, (float) speedY * speedMul);
            speedX += xAcceleration * speedMul;
            speedY += yAcceleration * speedMul;
            rotationRate += rotRateInc * speedMul;
            cycle += speedMul;
        }
    }

    @Override
    protected void onDestroy() {
        StaticRef.particleCount--;
    }
}

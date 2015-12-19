/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.objects.particles;

import org.easyway.interfaces.base.ITexture;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.animo.Animo;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;
import org.easyway.utils.Timer.SyncTimer;

/**
 *
 * @author Do$$e
 */
public class Emitter extends Sprite implements ILoopable {

    private float extraParticles = 0f;
    private int particlesPerSecond = 60;
    private Texture particle = Texture.getTexture("org/easyway/objects/particles/defaultParticle.png");
    private Animo animazione = new Animo();
    private float scale = 1f, xOffsetMin = 0f, yOffsetMin = 0f, xOffsetMax = 20f, yOffsetMax = 0f, xSpeedMin = 0f, ySpeedMin = 10, xSpeedMax = 0f, ySpeedMax = 20, RMin = 0f, GMin = 0f, BMin = 0f, RMax = 1f, GMax = 1f, BMax = 1f, initialRotationMin = 0f, initialRotationMax = 1f, rotRateMin = -0.01f, rotRateMax = 0.01f, xAccelerationMin = 0f, xAccelerationMax = 0f, yAccelerationMax = 0f, yAccelerationMin = 0f, rotRateIncMin = 0f, rotRateIncMax = 0f, drawScale = 1f, opacityMin = 1, opacityMax = 1;
    private int lifeMin = 400, lifeMax = 1400, fadeIn = 200, fadeOut = 200, xSizeIncMin = 0, ySizeIncMin = 0, xSizeIncMax = 2, ySizeIncMax = 2, particlesLayer = 5, length = 0;
    private boolean enableChecks = false, useanimazione = false;
    private float movementX = 0, movementY = 0;

    public float getOpacityMax() {
        return opacityMax;
    }

    public void setOpacityMax(float opacityMax) {
        this.opacityMax = opacityMax;
    }

    public float getOpacityMin() {
        return opacityMin;
    }

    public void setOpacityMin(float opacityMin) {
        this.opacityMin = opacityMin;
    }

    public float getDrawScale() {
        return drawScale;
    }

    public void setDrawScale(float drawScale) {
        this.drawScale = drawScale;
    }

    public int getParticlesLayer() {
        return particlesLayer;
    }

    public void setParticlesLayer(int particlesLayer) {
        this.particlesLayer = particlesLayer;
    }

    public float getRotRateIncMax() {
        return rotRateIncMax;
    }

    public void setRotRateIncMax(float rotRateIncMax) {
        this.rotRateIncMax = rotRateIncMax;
    }

    public float getRotRateIncMin() {
        return rotRateIncMin;
    }

    public void setRotRateIncMin(float rotRateIncMin) {
        this.rotRateIncMin = rotRateIncMin;
    }

    public float getxAccelerationMax() {
        return xAccelerationMax;
    }

    public void setxAccelerationMax(float xAccelerationMax) {
        this.xAccelerationMax = xAccelerationMax;
    }

    public float getxAccelerationMin() {
        return xAccelerationMin;
    }

    public void setxAccelerationMin(float xAccelerationMin) {
        this.xAccelerationMin = xAccelerationMin;
    }

    public float getyAccelerationMax() {
        return yAccelerationMax;
    }

    public void setyAccelerationMax(float yAccelerationMax) {
        this.yAccelerationMax = yAccelerationMax;
    }

    public float getyAccelerationMin() {
        return yAccelerationMin;
    }

    public void setyAccelerationMin(float yAccelerationMin) {
        this.yAccelerationMin = yAccelerationMin;
    }

    public boolean isUseanimazione() {
        return useanimazione;
    }

    public void setUseanimazione(boolean useanimazione) {
        this.useanimazione = useanimazione;
    }

    public Animo getAnimazione() {
        return animazione;
    }

    public void setAnimazione(Animo animazione) {
        this.animazione = animazione;
    }

    public boolean isEnableChecks() {
        return enableChecks;
    }

    public void setEnableChecks(boolean enableChecks) {
        this.enableChecks = enableChecks;
    }

    public float getxSpeedMax() {
        return xSpeedMax;
    }

    public void setxSpeedMax(float xSpeedMax) {
        this.xSpeedMax = xSpeedMax;
    }

    public float getxSpeedMin() {
        return xSpeedMin;
    }

    public void setxSpeedMin(float xSpeedMin) {
        this.xSpeedMin = xSpeedMin;
    }

    public float getySpeedMax() {
        return ySpeedMax;
    }

    public void setySpeedMax(float ySpeedMax) {
        this.ySpeedMax = ySpeedMax;
    }

    public float getySpeedMin() {
        return ySpeedMin;
    }

    public void setySpeedMin(float ySpeedMin) {
        this.ySpeedMin = ySpeedMin;
    }

    public float getBMax() {
        return BMax;
    }

    public void setBMax(float BMax) {
        this.BMax = BMax;
    }

    public float getBMin() {
        return BMin;
    }

    public void setBMin(float BMin) {
        this.BMin = BMin;
    }

    public float getGMax() {
        return GMax;
    }

    public void setGMax(float GMax) {
        this.GMax = GMax;
    }

    public float getGMin() {
        return GMin;
    }

    public void setGMin(float GMin) {
        this.GMin = GMin;
    }

    public float getRMax() {
        return RMax;
    }

    public void setRMax(float RMax) {
        this.RMax = RMax;
    }

    public float getRMin() {
        return RMin;
    }

    public void setRMin(float RMin) {
        this.RMin = RMin;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public void setFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    public void setFadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
    }

    public float getInitialRotationMax() {
        return initialRotationMax;
    }

    public void setInitialRotationMax(float initalRotationMax) {
        this.initialRotationMax = initalRotationMax;
    }

    public float getInitialRotationMin() {
        return initialRotationMin;
    }

    public void setInitialRotationMin(float initialRotationMin) {
        this.initialRotationMin = initialRotationMin;
    }

    public int getLifeMax() {
        return lifeMax;
    }

    public void setLifeMax(int lifeMax) {
        this.lifeMax = lifeMax;
    }

    public int getLifeMin() {
        return lifeMin;
    }

    public void setLifeMin(int lifeMin) {
        this.lifeMin = lifeMin;
    }

    public Texture getParticle() {
        return particle;
    }

    public void setParticle(Texture particle) {
        this.particle = particle;
    }

    public int getParticlesPerSecond() {
        return particlesPerSecond;
    }

    public void setParticlesPerSecond(int particlesPerSecond) {
        this.particlesPerSecond = particlesPerSecond;
    }

    public float getRotRateMin() {
        return rotRateMin;
    }

    public void setRotRateMin(float rotRateMin) {
        this.rotRateMin = rotRateMin;
    }

    public float getRotRateMax() {
        return rotRateMax;
    }

    public void setRotRateMax(float rotRateMax) {
        this.rotRateMax = rotRateMax;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getxOffsetMax() {
        return xOffsetMax;
    }

    public void setxOffsetMax(float xOffsetMax) {
        this.xOffsetMax = xOffsetMax;
    }

    public float getxOffsetMin() {
        return xOffsetMin;
    }

    public void setxOffsetMin(float xOffsetMin) {
        this.xOffsetMin = xOffsetMin;
    }

    public int getxSizeIncMax() {
        return xSizeIncMax;
    }

    public void setxSizeIncMax(int xSizeIncMax) {
        this.xSizeIncMax = xSizeIncMax;
    }

    public int getxSizeIncMin() {
        return xSizeIncMin;
    }

    public void setxSizeIncMin(int xSizeIncMin) {
        this.xSizeIncMin = xSizeIncMin;
    }

    public float getyOffsetMax() {
        return yOffsetMax;
    }

    public void setyOffsetMax(float yOffsetMax) {
        this.yOffsetMax = yOffsetMax;
    }

    public float getyOffsetMin() {
        return yOffsetMin;
    }

    public void setyOffsetMin(float yOffsetMin) {
        this.yOffsetMin = yOffsetMin;
    }

    public int getySizeIncMax() {
        return ySizeIncMax;
    }

    public void setySizeIncMax(int ySizeIncMax) {
        this.ySizeIncMax = ySizeIncMax;
    }

    public int getySizeIncMin() {
        return ySizeIncMin;
    }

    public void setySizeIncMin(int ySizeIncMin) {
        this.ySizeIncMin = ySizeIncMin;
    }

    public float getMovementX() {
        return movementX;
    }

    public void setMovementX(float movementX) {
        this.movementX = movementX;
    }

    public float getMovementY() {
        return movementY;
    }

    public void setMovementY(float movementY) {
        this.movementY = movementY;
    }

    public Emitter() {
        super(0f, 0f, (ITexture)null);
    }

    public Emitter(float x, float y) {
        super(x, y, (ITexture)null);
    }

    public Emitter(float x, float y, int length) {
        super(x, y, (ITexture)null);
        this.length = length;
        new LifeTimer().start();
    }

    private class LifeTimer extends SyncTimer {

        public LifeTimer() {
            super(length);
        }

        @Override
        public void onTick() {
            Emitter.this.destroy();
            destroy();
        }
    }

    private class Particle extends Sprite implements ILoopable {

        float speedX, speedY, incY, incX, scale, r, g, b, rotationRate, xAcceleration, yAcceleration, rotRateInc, opacity;
        boolean useAnimo;
        int fadeInFrames, fadeOutFrames, life;
        float cycle = 0;
        Animo animation;

        public Particle(ITexture img, Animo animazione, boolean useAnimo, float x, float y, float speedX, float speedY, int lifeMs, float incX, float incY, int fadeInFramesMs, int fadeOutFramesMs, float scale, float R, float G, float B, float opacity, int layer, float rotation, float rotationRate, float xAcceleration, float yAcceleration, float rotRateInc, float drawScale) {
            super(x, y, img);
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
            setLayer(layer);
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
                move(speedX * speedMul, speedY * speedMul);
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

    public void makeParticle() {
        float xOffset = (float) (Math.random() * (xOffsetMax - xOffsetMin)) + xOffsetMin;
        float yOffset = (float) (Math.random() * (yOffsetMax - yOffsetMin)) + yOffsetMin;
        float xSpeed = (float) (Math.random() * (xSpeedMax - xSpeedMin)) + xSpeedMin;
        float ySpeed = (float) (Math.random() * (ySpeedMax - ySpeedMin)) + ySpeedMin;
        int life = (int) (Math.random() * (lifeMax - lifeMin)) + lifeMin;
        int xSizeInc = (int) (Math.random() * (xSizeIncMax - xSizeIncMin)) + xSizeIncMin;
        int ySizeInc = (int) (Math.random() * (ySizeIncMax - ySizeIncMin)) + ySizeIncMin;
        float R = (float) (Math.random() * (RMax - RMin)) + RMin;
        float G = (float) (Math.random() * (GMax - GMin)) + GMin;
        float B = (float) (Math.random() * (BMax - BMin)) + BMin;
        float initialRotation = (float) (Math.random() * (initialRotationMax - initialRotationMin)) + initialRotationMin;
        float rotRate = (float) (Math.random() * (rotRateMax - rotRateMin)) + rotRateMin;
        float xAcceleration = (float) (Math.random() * (xAccelerationMax - xAccelerationMin)) + xAccelerationMin;
        float yAcceleration = (float) (Math.random() * (yAccelerationMax - yAccelerationMin)) + yAccelerationMin;
        float rotRateInc = (float) (Math.random() * (rotRateIncMax - rotRateIncMin)) + rotRateIncMin;
        float opacity = (float) (Math.random() * (opacityMax - opacityMin)) + opacityMin;
        new Particle(particle, animazione, useanimazione, getX() + (xOffset * scale), getY() + (yOffset * scale), xSpeed * scale, ySpeed * scale, life, xSizeInc * scale, ySizeInc * scale, fadeIn, fadeOut, scale, R, G, B, opacity, particlesLayer, initialRotation, rotRate, xAcceleration * scale, yAcceleration * scale, rotRateInc, drawScale);
    }

    public void checkValues() {
        if (xOffsetMax < xOffsetMin) {
            throw new UnsupportedOperationException("Error: xOffsetMax should not be greater than xOffsetMin, you fuckin' idiot!");
        }
        if (yOffsetMax < yOffsetMin) {
            throw new UnsupportedOperationException("Error: yOffsetMax should not be greater than yOffsetMin, you fuckin' idiot!");
        }
        if (xSpeedMax < xSpeedMin) {
            throw new UnsupportedOperationException("Error: xSpeedMax should not be greater than xSpeedMin, you fuckin' idiot!");
        }
        if (ySpeedMax < ySpeedMin) {
            throw new UnsupportedOperationException("Error: ySpeedMax should not be greater than ySpeedMin, you fuckin' idiot!");
        }
        if (lifeMax < lifeMin) {
            throw new UnsupportedOperationException("Error: lifeMax should not be greater than lifeMin, you fuckin' idiot!");
        }
        if (xSizeIncMax < xSizeIncMin) {
            throw new UnsupportedOperationException("Error: xSizeIncMax should not be greater than xSizeIncMin, you fuckin' idiot!");
        }
        if (ySizeIncMax < ySizeIncMin) {
            throw new UnsupportedOperationException("Error: ySizeIncMax should not be greater than ySizeIncMin, you fuckin' idiot!");
        }
        if (RMax < RMin) {
            throw new UnsupportedOperationException("Error: RMax should not be greater than RMin, you fuckin' idiot!");
        }
        if (GMax < GMin) {
            throw new UnsupportedOperationException("Error: GMax should not be greater than GMin, you fuckin' idiot!");
        }
        if (BMax < BMin) {
            throw new UnsupportedOperationException("Error: BMax should not be greater than BMin, you fuckin' idiot!");
        }
        if (initialRotationMax < initialRotationMin) {
            throw new UnsupportedOperationException("Error: initialRotationMax should not be greater than initialRotationMin, you fuckin' idiot!");
        }
        if (rotRateMax < rotRateMin) {
            throw new UnsupportedOperationException("Error: rotRateMax should not be greater than rotRateMin, you fuckin' idiot!");
        }
    }

    @Override
    public void loop() {
        if (!Core.getInstance().isGamePaused()) {
            if (enableChecks) {
                checkValues();
            }
            move(movementX, movementY);
            float speedMul = Core.getInstance().getSpeedMultiplier();
            float particlesPerFrame = (float) (particlesPerSecond * speedMul) / 60;
            float i;
            if (particlesPerFrame >= 1f) {
                for (i = 0; i < particlesPerFrame; i++) {
                    makeParticle();
                }
            } else {
                i = 0;
            }
            extraParticles += Math.abs(i - particlesPerFrame);
            if (extraParticles >= 1f) {
                makeParticle();
                extraParticles = 0f;
            }
        }
    }
}

package org.easyway.gui.base;

import java.util.ArrayList;
import org.easyway.system.state.OpenGLState;
import org.lwjgl.opengl.GL11;

public class ButtonEffect extends GuiContainer {
	
	private static final long serialVersionUID = 1L;

	Button button;
	
	float alpha = 1.0f;
	
	float xEffect, yEffect;
	
	public ButtonEffect( Button button ) {
		setFather(null);
		gainFocus();
		this.button = button;
		setSize(button.getWidth(), button.getHeight());
		setXY(button.xOnScreen, button.yOnScreen);
	}
	
    @Override
	public void setSize( int width, int height ) {
		this.width = width;
		this.height = height;
	}
	
	public void customDraw() {
		alpha-=0.04f;
		if (alpha<=0) {
			destroy();
			return;
		}
		OpenGLState.enableBlending();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, alpha);
		drawComponent(xOnScreen+xEffect, yOnScreen+yEffect, getWidth(), getHeight(), button.fullTexture);
		setSize(getWidth()+2, getHeight()+2);
		--xEffect;
		--yEffect;
	}
	
	public void onDestroy() {
	}

}

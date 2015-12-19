/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.ueditor2.commands;

import java.util.ArrayList;
import org.easyway.debug.Selected;
import org.easyway.ueditor2.EditorCore;
import org.easyway.gui.base.Label;
import org.easyway.gui.base.Button;
import org.easyway.gui.base.Panel;
import org.easyway.gui.base.Window;
import org.easyway.interfaces.sprites.IPlain2D;
import org.easyway.objects.text.TextAlign;
import org.easyway.system.StaticRef;
import org.easyway.system.state.OpenGLState;
import org.lwjgl.opengl.GL11;

public class SelectInstance extends Panel {

    Window win;
    Label label;
    Button prevButton;
    Button nextButton;
    ArrayList<IPlain2D> list;
    IPlain2D selected;
    int index;

    public SelectInstance(ArrayList<IPlain2D> list, IPlain2D selected) {
        super(0, 0, StaticRef.getCamera().getWidth(), StaticRef.getCamera().getHeight(), StaticRef.getCamera().getMainFather());
        this.list = list;
        this.selected = selected;
        index = list.indexOf(selected);
        System.out.println("start index: " + index);
        gainFocus();
        win = new Window(this);
        win.setTitle("EasyWay Message");
        win.setTextAlign(TextAlign.MIDDLE);
        win.setSize(width, height);

        int left = win.getLeftBorderWidth();
        int top = win.getCaptionHeight();

        // TODO: the following code can be optimized

        label = new Label(left + 10, top + 10, "select an instance..", win);
        win.setWidth(Math.max(label.getWidth() + 20 + left * 2, 200));
        prevButton = new Button(10 + (int) ((float) win.getWidth() / 4) + left,
                label.getHeight() + 20 + top, "Prev", win) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onAction() {
                prevEffect();
            }
        };
        nextButton = new Button(10 + (int) ((float) win.getWidth() / 4 * 3) + left, label.getHeight() + 20 + top, "Next", win) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onAction() {
                nextEffect();
            }
        };

        Button okButton = new Button(10 + (int) ((float) win.getWidth() / 2) + left, label.getHeight() + 20 + top + nextButton.getHeight(), "OK", win) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onAction() {
                okEffect();
            }
        };
        okButton.setX(okButton.getX() - okButton.getWidth() / 2);
        prevButton.setX(prevButton.getX() - prevButton.getWidth() / 2);
        nextButton.setX(nextButton.getX() - nextButton.getWidth() / 2);

        win.setHeight(label.getHeight() + 30 + nextButton.getHeight() + top + okButton.getHeight());

        // win.setXY((width - win.getWidth()) / 2, (height - win.getHeight()) /
        // 2);
        win.center();
        EditorCore.disableInteraction();
    }

    protected void finish() {
        EditorCore.enableInteraction();
        destroy();
    }

    protected void prevEffect() {
        index--;
        if (index < 0) {
            index = list.size() - 1;
        }
        selected = list.get(index);
        new Selected(selected);
    }

    protected void nextEffect() {
        index++;
        if (index >= list.size()) {
            index = 0;
        }
        selected = list.get(index);
        new Selected(selected);
    }

    protected void okEffect() {
        EditorCore.getInstance().setSelected(selected);
        finish();
    }

    public void customDraw() {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGLState.enableBlending();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glColor4f(0.1f, 0, 0.1f, 0.2f);
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(width - 1, 0);
        GL11.glVertex2f(width - 1, height - 1);
        GL11.glVertex2f(0, height - 1);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}

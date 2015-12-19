package org.easyway.gui.base;

import org.easyway.objects.text.Text;

public class Label extends GuiContainer {

    private static final String blackColor = Text.createColor(0, 0, 0);
    private static final long serialVersionUID = 15415651L;
    Text text;

    public Label(float x, float y, String text, GuiContainer container) {
        super(x, y);

        setFather(container);

        boolean oldValue = Text.autoAddToLists;
        Text.autoAddToLists = false;
        this.text = new Text(0, 0, blackColor + text, null);
        Text.autoAddToLists = oldValue;

        setSize(this.text.getWidth(), this.text.getHeight());
    }

    @Override
    public void customDraw() {
        text.setXY(xOnScreen, yOnScreen);
        text.render();
    }

    public void setText(String text) {
        this.text.setText(blackColor + text);
        setSize(this.text.getWidth(), this.text.getHeight());
    }

    public String getText() {
        return text.getText();
    }

    public int getWidth() {
        return text.getWidth();
    }

    public int getHeight() {
        return text.getHeight();
    }
}

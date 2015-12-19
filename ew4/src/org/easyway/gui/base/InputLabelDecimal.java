package org.easyway.gui.base;

public class InputLabelDecimal extends InputLabel {

	public InputLabelDecimal(int x, int y, int width, int height,
			GuiContainer container) {
		super(x, y, width, height, container);
	}

	public String checkKey(String key) {
		if (key==null || key.length()<1)
			return "";
		char c = key.charAt(0);
		if (c <= '9' && c >= '0')
			return key;
		return "";
	}

    @Override
    protected void append(String text) {
        super.append(checkKey(text));
    }



}

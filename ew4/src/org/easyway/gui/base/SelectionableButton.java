package org.easyway.gui.base;

public class SelectionableButton extends Button {

	private static final long serialVersionUID = 34578937498573L;

	boolean selected = false;

	public SelectionableButton(int x, int y, String text, GuiContainer container) {
		super(x, y, text, container);
	}

	public void onFeedBack() {
		super.onFeedBack();
		if (selected = !selected) { // selected == true
			red = green = 1.0f;
			blue = 0.0f;
		} else { // selected == false
			red = green = blue = 1.0f;
		}

	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean value) {
		selected = !value;
		onFeedBack();
	}

	public void onExit() {
		if (selected) {
			red = green = 1.0f;
			blue = 0.0f;
		} else {
			super.onExit();
		}
	}

	@Override
	public void onAction() {

	}
}

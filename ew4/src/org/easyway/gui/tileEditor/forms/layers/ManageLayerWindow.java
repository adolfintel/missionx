package org.easyway.gui.tileEditor.forms.layers;

import org.easyway.gui.base.GuiContainer;
import org.easyway.gui.base.Button;
import org.easyway.gui.base.Window;

public class ManageLayerWindow extends Window {

	Button addButton;

	Button removeButton;

	Button moveUpButton;

	Button moveDownButton;

	Button editButton;

	public ManageLayerWindow(GuiContainer container) {
		super(container);
		setSize(415, 310);
		center();

		int y = 5 + getCaptionHeight();

		addButton = new Button(210, y, "Add", this) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onAction() {
			}

		};
		addButton.setWidth(200);

		removeButton = new Button(210, y += addButton.getHeight(), "Remove",
				this) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onAction() {

			}
		};
		removeButton.setWidth(200);

		editButton = new Button(210, y += removeButton.getHeight(), "Edit",
				this) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onAction() {

			}
		};
		editButton.setWidth(200);

		moveUpButton = new Button(210, y += editButton.getHeight(), "Move Up",
				this) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onAction() {

			}
		};
		moveUpButton.setWidth(200);

		moveDownButton = new Button(210, y += moveUpButton.getHeight(),
				"Move Down", this) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onAction() {

			}
		};
		moveDownButton.setWidth(200);
	}
}

package org.easyway.gui.tileEditor;

import org.easyway.gui.base.ScrollPanel;
import org.easyway.system.Core;

public class BodyEditor extends ScrollPanel {

	private static final long serialVersionUID = 1L;

	public BodyEditor(int x, int y) {
		super(x, y, Core.getInstance().getWidth() - x, Core.getInstance().getHeight()
				- y, null);
	}

}

package org.easyway.input;

import org.easyway.interfaces.sprites.IClickable;
import org.easyway.objects.BaseObject;

public class MouseAssociation extends BaseObject {

	private static final long serialVersionUID = 2337316173004188687L;

	public boolean over;

	public boolean down;

	public IClickable object;
	
	protected MouseAssociation() {
		super(false);
		object = (IClickable)this;
	}

	public MouseAssociation(IClickable obj) {
		super(false);
		object = obj;
		type = "$_MouseAssociation";
	}

	public boolean equals(Object obj) {
		return obj == object;// object.hashCode() == obj.hashCode();
	}

	public int hashCode() {
		return object.hashCode();
	}
}

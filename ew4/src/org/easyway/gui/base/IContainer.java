package org.easyway.gui.base;

import org.easyway.input.MouseAssociation;
import org.easyway.interfaces.sprites.IClickable;
import org.easyway.interfaces.sprites.IPlain2D;
import org.easyway.lists.GameList;

/**
 * 
 * @author remalkoil
 *
 */
public interface IContainer extends IPlain2D, IClickable {
	public void draw(float x, float y);
	
	public IContainer getFather();
	
	public GameList<IContainer> getChildren();
	
	/**
	 * checks if the the container is in a correct position<br>
	 * 
	 */
	public boolean checkBounds();
	
	public MouseAssociation getMouseAssociation();
	
}

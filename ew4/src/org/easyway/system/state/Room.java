/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.system.state;

import org.easyway.interfaces.extended.IRoom;
import org.easyway.system.Core;

/**
 *
 * @author Daniele Paggi
 */
public abstract class Room implements IRoom {

    private GameState gameState;

    @Override
    public abstract void creation();

    @Override
    public abstract void loop();

    public boolean isSavedState() {
        return gameState != null;
    }

    public void restoreState() {
        if (isSavedState()) {
            gameState.setThisState();
        } else {
            gameState = new GameState();
            gameState.setThisState();
            creation();
        }
    }

    public void discardSavedState() {
        gameState = null;
    }
    
     /**
     * changes the current game's room<br/>
      * caution: Look the code: this method recall the 'setRoom' of the Core class!<br/>
      * It's placed in this class only for simplify the programmer's life..
     * @param level the new room..
     */
    public void setRoom(Room level) {
        Core.getInstance().setRoom(level);
    }
}
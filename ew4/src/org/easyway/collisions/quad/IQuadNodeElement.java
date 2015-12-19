/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easyway.collisions.quad;

import java.util.ArrayList;

/**
 *
 * @author RemalKoil
 */
public interface IQuadNodeElement<E extends QuadEntry> {
    public ArrayList<E> getQuadEntries();
    public ArrayList<QuadNode> getUsedInQuadNodes();
    public boolean isQuadTreeUsable();
    
}

/* EasyWay Game Engine
 * Copyright (C) 2006 Daniele Paggi.
 *
 * Written by: 2006 Daniele Paggi<dshnt@hotmail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.easyway.input;

import java.util.ArrayList;
import org.easyway.interfaces.sprites.IClickable;
import org.easyway.lists.BaseList;
import org.easyway.lists.Entry;
import org.easyway.lists.GameEntry;
import org.easyway.lists.GameList;

/**
 *
 * @author Daniele Paggi
 */
class EWMouseList extends GameList<MouseAssociation> {

    private static final long serialVersionUID = 7209196555027509593L;

    public EWMouseList() {
        super(true);
        type = "$_MOUSELIST";
    }

    @Override
    protected GameEntry<MouseAssociation> createEntry(GameEntry<MouseAssociation> next, GameEntry<MouseAssociation> prev, MouseAssociation value, BaseList list) {
        return new MouseEntry(next, prev, value != null ? value.object : null, value, list);
    }

    @Override
    public void remove(MouseAssociation obj) {
        ArrayList<Entry> list = obj.getEntries();
        for (Entry entry : list) {
            if (entry.list == this) {
                obj.getEntries().remove(entry); // remove the entry from the Object
                entry.remove(); // remove the entry from the MouseAssociation\List
                break;
            }
        }
    }

    /** remove the sprite from the list */
    @Override
    public void remove(int index) {
        MouseAssociation ass = get(index);
        Entry entry = getEntry(ass);
        ass.object.getEntries().remove(entry);
        entry.remove();
    }

    /** remove the sprite from the list */
    public void remove(IClickable obj) {
        remove((MouseAssociation) obj);
    }

    public MouseAssociation getAssociation(IClickable obj) {
        MouseAssociation ass;
        for (int i = size() - 1; i >= 0; --i) {
            if ((ass = get(i)).equals(obj)) {
                return ass;
            }
        }
        // not found
        return null;
    }
}

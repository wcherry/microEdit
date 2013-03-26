/*
* Copyright © 2013 William R. Cherry
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package org.sss.micro.core

/**
 * Stores information about an action. An action is a packagable unit of work that can be invoked by a menu item,
 *  shortcut key, or toolbar (etc...)
 * @author  wcherry
 * @since   1.0
 * @version 1.0
 */
class Action {
    String defaultKeyStroke
    String name
    String toolTip
    String description
    String execute
    int order

    Closure action
}
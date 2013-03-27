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
package org.sss.micro.swt;

import java.io.File;

import org.eclipse.jface.viewers.*;

class FileTreeContentProvider implements ITreeContentProvider {
  public Object[] getChildren(Object arg0) {
    // Return the files and subdirectories in this directory
    return ((File) arg0).listFiles();
  }

  public Object getParent(Object arg0) {
    // Return this file's parent file
    return ((File) arg0).getParentFile();
  }

  public boolean hasChildren(Object arg0) {
    Object[] obj = getChildren(arg0);

    return obj == null ? false : obj.length > 0;
  }

  public Object[] getElements(Object dir) {
    // These are the root elements of the tree
    // We don't care what arg0 is, because we just want all
    // the root nodes in the file system
    return ((File)dir).listFiles();
  }

  public void dispose() {
    // Nothing to dispose
  }

  public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
    // Nothing to change
  }
}


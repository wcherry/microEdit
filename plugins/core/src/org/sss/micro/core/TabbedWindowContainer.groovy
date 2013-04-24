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

import java.util.*

import org.sss.micro.swt.*

import org.eclipse.swt.*
import org.eclipse.swt.events.*
import org.eclipse.swt.graphics.*
import org.eclipse.swt.layout.*
import org.eclipse.swt.widgets.*
import org.eclipse.swt.custom.*
import org.eclipse.swt.custom.CTabFolder2Listener
import org.eclipse.swt.events.SelectionListener

class TabbedWindowContainer implements WindowContainer {
  CTabFolder tabFolder = null // Native

  ArrayList<Window> windows = new ArrayList<Window>()
  AppWindow appWindow = null
  PluginContext context = null

  public TabbedWindowContainer(){}

  public TabbedWindowContainer(AppWindow appWindow){
    tabFolder = new CTabFolder (appWindow.getNativeShell(), SWT.CLOSE)
    tabFolder.setSimple(false)
	tabFolder.addCTabFolder2Listener([
			close: {evnt->
				def win = evnt.item.getData('window')
				if(!promptSaveDirtyWindow(win)) evnt.doit = false	// flag as not closeable
			}
		] as CTabFolder2Listener)

	tabFolder.addSelectionListener([
			widgetDefaultSelected :{evnt-> context.fireEvent("windowSelected", ['window': evnt.item.getData('window')])},
			widgetSelected : {evnt->context.fireEvent("windowSelected", ['window': evnt.item.getData('window')]) }
		] as SelectionListener) 

    this.appWindow = appWindow
    context = appWindow.context
    context.registerEvent('windowAdd')
    context.registerEvent('windowRemove')
    context.registerEvent('windowSelected')
  }

  public Window getFocusedWindow(){
    return tabFolder.selection?.getData('window')
  }
  
  public void setFocusedWindow(int index){
    tabFolder.setSelection(index)
  }

	boolean promptSaveDirtyWindow(Window win){
		boolean closeable = false
		if(win.modified) {
			int state = DialogHelper.alert(context, context.getResourceString("unsavedDocument.text", win.title), context.getResourceString("unsavedDocument.title"))
			if(state == SWT.YES) win.document.save()
			if(state == SWT.YES | state == SWT.NO){
				context.fireEvent("windowRemove", ['window': win])
				closeable = true
			}
		} else closeable = true
		return closeable
	}

	boolean closeWindow(Window win){
		if(promptSaveDirtyWindow(win)){
			removeWindow(win)
		}
	}

  public List getWindows(){
    return tabFolder.items.collect{it.getData('window')}
  }
  
  def getNativeControl(){
    tabFolder
  }

	void removeWindow(Window win){
		windows.remove(win)
		win.tabItem.dispose()
	}
  
  public void addWindow(Window window){
//    assert(!windows.contains(window))
    windows.add(window)
    window.keyMap = context.keyMap
    
    context.fireEvent('windowAdd', [window: window, container: this])
    window.show(this, context)
  
    CTabItem item = new CTabItem (tabFolder, SWT.NULL)
    if(window.title) item.setText(window.title)
    item.setControl(window.getNativeControl())
	item.setData('window', window)
	window.tabItem = item
//TODO: Fix later    window.setTitleChangeEventListener{oldName, newName->item.setText(newName)}
    tabFolder.setSelection(item)
  }    
}
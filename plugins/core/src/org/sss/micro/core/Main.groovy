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

import org.sss.micro.swt.Helper
import org.sss.micro.swt.DialogHelper

import org.eclipse.swt.widgets.Event
import org.eclipse.swt.widgets.Listener
import org.eclipse.swt.SWT

class Main {
    // Extension Points
    def appWindow = null
    def context = null

    def keyMap = [:]
    def menu = [:]

    def load(PluginContext context) {
        println "Loading Core plugin"
        appWindow = new AppWindow(context.getResourceString("application.title"), context)
        context.addBean('appWindow', appWindow)
		context.usersPropertiesDirectory = new File("${System.getProperty('user.home', '.')}\\.micro\\")
        this.context = context
        context.addEventListener('newPluginsFound') {data ->
            println "New Plugins"
            data.plugins.each {plugin ->
                println "Processing ${plugin.name}"
                plugin.keyMap.each {shortcut, action ->
                    def ke = Helper.convertKeyStringToEvent(shortcut)
                    keyMap[ke] = action
                }
                plugin.menu.each {topMenu, items ->
                    def menuActions = menu[topMenu]
                    if (!menuActions) {
                        menu[topMenu] = []
                        menuActions = menu[topMenu]
                    }
                    items.each {actionName, order ->
                        Action a = context.actions[actionName]
                        if(a){
                            a.order = order
                            menuActions << a
                        } else {
                            println "Action $actionName not found is plugin ${plugin.name}"
                        }
                    }
                }
            }
            menu.each {name, actions -> appWindow.createMenu(name, actions)}
            keyMap.each{k,v->context.keyMap[k] = v}
        }
    }

    def start(PluginContext context) {
        println "Starting Core plugin"
		context.registerEvent('applicationShuttingdown')
		context.shell.addListener(SWT.Close, new Listener() {
      		public void handleEvent(Event event) {
				context.fireEvent('applicationShuttingdown', [:])
				boolean flag= closeAllDocuments()
				println "Flag: $flag"
        		event.doit = flag;
      		}
    	});


        // merge the cached menus and keymaps with the new ones

        //println context.menu.values()
        //context.menu.values().each{
        //  context.actions[it[0]]
        // appWindow.createMenu(it[0], it[2], context)
        //}
    }

    def startMain(PluginContext context) {
        appWindow.start()
    }

    def stop(PluginContext context) {}

    def unload(PluginContext context) {}

    def newDocument() {
        appWindow.addWindow(new SourceWindow())
    }

	def closeDocument(){
		println "Closing ${appWindow.focusedWindow.title}"
		appWindow.closeWindow(appWindow.focusedWindow)
	}

	def closeAllDocuments(){
		appWindow.windowContainer.windows.find{appWindow.closeWindow(it)} == null
	}

    def displaySysProps() {
        context.beans.each {k, v -> println "Bean $k = $v"}
    }

    def quitApp() {
        //TODO: Add more stuff to do when exiting.
		context.fireEvent('applicationShuttingdown', [:])
		closeAllDocuments()
        context.shell.dispose()
    }

    def cutToClipboard() {
        appWindow.focusedWindow.cut()
    }

    def copyToClipboard() {
        appWindow.focusedWindow.copy()
    }

    def pasteFromClipboard() {
        appWindow.focusedWindow.paste()
    }

    def selectAll() {
        appWindow.focusedWindow.selectAll()
    }

    def undo() {
        appWindow.focusedWindow.undo()
    }

    def redo() {
        appWindow.focusedWindow.redo()
    }

    def search() {
        appWindow.focusedWindow.searchMgr.search()
    }

    def changeFont(){
        appWindow.changeFont()
    }

    public void loadKeyMap() {
        def writer = new FileWriter('keymap.properties')
        keyMap.each {k, v ->
            writer.println "$k=$v"
        }
        writer.close()
    }

    public void storeKeyMap() {
        def reader = new FileWriter('keymap.properties')
        reader.eachLine {line ->
            def (key, value) = line.split('=')
            keyMap[key] = value
        }
    }
}
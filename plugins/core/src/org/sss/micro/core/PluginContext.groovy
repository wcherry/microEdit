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

class PluginContext {
    def pluginDir
    def plugins
    def beans = [:]
    def properties = [:]
    def actions = [:]
    def menu = [:]
    Map keyMap = [:]
    private List keyList = []
    Map events = [:]


    void registerEvent(String name) {
        events[name] = []
    }

    void addEventListener(eventName, listener) {
        def listeners = events[eventName]
        if (listeners != null) {
            listeners << listener
        } else throw new Exception("Unknown event $eventName")
    }

    void fireEvent(eventName, data = [:]) {
        def listeners = events[eventName]
        data.context = this
        if (listeners != null) {
            println "Firing event...$eventName(${listeners.size()})"
            listeners.each {listener -> listener.call(data)}
        } else println "Unknown event $eventName"
    }

    void callAction(String actionName) {
        def action = actions[actionName]
        if (action && action.action) {
            action.action.call()
        } else println "Unable to find action for $actionName"
    }

    /*Map getKeyMap(){
    if(!keyList.isEmpty()) {
        keyList.each{data->
          def keyString = data[0]
            def action = data[1]
          KeyStroke ks = Helper.convertKeyStringToEvent(keyString)
            _keyMap[ks] = action
        }
        keyList = [] //TODO: Synchronization Issue
    }
      _keyMap
  }
    */

    // Install method - no libraries (including SWT) are loaded yet!!!
    void addToKeyMap(map) {
        map.each {keyString, action ->
            keyList << [keyString, action]
        }
    }

    // Install method - no libraries (including SWT) are loaded yet!!!
    void addMenu(name, order, items) {
        if (menu[name]) {
            def oldItems = menu[name][2]
            oldItems << items
            menu[name][2] = oldItems.flatten()
        } else {
            menu[name] = [name, order, items]
        }
    }

    def registerAction(String name, PluginInfo info, Map args) {
        def action = new Action(args)
        action.action = {try{info.pluginInstance."${args['execute']}"()}catch(Exception ex){ex.printStackTrace()}} //TODO: Process error to user

        actions[name] = action //[info, args]
        println "Registering action $name ${action.name}"
    }

    def addTransferType(DataTransfer type) {
        appWindow.dnd.addTransferType(type)
    }

    def addBean(name, obj) {
        beans[name] = obj
    }

    def propertyMissing(name) {
        def value = beans[name]
        if (!value) value = properties[name]
        //if(!value) throw new Exception("Could not find a match for property $name")
        return value
    }

    def propertyMissing(name, value) {
        beans[name] = value
    }

    void setProperty(name, value) {
        properties[name] = value
    }
}
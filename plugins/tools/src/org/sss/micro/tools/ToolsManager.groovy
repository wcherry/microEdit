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
package org.sss.micro.tools

import org.sss.micro.core.*
import org.sss.micro.swt.DialogHelper

class ToolsManager {
    PluginContext context 	= null
    File macrosDir 					=	null
    List<Action> actions 		= []

    def load(PluginContext context) {
        println "Loading Tools plugin"
        this.context = context
    }

    def start(PluginContext context) {
        println "Starting Tools plugin"
		macrosDir = new File(context.usersPropertiesDirectory, "macros")
        if(!macrosDir.exists()) macrosDir.mkdirs();
		refreshMacros()
    }

    def stop(PluginContext context) {}

    def unload(PluginContext context) {}

    String execute(String text){
        Window win = context.appWindow.getFocusedWindow()

        StringWriter writer = new StringWriter()
        PrintWriter printer = new PrintWriter(writer)
        try {
            GroovyShell shell = new GroovyShell()
            shell.setProperty('app', context)
            shell.setProperty('out', printer)
            shell.setProperty('win', win)
            def value = shell.evaluate(text)
            printer.println()
            printer.println value
        } catch (Exception e) {
            e.printStackTrace(printer)
        }
        writer.toString()
    }

    def executeTab() {
        Window win = context.appWindow.getFocusedWindow()
        String text = win.getBody()

        String s = execute(text)

        SourceWindow pw = new SourceWindow()
        pw.setBody(s)
        context.appWindow.addWindow(pw)
    }

    def createMacro() {
        String result = DialogHelper.showInputDialog(context, "New Macro Name", "Enter name for Macro")
        if (result) {
            File file = new File(macrosDir, result + ".macro")
            file.write(
'''// Enter your macro text here:
//   app represent the application context
//   out is the standard output, piped to a new window if required
//   win is the window that had focus when the macro is played back
 '''
            )
            context.Filesystem.loadFile(file.getAbsolutePath())

            refreshMacros()
        }

    }

    def runMacro(String name) {
        println "Running macro $name"
        String text = new File(macrosDir, "${name}.macro").text

        def s = execute(text)
        println s
    }

    def refreshMacros() {
        int index = 50
        macrosDir.eachFile {file ->
            String name = file.name - '.macro'
            Action action = new Action()
            action.name = name
            action.action = {
                println "Action $name"
                runMacro(name)
            }
            action.order = index++
            actions << action
        }
        if(actions) context.appWindow.createMenu('&Macros', actions)
    }

    def pluginInfo() {}

}
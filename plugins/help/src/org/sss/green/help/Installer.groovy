package org.sss.micro.help

import org.sss.micro.core.*

class Installer {
	def install(String pluginDir, PluginContext context){
    println "Installing Help plugin..."
		// langauge (all means all), default keybinding, action

		context.addMenu('&Help',1,['showHelp'])

    println "Completed installing Help plugin!"
	}
	def uninstall(String pluginDir, PluginContext context){}
}
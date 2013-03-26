package org.sss.micro.tools

import org.sss.micro.core.*

class Installer {
	def install(String pluginDir, PluginContext context){
    println "Installing Tools plugin..."
		// langauge (all means all), default keybinding, action

		context.addMenu('&Tools',1,['executeTab', 'displayPluginInfo'])

    println "Completed installing Tools plugin!"
	}
	def uninstall(String pluginDir, PluginContext context){}
}
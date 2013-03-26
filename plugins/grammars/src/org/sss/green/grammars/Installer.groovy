package org.sss.micro.grammars

import org.sss.micro.core.*

class Installer {
	def install(String pluginDir, PluginContext context){
    println "Installing Grammars plugin..."
		// language (all means all), default keybinding, action
    context.addToKeyMap(['{TAB}': 'completeSnippet']);

		    context.addMenu('&Tools',1,['showGrammars','switchGrammar'])

    println "Completed installing Grammars plugin!"
	}
	def uninstall(String pluginDir, PluginContext context){}
}
package org.sss.micro.tools
name='Tools'
description='Provides Misc Debugging tools'
version=0.1
requires=[core: 0.01, filesystem: 0.1]
// class that implements load and optionally init and unload methods
pluginClass='org.sss.micro.tools.ToolsManager'
// class that implements install and uninstall methods
installer='org.sss.micro.tools.Installer'

actions = [
  executeTabAction : [
		name: 'Execute Tab',
		description: 'Executes the current tab idsplaing its output in a new window',
		execute: 'executeTab'
  ],

        createMacroAction : [
                name: 'Create Macro',
                description: 'Opens a new window to create a new macro',
                execute: 'createMacro'
        ],

        displayPluginInfoAction : [
		name: 'Plugin Info',
		description: 'Displays info about the currently installed plugins in a new window',
		execute: 'pluginInfo'
  ]
]

keyMap = [
	'CTRL+SHIFT+E': 'executeTabAction'
]  


menu = [
        '&Tools': ['executeTabAction': 10, 'displayPluginInfoAction': 99],
        '&Macros': ['createMacroAction': 10]
]

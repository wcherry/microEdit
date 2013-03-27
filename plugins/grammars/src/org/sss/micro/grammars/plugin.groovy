package org.sss.micro.grammars
name='Grammars'
description='Provides Grammar services'
version=0.1
requires=[core: 0.01]
// class that implements load and optionally init and unload methods
pluginClass='org.sss.micro.grammars.GrammarsManager'
// class that implements install and uninstall methods
installer='org.sss.micro.grammars.Installer'

actions = [
  showGrammars : [
		name: 'Show Grammars',
    order: 40,
		toolTip: 'Displays the currently installed gramamrs',
		execute: 'showGrammars'
  ],

  switchGrammar : [
		name: 'Switch Grammar',
    order: 41,
		toolTip: 'Switches the current grammar for the current document',
		execute: 'switchGrammar'
  ],
  completeSnippet:[
		name: 'Switch Grammar',
    order: 41,
		toolTip: 'Switches the current grammar for the current document',
		execute: 'completeSnippet'
  ] ]
  

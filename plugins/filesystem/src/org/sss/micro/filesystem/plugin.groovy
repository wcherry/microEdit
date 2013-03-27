package org.sss.micro.filesystem
name='Filesystem'
description='Provides filesystem services'
version=0.1
requires=[core: 0.01]
// class that implements load and optionally init and unload methods
pluginClass='org.sss.micro.filesystem.Filesystem'
// class that implements install and uninstall methods
installer='org.sss.micro.filesystem.Installer'

actions = [
  saveAction : [
		name: 'Save',
		description: 'Saves the current document to a file',
		execute: 'saveFile'
  ],

  saveAsAction : [
		name: 'Save As',
		description: 'Saves the current document to a file, prompting for a new name',
		execute: 'saveFileAs'
  ],

  openAction : [
		name: 'Open',
		description: 'Opens a new document from a file',
		execute: 'openFile'
  ],
	toggleFileViewerAction: [
		name: 'Toggle Directory Viewer',
		description: 'Toggles the display of the Driectory Viewer',
		execute: 'toggleFileViewer'
]
]
  
keyMap = [
	'CTRL+S': 'saveAction',
	'CTRL+SHIFT+S': 'saveAsAction',
	'CTRL+O': 'openAction',
	'{F7}': 'toggleFileViewerAction'
]

menu = [
	'&File': ['openAction':20,'saveAction':30,'saveAsAction':31,'reloadAction':40, 'toggleFileViewerAction':90]
]
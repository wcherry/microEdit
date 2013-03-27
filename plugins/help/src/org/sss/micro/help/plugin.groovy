package org.sss.micro.help
name = 'Help'
description = 'Provides the Help Engine'
version = 0.1
requires = [core: 0.01]
// class that implements load and optionally init and unload methods
pluginClass = 'org.sss.micro.help.HelpEngine'
// class that implements install and uninstall methods
installer = 'org.sss.micro.help.Installer'

actions = [
        showHelp: [
                name: 'Help',
                order: 40,
                toolTip: 'Shows help in a new window',
                execute: 'showHelp'
        ]]

keyMap = [
        '{F1}': 'showHelp'
]

menu = [
        '&Help': ['showHelp': 10]
]
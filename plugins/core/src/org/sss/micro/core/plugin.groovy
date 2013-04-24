package org.sss.micro.core
name = 'Core'
description = 'Provides the core services'
version = 0.1
// class that implements load and optionally init and unload methods
pluginClass = 'org.sss.micro.core.Main'
// class that implements install and uninstall methods
installer = 'org.sss.micro.core.Installer'

actions = [
        newAction: [
                name: 'New',
                description: 'Creates a new document',
                execute: 'newDocument'
        ],
        closeAction: [
                name: 'Close',
                description: 'Closes the currently focused document',
                execute: 'closeDocument'
        ],
        closeAllAction: [
                name: 'Close All',
                description: 'Closes all opened documents',
                execute: 'closeAllDocuments'
        ],
        sysPropsAction: [
                name: 'System Properties',
                description: 'Displays certain system properties',
                execute: 'displaySysProps'
        ],
        quitAction: [
                name: 'Quit',
                description: 'Quits the application, prompting to close open editors',
                execute: 'quitApp'
        ],
        cutAction: [
                name: 'Cut',
                description: 'Cuts the current selection to the clipboard',
                execute: 'cutToClipboard'
        ],
        copyAction: [
                name: 'Copy',
                description: 'Copies the current selection to the clipboard',
                execute: 'copyToClipboard'
        ],
        pasteAction: [
                name: 'Paste',
                description: 'Pastes the clipboard contents to the current poisition',
                execute: 'pasteFromClipboard'
        ],
        selectAllAction: [
                name: 'Select All',
                description: 'Selects all of the text in the current window',
                execute: 'selectAll'
        ],
        undoAction: [
                name: 'Undo',
                description: 'Undo the last action',
                execute: 'undo'
        ],
        redoAction: [
                name: 'System Properties',
                description: 'Redo the last undo action',
                execute: 'redo'
        ],
        changeFontAction: [
                name: 'Change Font',
                description: 'Changes the default font used by all windowsRedo the last undo action',
                execute: 'changeFont'
        ],
        searchAction: [
                name: 'Search',
                description: 'Searches the current document for text',
                execute: 'search'
        ]

]

keyMap = [
        'CTRL+N': 'newAction',
        'CTRL+Q': 'quitAction',
        'CTRL+X': 'cutAction',
        'CTRL+C': 'copyAction',
        'CTRL+V': 'pasteAction',
        'CTRL+A': 'selectAllAction',
        'CTRL+Z': 'undoAction',
        'CTRL+Y': 'redoAction',
        'CTRL+F': 'searchAction'
]

menu = [
        '&File': ['newAction': 10, 'closeAction': 41, 'closeAllAction':42, 'quitAction': 99],
        '&Edit': ['undoAction': 10, 'redoAction': 15, 'cutAction': 20, 'copyAction': 25, 'pasteAction': 30, 'selectAllAction': 40],
        '&Options': ['changeFontAction': 10],
        '&Search': ['searchAction': 10]
]
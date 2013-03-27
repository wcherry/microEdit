microEdit
=========

µ-Edit is a lightweight pluggable editor written in Groovy. The goal of the project is to provide a easy to use source 
code editor that allows use of Textmate(tm) bundles. Textmate(tm) bundles are open source and are targeted at the Mac-OS 
operating system for use in Textmate(tm). These bundles provide functionality in the following areas:
  1. Grammar - Syntax highlighting, code folding, 
  2. Snippets - Provides text expansion
  3. Commands -
  4. Preferences -

The development style guides are as follows:
  1. Source code files should be 200 lines are less in length.
  2. Native code, namely SWT, should be isolated from the main code
  3. Plugins should not cause unnecessary dependencies


Startup procedure
  The core plugin includes the bootstrap code as well as the core module.
  1. The Application class loads the properties file from either the start up directory or the users home directory. 
It then creates the PluginManager object calling loadPlugins(), startPlugins(), and then pluginMgr.startMain().
  2. PluginManager calls each plugins load() and start() process letting the plugin 


Plugin configuration file
 The plugin configuration file MUST be named plugin.class and can be anywhere in the package hierarchy. The jar file
   must be in the plugins directory.
  name - the name of the plugin, should be a single camel-cased word
  description - a brief english language description
  version - version number, should be a decimal number (e.g 1.02)
  pluginClass - full class name that will be used to execute any actions. Can optionally provide the `load()`, 
`start()`, `stop()`, and `unload()` methods.
  installer - full class name that will be called with the `install()` and `uninstall()` methods when the plugin is
    first installed and when the plugin is removed.
  actions - list of actions that the plugin provides
  keyMap - the default key map, this is used when the plugin is first installed or when the key map is reset.
  menu - the menu layout to use, only one level of submenus are currently supported.


Event bus
    The plugins can communicate asynchronous events to each other using the event bus. Events must be registered
      before the can be listened to or fired. For example the core plugin fires the 'documentLoaded' event that
      the Grammar plugin subscribes to. When it receives the event it applies the appropriate grammar styler to
      the window.

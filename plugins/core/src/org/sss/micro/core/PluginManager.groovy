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
package org.sss.micro.core

import java.util.jar.JarFile

/**
 * Starting point for the entire application. This class processes the command line arguments, loads the correct
 * property files, and then loads and starts the Plugin Manager
 * @author  wcherry
 * @since   1.0
 * @version 1.0
 */
class PluginManager {
	def pluginDir = '.'
	def plugins = [:]
	def context = null
    def logger = null
    def newPlugins = []
	Map resources = [:]

    boolean libraryArc64 = System.getProperty("sun.arch.data.model")=="64";

    PluginManager(Map map){
  	    map?.each{k,v->
            if(this.hasProperty(k)) this[k] = v
  	    }
        if(!context) context = new PluginContext(pluginDir: pluginDir, plugins: plugins)
	
		context.resources = resources
        context.registerEvent('newPluginsFound')
    }

    def loadPlugins(){
      println "Loading ${libraryArc64?"64":"32"} bit library, Current Classpath: "

      loadCache()
	  installNewPlugins()

	  plugins.each{name,plugin->
      loadResources(name)
          loadLibraries(pluginDir+File.separator+'lib'+File.separator+name)
		  plugin.pluginInstance = Class.forName(plugin.pluginClass).newInstance()
		  context.addBean(name, plugin.pluginInstance)
	    if(plugin.pluginInstance.hasMethod('load'))
		    plugin.pluginInstance.load(context)
    }
  }

	def startPlugins(){
		plugins.each{name,plugin->
			if(plugin.pluginInstance.hasMethod('start'))
				plugin.pluginInstance.start(context)
		}
	}

	def startMain(){
		plugins.each{name,plugin->
			if(plugin.pluginInstance.hasMethod('startMain'))
				plugin.pluginInstance.startMain(context)
		}
	}
  
  def loadResources(String name){
    try{
		def res = ResourceBundle.getBundle("${name}Resources")
        Enumeration<String> keys = res.getKeys();
        while (keys.hasMoreElements()) {
          String key = keys.nextElement()
          resources[key] = res.getString(key)
        }
    } catch(Exception ex){
        println "Can't load resource bundle for plugin $name (${ex.key})"
        println ex.message
    }
  }

	def installNewPlugins(){
		// Install any new plugins, plugins are loaed at the end
		def newPluginFiles = checkForNewPlugins()
		if(newPluginFiles){
			newPluginFiles.each{name, file->
				println name
				new JarFile(file).entries().each{ entry ->
					if ( entry.name.toLowerCase() =~ 'plugin.class'){
						String clazz = entry.name.replace('/', '.') - '.class'
						addJarToClasspath(file)
						def info = readPluginInfo(clazz)
						plugins[info.name] = info
                        newPlugins << info
						info.actions?.each{actionName, values->
							context.registerAction(actionName, info, values)
						}
						if(info.installer)
							Class.forName(info.installer).newInstance().install(pluginDir, context)
					}
				}
			}
			storeCache()  // Update the cache with the new plugins
		}
	}

	PluginInfo readPluginInfo(String className){
		def script = Class.forName(className).newInstance()
		script.run()
		new PluginInfo(script.binding.variables)
	}


  def fireNewPluginsEvent(){
    context.fireEvent('newPluginsFound', [plugins: newPlugins])
  }


  void addJarToClasspath(file){
    println "Adding '$file' to classpath"
    getClass().getClassLoader().addURL(file.toURL())
  }
  

	def checkForNewPlugins(){
    println "Plugin Dir: $pluginDir"
    def newPluginFiles = [:]
		new File(pluginDir).eachFileMatch(~/.*\.jar/){file->
			def name = file.name.toLowerCase()-'.jar'
			newPluginFiles[name] = file
		}
    return newPluginFiles
	}
	
  def loadCache(){
    File cacheFile = new File(pluginDir, 'plugins.cache')
    return false    
  }
  
  def storeCache(){
    File cacheFile = new File(pluginDir, 'plugins.cache')
    StringBuffer buf = new StringBuffer()
    buf << "Last Updated: ${new Date()}\n"
    //buf << "Version: ${Application.VERSION}"
    plugins.each{name,plugin->buf << "$name: $plugin"}
    cacheFile.text = buf.toString()
  }
	
	def loadAllPlugins(){
    	plugins.each{name,plugin->
      		Class.forName(plugin.pluginClass).newInstance().load(context)
    	}
	}
	
	boolean versionCompatiable(String version, String required){
		return true
	}
	
	def loadLibraries(String path){
        File dir = new File(path)
        if(libraryArc64){
            File dir64 = new File(dir, "64")
            if(dir64.exists()){
                dir = dir64
            }
        } else {
            File dir32 = new File(dir, "32")
            if(dir32.exists()){
                dir = dir32
            }
        }

	    if(dir.exists()){
		    dir.eachFile{file->
                if(file.isDirectory() && file.name !='32' && file.name !='64') loadLibraries(file.absolutePath) else
                if(file.name.endsWith('.jar'))  addJarToClasspath(file)
		    }
	    }
    }
  
	void processKeyMap(){
    	newPlugins.each{name,plugin->
      		plugin.keyMap.each {shortcut, action->
				def ke = convertKeyStringToEvent(shortcut)
				keyMap[ke] = action
			}				 
    	}
	}

	// Order the menu before adding it to the menu widget
	void processMenu(){
    	newPlugins.each{name,plugin->
      		plugin.menu.each {topMenu, items->
			    def m = menu[topMenu]
				if(!m){
					menu[topMenu] = []
					m = menu[topMenu]
				}
				items.each{action, order->
					m << [action, order] 
    			}
			}				 
    	}
	}
 }
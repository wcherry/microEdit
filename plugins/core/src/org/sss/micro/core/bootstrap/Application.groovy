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

package org.sss.micro.core.bootstrap

import org.sss.micro.core.Helper
import org.sss.micro.core.PluginManager

/**
 * Starting point for the entire application. This class processes the command line arguments, loads the correct
 * property files, and then loads and starts the Plugin Manager
 * @author  wcherry
 * @since   1.0
 * @version 1.0
 */
class Application {
    static String VERSION = '0.0.01'
    private static PluginManager pluginMgr = null
    private static String PROPERTY_FILENAME = "micro.properties"

    public static void main(String[] args) {

        // Add the hasMethod to all objects, this is used when loading/starting plugins
        Object.metaClass.hasMethod = {String name ->
            delegate.metaClass.methods.find {it.name == name} != null
        }

        // Loads the properties, starting with system and then check the startup directory or the user homes directory
        Properties properties = new Properties(System.properties)
        properties.setProperty('version', VERSION)
        File f = new File(System.getProperty("user.dir"),PROPERTY_FILENAME)
        if (f.exists()) {
            try {
                properties.load(new FileInputStream(f))
            } catch (Exception ex) {ex.printStackTrace()}
        } else {
            f = new File(System.getProperty("user.home"),PROPERTY_FILENAME)
            if (f.exists()) {
                try {
                    properties.load(new FileInputStream(f))
                } catch (Exception ex) {ex.printStackTrace()}
            }
        }

        // Figure put the plugins directory, the -d is used to change the startup directory.
        String pluginDir = properties.getProperty('user.dir') + '\\..\\plugins'
        for(int i = 0; i < args.length; i+=2){
            if(args[i]=='-d') pluginDir = args[i+1] + '\\plugins'
        }

        // Start the plugin manager, this does the really hard work.
        // Load plugins and then start them, finally starting the plugin manager.
        PluginManager pluginMgr = new PluginManager(pluginDir: pluginDir)
        try {
            pluginMgr.loadPlugins()
            pluginMgr.fireNewPluginsEvent()
            pluginMgr.startPlugins()
            println "starting"
            try {pluginMgr.startMain()} catch (Throwable t) {System.err.println "You threw this all the way to the boot strap!!! ${t.message}"; Helper.prettyPrintStackTrace(t)}
            println "Ending"
        } catch (Exception ex) {
            Helper.prettyPrintStackTrace(ex)
        }
    }
}
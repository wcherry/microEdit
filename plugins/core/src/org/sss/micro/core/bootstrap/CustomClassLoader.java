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
package org.sss.micro.core.bootstrap;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * Custom class load extends the URLClassLoader by exposing the addURL methods so that the PluginManager can add new
 * jars dynamically as it loads the plugins.
 * @author  wcherry
 * @since   1.0
 * @version 1.0
 */
public class CustomClassLoader extends URLClassLoader {

  public CustomClassLoader(ClassLoader cl) {super(new URL[0], cl);}
  public CustomClassLoader(URL[] urls) {super(urls);}
  public CustomClassLoader(URL[] urls, ClassLoader parent) {super(urls, parent);}
  public CustomClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {super(urls,parent,factory);}

    /**
     * addURL is exposed as a public method.
     * @param url the url of the jar file to load.
     */
  public void addURL(URL url){
    super.addURL(url);
  }
}
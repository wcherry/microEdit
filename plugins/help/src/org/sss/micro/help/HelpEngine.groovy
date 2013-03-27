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
package org.sss.micro.help

import org.sss.micro.core.*
import org.sss.micro.swt.*
class HelpEngine {
  def context = null

  def load(PluginContext context){
    println "Loading Help plugin"
    this.context = context
    context.addEventListener('newPluginsFound'){plugins->buildIndex()}
  }
  def start(PluginContext context){
    println "Starting Help plugin"
  }
  def stop(PluginContext context){}
  def unload(PluginContext context){}

  def showHelp(){
    HtmlWindow win = new HtmlWindow()
    win.url = new File("${context.pluginDir}\\assets\\help\\index.html").toURL()
	  context.appWindow.addWindow(win)
  }
  
  def buildIndex(){
    StringBuffer buf = new StringBuffer()
    buf << '''<html>
	<head>
		<title>Green Car Version 1.0 Help System</title>
	</head>
	<body>
		<h1>Green Car Version 1.0 Help System</h1>
    <ul>
    '''
    new File("${context.pluginDir}\\assets\\help").eachFileMatch('\\.html'){file->buf << "<li>${file.name}</li>"}
    buf << '''</ul>
    </body>
</html>'''

      
  }
  
  def pluginInfo(){}

}
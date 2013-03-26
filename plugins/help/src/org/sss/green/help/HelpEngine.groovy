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
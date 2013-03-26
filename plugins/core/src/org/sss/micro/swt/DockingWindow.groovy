package org.sss.micro.swt

import org.sss.micro.core.*

import org.eclipse.swt.*
import org.eclipse.swt.events.*
import org.eclipse.swt.graphics.*
import org.eclipse.swt.layout.*
import org.eclipse.swt.widgets.*
import org.eclipse.swt.custom.*

class DockingWindow {
	PluginContext context
	Shell shell
	String title
	Closure setup

	DockingWindow(map){
		context = map.context
		title = map.title
        setup = map.setup
		context?.dockingManager?.addWindow(this)
	}

    public boolean isVisible(){
        return shell?!shell.isDisposed():false
    }

	
 	public void show(){
 		shell = new Shell(context.display)
 		if(title) shell.setText(title)
		Point p = context.shell.getLocation()
		p.x = p.x-220
		shell.setLocation(p)
		Point s = context.shell.getSize()
		s.x = 200
 		shell.setSize(s)
   		shell.setLayout(new FillLayout())
		setup?.call(this)
 		shell.open ()
	}

    public void hide(){
        shell.close()
    }
 
}
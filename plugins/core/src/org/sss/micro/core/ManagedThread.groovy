package org.sss.micro.core

import java.util.HashMap

class ManagedThread {
	static HashMap<String, Thread> threadPool = new HashMap<String, Thread>()
	
	boolean running = false;
	Thread thread
	String name
	Closure runMethod

	ManagedThread(String threadName, Closure runner){
		name = threadName
		runMethod = runner
		thread = new Thread() {
			public void run(){
				running = true
				try{
					runMethod.call()
				} catch(Exception ex){
					Helper.prettyPrintStackTrace(ex)
				}
				running = false				
			}
		}
	}
	
	void start(){
		org.eclipse.swt.widgets.Display.getDefault().asyncExec(thread)
	}

	static Thread getThread(String threadName){
		return threadPool[threadName]
	}
}
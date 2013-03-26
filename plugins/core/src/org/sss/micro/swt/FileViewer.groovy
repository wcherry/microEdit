package org.sss.micro.swt

import org.eclipse.swt.SWT
import org.eclipse.swt.events.MouseAdapter
import org.eclipse.swt.events.MouseEvent
import org.eclipse.swt.graphics.Point
import org.eclipse.swt.widgets.Tree
import org.eclipse.swt.widgets.TreeItem
import org.sss.micro.core.PluginContext


class FileViewer {
    static final String FULL_FILENAME_KEY = '_filename'
	DockingWindow win = null
	Tree tree = null

	FileViewer(PluginContext context){
		win = new DockingWindow(context: context, title: 'File System Browser', setup: {
			tree = new Tree(it.shell, SWT.BORDER)
            tree.addMouseListener(new MouseAdapter() {
                void mouseDoubleClick(MouseEvent mouseEvent) {
                    TreeItem item = tree.getItem(new Point(mouseEvent.x, mouseEvent.y))
                    String fullName = item.getData(FULL_FILENAME_KEY)
                    if (fullName) {
                        println "Loading $fullName"
                        context.Filesystem.loadFile(fullName)
                    }

                }
            })
			
			scanDirectory(new File(System.properties.getProperty("user.home")), tree)
		})
	}
	protected scanDirectory(File dir, def tree){
		dir.eachFile{file->
			if(file.name!='.' && file.name!='..'){
				TreeItem item = new TreeItem(tree, 0)
				item.text = file.name
				if(file.isFile()) item.setData(FULL_FILENAME_KEY, file)
				if(file.isDirectory()) scanDirectory(file, item)
			}
		}
	}

	void toggle(){
		if (win.visible) win.hide()
        else win.show()
	}

    FileViewer show(){
        if (!win.visible) win.show()
        return this
    }

}